import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  // Test case for shouldTraverse when function is a constructor
  @Test
  public void testShouldTraverse_ConstructorFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = NodeUtil.newDeclNode("function");
    Node parent = NodeUtil.newNode("constructor");
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(functionNode, parent);
    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);

    assertEquals(false, result);
  }

  // Test case for shouldTraverse when function is annotated with @this
  @Test
  public void testShouldTraverse_AnnotatedFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = NodeUtil.newDeclNode("function");
    Node parent = NodeUtil.newNode("@this");
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(functionNode, parent);
    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);

    assertEquals(false, result);
  }

  // Test case for shouldTraverse when function is a function with no logical place to put @this
  @Test
  public void testShouldTraverse_NoLogicalPlaceForThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = NodeUtil.newDeclNode("function");
    Node parent = NodeUtil.newNode("noLogicalPlaceForThis");
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(functionNode, parent);
    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);

    assertEquals(false, result);
  }

  // Test case for shouldTraverse when function is in global context
  @Test
  public void testShouldTraverse_GlobalContext() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = NodeUtil.newNode("function");
    Node parent = NodeUtil.newNode("globalContext");
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(functionNode, parent);
    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);

    assertEquals(true, result);
  }

  // Test case for shouldTraverse when parent is an assignment
  @Test
  public void testShouldTraverse_ParentAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = NodeUtil.newNode("function");
    Node parent = NodeUtil.newNode("assignment");
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(functionNode, parent);
    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, parent);

    assertEquals(true, result);
  }

  // Test case for shouldTraverse when left side of assignment is THIS
  @Test
  public void testShouldTraverse_LeftSideThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = NodeUtil.newNode("function");
    Node parent = NodeUtil.newNode("assignment");
    Node leftChild = NodeUtil.newNode("this");
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(functionNode, parent);
    checkGlobalThis.assignLhsChild = leftChild;
    boolean result = checkGlobalThis.shouldTraverse(traversal, leftChild, parent);

    assertEquals(true, result);
  }

  // Test case for shouldTraverse when right side of assignment is prototype property
  @Test
  public void testShouldTraverse_RightSidePrototypeProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = NodeUtil.newNode("function");
    Node parent = NodeUtil.newNode("assignment");
    Node leftChild = NodeUtil.newNode("prototype");
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(functionNode, parent);
    checkGlobalThis.assignLhsChild = NodeUtil.newNode("property");
    checkGlobalThis.assignLhsChild.addChildToBack(leftChild);
    boolean result = checkGlobalThis.shouldTraverse(traversal, leftChild, parent);

    assertEquals(false, result);
  }

  // Test case for shouldTraverse when left side of assignment is a property access
  @Test
  public void testShouldTraverse_LeftSidePropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = NodeUtil.newNode("function");
    Node parent = NodeUtil.newNode("assignment");
    Node leftChild = NodeUtil.newNode("propertyAccess");
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(functionNode, parent);
    checkGlobalThis.assignLhsChild = leftChild;
    boolean result = checkGlobalThis.shouldTraverse(traversal, leftChild, parent);

    assertEquals(true, result);
  }

  // Test case for visit when n is THIS and should report this
  @Test
  public void testVisit_ShouldReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node n = NodeUtil.newNode("THIS");
    Node parent = NodeUtil.newNode("propertyAccess");
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(n, parent);
    checkGlobalThis.assignLhsChild = null;
    checkGlobalThis.visit(traversal, n, parent);

    // Need to assert that the report is made
    // assertEquals(expectedReport, compiler.getReport());
  }

  // Test case for visit when n is THIS and should not report this
  @Test
  public void testVisit_ShouldNotReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node n = NodeUtil.newNode("THIS");
    Node parent = NodeUtil.newNode();
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(n, parent);
    checkGlobalThis.assignLhsChild = null;
    checkGlobalThis.visit(traversal, n, parent);

    // Need to assert that no report is made
    // assertEquals(expectedReport, compiler.getReport());
  }

  // Test case for visit when n is assignLhsChild
  @Test
  public void testVisit_AssignLhsChild() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node n = NodeUtil.newNode("assignLhsChild");
    Node parent = NodeUtil.newNode("propertyAccess");
    NodeTraversal.ScopedCallback callback = new NodeTraversal.ScopedCallback(n, parent);
    checkGlobalThis.assignLhsChild = n;
    checkGlobalThis.visit(traversal, n, parent);

    // Need to assert that assignLhsChild is set to null
    // assertEquals(expectedValue, checkGlobalThis.assignLhsChild);
  }

}