import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.IR;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAnnotations;
import com.google.javascript.jscomp.Preconditions.*;

import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testProcess_NullExternsAndRoot() {
    PrepareAst prepareAst = new PrepareAst(null);
    prepareAst.process(null, null);
  }

  @Test
  public void testProcess_NotNullExternsAndRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);
    Node externs = createNode("{}");
    Node root = createNode("function(){}");
    prepareAst.process(externs, root);
  }

  @Test
  public void testProcess_CheckOnly() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, true);
    Node externs = createNode("{}");
    Node root = createNode("function(){}");
    prepareAst.process(externs, root);
  }

  @Test
  public void testNormalizeNodeTypes_NullNode() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node n = null;
    prepareAst.normalizeNodeTypes(n);
  }

  @Test(expected = IllegalStateException.class)
  public void testNormalizeNodeTypes_NullParent() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node n = createNode("{}");
    prepareAst.normalizeNodeTypes(n);
    n.setParent(null);
  }

  @Test
  public void testNormalizeNodeTypes() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    Node root = createNode("if (true) { return 1; }");
    prepareAst.normalizeNodeTypes(root);
    assertFalse(NodeUtil.isControlStructure(root));
    Node ifNode = getFirstChildWithType(root, Token.IF);
    assertTrue(ifNode.isControlStructure());
    Node block = getFirstChildWithType(ifNode, Token.BLOCK);
    assertTrue(block.isBlock());
  }

  @Test
  public void testNormalizeBlocks_NullNode() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node n = null;
    prepareAst.normalizeBlocks(n);
  }

  @Test
  public void testNormalizeBlocks() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    Node root = createNode("if (true) {} else {}");
    prepareAst.normalizeBlocks(root);
    Node ifNode = getFirstChildWithType(root, Token.IF);
    Node ifBlock = getFirstChildWithType(ifNode, Token.BLOCK);
    Node elseBlock = getNextSiblingWithType(ifBlock, Token.BLOCK);
    assertNotNull(ifBlock);
    assertNotNull(elseBlock);
  }

  @Test
  public void testPrepareAnnotations_NullNode() {
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    Node n = null;
    prepareAnnotations.visit(null, n, null);
  }

  @Test
  public void testPrepareAnnotations() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    Node root = createNode("function(){}");
    NodeTraversal.traverse(prepareAst, root, prepareAnnotations);
    assertFalse(root.isFunction());
    assertTrue(getFirstChildWithType(root, Token.BLOCK).isBlock());
  }

  @Test
  public void testAnnotateCalls() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    Node root = createNode("eval()");
    NodeTraversal.traverse(prepareAst, root, prepareAnnotations);
    assertTrue(root.getFirstChild().isName());
    assertTrue(root.getFirstChild().getString().equals("eval"));
    assertTrue(root.getFirstChild().getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testAnnotateDispatchers() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    Node obj = createNode("{ get x() { return 1; } }");
    Node method = getFirstChildWithType(obj, Token.FUNCTION);
    NodeTraversal.traverse(prepareAst, obj, prepareAnnotations);
    assertTrue(method.isFunction());
    assertTrue(obj.getFirstChild().isAssign());
    assertTrue(obj.getFirstChild().getLastChild() == method);
    assertTrue(method.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    Node objlit = createNode("{@type: 'foo'} a: @{type: 'bar'}");
    Node keyA = getFirstChildWithType(objlit, Token.IDENT);
    Node valueA = getNextSibling(keyA);
    Node keyB = getNextSiblingWithType(valueA, Token.IDENT);
    Node valueB = getNextSibling(keyB);
    NodeTraversal.traverse(prepareAst, objlit, prepareAnnotations);
    assertEquals("bar", valueA.getString());
    assertEquals("foo", valueB.getString());
  }

  private Node createNode(String nodeCode) {
    Node node = IR.parseTree(nodeCode, null, null, null);
    node.setSourceFile(new java.io.File("Test.js"));
    return node;
  }

  private <T extends Node> T getFirstChildWithType(Node parent, int nodeType) {
    Node child = parent.getFirstChild();
    while (child != null) {
      if (child.getType() == nodeType) {
        return (T) child;
      }
      child = child.getNext();
    }
    return null;
  }

  private Node getNextSiblingWithType(Node node, int nodeType) {
    Node sibling = node.getNext();
    while (sibling != null && sibling.getType() != nodeType) {
      sibling = sibling.getNext();
    }
    return sibling;
  }

  private Node getNextSibling(Node node) {
    Node sibling = node.getNext();
    if (sibling == null) {
      throw new IllegalStateException("No next sibling.");
    }
    return sibling;
  }
}