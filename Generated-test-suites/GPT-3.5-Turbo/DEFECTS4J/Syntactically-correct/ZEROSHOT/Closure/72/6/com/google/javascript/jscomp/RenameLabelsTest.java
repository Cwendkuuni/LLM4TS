import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);

    // Test enterScope()
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    Node node = new Node(1, new Node(2), null);
    renameLabels.enterScope(nodeTraversal);
    assertEquals(1, renameLabels.namespaceStack.size());
    renameLabels.exitScope(nodeTraversal);

    // Test exitScope()
    renameLabels.enterScope(nodeTraversal);
    renameLabels.enterScope(nodeTraversal);
    assertEquals(2, renameLabels.namespaceStack.size());
    renameLabels.exitScope(nodeTraversal);
    assertEquals(1, renameLabels.namespaceStack.size());

    // Test shouldTraverse()
    node = new Node(Token.LABEL);
    node.addChildToFront(new Node("test"));
    Node parent = new Node();
    renameLabels.shouldTraverse(nodeTraversal, node, parent);
    assertEquals(1, renameLabels.namespaceStack.size());

    // Test visit()
    renameLabels.visit(nodeTraversal, node, parent);
    assertEquals(0, renameLabels.namespaceStack.size());
  }
}