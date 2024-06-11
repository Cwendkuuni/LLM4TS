import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
  }

  @Test
  public void testShouldTraverse_FunctionConstructor_ShouldNotTraverse() {
    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), new Node(Token.FUNCTION), new Node());
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionThisAnnotation_ShouldNotTraverse() {
    String jsDocString = "/** @this SomeContext */";
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.addSuppressions(new Node(Token.THIS));
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), functionNode, new Node());
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNoLogicalPlace_ShouldNotTraverse() {
    String jsDocString = "/** @this SomeContext */";
    JSDocInfo jsDoc = new JSDocInfo();
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(jsDoc);

    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToFront(functionNode);

    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), functionNode, blockNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAssignment_ShouldNotTraverse() {
    String jsDocString = "/** @this SomeContext */";
    JSDocInfo jsDoc = new JSDocInfo();
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(jsDoc);

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToFront(functionNode);

    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), functionNode, assignNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithObjectLiteralProperty_ShouldNotTraverse() {
    String jsDocString = "/** @this SomeContext */";
    JSDocInfo jsDoc = new JSDocInfo();
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(jsDoc);

    Node stringKeyNode = new Node(Token.STRING_KEY);
    stringKeyNode.addChildToFront(functionNode);

    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), functionNode, stringKeyNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionGettingLentPrototype_ShouldNotTraverse() {
    String jsDocString = "/** @this SomeContext */";
    JSDocInfo jsDoc = new JSDocInfo();
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(jsDoc);

    Node lhsNode = new Node(Token.GETPROP, new Node(Token.GETPROP));
    lhsNode.addChildToFront(functionNode);

    Node grampsNode = new Node(Token.OBJECTLIT);
    grampsNode.setJSDocInfo(jsDoc);
    lhsNode.getParent().addChildBefore(grampsNode, lhsNode);

    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), functionNode, lhsNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_AssignOnPrototype_ShouldNotTraverse() {
    Node assignNode = new Node(Token.ASSIGN);
    Node getPropNode = new Node(Token.GETPROP);
    assignNode.addChildToFront(getPropNode);

    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), getPropNode, assignNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_AssignSubPropertiesOnPrototype_ShouldNotTraverse() {
    Node assignNode = new Node(Token.ASSIGN);
    Node getPropNode = new Node(Token.GETPROP);
    getPropNode.addChildToFront(new Node(Token.GETPROP));
    assignNode.addChildToFront(getPropNode);

    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), getPropNode, assignNode);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_LhsNodeIsAssign_ShouldTraverse() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.NAME);

    assignNode.addChildToFront(lhsNode);

    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), lhsNode, assignNode);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_LhsNodeIsGetPropNotPrototype_ShouldTraverse() {
    Node lhsNode = new Node(Token.GETPROP, new Node(Token.GETPROP));

    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), lhsNode, lhsNode.getParent());
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_LhsNodeIsGetPropPrototype_ShouldNotTraverse() {
    Node lhsNode = new Node(Token.GETPROP, new Node(Token.GETPROP));
    lhsNode.getLastChild().setString("prototype");

    boolean result = checkGlobalThis.shouldTraverse(
        new NodeTraversal(), lhsNode, lhsNode.getParent());
    assertFalse(result);
  }

  @Test
  public void testVisit_N_IsThisAndShouldReportThis_ShouldReportDiagnostic() {
    Node n = new Node(Token.THIS);
    NodeTraversal t = new NodeTraversal();
    Node parent = new Node();

    checkGlobalThis.assignLhsChild = null;
    checkGlobalThis.visit(t, n, parent);

    assertTrue(t.hasError());
    // Assert specific diagnostic type
    // assertTrue(t.getError().getType().equals(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testVisit_N_IsAssignLhsChild_ShouldUnsetAssignLhsChild() {
    Node n = new Node(Token.NAME);
    NodeTraversal t = new NodeTraversal();
    Node parent = new Node();

    checkGlobalThis.assignLhsChild = n;
    checkGlobalThis.visit(t, n, parent);

    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testShouldReportThis_N_IsAssignLhsChild_ShouldAlwaysReport() {
    Node n = new Node(Token.NAME);

    boolean result = checkGlobalThis.shouldReportThis(n);

    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_N_IsPropertyAccess_ShouldReport() {
    Node n = new Node(Token.GETPROP);
    Node parent = new Node();
    parent.addChildToFront(n);

    boolean result = checkGlobalThis.shouldReportThis(n);

    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_N_IsNotPropertyAccess_ShouldNotReport() {
    Node n = new Node(Token.THIS);
    
    boolean result = checkGlobalThis.shouldReportThis(n);

    assertFalse(result);
  }
}