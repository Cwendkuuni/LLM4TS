package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.Token;

import java.util.Arrays;

public class UnreachableCodeEliminationTest {

  @SuppressWarnings("unchecked")
  private static <T> T createNode(int type, int line, int column) {
    return (T) Node.newNode(type, line, column);
  }

  private static Node createFunction(String name) {
    Node function = Node.newBlock("function", null);
    Node n = Node.newName(name);
    function.addChild(n);
    return function;
  }

  private static Node createStatement(int type, int line, int column) {
    return Node.newStatement(type, line, column);
  }

  private static Node createBlock(Node statements) {
    return Node.newBlock("{", null, statements);
  }

  private static Node createVariable(String name) {
    Node variable = Node.newVar(name, Node.variableTypeInt());
    Node initializer = Node.newLiteral(1);
    variable.setInitializer(initializer);
    return variable;
  }

  private static Node createAssign(Node lhs, Node rhs) {
    Node assign = Node.newAssign(lhs, rhs);
    return assign;
  }

  private static Node createIf(Node condition, Node ifBlock, Node elseBlock) {
    Node ifStatement = Node.newIfStatement(condition, ifBlock, elseBlock);
    return ifStatement;
  }

  private static Node createReturn(Node value) {
    return Node.newReturnStatement(value);
  }

  private static Node createBreak(int line, int column) {
    return Node.newStatement(Token.BREAK, line, column);
  }

  private static Node createContinue(int line, int column) {
    return Node.newStatement(Token.CONTINUE, line, column);
  }

  private static Node createWhile(Node condition, Node statement) {
    Node whileStatement = Node.newWhileStatement(condition, statement);
    return whileStatement;
  }

  private static Node createSwitch(Node expression, Node[] cases) {
    Node switchStatement = Node.newSwitchStatement(expression);
    for (Node caseNode : cases) {
      Node caseLabel = Node.newExpressionStatement(Node.newName("label"), Node.newIntLiteral(caseLabelNumber++));
      switchStatement.addChild(caseLabel);
      switchStatement.addChild(caseNode);
    }
    return switchStatement;
  }

  private static int caseLabelNumber = 1;

  @org.junit.Test
  public void testUnreachableCodeElimination() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    options.setAllowAllTypesIf(false);
    AbstractCompiler compiler = new AbstractCompiler(options);

    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        // No-op.
      }
    });
    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        UnreachableCodeElimination elimination =
            new UnreachableCodeElimination(compiler, false);
        elimination.process(externs, root);
      }
    });

    Node root = Node.newProgram(Arrays.asList(
        createFunction("main"),
        createFunction("mainBranch"),
        createFunction("elseBranch"),
        createFunction("deadCode")
    ));

    Node mainFunction = root.getChild(0);
    Node mainBranch = mainFunction.getChild(0);
    Node elseBranch = mainFunction.getChild(1);
    Node deadCodeFunction = mainFunction.getChild(2);

    mainBranch.addChild(
        createAssign(createVariable("x"), Node.newLiteral(1))
    );

    mainBranch.addChild(createReturn(createLiteral(1)));

    Node deadCode = deadCodeFunction.getChild(0);
    deadCode.addChild(createReturn(createLiteral(2)));

    Node elseBlock = elseBranch.getChild(1);
    Node elseStatement = elseBlock.getChild(0);
    elseStatement.setLine(10);
    elseStatement.setColumn(1);
    elseStatement.addChild(createReturn(createLiteral(3)));

    mainBranch.addChild(createIf(
        Node.newBinaryOperator(Node.OpTypes.LOGICAL_AND, createVariable("x"), Node.newLiteral(0)),
        mainBranch, elseBranch
    ));

    Node body = Node.newBlock("{", null);
    body.addChild(createAssign(createVariable("y"), Node.newName("x")));
    body.addChild(deadCode);

    mainBranch.addChild(createAssign(createVariable("z"), Node.newName("y")));
    mainBranch.addChild(createReturn(createVariable("z")));

    passManager.runPasses(root);

    // Check that dead code was removed.
    Node processedRoot = compiler.getProcessedSource();
    Node processedMain = processedRoot.getChild(0);
    Node processedBody = processedMain.getChild(0).getChild(0);

    assertFalse(processedBody.getChild(1).hasChildren());
    assertEquals(ProcessedSource.Kind.REMOVED, processedBody.getChild(1).getType());

    // Check that control flow is preserved.
    Node processedElse = processedMain.getChild(1).getChild(0);
    assertEquals(3, processedElse.getChildCount());
    assertTrue(processedElse.getChild(1).getType() == Token.RETURN);
    assertEquals(3, ((Node) processedElse.getChild(1).getValue()).getLine());
    assertEquals(1, ((Node) processedElse.getChild(1).getValue()).getColumn());

    // Test that unreachable code is removed in if-else statements.
    Node ifStatement = Node.newIfStatement(
        Node.newBinaryOperator(Node.OpTypes.LOGICAL_AND,
            Node.newName("x"), Node.newLiteral(0)),
        Node.newBlock("{", null),
        Node.newBlock("{", null)
    );

    Node unreachableCode = ifStatement.getChild(1).getChild(0);
    unreachableCode.addChild(Node.newStatement(Token.PRINT, 0, 0, Node.newStringLiteral("unreachable")));

    Node block = Node.newBlock("{", null);
    block.addChild(createAssign(createVariable("x"), Node.newLiteral(1)));
    block.addChild(ifStatement);
    block.addChild(createReturn(createVariable("x")));

    Node nodeToProcess = Node.newProgram(Arrays.asList(createFunction("test"), block));

    passManager.runPasses(nodeToProcess);

    Node processedTest = nodeToProcess.getChild(0);
    Node processedBlock = processedTest.getChild(0);

    assertTrue(processedBlock.getChild(1).hasChildren());
    assertTrue(processedBlock.getChild(1).getChild(0).getType() == Token.PRINT);

    // Test that unreachable code is removed in switch statements.
    Node switchStatement = Node.newSwitchStatement(
        Node.newName("x"),
        new Node[]{
            Node.newExpressionStatement(Node.newName("label1"), Node.newIntLiteral(1)),
            Node.newBlock("{", null),
            Node.newExpressionStatement(Node.newName("label2"), Node.newIntLiteral(2)),
            Node.newBlock("{", null),
            Node.newReturnStatement(Node.newName("dead")),
            Node.newExpressionStatement(Node.newName("label3"), Node.newIntLiteral(3)),
            Node.newBlock("{", null)
        }
    );

    Node unreachableCodeSwitch = switchStatement.getChild(2).getChild(0);
    unreachableCodeSwitch.addChild(createReturn(Node.newName("deadDead")));

    Node blockSwitch = Node.newBlock("{", null);
    blockSwitch.addChild(createAssign(createVariable("x"), Node.newLiteral(0)));
    blockSwitch.addChild(switchStatement);
    blockSwitch.addChild(createReturn(createVariable("x")));

    Node nodeToProcessSwitch = Node.newProgram(Arrays.asList(createFunction("testSwitch"), blockSwitch));

    passManager.runPasses(nodeToProcessSwitch);

    Node processedTestSwitch = nodeToProcessSwitch.getChild(0);
    Node processedBlockSwitch = processedTestSwitch.getChild(0);

    assertTrue(processedBlockSwitch.getChild(2).hasChildren());
    assertTrue(processedBlockSwitch.getChild(2).getChild(0).getType() == Token.RETURN);
    assertEquals("dead", processedBlockSwitch.getChild(2).getChild(0).getFirstChild().getName());
  }
}