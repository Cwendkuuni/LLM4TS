import static com.google.javascript.jscomp.CheckGlobalThis.GLOBAL_THIS;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void shouldTraverse_NonFunctionNode_ReturnsTrue() {
    NodeTraversal t = createNodeTraversal(Node.newString("Some node"), null);
    Node n = Node.newString("Some node");
    Node parent = null;

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(true, result);
  }

  @Test
  public void shouldTraverse_FunctionNodeWithConstructorAnnotation_ReturnsFalse() {
    NodeTraversal t = createNodeTraversal(Node.newString("function Test() {}"), null);
    Node n = Node.newString("function Test() {}");
    Node parent = null;

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_FunctionNodeWithThisAnnotation_ReturnsFalse() {
    NodeTraversal t = createNodeTraversal(Node.newString("/** @this {Object} */ function test() {}"), null);
    Node n = Node.newString("/** @this {Object} */ function test() {}");
    Node parent = null;

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_FunctionNodeNotAbleToHaveThisAnnotation_ReturnsTrue() {
    NodeTraversal t = createNodeTraversal(Node.newString("var a = function() {};"), null);
    Node n = Node.newString("var a = function() {};");
    Node parent = null;

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(true, result);
  }

  @Test
  public void shouldTraverse_LeftSideOfAssign_ReturnsTrue() {
    NodeTraversal t = createNodeTraversal(Node.newString("var a;"), Node.newString("var a"));
    Node n = Node.newString("var a");
    Node parent = Node.newString("var a");

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(true, result);
  }

  @Test
  public void shouldTraverse_PrototypeAssignment_ReturnsFalse() {
    NodeTraversal t = createNodeTraversal(Node.newString("obj.prototype.test = function() {};"), null);
    Node n = Node.newString("obj.prototype.test");
    Node parent = Node.newString("obj.prototype.test");

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void shouldTraverse_SubPropertyAssignment_ReturnsFalse() {
    NodeTraversal t = createNodeTraversal(Node.newString("a.b.c = function() {};"), null);
    Node n = Node.newString("a.b.c");
    Node parent = Node.newString("a.b.c");

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertEquals(false, result);
  }

  @Test
  public void visit_NodeIsThisAndShouldReport_ReturnsWarning() {
    NodeTraversal t = createNodeTraversal(Node.newString("this"), null);
    Node n = Node.newString("this");
    Node parent = null;
    DiagnosticType expectedError = GLOBAL_THIS;

    checkGlobalThis.visit(t, n, parent);

    assertEquals(1, compiler.getErrors().size());
    assertEquals(expectedError, compiler.getErrors().get(0).getType());
  }

  @Test
  public void visit_NodeIsAssignLhsChild_DoesNotReturnWarning() {
    NodeTraversal t = createNodeTraversal(Node.newString("var a = this;"), Node.newString("a"));
    Node n = Node.newString("a");
    Node parent = Node.newString("a");

    checkGlobalThis.visit(t, n, parent);

    assertEquals(0, compiler.getErrors().size());
  }

  private static NodeTraversal createNodeTraversal(Node n, Node parent) {
    NodeTraversal t = new NodeTraversal(null);
    t.traverse(n, parent);
    return t;
  }

}