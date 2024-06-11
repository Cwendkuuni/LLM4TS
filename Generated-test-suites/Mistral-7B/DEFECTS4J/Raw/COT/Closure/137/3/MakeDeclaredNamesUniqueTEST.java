package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Renamer;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MakeDeclaredNamesUniqueTest {

  private Node rootNode;
  private TokenStream tokenStream;
  private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
  private Renamer renamer;

  @Before
  public void setUp() {
    rootNode = Node.objectLiteral();
    tokenStream = new TokenStream();
    tokenStream.putTokens(Lists.newArrayList(
        Token.NAME, "a",
        Token.FUNCTION,
        Token.KEYWORD, Token.KEYWORD_VAR,
        Token.NAME, "b",
        Token.SEMICOLON,
        Token.NAME, "c",
        Token.SEMICOLON,
        Token.NAME, "d",
        Token.SEMICOLON,
        Token.FUNCTION,
        Token.KEYWORD, Token.KEYWORD_FUNCTION,
        Token.NAME, "e",
        Token.LPAREN,
        Token.RPAREN,
        Token.LBRACE,
        Token.NAME, "a", // redeclared variable
        Token.SEMICOLON,
        Token.NAME, "f",
        Token.SEMICOLON,
        Token.RBRACE,
        Token.EOF
    ));

    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.process(tokenStream, rootNode);

    renamer = mock(Renamer.class);
  }

  @Test
  public void testEnterScope() {
    ArgumentCaptor<NodeTraversal> nodeTraversalArgumentCaptor = ArgumentCaptor.forClass(NodeTraversal.class);
    when(renamer.forChildScope()).thenReturn(renamer);

    makeDeclaredNamesUnique.enterScope(nodeTraversalArgumentCaptor.capture());
    NodeTraversal nodeTraversal = nodeTraversalArgumentCaptor.getValue();

    assertEquals(makeDeclaredNamesUnique.rootRenamer, renamer);
    nodeTraversal.assertInGlobalScope();
  }

  @Test(expected = AssertionError.class)
  public void testEnterScopeFunction() {
    ArgumentCaptor<NodeTraversal> nodeTraversalArgumentCaptor = ArgumentCaptor.forClass(NodeTraversal.class);
    when(renamer.forChildScope()).thenReturn(renamer);

    makeDeclaredNamesUnique.enterScope(nodeTraversalArgumentCaptor.capture());
    NodeTraversal nodeTraversal = nodeTraversalArgumentCaptor.getValue();

    Node functionNode = new Node(Token.FUNCTION);
    nodeTraversal.traverse(rootNode, functionNode);

    makeDeclaredNamesUnique.enterScope(nodeTraversalArgumentCaptor.capture());
    NodeTraversal functionNodeTraversal = nodeTraversalArgumentCaptor.getValue();

    assertNull(functionNodeTraversal.getScope());
    Preconditions.checkState(functionNodeTraversal.inGlobalScope());
  }

  @Test
  public void testVisitName() {
    ArgumentCaptor<NodeTraversal> nodeTraversalArgumentCaptor = ArgumentCaptor.forClass(NodeTraversal.class);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);

    when(renamer.getReplacementName(nodeArgumentCaptor.capture())).thenReturn("renamedName");

    makeDeclaredNamesUnique.visit(null, null, null, nodeTraversalArgumentCaptor.capture(), nodeArgumentCaptor.capture());

    Node nameNode = nodeArgumentCaptor.getValue();
    assertEquals("renamedName", nameNode.getString());
  }

  @Test
  public void testVisitNameWithIdentifier() {
    ArgumentCaptor<NodeTraversal> nodeTraversalArgumentCaptor = ArgumentCaptor.forClass(NodeTraversal.class);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);

    when(renamer.getReplacementName(nodeArgumentCaptor.capture())).thenReturn("renamedName");

    makeDeclaredNamesUnique.visit(null, null, null, nodeTraversalArgumentCaptor.capture(), nodeArgumentCaptor.capture());

    Node nameNode = nodeArgumentCaptor.getValue();
    assertEquals("renamedName", nameNode.getString());
    // Make sure that isIdentifier() is not affected
    assertEquals(Node.IS_IDENTIFIER_NAME, nameNode.getProp(Node.IS_IDENTIFIER));
  }

  @Test
  public void testVisitFunction() {
    ArgumentCaptor<NodeTraversal> nodeTraversalArgumentCaptor = ArgumentCaptor.forClass(NodeTraversal.class);

    makeDeclaredNamesUnique.visit(null, null, null, nodeTraversalArgumentCaptor.capture(), rootNode);

    Node functionNode = new Node(Token.FUNCTION);
    nodeTraversalArgumentCaptor.getValue().traverse(rootNode, functionNode);

    assertEquals(makeDeclaredNamesUnique.rootRenamer, nodeTraversalArgumentCaptor.getValue().getScope().getRenamer());
  }

  @Test
  public void testVisitCatch() {
    ArgumentCaptor<NodeTraversal> nodeTraversalArgumentCaptor = ArgumentCaptor.forClass(NodeTraversal.class);

    makeDeclaredNamesUnique.visit(null, null, null, nodeTraversalArgumentCaptor.capture(), rootNode);

    Node catchNode = new Node(Token.CATCH);
    nodeTraversalArgumentCaptor.getValue().traverse(rootNode, catchNode);

    assertEquals(makeDeclaredNamesUnique.rootRenamer, nodeTraversalArgumentCaptor.getValue().getScope().getRenamer());
  }

  @Test
  public void testFindDeclaredNames() {
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> parentArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Renamer> renamerArgumentCaptor = ArgumentCaptor.forClass(Renamer.class);

    makeDeclaredNamesUnique.findDeclaredNames(nodeArgumentCaptor.capture(), null, renamerArgumentCaptor.capture());

    Node varNode1 = nodeArgumentCaptor.getValue();
    assertEquals("a", varNode1.getString());

    Node varNode2 = new Node(Token.VAR, null, Token.NAME, "a");
    makeDeclaredNamesUnique.findDeclaredNames(varNode2, null, renamerArgumentCaptor.capture());
    assertEquals("a_1", varNode2.getString());

    Node functionNode = new Node(Token.FUNCTION);
    Node functionBody = new Node(Token.BLOCK);
    Node varNode3 = new Node(Token.VAR, null, Token.NAME, "c");
    Node varNode4 = new Node(Token.VAR, null, Token.NAME, "d");
    Node varNode5 = new Node(Token.VAR, null, Token.NAME, "e");
    functionBody.addChild(varNode3);
    functionBody.addChild(varNode4);
    functionBody.addChild(varNode5);
    functionNode.setLastChild(functionBody);

    makeDeclaredNamesUnique.findDeclaredNames(functionNode, null, renamerArgumentCaptor.capture());
    assertEquals("c_1", varNode3.getString());
    assertEquals("d_1", varNode4.getString());
    assertEquals("e_1", varNode5.getString());

    Node functionNode2 = new Node(Token.FUNCTION);
    Node functionBody2 = new Node(Token.BLOCK);
    Node varNode6 = new Node(Token.VAR, null, Token.NAME, "b"); // already declared
    Node varNode7 = new Node(Token.VAR, null, Token.NAME, "f");
    functionBody2.addChild(varNode6);
    functionBody2.addChild(varNode7);
    functionNode2.setLastChild(functionBody2);

    makeDeclaredNamesUnique.findDeclaredNames(functionNode2, functionNode, renamerArgumentCaptor.capture());
    assertEquals("b_2", varNode6.getString());
    assertEquals("f_1", varNode7.getString());
  }

  @Test
  public void testGetReplacementName() {
    ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

    Renamer renamer = new ContextualRenamer();
    String originalName = "name";
    String replacementName = renamer.getReplacementName(originalName);
    assertEquals(originalName, replacementName);

    renamer.addDeclaredName("name");
    replacementName = renamer.getReplacementName(originalName);
    assertEquals("name_1", replacementName);

    renamer = new ContextualRenamer(Sets.newHashSet("name"));
    replacementName = renamer.getReplacementName(originalName);
    assertEquals("name_2", replacementName);
  }
}