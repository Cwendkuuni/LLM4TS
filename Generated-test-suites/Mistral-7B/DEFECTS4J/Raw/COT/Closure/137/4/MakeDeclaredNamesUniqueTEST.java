package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class MakeDeclaredNamesUniqueTest {

  private Node testNode;
  private MakeDeclaredNamesUnique makeDeclaredNamesUnique;

  @Before
  public void setUp() {
    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    testNode = createTestNode();
  }

  @Test
  public void testEnterScopeFunction() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.enterScope(nodeTraversal);
  }

  @Test
  public void testEnterScopeVariableBlock() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.enterScope(nodeTraversal);
  }

  @Test
  public void testExitScope() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.enterScope(nodeTraversal);
    makeDeclaredNamesUnique.exitScope(nodeTraversal);
  }

  @Test
  public void testShouldTraverseFunctionDeclaration() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.shouldTraverse(nodeTraversal, testNode, null);
  }

  @Test
  public void testShouldTraverseVariableDeclaration() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.shouldTraverse(nodeTraversal, testNode, null);
  }

  @Test
  public void testShouldTraverseFunctionCall() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.shouldTraverse(nodeTraversal, testNode, null);
  }

  @Test
  public void testVisitFunctionDeclaration() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.visit(nodeTraversal, testNode, null);
  }

  @Test
  public void testVisitVariableDeclaration() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.visit(nodeTraversal, testNode, null);
  }

  @Test
  public void testVisitFunctionCall() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.visit(nodeTraversal, testNode, null);
  }

  @Test
  public void testFindDeclaredNamesFunction() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.findDeclaredNames(testNode, null, null);
  }

  @Test
  public void testFindDeclaredNamesVariableBlock() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    makeDeclaredNamesUnique.findDeclaredNames(testNode, null, null);
  }

  @Test
  public void testGetReplacementNameSameName() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    Renamer renamer = nameStack(nodeTraversal, "test");
    String replacementName = renamer.getReplacementName("test");
    assertThat(replacementName).isNull();
  }

  @Test
  public void testGetReplacementNameDifferentName() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    Renamer renamer = nameStack(nodeTraversal, "test1");
    String replacementName = renamer.getReplacementName("test2");
    assertThat(replacementName).isNotNull();
  }

  @Test
  public void testStripConstIfReplacedTrue() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    Renamer renamer = nameStack(nodeTraversal, "test");
    Node node = new Node(Token.NAME, "test", null);
    node.setProp(Node.IS_CONSTANT_NAME, true);
    renamer.visit(nodeTraversal, node, null);
    assertThat(node.getProp(Node.IS_CONSTANT_NAME)).isFalse();
  }

  @Test
  public void testStripConstIfReplacedFalse() {
    NodeTraversal nodeTraversal = new NodeTraversal(testNode, null, null);
    Renamer renamer = nameStack(nodeTraversal, "test");
    Node node = new Node(Token.NAME, "test", null);
    renamer.visit(nodeTraversal, node, null);
    assertThat(node.getProp(Node.IS_CONSTANT_NAME)).isTrue();
  }

  private Node createTestNode() {
    Node root = new Node(Token.PROGRAM, null, null);
    Node functionDeclaration = new Node(Token.FUNCTION, null, null);
    Node functionName = new Node(Token.NAME, "test", null);
    functionDeclaration.addChild(functionName);
    Node functionBody = new Node(Token.BLOCK, null, null);
    functionDeclaration.addChild(functionBody);
    root.addChild(functionDeclaration);

    Node variableDeclaration = new Node(Token.VAR, null, null);
    Node variableName = new Node(Token.NAME, "test", null);
    variableDeclaration.addChild(variableName);
    Node variableInitializer = new Node(Token.NUMBER, 0, null);
    variableDeclaration.addChild(variableInitializer);
    Node variableBlock = new Node(Token.BLOCK, null, null);
    variableDeclaration.addChild(variableBlock);
    functionBody.addChild(variableDeclaration);

    return root;
  }

  private Renamer nameStack(NodeTraversal nodeTraversal, String testName) {
    makeDeclaredNamesUnique.enterScope(nodeTraversal);
    makeDeclaredNamesUnique.addDeclaredName(testName);
    makeDeclaredNamesUnique.exitScope(nodeTraversal);
    return makeDeclaredNamesUnique;
  }
}