import com.google.common.base.Supplier;
import com.google.common.base.Function;
import com.google.common.collect.Sets;
import static org.junit.Assert.*;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class RenameLabelsTest {

  @Test
  public void testEnterScope() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node();
    processLabels.enterScope(nodeTraversal);
    processLabels.shouldTraverse(nodeTraversal, node, parent);

    assertEquals(1, processLabels.namespaceStack.size());
  }

  @Test
  public void testExitScope() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal();
    processLabels.exitScope(nodeTraversal);

    assertEquals(0, processLabels.namespaceStack.size());
  }

  @Test
  public void testShouldTraverse() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node();
    processLabels.shouldTraverse(nodeTraversal, node, parent);

    assertEquals(1, processLabels.namespaceStack.size());
    assertEquals(1, processLabels.namespaceStack.peek().renameMap.size());
    assertEquals(1, processLabels.names.size());
  }

  @Test
  public void testVisitLabel_referenced() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node();
    Node nameNode = new Node(Token.NAME, "name");
    node.addChildToFront(nameNode);
    processLabels.namespaceStack.push(processLabels.new LabelNamespace());
    processLabels.names.add("a");
    processLabels.namespaceStack.peek().renameMap.put("name", processLabels.new LabelInfo(1));

    processLabels.visitLabel(node, parent);

    assertEquals("a", nameNode.getString());
    assertFalse(node.hasChildAtIndex(0));
    assertEquals(0, processLabels.namespaceStack.peek().renameMap.size());
  }

  @Test
  public void testVisitLabel_unreferenced() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node();
    Node nameNode = new Node(Token.NAME, "name");
    node.addChildToFront(nameNode);
    processLabels.namespaceStack.push(processLabels.new LabelNamespace());
    processLabels.names.add("a");
    processLabels.namespaceStack.peek().renameMap.put("name", processLabels.new LabelInfo(1));
    processLabels.namespaceStack.peek().renameMap.get("name").referenced = false;

    processLabels.visitLabel(node, parent);

    assertTrue(node.hasOneChild());
    assertFalse(node.getFirstChild().hasChildren());
    assertEquals(Token.BLOCK, node.getFirstChild().getType());
    assertEquals(0, processLabels.namespaceStack.peek().renameMap.size());
  }

  @Test
  public void testVisitBreakOrContinue() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.BREAK);
    Node nameNode = new Node(Token.NAME, "name");
    node.addChildToFront(nameNode);
    processLabels.namespaceStack.push(processLabels.new LabelNamespace());
    processLabels.names.add("a");
    processLabels.namespaceStack.peek().renameMap.put("name", processLabels.new LabelInfo(1));

    processLabels.visitBreakOrContinue(node);

    assertEquals("a", nameNode.getString());
    assertTrue(processLabels.namespaceStack.peek().renameMap.get("name").referenced);
  }

  @Test
  public void testGetNameForId() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

    processLabels.names.add("a");
    String name = processLabels.getNameForId(1);

    assertEquals("a", name);
  }

  @Test
  public void testGetLabelInfo() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

    processLabels.namespaceStack.push(processLabels.new LabelNamespace());
    processLabels.namespaceStack.peek().renameMap.put("name", processLabels.new LabelInfo(1));

    RenameLabels.LabelInfo li = processLabels.getLabelInfo("name");

    assertEquals(1, li.id);
  }

  @Test
  public void testProcess() {
    RenameLabels renameLabels = new RenameLabels();

    renameLabels.process(new Node(), new Node());

    //..
  }

}