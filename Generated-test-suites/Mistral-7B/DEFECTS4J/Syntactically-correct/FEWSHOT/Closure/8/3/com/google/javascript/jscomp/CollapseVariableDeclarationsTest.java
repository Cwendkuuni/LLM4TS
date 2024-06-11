package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;
import java.util.Set;

/**
 * Test class for CollapseVariableDeclarations class.
 */
@RunWith(JUnit4.class)
public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseSingleVariable() {
    AbstractCompiler compiler = new Compiler();
    Node root = createTestNodeForSingleVariableDeclaration();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    Node actual = root;
    Node expected = createTestNodeForSingleVariableDeclarationCollapsed();
    assertNodeEquals(expected, actual);
  }

  @Test
  public void testCollapseMultipleVariables() {
    AbstractCompiler compiler = new Compiler();
    Node root = createTestNodeForMultipleVariableDeclarations();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    Node actual = root;
    Node expected = createTestNodeForMultipleVariableDeclarationsCollapsed();
    assertNodeEquals(expected, actual);
  }

  @Test
  public void testCollapseVariableAndAssignment() {
    AbstractCompiler compiler = new Compiler();
    Node root = createTestNodeForVariableAndAssignment();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    Node actual = root;
    Node expected = createTestNodeForVariableAndAssignmentCollapsed();
    assertNodeEquals(expected, actual);
  }

  @Test
  public void testCollapseVariablesWithinIfStatement() {
    AbstractCompiler compiler = new Compiler();
    Node root = createTestNodeForVariablesWithinIfStatement();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    Node actual = root;
    Node expected = createTestNodeForVariablesWithinIfStatementCollapsed();
    assertNodeEquals(expected, actual);
  }

  @Test
  public void testCollapseVariablesWithinFunction() {
    AbstractCompiler compiler = new Compiler();
    Node root = createTestNodeForVariablesWithinFunction();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    Node actual = root;
    Node expected = createTestNodeForVariablesWithinFunctionCollapsed();
    assertNodeEquals(expected, actual);
  }

  private Node createTestNodeForSingleVariableDeclaration() {
    Node varNode = new Node(Token.VAR);
    Node nameNode = new Node(Token.NAME, "x");
    varNode.addChildToBack(nameNode);
    return varNode;
  }

  private Node createTestNodeForSingleVariableDeclarationCollapsed() {
    Node varNode = new Node(Token.VAR);
    return varNode;
  }

  private Node createTestNodeForMultipleVariableDeclarations() {
    Node varNode1 = new Node(Token.VAR);
    Node nameNode1 = new Node(Token.NAME, "x");
    varNode1.addChildToBack(nameNode1);
    Node varNode2 = new Node(Token.VAR);
    Node nameNode2 = new Node(Token.NAME, "y");
    varNode2.addChildToBack(nameNode2);
    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildrenToBack(varNode1, varNode2);
    return parentNode;
  }

  private Node createTestNodeForMultipleVariableDeclarationsCollapsed() {
    Node varNode = new Node(Token.VAR);
    Node nameNode1 = new Node(Token.NAME, "x");
    Node nameNode2 = new Node(Token.NAME, "y");
    varNode.addChildrenToBack(nameNode1, nameNode2);
    return varNode;
  }

  private Node createTestNodeForVariableAndAssignment() {
    Node varNode = new Node(Token.VAR);
    Node nameNode = new Node(Token.NAME, "x");
    varNode.addChildToBack(nameNode);
    Node assignNode = new Node(Token.ASSIGN);
    Node valueNode = new Node(Token.NUM, 1);
    assignNode.addChildrenToBack(varNode, valueNode);
    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildrenToBack(varNode, assignNode);
    return parentNode;
  }

  private Node createTestNodeForVariableAndAssignmentCollapsed() {
    Node varNode = new Node(Token.VAR);
    Node nameNode = new Node(Token.NAME, "x");
    varNode.addChildToBack(nameNode);
    Node assignNode = new Node(Token.ASSIGN);
    Node valueNode = new Node(Token.NUM, 1);
    varNode.addChildToBack(assignNode);
    assignNode.addChildToBack(valueNode);
    return varNode;
  }

  private Node createTestNodeForVariablesWithinIfStatement() {
    Node ifNode = new Node(Token.IF);
    Node condition = new Node(Token.BOOL, true);
    ifNode.addChildToBack(condition);
    Node varNode1 = new Node(Token.VAR);
    Node nameNode1 = new Node(Token.NAME, "x");
    varNode1.addChildToBack(nameNode1);
    Node ifBody = new Node(Token.BLOCK);
    Node varNode2 = new Node(Token.VAR);
    Node nameNode2 = new Node(Token.NAME, "y");
    varNode2.addChildToBack(nameNode2);
    ifBody.addChildrenToBack(varNode1, varNode2);
    ifNode.addChildToBack(ifBody);
    Node elsePart = new Node(Token.ELSE);
    Node elseBody = new Node(Token.BLOCK);
    elsePart.addChildToBack(elseBody);
    ifNode.addChildToBack(elsePart);
    return ifNode;
  }

  private Node createTestNodeForVariablesWithinIfStatementCollapsed() {
    Node ifNode = new Node(Token.IF);
    Node condition = new Node(Token.BOOL, true);
    ifNode.addChildToBack(condition);
    Node varNode = new Node(Token.VAR);
    Node nameNode1 = new Node(Token.NAME, "x");
    Node nameNode2 = new Node(Token.NAME, "y");
    varNode.addChildrenToBack(nameNode1, nameNode2);
    Node ifBody = new Node(Token.BLOCK);
    ifNode.addChildToBack(ifBody);
    Node elsePart = new Node(Token.ELSE);
    Node elseBody = new Node(Token.BLOCK);
    elsePart.addChildToBack(elseBody);
    ifNode.addChildToBack(elsePart);
    return ifNode;
  }

  private Node createTestNodeForVariablesWithinFunction() {
    Node functionNode = new Node(Token.FUNCTION);
    Node body = new Node(Token.BLOCK);
    Node varNode1 = new Node(Token.VAR);
    Node nameNode1 = new Node(Token.NAME, "x");
    varNode1.addChildToBack(nameNode1);
    Node varNode2 = new Node(Token.VAR);
    Node nameNode2 = new Node(Token.NAME, "y");
    varNode2.addChildToBack(nameNode2);
    body.addChildrenToBack(varNode1, varNode2);
    functionNode.addChildToBack(body);
    return functionNode;
  }

  private Node createTestNodeForVariablesWithinFunctionCollapsed() {
    Node functionNode = new Node(Token.FUNCTION);
    Node body = new Node(Token.BLOCK);
    Node varNode = new Node(Token.VAR);
    Node nameNode1 = new Node(Token.NAME, "x");
    Node nameNode2 = new Node(Token.NAME, "y");
    varNode.addChildrenToBack(nameNode1, nameNode2);
    body.addChildToBack(varNode);
    functionNode.addChildToBack(body);
    return functionNode;
  }

  private void assertNodeEquals(Node expected, Node actual) {
    assertEquals(expected.toString(), actual.toString());
  }

  private static class Compiler extends AbstractCompiler {
    @Override
    public CompilerOptions getOptions() {
      CompilerOptions options = new CompilerOptions();
      options.setSkipDuplicates(true);
      return options;
    }
  }
}