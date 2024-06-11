import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
public class RenameLabelsTest {

  @Test
  public void testProcessLabels() {
    AbstractCompiler compiler = new DummyCompiler();
    Supplier<String> supplier = new TestNameSupplier();
    RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);

    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);

    renameLabels.process(externs, root);

    // assertSomething()
  }
  
  private static class TestNameSupplier implements Supplier<String> {
    private List<String> names;
    private int currentIndex;
    
    public TestNameSupplier() {
      names = new ArrayList<String>();
      names.add("a");
      names.add("b");
      names.add("c");
    }
    
    @Override
    public String get() {
      if (currentIndex < names.size()) {
        String name = names.get(currentIndex);
        currentIndex++;
        return name;
      } else {
        return "";
      }
    }
  }
  
  private static class DummyCompiler extends AbstractCompiler {
    
    @Override
    public void addToDebugLog(String message) {
      // do nothing
    }
    
    @Override
    public void reportCodeChange() {
      // do nothing
    }
  }

  // test visitLabel method
  
  @Test
  public void testVisitLabel_labelUsed_shouldChangeName() {
    Node node = createNode(Token.LABEL, createNode(Token.NAME, "labelName"));
    Node parent = new Node(Token.SCRIPT);
  
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    processLabels.names.add("newLabelName");
  
    processLabels.visitLabel(node, parent);
  
    assertEquals("newLabelName", node.getFirstChild().getString());
  }
  
  @Test
  public void testVisitLabel_labelUnused_shouldRemoveLabel() {
    Node node = createNode(Token.LABEL, createNode(Token.NAME, "labelName"));
    Node parent = createNode(Token.BLOCK, node, createNode(Token.BLOCK));
  
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    processLabels.labelInfo.put("labelName", new RenameLabels.LabelInfo(1));
  
    processLabels.visitLabel(node, parent);
  
    assertEquals(parent, node.getParent());
    assertEquals(1, parent.getChildCount());
  }
  
  // test visitBreakOrContinue method

  @Test
  public void testVisitBreakOrContinue_labelUsed_shouldChangeName() {
    Node node = createNode(Token.BREAK, createNode(Token.NAME, "labelName"));
  
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    processLabels.names.add("newLabelName");
    processLabels.labelInfo.put("labelName", new RenameLabels.LabelInfo(1));
  
    processLabels.visitBreakOrContinue(node);
  
    assertEquals("newLabelName", node.getFirstChild().getString());
  }
  
  @Test
  public void testVisitBreakOrContinue_labelUnused_shouldNotChangeName() {
    Node node = createNode(Token.BREAK, createNode(Token.NAME, "labelName"));
  
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    processLabels.names.add("newLabelName");
  
    processLabels.visitBreakOrContinue(node);
  
    assertEquals("labelName", node.getFirstChild().getString());
  }
  
  // test getNameForId method
  
  @Test
  public void testGetNameForId_id1_shouldReturnA() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    processLabels.names.add("a");
    processLabels.names.add("b");
  
    String result = processLabels.getNameForId(1);
  
    assertEquals("a", result);
  }
  
  @Test
  public void testGetNameForId_id2_shouldReturnB() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    processLabels.names.add("a");
    processLabels.names.add("b");
  
    String result = processLabels.getNameForId(2);
  
    assertEquals("b", result);
  }
  
  // test getLabelInfo method
  
  @Test
  public void testGetLabelInfo_labelExists_shouldReturnLabelInfo() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    processLabels.labelInfo.put("labelName", new RenameLabels.LabelInfo(1));
  
    RenameLabels.LabelInfo result = processLabels.getLabelInfo("labelName");
  
    assertEquals(1, result.id);
  }
  
  @Test
  public void testGetLabelInfo_labelNotExists_shouldReturnNull() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
  
    RenameLabels.LabelInfo result = processLabels.getLabelInfo("labelName");
  
    assertEquals(null, result);
  }
  
  // test enterScope method
  
  @Test
  public void testEnterScope_shouldPushNewLabelNamespace() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
  
    processLabels.enterScope(null);
  
    assertEquals(2, processLabels.namespaceStack.size());
  }
  
  // test exitScope method
  
  @Test
  public void testExitScope_shouldPopLabelNamespace() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    processLabels.namespaceStack.push(new RenameLabels.LabelNamespace());
  
    processLabels.exitScope(null);
  
    assertEquals(0, processLabels.namespaceStack.size());
  }

  // test shouldTraverse method
  
  @Test
  public void testShouldTraverse_nodeWithTypeLabel_shouldStoreContextForLabelName() {
    Node node = createNode(Token.LABEL, createNode(Token.NAME, "labelName"));
    Node parent = new Node(Token.SCRIPT);
  
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
  
    boolean result = processLabels.shouldTraverse(null, node, parent);
  
    assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("labelName"));
  }
  
  // helper method
  
  private Node createNode(int type, Node... children) {
    Node node = new Node(type);
    for (Node child : children) {
      node.addChildToBack(child);
    }
    return node;
  }

}