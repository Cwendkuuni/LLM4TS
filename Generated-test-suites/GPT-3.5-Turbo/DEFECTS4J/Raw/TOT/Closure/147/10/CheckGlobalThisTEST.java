import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  private CheckLevel level;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForGlobalContext() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.SCRIPT);
    Node parent = null;

    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForConstructorFunctionWithJSDoc() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = null;

    // Add @constructor annotation to JSDoc info
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForInterfaceFunctionWithJSDoc() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = null;

    // Add @interface annotation to JSDoc info
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordInterface();
    n.setJSDocInfo(jsDoc);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionWithThisTypeJSDoc() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = null;

    // Add @this annotation to JSDoc info
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionWithOverrideJSDoc() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = null;

    // Add @override annotation to JSDoc info
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    n.setJSDocInfo(jsDoc);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionInNonGlobalContext() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);

    // Create a parent node that is not a valid context for a function
    Node parent = new Node(Token.ADD);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForRightSideOfAssignmentToPrototypeProperty() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);

    // Create the left side of the assignment to be assignment to prototype property
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.THIS));
    lhs.addChildToBack(new Node(Token.STRING, "prototype"));

    parent.addChildToFront(lhs);
    parent.addChildToBack(n);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void shouldReportThis_shouldReturnFalseWhenAssignLhsChildIsNotNull() {
    Node n = new Node(Token.THIS);
    Node parent = null;

    // Set assignLhsChild to not null
    checkGlobalThis.setAssignLhsChild(new Node(Token.NAME));

    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void shouldReportThis_shouldReturnFalseWhenParentIsNull() {
    Node n = new Node(Token.THIS);
    Node parent = null;

    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void shouldReportThis_shouldReturnFalseWhenParentIsNotPropertyAccess() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);

    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void shouldReportThis_shouldReturnTrueWhenParentIsPropertyAccess() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void visit_shouldReportGlobalThisWhenEncountered() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Mock compiler report method and create CheckGlobalThisTest custom error message
    CustomErrorObject error = null;
    try {
      PowerMockito.doAnswer(new Answer<Void>() {
        public Void answer(InvocationOnMock invocation) {
          Object[] args = invocation.getArguments();
          error = (CustomErrorObject) args[0];
          return null;
        }
      }).when(compiler).report(anyObject());
    } catch (Exception e) {}

    checkGlobalThis.visit(t, node, parent);

    assertNotNull(error);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, error.getType());
  }

  @Test
  public void visit_shouldSetAssignLhsChildToNullWhenNodeIsAssignLhsChild() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node node = new Node(Token.THIS);
    Node parent = null;

    // Set assignLhsChild to node
    checkGlobalThis.setAssignLhsChild(node);

    checkGlobalThis.visit(t, node, parent);

    assertNull(checkGlobalThis.getAssignLhsChild());
  }

}