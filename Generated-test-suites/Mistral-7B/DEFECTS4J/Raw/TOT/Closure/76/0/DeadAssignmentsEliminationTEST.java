package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.google.javascript.rhino.NodeProperty.ASSIGN;
import static com.google.javascript.rhino.NodeProperty.COND;
import static com.google.javascript.rhino.NodeProperty.FOR;
import static com.google.javascript.rhino.NodeProperty.IF;
import static com.google.javascript.rhino.NodeProperty.NAME;
import static com.google.javascript.rhino.NodeProperty.NUM;
import static com.google.javascript.rhino.NodeProperty.SWITCH;
import static com.google.javascript.rhino.NodeProperty.VAR;
import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class DeadAssignmentsEliminationTest {

  public static class TestDeadAssignmentsElimination {

    @Test
    public void testNoDeadAssignments() {
      String jsCode = "var x = 1; var y = x; x = 2;";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
      assertEquals(jsCode, RhinoUtils.toSource(rootNode));
    }

    @Test
    public void testDeadAssignmentBeforeReturn() {
      String jsCode = "function test() { var x = 1; return x = 2; }";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
      assertEquals("function test() { return 1; }", RhinoUtils.toSource(rootNode));
    }

    @Test
    public void testDeadAssignmentBeforeReturnWithExpression() {
      String jsCode = "function test() { return x = 1 + x; }";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
      assertEquals("function test() { return 2; }", RhinoUtils.toSource(rootNode));
    }

    @Test
    public void testDeadAssignmentBeforeIf() {
      String jsCode = "var x = 1; if (true) { x = 2; }";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
      assertEquals("var x = 1;", RhinoUtils.toSource(rootNode));
    }

    @Test
    public void testDeadAssignmentBeforeSwitch() {
      String jsCode = "var x = 1; switch (0) { case 0: x = 2; }";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
      assertEquals("var x = 1;", RhinoUtils.toSource(rootNode));
    }

    @Test
    public void testDeadAssignmentBeforeWhile() {
      String jsCode = "var x = 1; while (true) { x = 2; }";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
      assertEquals("var x = 1;", RhinoUtils.toSource(rootNode));
    }

    @Test
    public void testDeadAssignmentBeforeFor() {
      String jsCode = "for (var i = 0; i < 1; i++) { i = 1; }";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
      assertEquals("for (var i = 0; i < 1; i++) { }", RhinoUtils.toSource(rootNode));
    }

    @Test
    public void testDeadAssignmentWithSameVariable() {
      String jsCode = "var x = 1; x = x;";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
      assertEquals("var x = 1;", RhinoUtils.toSource(rootNode));
    }

    @Test(expected = AssertionError.class)
    public void testDeadAssignmentWithIncorrectNodeType() {
      String jsCode = "var x = 1; ; x = 2;";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
    }

    @Test(expected = AssertionError.class)
    public void testDeadAssignmentWithIncorrectScopeType() {
      String jsCode = "function test() { var x = 1; test(); x = 2; }";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
    }

    @Test(expected = AssertionError.class)
    public void testDeadAssignmentWithIncorrectAssignmentOperator() {
      String jsCode = "var x = 1, y = x + 1; x ++;";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
    }

    @Test(expected = AssertionError.class)
    public void testDeadAssignmentWithIncorrectOperatorType() {
      String jsCode = "function test() { var x = 1; +x; }";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
    }

    @Test(expected = AssertionError.class)
    public void testDeadAssignmentWithNonLocalVariable() {
      String jsCode = "var x = 1; window.x = x; x = 2;";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
    }

    @Test(expected = AssertionError.class)
    public void testDeadAssignmentWithFunctionVariable() {
      String jsCode = "function test() { var x = function() {}; x = 1; }";
      AbstractCompiler compiler = new Compiler();
      DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
      Node rootNode = RhinoUtils.compileToNode(jsCode, compiler);
      eliminator.process(compiler.getExterns(), rootNode);
    }

    private static class RhinoUtils {
      public static Node compileToNode(String code, AbstractCompiler compiler) {
        CompilerOptions options = new CompilerOptions();
        options.setOptIn(CompilerOptions.OptFlag.ALL);
        compiler.setOptions(options);
        Node rootNode = compiler.parseAndCompile(code, "", null);
        return rootNode;
      }

      public static String toSource(Node node) {
        if (node.getType() == FOR) {
          String forInit = "";
          if (node.getFirstChild().getType() == VAR) {
            forInit = node.getFirstChild().getString();
            node = node.getFirstChild().getNext();
          }
          String forCondition = "";
          if (node.getType() == COND) {
            forCondition = NodeUtil.toString(node);
            node = node.getNext();
          }
          String forUpdate = "";
          if (node.getType() == ASSIGN) {
            forUpdate = NodeUtil.toString(node);
          }
          return String.format("for (%s; %s; %s) { %s }", forInit, forCondition, forUpdate, RhinoUtils.toSource(node.getNext()));
        }
        if (node.getType() == IF) {
          String ifCondition = NodeUtil.toString(node.getFirstChild());
          Node ifBody = node.getLastChild();
          String ifElse = "";
          if (node.getNext() != null && node.getNext().getType() == IF) {
            ifElse = RhinoUtils.toSource(node.getNext());
          }
          return String.format("if (%s) { %s }%s", ifCondition, RhinoUtils.toSource(ifBody), ifElse);
        }
        if (node.getType() == SWITCH) {
          String switchExpression = NodeUtil.toString(node.getFirstChild());
          StringBuilder switchCase = new StringBuilder();
          Node switchLabel = node.getFirstChild().getNext();
          while (switchLabel != null) {
            switchCase.append(String.format("case %s: %s", NodeUtil.toString(switchLabel), RhinoUtils.toSource(switchLabel.getNext())));
            switchLabel = switchLabel.getNext();
          }
          return String.format("switch(%s) { %s }", switchExpression, switchCase.toString());
        }
        return NodeUtil.toString(node);
      }
    }
  }
}