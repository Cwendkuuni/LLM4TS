
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Suppliers;

public class RenameLabelsTest {

  private Compiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
  }

  @Test
  public void testProcessLabels_NoLabels() {
    Node root = new Node(Token.ROOT);
    RenameLabels.ProcessLabels processLabels = new RenameLabels(processor);
    processLabels.process(compiler, root);
    // Verify that no changes were made
    assertFalse(compiler.hasCodeChanged());
  }
  
  @Test
  public void testProcessLabels_OneLabel() {
    Node root = new Node(Token.ROOT,
        new Node(
            new Node(Token.LABEL, new Node(""), new Node(Token.BLOCK))),
        new Node(Token.EXPR_RESULT, Node.newString(Token.BREAK, ""), false));
    RenameLabels.ProcessLabels processLabels = new RenameLabels(processor);
    processLabels.process(compiler, root);
    // Verify that the label and break statements were not changed
    assertFalse(compiler.hasCodeChanged());
  }
  
  @Test
  public void testProcessLabels_UnreferencedLabel() {
    Node root = new Node(Token.ROOT,
        new Node(
            new Node(Token.LABEL, new Node("label"), new Node(Token.BLOCK))),
        new Node(Token.EXPR_RESULT, Node.newString(Token.BREAK, "label"), false));
    RenameLabels.ProcessLabels processLabels = new RenameLabels(processor);
    processLabels.process(compiler, root);
    // Verify that the label and break statements were removed
    assertEquals(Token.BLOCK, root.getNext().getType());
    assertFalse(compiler.hasCodeChanged());
  }
  
  @Test
  public void testProcessLabels_ReferencedLabel() {
    Node root = new Node(Token.ROOT,
        new Node(
            new Node(Token.LABEL, new Node("label"), new Node(Token.BLOCK))),
        new Node(Token.EXPR_RESULT, Node.newString(Token.BREAK, "label"), false));
    RenameLabels.ProcessLabels processLabels = new RenameLabels(processor);
    processLabels.process(compiler, root);
    // Verify that the label and break statements were renamed
    Node breakNode = root.getNext();
    assertEquals(Token.EXPR_RESULT, breakNode.getType());
    assertEquals(Token.BREAK, breakNode.getFirstChild().getType());
    assertEquals("a", breakNode.getFirstChild().getString());
    assertTrue(compiler.hasCodeChanged());
  }
  
  @Test
  public void testProcessLabels_NestedLabels() {
    Node root = new Node(Token.ROOT,
        new Node(
          new Node(Token.LABEL, new Node("label1"),
            new Node(
              new Node(Token.LABEL, new Node("label2"), new Node(Token.BLOCK))))),
        new Node(Token.EXPR_RESULT, Node.newString(Token.BREAK, "label1"), false));
    RenameLabels.ProcessLabels processLabels = new RenameLabels(processor);
    processLabels.process(compiler, root);
    // Verify that the labels and break statements were renamed
    Node breakNode = root.getNext();
    assertEquals(Token.EXPR_RESULT, breakNode.getType());
    assertEquals(Token.BREAK, breakNode.getFirstChild().getType());
    assertEquals("a", breakNode.getFirstChild().getString());
    assertTrue(compiler.hasCodeChanged());
  }
  
  @Test
  public void testProcessLabels_GlobalNamespace() {
    Node root = new Node(Token.ROOT,
        new Node(
          new Node(Token.LABEL, new Node("a"),
            new Node(
              new Node(Token.LABEL, new Node("a"), new Node(Token.BLOCK))))),
        new Node(Token.EXPR_RESULT, Node.newString(Token.BREAK, "a"), false));
    RenameLabels.ProcessLabels processLabels = new RenameLabels(processor);
    processLabels.process(compiler, root);
    // Verify that the labels and break statements were renamed
    Node breakNode = root.getNext();
    assertEquals(Token.EXPR_RESULT, breakNode.getType());
    assertEquals(Token.BREAK, breakNode.getFirstChild().getType());
    assertEquals("a", breakNode.getFirstChild().getString());
    assertTrue(compiler.hasCodeChanged());
  }
  
  @Test
  public void testProcessLabels_RemoveUnusedLabels() {
    CompilerOptions options = new CompilerOptions();
    options.removeUnusedLabels = true;
    compiler.initOptions(options);
    
    Node root = new Node(Token.ROOT,
        new Node(
          new Node(Token.LABEL, new Node("label"),
            new Node(
              new Node(Token.LABEL, new Node("unused"), new Node(Token.BLOCK))))),
        new Node(Token.EXPR_RESULT, Node.newString(Token.BREAK, "label"), false));
    RenameLabels.ProcessLabels processLabels = new RenameLabels(processor);
    processLabels.process(compiler, root);
    // Verify that the unused label and break statements were removed
    assertEquals(Token.BLOCK, root.getNext().getType());
    assertFalse(compiler.hasCodeChanged());
  }
}
