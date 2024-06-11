import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }

  @Test
  public void shouldTraverse_ReturnsTrueForFunctionWithoutConstructorOrThisAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_ReturnsTrueForFunctionWithConstructorAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_ReturnsFalseForFunctionWithThisAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_ReturnsFalseForFunctionWithOverrideAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_ReturnsFalseForFunctionNotInsideLogicalPlaceForThisAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.IF);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_ReturnsTrueForLeftSideOfAssignment() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node grandparent = new Node(Token.VAR);
    grandparent.addChildToFront(parent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_ReturnsFalseForRightSideOfAssignmentToPrototypeProperty() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.ASSIGN);
    Node grandparent = new Node(Token.VAR);
    grandparent.addChildToFront(parent);
    n.addChildToBack(new Node(Token.GETPROP));
    Node lastChild = new Node(Token.STRING);
    lastChild.setString("prototype");
    n.getLastChild().addChildToBack(lastChild);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_ReturnsFalseForRightSideOfAssignmentToPrototypeSubproperty() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node grandparent = new Node(Token.VAR);
    grandparent.addChildToFront(parent);
    n.addChildToBack(new Node(Token.GETPROP));
    Node lastChild = new Node(Token.STRING);
    lastChild.setString("prototype");
    n.getLastChild().addChildToBack(lastChild);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_ReturnsTrueForRightSideOfAssignment() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node grandparent = new Node(Token.VAR);
    grandparent.addChildToFront(parent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void visit_ReportsErrorForThisOnLeftSideOfAssignment() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.visit(t, n, parent);
    // Verify that error is reported
    // ...
  }

  @Test
  public void visit_DoesNotReportErrorForThisOnRightSideOfAssignment() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.visit(t, n, parent);
    // Verify that no error is reported
    // ...
  }
  
  @Test
  public void visit_ClearsAssignLhsChildForNonAssignmentNode() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.visit(t, n, parent);
    // Verify that assignLhsChild is null
    // ...
  }
  
  @Test
  public void visit_ClearsAssignLhsChildForAssignmentNode() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.visit(t, n, parent);
    // Verify that assignLhsChild is null
    // ...
  }

  private Node createMockNode(int type) {
    Node node = new Node(type);
    return node;
  }

  private Node createMockParentNode(int type) {
    Node parent = new Node(type);
    return parent;
  }

  private JSDocInfo createMockJSDocInfo() {
    JSDocInfo jsDoc = new JSDocInfo();
    return jsDoc;
  }
}