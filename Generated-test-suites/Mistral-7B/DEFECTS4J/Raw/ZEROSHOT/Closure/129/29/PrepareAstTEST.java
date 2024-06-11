import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.PrepareAnnotations;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

  private AbstractCompiler compiler;
  private PrepareAst prepareAst;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    prepareAst = new PrepareAst(compiler);
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node root = makeNode("if (true) {}");
    prepareAst.process(null, root);

    Node ifNode = root.getFirstChild();
    assertTrue(ifNode.isIf());
    Node blockNode = ifNode.getFirstChild();
    assertTrue(blockNode.isBlock());
  }

  @Test
  public void testNormalizeNodeTypesWithNoBlock() {
    Node root = makeNode("if (true)");
    prepareAst.process(null, root);

    Node ifNode = root.getFirstChild();
    assertTrue(ifNode.isIf());
    assertNull(ifNode.getFirstChild());
  }

  @Test
  public void testNormalizeNodeTypesCheckOnly() {
    prepareAst = new PrepareAst(compiler, true);
    Node root = makeNode("if (true) {}");
    prepareAst.process(null, root);

    Node ifNode = root.getFirstChild();
    assertTrue(ifNode.isIf());
    Node blockNode = ifNode.getFirstChild();
    assertTrue(blockNode.isBlock());

    Preconditions.checkState(false, "normalizeNodeTypes constraints violated");
  }

  @Test
  public void testPrepareAnnotations() {
    Node root = makeNode("var x = { a: function() {} }");
    prepareAst.process(null, root);

    Node objectLit = root.getFirstChild();
    assertFalse(objectLit.getJSDocInfo() == null);

    Node functionNode = objectLit.getLastChild();
    assertFalse(functionNode.getJSDocInfo() == null);
    assertFalse(functionNode.getJSDocInfo().isJavaDispatch());

    Node functionLit = functionNode.getFirstChild();
    assertFalse(functionLit.getJSDocInfo() == null);
    assertTrue(functionLit.getJSDocInfo().isJavaDispatch());
  }

  @Test
  public void testPrepareAnnotationsDispatcher() {
    Node root = makeNode("var x = { dispatch: function() {} }");
    prepareAst.process(null, root);

    Node objectLit = root.getFirstChild();
    assertFalse(objectLit.getJSDocInfo() == null);

    Node functionNode = objectLit.getLastChild();
    assertFalse(functionNode.getJSDocInfo() == null);
    assertFalse(functionNode.isAssign());

    Node functionLit = functionNode.getFirstChild();
    assertFalse(functionLit.getJSDocInfo() == null);
    assertFalse(functionLit.getJSDocInfo().isJavaDispatch());

    Node assignNode = functionNode.getPreviousSibling();
    assertTrue(assignNode.isAssign());
    assertTrue(assignNode.getLastChild() == functionNode);
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testPrepareAnnotationsDirectEval() {
    Node root = makeNode("(eval)()");
    prepareAst.process(null, root);

    Node callNode = root.getFirstChild();
    assertTrue(callNode.isCall());
    assertTrue(callNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotationsFreeCall() {
    Node root = makeNode("f()");
    prepareAst.process(null, root);

    Node callNode = root.getFirstChild();
    assertTrue(callNode.isCall());
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
  }

  private Node makeNode(String nodeCode) {
    return NodeTraversal.buildNode(
        compiler.getFactory(), new Node(Token.EMPTY, nodeCode));
  }

  private static boolean NodeUtil_isControlStructure(Node node) {
    return NodeUtil.isControlStructure(node);
  }

  private static boolean NodeUtil_isControlStructureCodeBlock(
      Node parent, Node child) {
    return NodeUtil.isControlStructureCodeBlock(parent, child);
  }
}