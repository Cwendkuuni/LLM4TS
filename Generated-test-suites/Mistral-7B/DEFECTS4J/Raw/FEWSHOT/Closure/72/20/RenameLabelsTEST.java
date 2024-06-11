package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RenameLabelsTest {

  @Test
  public void testProcessLabels() {
    RenameLabels renameLabels = new RenameLabels(null);
    ProcessLabels processLabels = new ProcessLabels();

    // Test empty tree
    Node root = Node.createTree();
    renameLabels.process(null, root);
    assertTrue(processLabels.namespaceStack.isEmpty());

    // Test single label
    root = Node.createLabel("a");
    renameLabels.process(null, root);
    LabelInfo labelInfo = processLabels.namespaceStack.peek().renameMap.get("a");
    assertSame(labelInfo, processLabels.namespaceStack.peek().renameMap.values().iterator().next());
    assertNull(labelInfo.referenced);

    // Test multiple labels
    root = Node.createBlock("{a: {} b: {} a: {} b: {}}");
    renameLabels.process(null, root);
    LabelNamespace current = processLabels.namespaceStack.peek();
    Map<String, LabelInfo> renameMap = current.renameMap;
    LabelInfo a1 = renameMap.get("a");
    LabelInfo a2 = renameMap.get("a");
    LabelInfo b1 = renameMap.get("b");
    LabelInfo b2 = renameMap.get("b");

    // Check labels are unique within the same scope
    assertSame(a1, a2);
    assertSame(b1, b2);

    // Check label referencing works
    root = Node.createBlock("{a: {break a;}}");
    renameLabels.process(null, root);
    labelInfo = processLabels.namespaceStack.peek().renameMap.get("a");
    assertTrue(labelInfo.referenced);

    // Check label removal works
    root = Node.createBlock("{a: {}}");
    renameLabels.process(null, root);
    assertNull(processLabels.namespaceStack.peek().renameMap.get("a"));

    // Check label removal with break statement works
    root = Node.createBlock("{a: {break a;}}");
    renameLabels.process(null, root);
    labelInfo = processLabels.namespaceStack.peek().renameMap.get("a");
    assertSame(labelInfo, processLabels.namespaceStack.peek().renameMap.values().iterator().next());
    assertTrue(labelInfo.referenced);

    // Test nested labels
    root = Node.createBlock("{a: {b: {}} b: {a: {}} a: {b: {}} b: {}}");
    renameLabels.process(null, root);
    current = processLabels.namespaceStack.peek();
    LabelNamespace outer = processLabels.namespaceStack.getLast();
    LabelInfo a1 = current.renameMap.get("a");
    LabelInfo a2 = outer.renameMap.get("a");
    LabelInfo b1 = current.renameMap.get("b");
    LabelInfo b2 = outer.renameMap.get("b");

    // Check labels are unique across nested scopes
    assertSame(a1, a2);
    assertSame(b1, b2);

    // Test labels are renamed within each scope
    String a = getNameForId(a1.id);
    String b = getNameForId(b1.id);
    assertTrue(a.length() > 0);
    assertTrue(b.length() > 0);
    assertFalse(a.equals(b));
  }

  @Test
  public void testRemoveUnusedLabels() {
    RenameLabels renameLabels = new RenameLabels(null, new RemoveUnusedNameSupplier(), true);

    // Test empty tree
    Node root = Node.createTree();
    renameLabels.process(null, root);
    assertTrue(renameLabels.processLabels.namespaceStack.isEmpty());

    // Test single label
    root = Node.createLabel("a");
    renameLabels.process(null, root);
    LabelNamespace current = renameLabels.processLabels.namespaceStack.peek();
    Map<String, LabelInfo> renameMap = current.renameMap;
    LabelInfo labelInfo = renameMap.get("a");
    assertNull(labelInfo.referenced);
    assertNull(renameMap.get("a"));

    // Test multiple labels
    root = Node.createBlock("{a: {} b: {} a: {} b: {}}");
    renameLabels.process(null, root);
    current = renameLabels.processLabels.namespaceStack.peek();
    renameMap = current.renameMap;
    labelInfo = renameMap.get("a");
    assertNull(labelInfo.referenced);
    assertNull(renameMap.get("a"));
    labelInfo = renameMap.get("b");
    assertNull(labelInfo.referenced);
    assertNull(renameMap.get("b"));

    // Test labeled statements
    root = Node.createBlock("{a: {break a;}}");
    renameLabels.process(null, root);
    current = renameLabels.processLabels.namespaceStack.peek();
    renameMap = current.renameMap;
    labelInfo = renameMap.get("a");
    assertNull(labelInfo.referenced);
    assertNull(renameMap.get("a"));

    // Test labeled block with break statement
    root = Node.createBlock("{a: {break a;}}");
    renameLabels.process(null, root);
    current = renameLabels.processLabels.namespaceStack.peek();
    renameMap = current.renameMap;
    labelInfo = renameMap.get("a");
    assertSame(labelInfo, processLabels.namespaceStack.peek().renameMap.values().iterator().next());
    assertFalse(labelInfo.referenced);

    // Test nested labels
    root = Node.createBlock("{a: {b: {}} b: {a: {}} a: {b: {}} b: {}}");
    renameLabels.process(null, root);
    current = renameLabels.processLabels.namespaceStack.peek();
    LabelNamespace outer = renameLabels.processLabels.namespaceStack.getLast();
    LabelInfo a1 = current.renameMap.get("a");
    LabelInfo a2 = outer.renameMap.get("a");
    LabelInfo b1 = current.renameMap.get("b");
    LabelInfo b2 = outer.renameMap.get("b");

    // Check labels are removed if not used
    assertNull(a1);
    assertNull(a2);
    assertNull(b1);
    assertNull(b2);
  }

  private String getNameForId(int id) {
    ProcessLabels processLabels = RenameLabelsTest.this.renameLabels.processLabels;
    LabelNamespace current = processLabels.namespaceStack.peek();
    ArrayList<String> names = processLabels.names;
    return names.get(id - 1);
  }

  private RenameLabels renameLabels = new RenameLabels(null);

}

class RemoveUnusedNameSupplier implements Supplier<String> {
  @Override
  public String get() {
    return RenameLabelsTest.this.renameLabels.nameSupplier.get();
  }
}