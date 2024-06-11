import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

  private PrepareAst prepareAst;

  @Before
  public void setUp() {
    prepareAst = new PrepareAst(new AbstractCompiler());
  }

  @Test
  public void testNormalizeBlocks() {
    Node n = IR.block().srcref(0);
    Node ifNode = IR.ifNode(IR.trueNode(), IR.block().srcref(1), null).srcref(2);
    Node whileNode = IR.whileNode(IR.trueNode(), IR.block().srcref(3)).srcref(4);
    n.addChildToBack(ifNode);
    n.addChildToBack(whileNode);

    prepareAst.normalizeBlocks(n);

    assertTrue(NodeUtil.isControlStructureCodeBlock(n, ifNode.getFirstChild()));
    assertTrue(NodeUtil.isControlStructureCodeBlock(n, whileNode.getFirstChild()));
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node n = IR.block().srcref(0);
    Node exprVoidNode = IR.exprResult(IR.voidNode(IR.name("test")).srcref(1)).srcref(2);
    n.addChildToBack(exprVoidNode);

    prepareAst.normalizeNodeTypes(n);

    assertTrue(exprVoidNode.isExprResult());
    assertFalse(exprVoidNode.isExprResult(IR.name("test")));
  }

  @Test
  public void testPrepareAnnotations_shouldTraverseObjectLit() {
    Node objlit = IR.objectlit().srcref(0);
    prepareAst.prepareAnnotations.shouldTraverse(new NodeTraversal(), objlit, null);
    // TODO: Add assertions
  }

  @Test
  public void testPrepareAnnotations_visitCall() {
    Node callNode = IR.call().srcref(0);
    prepareAst.prepareAnnotations.visit(new NodeTraversal(), callNode, null);
    // TODO: Add assertions
  }

  @Test
  public void testPrepareAnnotations_visitFunction() {
    Node functionNode = IR.function().srcref(0);
    prepareAst.prepareAnnotations.visit(new NodeTraversal(), functionNode, null);
    // TODO: Add assertions
  }

  @Test
  public void testPrepareAnnotations_normalizeObjectLiteralAnnotations() {
    Node objlit = IR.objectlit().srcref(0);
    Node key1 = IR.name("key1").srcref(1);
    Node key2 = IR.name("key2").srcref(2);
    objlit.addChildToBack(key1);
    objlit.addChildToBack(key2);

    prepareAst.prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);

    // TODO: Add assertions
  }

  @Test
  public void testPrepareAnnotations_annotateCalls() {
    Node callNode = IR.call().srcref(0);
    prepareAst.prepareAnnotations.annotateCalls(callNode);
    // TODO: Add assertions
  }

  @Test
  public void testPrepareAnnotations_annotateDispatchers() {
    Node functionNode = IR.function().srcref(0);
    Node parent = IR.assign(IR.name("x"), functionNode).srcref(1);
    prepareAst.prepareAnnotations.annotateDispatchers(functionNode, parent);
    // TODO: Add assertions
  }

  @Test
  public void testPrepareAnnotations_normalizeObjectLiteralKeyAnnotations() {
    Node objlit = IR.objectlit().srcref(0);
    Node key = IR.name("key").srcref(1);
    Node value = IR.function().srcref(2);
    objlit.addChildToBack(key);
    key.addChildToBack(value);

    prepareAst.prepareAnnotations.normalizeObjectLiteralKeyAnnotations(objlit, key, value);

    // TODO: Add assertions
  }

  @Test
  public void testProcess() {
    Node externs = IR.block().srcref(0);
    Node root = IR.block().srcref(1);

    prepareAst.process(externs, root);

    // TODO: Add assertions
  }
}