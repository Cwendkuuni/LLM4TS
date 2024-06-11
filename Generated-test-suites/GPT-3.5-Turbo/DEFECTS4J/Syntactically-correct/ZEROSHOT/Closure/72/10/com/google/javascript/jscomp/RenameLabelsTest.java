import org.junit.*;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;

public class RenameLabelsTest {

  @Test
  public void testProcessLabels() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    
    // Test enterScope()
    processLabels.enterScope(null);
    assertEquals(1, processLabels.namespaceStack.size());

    // Test exitScope()
    processLabels.exitScope(null);
    assertEquals(0, processLabels.namespaceStack.size());
    
    // Test getNameForId()
    processLabels.names.add("a");
    processLabels.names.add("b");
    
    assertEquals("a", processLabels.getNameForId(1));
    assertEquals("b", processLabels.getNameForId(2));
    
    // Test getLabelInfo()
    RenameLabels.LabelNamespace labelNamespace = renameLabels.new LabelNamespace();
    RenameLabels.LabelInfo labelInfo = renameLabels.new LabelInfo(1);
    labelNamespace.renameMap.put("label", labelInfo);
    processLabels.namespaceStack.push(labelNamespace);
    
    assertEquals(labelInfo, processLabels.getLabelInfo("label"));
    
    // Test visitBreakOrContinue() - referenced label
    RenameLabels.LabelInfo referencedLabel = renameLabels.new LabelInfo(1);
    labelNamespace.renameMap.put("referencedLabel", referencedLabel);
    Node breakNode = new Node(Token.BREAK);
    Node nameNode = new Node(Token.NAME, "referencedLabel");
    breakNode.addChildToBack(nameNode);
    processLabels.visitBreakOrContinue(breakNode);
    assertEquals("a", nameNode.getString());
    assertTrue(referencedLabel.referenced);

    // Test visitBreakOrContinue() - unreferenced label
    RenameLabels.LabelInfo unreferencedLabel = renameLabels.new LabelInfo(2);
    labelNamespace.renameMap.put("unreferencedLabel", unreferencedLabel);
    Node continueNode = new Node(Token.CONTINUE);
    Node nameNode2 = new Node(Token.NAME, "unreferencedLabel");
    continueNode.addChildToBack(nameNode2);
    processLabels.visitBreakOrContinue(continueNode);
    assertEquals("b", nameNode2.getString());
    assertFalse(unreferencedLabel.referenced);
    
    // Test visitLabel() - referenced label
    RenameLabels.LabelInfo referencedLabel2 = renameLabels.new LabelInfo(1);
    labelNamespace.renameMap.put("referencedLabel2", referencedLabel2);
    Node labelNode = new Node(Token.LABEL);
    Node nameNode3 = new Node(Token.NAME, "referencedLabel2");
    labelNode.addChildToBack(nameNode3);
    processLabels.visitLabel(labelNode, null);
    assertEquals("a", nameNode3.getString());
    assertTrue(referencedLabel2.referenced);
    
    // Test visitLabel() - unreferenced label
    RenameLabels.LabelInfo unreferencedLabel2 = renameLabels.new LabelInfo(2);
    labelNamespace.renameMap.put("unreferencedLabel2", unreferencedLabel2);
    Node labelNode2 = new Node(Token.LABEL);
    Node nameNode4 = new Node(Token.NAME, "unreferencedLabel2");
    labelNode2.addChildToBack(nameNode4);
    processLabels.visitLabel(labelNode2, null);
    assertEquals("", labelNode2.getFirstChild().getString());
    assertFalse(labelNamespace.renameMap.containsKey("unreferencedLabel2"));

  }

  @Test
  public void testProcess() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    Node externs = new Node(Token.ROOT);
    Node root = new Node(Token.ROOT);
    renameLabels.process(externs, root);
  }
  
}