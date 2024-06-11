import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.DiagnosticType;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseValidFunction() {
    // Test the shouldTraverse() method when the function is a valid function
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createDummyFunctionNode();

    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverseConstructorFunction() {
    // Test the shouldTraverse() method when the function is a constructor function
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createDummyConstructorFunctionNode();

    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    // Test the shouldTraverse() method when the function has @this annotation
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createDummyThisAnnotationFunctionNode();

    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverseFunctionWithOverrideAnnotation() {
    // Test the shouldTraverse() method when the function has @override annotation
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createDummyOverrideAnnotationFunctionNode();

    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverseFunctionWithoutThisAnnotation() {
    // Test the shouldTraverse() method when the function does not have any relevant annotations
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createDummyNonAnnotationFunctionNode();

    assertTrue(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testVisitThisNode() {
    // Test the visit() method when encountering a "this" node that should be reported
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createDummyThisNode();
    Node parent = createDummyParentNode();

    checkGlobalThis.visit(t, n, parent);

    // Verify that a compiler error is reported
    assertEquals(1, compiler.getErrors().size());
    assertEquals(GLOBAL_THIS, compiler.getErrors().get(0).getType());
  }

  @Test
  public void testVisitNonReportedThisNode() {
    // Test the visit() method when encountering a "this" node that should not be reported
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createDummyThisNode();
    Node parent = createNonReportingDummyParentNode();

    checkGlobalThis.visit(t, n, parent);

    // Verify that no compiler error is reported
    assertEquals(0, compiler.getErrors().size());
  }

  @Test
  public void testVisitAssignLhsChildNode() {
    // Test the visit() method when encountering the assignLhsChild node
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node assignLhsChild = createDummyAssignLhsChild();

    checkGlobalThis.assignLhsChild = assignLhsChild;
    checkGlobalThis.visit(t, assignLhsChild, null);

    // Verify that assignLhsChild is set to null after visiting
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testShouldReportThisAssignLhsChild() {
    // Test the shouldReportThis() method when assignLhsChild is not null
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = createDummyThisNode();

    checkGlobalThis.assignLhsChild = createDummyAssignLhsChild();
    assertTrue(checkGlobalThis.shouldReportThis(n));
  }

  @Test
  public void testShouldReportThisGetParent() {
    // Test the shouldReportThis() method when the parent is a property access
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = createDummyThisNode();

    assertTrue(checkGlobalThis.shouldReportThis(n));
  }

  /**
   * Helper method to create a dummy function node for testing
   */
  private Node createDummyFunctionNode() {
    Node functionNode = new Node(Token.FUNCTION);
    return functionNode;
  }

  /**
   * Helper method to create a dummy constructor function node for testing
   */
  private Node createDummyConstructorFunctionNode() {
    Node constructorNode = new Node(Token.FUNCTION);
    Node jsDocNode = new Node(Token.JSDOC);
    jsDocNode.setJSDocInfo(new JSDocInfo().setConstructor(true));
    constructorNode.addChildToFront(jsDocNode);
    return constructorNode;
  }

  /**
   * Helper method to create a dummy function node with @this annotation for testing
   */
  private Node createDummyThisAnnotationFunctionNode() {
    Node functionNode = new Node(Token.FUNCTION);
    Node jsDocNode = new Node(Token.JSDOC);
    jsDocNode.setJSDocInfo(new JSDocInfo().setHasThisType(true));
    functionNode.addChildToFront(jsDocNode);
    return functionNode;
  }

  /**
   * Helper method to create a dummy function node with @override annotation for testing
   */
  private Node createDummyOverrideAnnotationFunctionNode() {
    Node functionNode = new Node(Token.FUNCTION);
    Node jsDocNode = new Node(Token.JSDOC);
    jsDocNode.setJSDocInfo(new JSDocInfo().setOverride(true));
    functionNode.addChildToFront(jsDocNode);
    return functionNode;
  }

  /**
   * Helper method to create a dummy function node without relevant annotations for testing
   */
  private Node createDummyNonAnnotationFunctionNode() {
    Node functionNode = new Node(Token.FUNCTION);
    return functionNode;
  }

  /**
   * Helper method to create a dummy "this" node for testing
   */
  private Node createDummyThisNode() {
    Node thisNode = new Node(Token.THIS);
    return thisNode;
  }

  /**
   * Helper method to create a dummy parent node for testing
   */
  private Node createDummyParentNode() {
    Node parentNode = new Node(Token.GETPROP);
    return parentNode;
  }

  /**
   * Helper method to create a dummy parent node that does not report "this" for testing
   */
  private Node createNonReportingDummyParentNode() {
    Node parentNode = new Node(Token.FUNCTION);
    return parentNode;
  }

  /**
   * Helper method to create a dummy assignLhsChild node for testing
   */
  private Node createDummyAssignLhsChild() {
    Node assignLhsChild = new Node(Token.NAME);
    return assignLhsChild;
  }
}