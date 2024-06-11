import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
  }

  @Test
  public void testPrepareAst() {
    Node root = new Node(Token.BLOCK);
    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);
    verifyPrepareAst(root);
  }

  @Test
  public void testCheckOnlyPrepareAst() {
    Node root = new Node(Token.BLOCK);
    PrepareAst prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);
    verifyPrepareAst(root, true);
  }

  private void verifyPrepareAst(Node root, boolean checkOnly) {
    assertTrue(root.isBlock());

    if (!checkOnly) {
      NodeTraversal.traverse(compiler, root, new VerifyPrepareAst());
    }

    if (checkOnly) {
      verifyCheckOnlyPrepareAst(root);
    }
  }

  /**
   * Verifies that the PrepareAst pass did not change the root node.
   */
  private void verifyCheckOnlyPrepareAst(Node root) {
    assertFalse(root.hasProperty(Node.IS_DISPATCHER));
    assertNull(root.getFirstChild());
  }

  /**
   * Verifies that the PrepareAst pass added a block node to control structures
   * and added annotations to calls and functions.
   */
  static class VerifyPrepareAst implements NodeTraversal.Callback {

    @Override
    public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
      if (n.isBlock()) {
        return false;
      }
      if (n.isLabel() || n.isSwitch()) {
        return false;
      }
      if (n.isObjectLit()) {
        Node first = n.getFirstChild();
        Node value = first.getFirstChild();
        if (value.isFunction() && first.getJSDocInfo() != null) {
          assertTrue(value.hasProperty(Node.IS_DISPATCHER));
        }
        return false;
      }
      if (n.isCall()) {
        Node first = n.getFirstChild();
        assertTrue(first.isName() || first.isGet());
        if (first.getString().equals("eval")) {
          assertTrue(n.hasProperty(Node.DIRECT_EVAL));
        }
      }
      return true;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      // Intentionally empty.
    }
  }
}