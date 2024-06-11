import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

  private AbstractCompiler compiler;
  private Node root;

  @Before
  public void setUp() {
    compiler = new Compiler();
    root = NodeOperations.createRoot();
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node ifNode = NodeOperations.createIfStatement();
    Node ifBlock = NodeOperations.createBlock();
    ifNode.addChildToBack(ifBlock);
    root.addChildToBack(ifNode);

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);
    assertTrue(ifBlock.isBlock());
  }

  @Test
  public void testNormalizeBlocks() {
    Node ifNode = NodeOperations.createIfStatement();
    Node ifBody = NodeOperations.createExpressionStatement(
        NodeOperations.createName("x").set("10"));
    ifNode.addChildToBack(ifBody);
    root.addChildToBack(ifNode);

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    Callback callback = new PrepareAst.PrepareAnnotations();
    traverse(compiler, root, callback);

    assertTrue(ifBody.isBlock());
  }

  @Test
  public void testAnnotateCalls() {
    Node callNode = NodeOperations.createCallExpression(
        NodeOperations.createName("myFunction"),
        new Node[] {NodeOperations.createNumber(1)});
    Node functionNode = NodeOperations.createFunctionExpression(
        NodeOperations.createName("myFunction"),
        new Node[0],
        NodeOperations.createBlock());
    root.addChildToBack(functionNode);
    functionNode.addChildToBack(callNode);

    PrepareAst prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);

    Callback callback = new PrepareAst.PrepareAnnotations();
    traverse(compiler, root, callback);

    assertTrue(callNode.isFreeCall());
  }

  @Test
  public void testAnnotateDispatchers() {
    Node obj = NodeOperations.createObjectLiteral();
    Node func = NodeOperations.createFunctionExpression(
        NodeOperations.createName("myFunction"),
        new Node[0],
        NodeOperations.createBlock());
    Node assign = NodeOperations.createAssign(
        NodeOperations.createName("x"), func);
    obj.addChildToBack(assign);
    root.addChildToBack(obj);

    PrepareAst prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);

    Callback callback = new PrepareAst.PrepareAnnotations();
    traverse(compiler, root, callback);

    assertTrue(func.isDispatcher());
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node objlit = NodeOperations.createObjectLiteral();
    Node key = NodeOperations.createName("a");
    Node value = NodeOperations.createFunctionExpression(
        NodeOperations.createName("getValue"),
        new Node[0],
        NodeOperations.createBlock());
    Node prop = NodeOperations.createProperty(key, value);
    objlit.addChildToBack(prop);
    root.addChildToBack(objlit);

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    Callback callback = new PrepareAst.PrepareAnnotations();
    traverse(compiler, root, callback);

    assertTrue(value.getJSDocInfo() != null);
  }
}