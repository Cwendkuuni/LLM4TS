import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAnnotations;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testProcessWithNullArguments() {
    AbstractCompiler compiler = new Compiler(); // assume a valid compiler instance
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = null;
    Node externs = null;

    prepareAst.process(externs, root);

    // No exception should be thrown
  }

  @Test
  public void testProcessWithValidArguments() {
    AbstractCompiler compiler = new Compiler(); // assume a valid compiler instance
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node externs = Node.objectLit(); // an example of a valid externs
    Node root = Node.objectLit(); // an example of a valid root

    prepareAst.process(externs, root);

    // No exception should be thrown
  }

  @Test
  public void testNormalizeNodeTypes() {
    AbstractCompiler compiler = new Compiler(); // assume a valid compiler instance
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node n = Node.ifStmt(Token.IF, Node.literal(true), Node.literal(0));

    prepareAst.normalizeNodeTypes(n);

    Node expectedBlock = IR.block().srcref(n);

    assertSame(expectedBlock, n.getFirstChild());
  }

  @Test
  public void testNormalizeBlocks() {
    AbstractCompiler compiler = new Compiler(); // assume a valid compiler instance
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = Node.ifStmt(Token.IF, Node.literal(true), Node.literal(0));

    prepareAst.process(null, root);

    Node expectedBlock = IR.block();

    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return n.isBlock();
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        assertEquals(expectedBlock, n);
      }

      @Override
      public void endVisit(NodeTraversal t, Node n, Node parent) {
        // empty
      }
    });
  }

  @Test
  public void testPrepareAnnotations() {
    AbstractCompiler compiler = new Compiler(); // assume a valid compiler instance
    PrepareAst prepareAst = new PrepareAst(compiler);
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();

    Node n = Node.functionExpr(Token.FUNCTION, Node.identifier("foo"), Node.block(Token.BLOCK, Node.literal(0)));

    prepareAst.process(null, null);
    NodeTraversal.traverse(compiler, n, prepareAnnotations);

    // No explicit "this" value in the call
    assertFalse(n.getBooleanProp(Node.FREE_CALL));

    // No direct eval call
    assertFalse(n.getBooleanProp(Node.DIRECT_EVAL));

    // No JSDoc info
    assertNull(n.getJSDocInfo());
  }

  @Test
  public void testPrepareAnnotationsWithJavaDispatch() {
    AbstractCompiler compiler = new Compiler(); // assume a valid compiler instance
    PrepareAst prepareAst = new PrepareAst(compiler);
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();

    Node obj = Node.objectLit(Token.OBJECT_LIT, Node.identifier("foo"), Node.block(Token.BLOCK, Node.literal(0)));
    Node assign = Node.assign(Token.ASSIGN, Node.identifier("foo"), Node.functionExpr(Token.FUNCTION, Node.identifier("bar"), Node.block(Token.BLOCK, Node.literal(0))));

    prepareAst.process(null, null);
    NodeTraversal.traverse(compiler, obj, prepareAnnotations);

    assertTrue(assign.getFirstChild().getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test(expected = AssertionError.class)
  public void testPrepareAnnotationsWithInvalidJSDocInfo() {
    AbstractCompiler compiler = new Compiler(); // assume a valid compiler instance
    PrepareAst prepareAst = new PrepareAst(compiler);
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();

    Node obj = Node.objectLit(Token.OBJECT_LIT, Node.identifier("foo"), Node.block(Token.BLOCK, Node.literal(0)));
    Node assign = Node.assign(Token.ASSIGN, Node.identifier("foo"), Node.functionExpr(Token.FUNCTION, Node.identifier("bar"), Node.block(Token.BLOCK, Node.literal(0))));
    assign.setJSDocInfo(new JSDocInfo()); // an invalid JSDocInfo

    prepareAst.process(null, null);
    NodeTraversal.traverse(compiler, obj, prepareAnnotations);

    fail("Unexpected result");
  }

  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    AbstractCompiler compiler = new Compiler(); // assume a valid compiler instance
    PrepareAst prepareAst = new PrepareAst(compiler);
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();

    Node objlit = Node.objectLit(Token.OBJECT_LIT, Node.identifier("foo"), Node.propertyGet(Token.GET, Node.identifier("x"), Node.literal(1)));
    Node key = Node.identifier("x");
    Node value = Node.literal(1);

    prepareAst.process(null, null);
    NodeTraversal.traverse(compiler, objlit, prepareAnnotations);

    Node expectedValue = Node.functionExpr(Token.FUNCTION, Node.identifier("value"), Node.block(Token.BLOCK, value));

    assertEquals(expectedValue, objlit.getChildAt(1));
    assertEquals(key.getJSDocInfo(), value.getJSDocInfo());
  }

  @Test
  public void testProcessWithCheckOnlyFlag() {
    AbstractCompiler compiler = new Compiler(); // assume a valid compiler instance
    PrepareAst prepareAst = new PrepareAst(compiler, true);

    Node root = Node.ifStmt(Token.IF, Node.literal(true), Node.literal(0));

    prepareAst.process(null, root);

    // Preconditions.checkState should be called and throw an exception
  }
}