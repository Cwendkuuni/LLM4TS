package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  // Basic test cases for each method

  @Test
  public void testProcess() {
    Node root = Node.root();
    minimizeExitPoints.process(null, root);
  }

  @Test
  public void testVisit() {
    Node node = Node.literal(1);
    minimizeExitPoints.visit(null, node, null);
  }

  // Test cases for edge cases and exception handling

  @Test
  public void testLabelWithBreak() {
    String javaScriptCode =
        "function test() {\n" +
        "  label1:\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (i % 2 == 0) continue;\n" +
        "    if (i == 5) break label1;\n" +
        "  }\n" +
        "}";

    Node root = Node.compile(javaScriptCode);
    minimizeExitPoints.process(null, root);

    Node label1 = NodeUtil.findLabel(root, "label1");
    Node breakNode = NodeUtil.findBreakNode(label1);

    assertNotNull(breakNode);
    assertTrue(label1.getLastChild() == breakNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    new MinimizeExitPoints(null);
  }
}