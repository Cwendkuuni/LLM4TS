package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.CommonSourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;
import java.io.File;
import java.util.List;
import org.junit.Test;

/**
 * Unit tests for {@link MinimizeExitPoints}.
 */
public class MinimizeExitPointsTest {

  private static final String MINIMIZE_EXIT_POINTS_TEST_JS =
      "function testMinimizeExitPoints() {"
          + "  var a = 1;"
          + "  var i = 0;"
          + "  for (; i < 10; i++) {"
          + "    if (a > 5) {"
          + "      return;"
          + "    }"
          + "    if (a < 0) {"
          + "      continue;"
          + "    }"
          + "    if (a == 5) {"
          + "      break label outer;"
          + "    }"
          + "    a++;"
          + "  }"
          + "outer:"
          + "}";

  @Test
  public void testMinimizeExitPoints() {
    SourceFile sourceFile = new CommonSourceFile(new File("test.js"), MINIMIZE_EXIT_POINTS_TEST_JS);
    CompilerOptions options = new CompilerOptions();
    TestCompiler compiler = new TestCompiler();
    compiler.setOptions(options);
    AbstractCompiler abstractCompiler = compiler.getOptions().getCompiler();
    Node root = NodeUtil.parseTreeRecursively(sourceFile);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(abstractCompiler);
    minimizer.process(null, root);
    List<Node> nodes = NodeUtil.getAllChildren(root);

    Node testFunction = null;
    for (Node node : nodes) {
      if (NodeUtil.isFunctionDeclaration(node)) {
        testFunction = node;
        break;
      }
    }

    // Test that the number of explicit exits is minimized.
    assertTrue(minimizeExitsInFunction(testFunction));

    // Test that the implied exit at the end of the loop is used instead of a continue.
    assertFalse(hasContinueStatements(testFunction));

    // Test that the implied exit at the end of the loop is used instead of a break.
    assertFalse(hasBreakStatements(testFunction));

    // Test that a return statement is not removed if it is the only statement in a function.
    Node singleReturnFunction = NodeFactory.createFunctionDeclaration(
        "singleReturnFunction", IR.createNull(), ImmutableList.of(), null);
    singleReturnFunction.getLastChild().setNodeValue(IR.CreateReturn(IR.createNull()));

    Node singleReturnFunctionAfterMinimizing = new MinimizeExitPoints(abstractCompiler).process(null, singleReturnFunction).getLastChild();
    assertTrue(singleReturnFunctionAfterMinimizing.isReturnStatement());
  }

  private boolean minimizeExitsInFunction(Node function) {
    int initialExitCount = countExits(function);
    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    minimizer.process(null, function);
    return countExits(function) < initialExitCount;
  }

  private int countExits(Node function) {
    int exitCount = 0;
    for (Node child : function.getChildren()) {
      if (child.isReturnStatement()) {
        exitCount++;
      } else if (child.isBlock()) {
        exitCount += countExits(child);
      }
    }
    return exitCount;
  }

  private boolean hasContinueStatements(Node function) {
    for (Node child : function.getChildren()) {
      if (child.isBlock()) {
        if (NodeUtil.hasStatement(child, NodeFactory.createContinueStatement())) {
          return true;
        }
      }
      if (hasContinueStatements(child)) {
        return true;
      }
    }
    return false;
  }

  private boolean hasBreakStatements(Node function) {
    for (Node child : function.getChildren()) {
      if (child.isBlock()) {
        if (NodeUtil.hasStatement(child, NodeFactory.createBreakStatement())) {
          return true;
        }
      }
      if (hasBreakStatements(child)) {
        return true;
      }
    }
    return false;
  }
}