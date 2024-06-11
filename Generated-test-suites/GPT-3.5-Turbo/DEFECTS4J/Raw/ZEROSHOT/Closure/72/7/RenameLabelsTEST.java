import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DefaultNameSupplier;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

public class RenameLabelsTest {

  private RenameLabels renameLabels;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
  }

  @Test
  public void testEnterScope() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    processLabels.enterScope(null);
    assertEquals(1, processLabels.namespaceStack.size());
    assertTrue(processLabels.namespaceStack.peek() instanceof RenameLabels.LabelNamespace);
  }

  @Test
  public void testExitScope() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    processLabels.enterScope(null);
    processLabels.exitScope(null);
    assertEquals(0, processLabels.namespaceStack.size());
  }

  @Test
  public void testShouldTraverse() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null);
    Node node = new Node(Token.LABEL);
    node.addChildToFront(new Node(Token.STRING, "labelName"));
    Node parent = new Node(Token.FUNCTION);
    assertTrue(processLabels.shouldTraverse(nodeTraversal, node, parent));
    assertEquals(1, processLabels.namespaceStack.size());
    assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("labelName"));
  }

  @Test
  public void testVisitLabel() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null);
    Node node = new Node(Token.LABEL);
    Node nameNode = new Node(Token.STRING, "labelName");
    node.addChildToFront(nameNode);
    Node parent = new Node(Token.FUNCTION);
    processLabels.namespaceStack.peek().renameMap.put("labelName", processLabels.new LabelInfo(1));
    processLabels.visitLabel(node, parent);

    assertFalse(processLabels.namespaceStack.peek().renameMap.containsKey("labelName"));
    assertEquals(0, node.getChildCount());
    assertEquals(Token.REGEXP, node.getType());
    assertEquals(1, compiler.getChangeCount());
  }

  @Test
  public void testVisitBreakOrContinue() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null);
    Node node = new Node(Token.BREAK);
    Node nameNode = new Node(Token.STRING, "labelName");
    node.addChildToFront(nameNode);
    processLabels.namespaceStack.peek().renameMap.put("labelName", processLabels.new LabelInfo(1));
    processLabels.visitBreakOrContinue(node);

    assertEquals("a", nameNode.getString());
    assertEquals(1, compiler.getChangeCount());
  }

  @Test
  public void testProcess() {
    Node externs = new Node(Token.EMPTY);
    Node root = new Node(Token.EMPTY);
    renameLabels.process(externs, root);
  }

}