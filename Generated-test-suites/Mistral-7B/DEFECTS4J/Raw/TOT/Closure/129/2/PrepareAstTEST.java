import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;

public class PrepareAstTest {

  @Test
  public void testProcessExternsAndRootNull() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    Node externs = null;
    Node root = null;
    prepareAst.process(externs, root);
    // No exception should be thrown
  }

  @Test
  public void testNormalizeNodeTypesSimpleNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);

    Node root = IR.literal(Token.NUMBER, 1);
    prepareAst.process(null, root);
    // Check that the node type has not changed
    assertTrue(root.isNumber());
  }

  @Test
  public void testNormalizeNodeTypesBlockNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);

    Node root = IR.block();
    prepareAst.process(null, root);
    // Check that a block node has been created
    assertTrue(root.isBlock());
  }

  @Test
  public void testNormalizeNodeTypesControlStructureNodeWithCodeBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);

    Node root = IR.if_(IR.literal(Token.BOOL, true), IR.literal(Token.NUMBER, 1), IR.literal(Token.NUMBER, 2));
    prepareAst.process(null, root);

    // Check that a code block has been created for the if statement
    Node ifBlock = root.getFirstChild();
    assertTrue(ifBlock.isBlock());
  }

  @Test
  public void testNormalizeNodeTypesControlStructureNodeWithoutCodeBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);

    Node root = IR.if_(IR.literal(Token.BOOL, true), null, IR.literal(Token.NUMBER, 2));
    prepareAst.process(null, root);

    // Check that a code block has not been created for the if statement
    assertNull(root.getFirstChild());
  }

  @Test(expected = AssertionError.class)
  public void testNormalizeNodeTypesCheckOnlyConstrainViolation() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, true);
    Node root = IR.if_(IR.literal(Token.BOOL, true), IR.literal(Token.NUMBER, 1), IR.literal(Token.NUMBER, 2));
    prepareAst.process(null, root);
  }

  @Test
  public void testPrepareAnnotationsSimpleFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);

    Node n = IR.function(Token.NONE, null, IR.block(), null);
    prepareAst.process(null, null);
    NodeTraversal.traverse(compiler, n, new PrepareAst.PrepareAnnotations());

    // Check that there are no annotations on the function
    assertNull(n.getJSDocInfo());
  }

  @Test
  public void testPrepareAnnotationsFunctionWithJSDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);

    Node n = IR.function(Token.NONE, null, IR.block(), null);
    n.setJSDocInfo(new JsDocInfo("MyFunction", "Description of function"));
    prepareAst.process(null, null);

    // Check that the JSDoc information has been copied to the first child
    Node funcDecl = n.getFirstChild();
    assertEquals("MyFunction", funcDecl.getJSDocInfo().getName());
    assertEquals("Description of function", funcDecl.getJSDocInfo().getDescription());
  }
}