package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.rhino.Node;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMakeLocalNamesUnique() {
    Node fnNode = NodeUtil.newFunctionNode("test", NodeUtil.newBlockNode(
        NodeUtil.newLabelNode("testLabel")), null, null);

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(
        AbstractCompiler.getDefault(), new LabelNameSupplier(
            new Supplier<String>() {
              int count = 0;

              @Override
              public String get() {
                return "testName" + (++count);
              }
            }));

    mutator.makeLocalNamesUnique(fnNode, false);

    NodeTraversal.traverse(null, fnNode, new NameChecker());

    assertNotNull(NodeUtil.findChildByName(fnNode, "testName0"));
    assertNotNull(NodeUtil.findChildByName(fnNode, "testName1"));
    assertNotNull(NodeUtil.findChildByName(fnNode, "testLabel"));

    // Check that no other names have been modified.
    for (Node child : fnNode.getChildren()) {
      if (child.getType() == Token.VAR) {
        Node name = child.getFirstChild();
        assertNotNull(name);
        assertFalse(name.toString().contains("inline_"));
      }
    }
  }

  @Test
  public void testReplaceReturns() {
    LabelNameSupplier labelSupplier = new LabelNameSupplier(
        new Supplier<String>() {
          int count = 0;

          @Override
          public String get() {
            return "testLabel" + (++count);
          }
        });

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(
        AbstractCompiler.getDefault(), labelSupplier);

    Node function = NodeUtil.newFunctionNode("test", NodeUtil.newBlockNode(
        NodeUtil.newLabelNode("testLabel")), null, null);

    Node block = NodeUtil.newBlockNode(
        NodeUtil.newReturnNode(Token.INT_LITERAL, 42));

    Node result = mutator.replaceReturns(block, null, "testLabel", false);

    assertNotNull(result);
    assertEquals(block.getType(), result.getType());
    assertEquals(42, result.getFirstChild().getDoubleValue());

    result = mutator.replaceReturns(block, "result", "testLabel", false);

    assertNotNull(result);
    assertEquals(block.getType(), result.getType());
    assertNotNull(NodeUtil.findChildByName(result, "result"));

    // Test with a label and a return statement
    function = NodeUtil.newFunctionNode("test", NodeUtil.newBlockNode(
        NodeUtil.newLabelNode("testLabel"),
        NodeUtil.newReturnNode(Token.INT_LITERAL, 42)), null, null);

    result = mutator.replaceReturns(function, null, "testLabel", false);

    assertNotNull(result);
    assertEquals(block.getType(), result.getType());
    assertEquals(42, result.getFirstChild().getDoubleValue());

    // Test with a label, a return statement, and a result name
    result = mutator.replaceReturns(function, "result", "testLabel", true);

    assertNotNull(result);
    assertEquals(block.getType(), result.getType());
    assertNotNull(NodeUtil.findChildByName(result, "result"));
  }

  private static class NameChecker implements NodeTraversal.Visitor<Void> {
    @Override
    public Void visit(Node currentNode, NodeTraversal.Callback callback) {
      if (currentNode.getType() == Token.VAR) {
        Node name = currentNode.getFirstChild();
        assertFalse(name.toString().contains("inline_"));
      }
      return null;
    }
  }
}