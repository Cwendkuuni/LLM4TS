import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class PrepareAstTest {

  private AbstractCompiler compiler;
  private CompilerPass prepareAst;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    prepareAst = new PrepareAst(compiler);
  }

  @Test
  public void testProcessExternsAndRootNull() {
    Node externs = null;
    Node root = null;
    prepareAst.process(externs, root);
  }

  @Test
  public void testProcessExternsAndRootNotNull() {
    Node externs = IR.objectLit().srcref(new Object());
    Node root = IR.identifier("test").srcref(new Object());
    prepareAst.process(externs, root);
  }

  @Test
  public void testNormalizeNodeTypesSimpleTree() {
    Node root = IR.identifier("test").srcref(new Object());
    Node child = IR.identifier("child").srcref(new Object());
    root.addChild(child);
    prepareAst.normalizeNodeTypes(root);
    Node expected = IR.block().srcref(new Object());
    Preconditions.checkState(root.getFirstChild() == expected);
    expected.addChildrenToFront(child);
  }

  @Test
  public void testNormalizeBlocksSimpleIfStatement() {
    Node ifNode = IR.ifExpression(IR.booleanLiteral(true), IR.block(), IR.block());
    prepareAst.normalizeBlocks(ifNode);
    Node expected = IR.block();
    Preconditions.checkState(ifNode.getFirstChild() == expected);
    expected.addChildrenToFront(ifNode.getFirstChild().getNext());
  }

  @Test
  public void testNormalizeBlocksNestedIfStatement() {
    Node ifNode1 = IR.ifExpression(IR.booleanLiteral(true), IR.block(), IR.block());
    Node ifNode2 = IR.ifExpression(IR.booleanLiteral(true), IR.block(), IR.block());
    ifNode1.addChild(ifNode2);
    prepareAst.normalizeBlocks(ifNode1);

    Node expected = IR.block();
    Preconditions.checkState(ifNode1.getFirstChild() == expected);
    expected.addChildrenToFront(ifNode1.getFirstChild().getNext());

    Node ifBlock1 = IR.block();
    Node ifBlock2 = IR.block();
    expected.addChildrenToFront(ifBlock1);
    ifBlock1.addChildrenToFront(ifNode2);
  }

  @Test
  public void testPrepareAnnotationsSimpleObjectLiteral() {
    Node objLit = IR.objectLit().srcref(new Object());
    prepareAst.process(null, objLit);
    Node expected = IR.objectLit().srcref(new Object());
    Preconditions.checkState(objLit.isObjectLit());
    Preconditions.checkState(objLit.getFirstChild() == expected.getFirstChild());
    Node key = IR.stringLiteral("key");
    Node value = IR.stringLiteral("value");
    objLit.addChild(key);
    objLit.addChild(value);
    prepareAst.process(null, objLit);

    Node annotatedObjLit = expected.getFirstChild();
    Node annotatedKey = annotatedObjLit.getFirstChild();
    Node annotatedValue = annotatedObjLit.getFirstChild().getNext();

    // JsDoc should be present on both key and value.
    Preconditions.checkState(annotatedKey.getJSDocInfo() != null);
    Preconditions.checkState(annotatedValue.getJSDocInfo() != null);
  }

  @Test
  public void testPrepareAnnotationsCalls() {
    Node callNode = IR.callExpression(IR.identifier("eval"), IR.list(), null);
    prepareAst.process(null, callNode);
    Node expected = IR.callExpression(IR.identifier("eval"), IR.list(), null);
    Preconditions.checkState(callNode.isCall());

    // Direct eval should be marked.
    Preconditions.checkState(callNode.getFirstChild().isName());
    Preconditions.checkState(callNode.getFirstChild().getString().equals("eval"));
    Preconditions.checkState(callNode.getFirstChild().getBooleanProp(Node.DIRECT_EVAL));
  }
}