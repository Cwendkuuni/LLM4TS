import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionNotConstructor_NoThis_NoOverride() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();

    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNotConstructor_ThisAndOverride() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    jsDoc.setThisType(true);

    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNotConstructor_This() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(true);

    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);

    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_NoParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();

    Node n = new Node(Token.FUNCTION);

    boolean result = checkGlobalThis.shouldTraverse(t, n, null);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_LeftSideAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();

    Node n = new Node(Token.NAME);

    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_RightSideAssignment_Prototype() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();

    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.NAME, "prototype"));
    lhs.addChildToBack(new Node(Token.STRING, "property"));

    Node n = new Node(Token.NAME);

    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(lhs);
    parent.addChildToBack(n);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_RightSideAssignment_PrototypeSubproperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();

    Node lhs = new Node(Token.NAME, "object.prototype.property");

    Node n = new Node(Token.NAME);

    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(lhs);
    parent.addChildToBack(n);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testVisit_ThisAndAssignNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();

    Node n = new Node(Token.THIS);

    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);

    Node grandParent = new Node(Token.SCRIPT);

    checkGlobalThis.visit(t, n, grandParent);

    // Assert that report method was called with the expected arguments
  }

  @Test
  public void testVisit_AssignLhsChildNotNull() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();

    Node n = new Node(Token.NAME);

    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);

    Node grandParent = new Node(Token.SCRIPT);

    checkGlobalThis.visit(t, n, parent);

    // Assert that report method was not called
  }
}