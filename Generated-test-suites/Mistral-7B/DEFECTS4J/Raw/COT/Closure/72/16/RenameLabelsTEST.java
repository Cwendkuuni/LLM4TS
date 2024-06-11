import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.visit;
import static com.google.javascript.jscomp.RenameLabels.LabelInfo.*;
import static com.google.javascript.jscomp.RenameLabels.ProcessLabels.getNameForId;
import static com.google.javascript.jscomp.RenameLabels.ProcessLabels.getLabelInfo;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testRenameLabels_EmptyCode() {
    Node root = Node.newEmpty();
    RenameLabels renamer = new RenameLabels(compiler);
    renamer.process(Node.externalObjects(), root);
    assertEquals("", compiler.getDiagnostics().getErrorCount(), 0);
  }

  @Test
  public void testRenameLabels_SingleLabel() {
    String code = "label a: {}";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    renamer.process(Node.externalObjects(), root);
    assertNotNull(renamer.namespaceStack.peek().renameMap);
    assertEquals(1, renamer.namespaceStack.peek().renameMap.size());
    LabelInfo labelInfo = renamer.namespaceStack.peek().renameMap.get("a");
    assertNotNull(labelInfo);
    assertEquals(1, labelInfo.id);
    assertFalse(labelInfo.referenced);
  }

  @Test
  public void testRenameLabels_NestedLabels() {
    String code = "label a: { label b: {} }";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    renamer.process(Node.externalObjects(), root);
    assertNotNull(renamer.namespaceStack.peek().renameMap);
    assertEquals(2, renamer.namespaceStack.peek().renameMap.size());
    LabelInfo labelB = renamer.namespaceStack.peek().renameMap.get("b");
    assertNotNull(labelB);
    assertEquals(2, labelB.id);
    assertFalse(labelB.referenced);

    LabelInfo labelA = renamer.namespaceStack.getLasts().get(0).renameMap.get("a");
    assertNotNull(labelA);
    assertEquals(1, labelA.id);
    assertFalse(labelA.referenced);
  }

  @Test
  public void testRenameLabels_LabelWithReferences() {
    String code = "label a: { break a; }";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    renamer.process(Node.externalObjects(), root);
    assertNotNull(renamer.namespaceStack.peek().renameMap);
    assertEquals(1, renamer.namespaceStack.peek().renameMap.size());
    LabelInfo labelInfo = renamer.namespaceStack.peek().renameMap.get("a");
    assertNotNull(labelInfo);
    assertEquals(1, labelInfo.id);
    assertTrue(labelInfo.referenced);
  }

  @Test(expected = RuntimeException.class)
  public void testRenameLabels_LabelNameCollision() {
    String code = "label a: {} label a: {}";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    renamer.process(Node.externalObjects(), root);
    fail("Should have thrown an exception");
  }

  @Test
  public void testRenameLabels_RemoveUnusedLabels() {
    String code = "label a: {} label b: {} label a: {}";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler, new RenameLabels.DefaultNameSupplier(), false);
    renamer.process(Node.externalObjects(), root);
    assertNotNull(renamer.namespaceStack.peek().renameMap);
    assertEquals(1, renamer.namespaceStack.peek().renameMap.size());
    LabelInfo labelB = renamer.namespaceStack.peek().renameMap.get("b");
    assertNotNull(labelB);
    assertEquals(1, labelB.id);
    assertTrue(labelB.referenced);

    LabelInfo labelA = renamer.namespaceStack.getLasts().get(1).renameMap.get("a");
    assertNull(labelA);
  }

  @Test
  public void testRenameLabels_LabelInsideFunction() {
    String code = "function foo() { label a: {} }";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    renamer.process(Node.externalObjects(), root);
    assertNotNull(renamer.namespaceStack.peek().renameMap);
    assertEquals(1, renamer.namespaceStack.peek().renameMap.size());
    LabelInfo labelA = renamer.namespaceStack.peek().renameMap.get("a");
    assertNotNull(labelA);
    assertEquals(1, labelA.id);
    assertFalse(labelA.referenced);
  }

  @Test
  public void testRenameLabels_LabelInsideBlock() {
    String code = "function foo() { {} label a: {} }";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    renamer.process(Node.externalObjects(), root);
    assertNotNull(renamer.namespaceStack.peek().renameMap);
    assertEquals(1, renamer.namespaceStack.peek().renameMap.size());
    LabelInfo labelA = renamer.namespaceStack.peek().renameMap.get("a");
    assertNotNull(labelA);
    assertEquals(1, labelA.id);
    assertFalse(labelA.referenced);
  }

  @Test
  public void testRenameLabels_LabelInsideLoop() {
    String code = "for (var i = 0; i < 1; i++) { label a: {} }";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    renamer.process(Node.externalObjects(), root);
    assertNotNull(renamer.namespaceStack.peek().renameMap);
    assertEquals(1, renamer.namespaceStack.peek().renameMap.size());
    LabelInfo labelA = renamer.namespaceStack.peek().renameMap.get("a");
    assertNotNull(labelA);
    assertEquals(1, labelA.id);
    assertFalse(labelA.referenced);
  }

  @Test
  public void testRenameLabels_LabelInsideIf() {
    String code = "if (true) { label a: {} }";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    renamer.process(Node.externalObjects(), root);
    assertNotNull(renamer.namespaceStack.peek().renameMap);
    assertEquals(1, renamer.namespaceStack.peek().renameMap.size());
    LabelInfo labelA = renamer.namespaceStack.peek().renameMap.get("a");
    assertNotNull(labelA);
    assertEquals(1, labelA.id);
    assertFalse(labelA.referenced);
  }

  @Test
  public void testRenameLabels_LabelWithDuplicateName() {
    String code = "label a: {} label a: {}";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    try {
      renamer.process(Node.externalObjects(), root);
      fail("Should have thrown an exception");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testRenameLabels_LabelWithDuplicateNameInsideScope() {
    String code = "function foo() { label a: {} label a: {} }";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    try {
      renamer.process(Node.externalObjects(), root);
      fail("Should have thrown an exception");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testRenameLabels_LabelWithInvalidName() {
    String code = "label 1: {}";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    try {
      renamer.process(Node.externalObjects(), root);
      fail("Should have thrown an exception");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testRenameLabels_LabelWithEmptyName() {
    String code = "label : {}";
    Node root = Node.fromString(code, null);
    RenameLabels renamer = new RenameLabels(compiler);
    try {
      renamer.process(Node.externalObjects(), root);
      fail("Should have thrown an exception");
    } catch (RuntimeException e) {
      // Expected
    }
  }
}