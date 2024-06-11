package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.testtype.CompilationTestBase;
import com.google.javascript.rhino.TokenStream;

import java.util.List;

public class MakeDeclaredNamesUniqueTest extends CompilationTestBase {

  public void testEnterScopeWithFunctionDeclaration() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    Scope mockScope = mock(Scope.class);
    when(mockScope.getVar("funcName"))
        .thenReturn(new Var(mockScope, "funcName", null, null, null, null));

    Node functionDeclaration = getFunctionDeclarationNode("funcName", ImmutableSet.of());
    makeDeclaredNamesUnique.enterScope(nodeTraversal(functionDeclaration, mockScope));

    // Check if the function name has been added to the renamer.
    MakeDeclaredNamesUnique renamer = makeDeclaredNamesUnique.nameStack.peek();
    assertEquals("funcName", renamer.getDeclaredNames().get("funcName"));
  }

  public void testEnterScopeWithVarDeclaration() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    Scope mockScope = mock(Scope.class);
    when(mockScope.getVar("varName"))
        .thenReturn(new Var(mockScope, "varName", null, null, null, null));

    Node varDeclaration = getVariableDeclarationNode("varName");
    makeDeclaredNamesUnique.enterScope(nodeTraversal(varDeclaration, mockScope));

    // Check if the variable name has been added to the renamer.
    MakeDeclaredNamesUnique renamer = makeDeclaredNamesUnique.nameStack.peek();
    assertEquals("varName", renamer.getDeclaredNames().get("varName"));
  }

  public void testEnterScopeWithFunctionAndVarDeclarations() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    Scope mockScope = mock(Scope.class);

    when(mockScope.getVar("funcName"))
        .thenReturn(new Var(mockScope, "funcName", null, null, null, null));
    when(mockScope.getVar("varName"))
        .thenReturn(new Var(mockScope, "varName", null, null, null, null));

    Node functionDeclaration = getFunctionDeclarationNode("funcName", ImmutableSet.of());
    Node varDeclaration = getVariableDeclarationNode("varName");
    Node functionAndVarDeclarations = getFunctionDeclarationNode("funcName", ImmutableSet.of(varDeclaration));

    makeDeclaredNamesUnique.enterScope(nodeTraversal(functionAndVarDeclarations, mockScope));

    // Check if both function and variable names have been added to the renamer.
    MakeDeclaredNamesUnique renamer = makeDeclaredNamesUnique.nameStack.peek();
    assertEquals("funcName", renamer.getDeclaredNames().get("funcName"));
    assertEquals("varName", renamer.getDeclaredNames().get("varName"));
  }

  public void testVisitNameNodeWithDeclaredName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    Scope mockScope = mock(Scope.class);
    when(mockScope.getVar("declaredName"))
        .thenReturn(new Var(mockScope, "declaredName", null, null, null, null));

    Node nameNode = getNameNode("declaredName");
    Node parentNode = getFunctionDeclarationNode("parentFunction");
    Node grandParentNode = getFunctionDeclarationNode("grandParentFunction");

    makeDeclaredNamesUnique.shouldTraverse(nodeTraversal(grandParentNode, mockScope), nameNode, parentNode);
    makeDeclaredNamesUnique.visit(nodeTraversal(parentNode, mockScope), nameNode, parentNode);

    // Check if the name node has been replaced.
    assertEquals("declaredName_0", nameNode.getString());
  }

  public void testVisitNameNodeWithUndefinedName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    Scope mockScope = mock(Scope.class);

    Node nameNode = getNameNode("undefinedName");
    Node parentNode = getFunctionDeclarationNode("parentFunction");

    makeDeclaredNamesUnique.shouldTraversal(nodeTraversal(parentNode, mockScope), nameNode, parentNode);
    makeDeclaredNamesUnique.visit(nodeTraversal(parentNode, mockScope), nameNode, parentNode);

    // Check if the name node has not been replaced.
    assertNull(nameNode.getString());
  }

  public void testGetReplacementNameWithUndefinedName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    // Check if the getReplacementName returns null for undefined names.
    assertNull(makeDeclaredNamesUnique.getReplacementName("undefinedName"));
  }

  private Node getFunctionDeclarationNode(String name, ImmutableSet<Node> arguments) {
    TokenStream ts = new TokenStream(getTestSourceFile());
    Node functionNode = ts.parseFunctionDeclaration(name, null, null, arguments, null);
    return functionNode;
  }

  private Node getVariableDeclarationNode(String name) {
    TokenStream ts = new TokenStream(getTestSourceFile());
    Node variableNode = ts.parseVarDeclaration(name, null, null, null, null);
    return variableNode;
  }

  private Node getNameNode(String name) {
    TokenStream ts = new TokenStream(getTestSourceFile());
    Node nameNode = ts.parseIdentifier(name);
    return nameNode;
  }

  private NodeTraversal nodeTraversal(Node node, Scope mockScope) {
    return new NodeTraversal(compiler, node, mockScope, this);
  }
}