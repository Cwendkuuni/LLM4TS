package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Set;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private VarCheck varCheck;
  private NodeTraversal nodeTraversal;
  private Node externs;
  private Node root;
  private Node DeclarationNode;
  private Node FunctionNode;
  private CompilerInput Input;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    nodeTraversal = mock(NodeTraversal.class);
    varCheck = new VarCheck(compiler);
    Input = mock(CompilerInput.class);

    when(compiler.getErrorLevel(any(DiagnosticType.class)))
        .thenReturn(CheckLevel.WARNING);

    externs = Node.objectLiteral();
    root = Node.script(nodeFactory, null, null, null, null, null);
    DeclarationNode = Node.newVar(Token.NAME, "varName");
    FunctionNode = Node.function(Token.NAME, null, null, null);
  }

  @Test
  public void testProcessExterns() {
    when(nodeFactory.makeString("{SyntheticVarsDeclar}")).thenReturn(Input);
    when(compiler.newExternInput(anyString())).thenReturn(Input);

    varCheck.process(externs, root);

    verify(compiler).newExternInput(eq("{SyntheticVarsDeclar}"));
  }

  @Test
  public void testProcessRoot() {
    // Test valid root node
    varCheck.process(externs, root);
    verifyNoMoreInteractions(compiler);

    // Test empty root node
    Node emptyRoot = Node.script(nodeFactory, null, null, null, null, null);
    varCheck.process(externs, emptyRoot);
    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testVisitNameDeclaration() {
    // Test already declared variable
    Scope scope = mock(Scope.class);
    when(scope.getVar(eq("varName"))).thenReturn(new Scope.Var());

    Node parentVariable = Node.newVar(Token.NAME, "parentVariable");
    when(nodeTraversal.getScope()).thenReturn(scope);
    when(nodeTraversal.getInput()).thenReturn(Input);
    when(nodeTraversal.getParent()).thenReturn(parentVariable);

    Node nameNode = Node.newString(Token.NAME, "varName");
    varCheck.visit(nodeTraversal, nameNode, parentVariable);

    verifyNoMoreInteractions(compiler);

    // Test undeclared variable
    when(scope.getVar(eq("varName"))).thenReturn(null);

    varCheck.visit(nodeTraversal, nameNode, parentVariable);

    verify(compiler).report(eq(nameNode), eq(UNDEFINED_VAR_ERROR), eq("varName"));
  }

  @Test
  public void testVisitFunctionDeclaration() {
    // Test empty function name
    Node parentFunction = FunctionNode;

    Node functionDeclaration = Node.newFunction(Token.NAME, null, null, null);
    when(nodeTraversal.getParent()).thenReturn(parentFunction);

    varCheck.visit(nodeTraversal, functionDeclaration, parentFunction);

    verifyNoMoreInteractions(compiler);

    // Test non-empty function name
    String functionName = "functionName";
    functionDeclaration = Node.newFunction(Token.NAME, null, null, null, null, functionName);

    varCheck.visit(nodeTraversal, functionDeclaration, parentFunction);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testVisitGetPropNode() {
    // Test valid getprop node
    Node parentVariable = Node.newVar(Token.NAME, "parentVariable");
    Node getPropNode = Node.getProp(Token.NAME, Node.newString(Token.NAME, "varName"));
    when(nodeTraversal.getParent()).thenReturn(parentVariable);

    Scope scope = mock(Scope.class);
    when(scope.getVar(eq("varName"))).thenReturn(new Scope.Var());

    varCheck.visit(nodeTraversal, getPropNode, parentVariable);

    verifyNoMoreInteractions(compiler);

    // Test undeclared getprop node
    when(scope.getVar(eq("varName"))).thenReturn(null);

    varCheck.visit(nodeTraversal, getPropNode, parentVariable);

    verify(compiler).report(eq(getPropNode), eq(UNDEFINED_VAR_ERROR), eq("varName"));
  }

  @Test
  public void testVisitOtherNodes() {
    // Test var node
    Node parentVariable = Node.newVar(Token.NAME, "parentVariable");
    Node varNode = Node.newVar(Token.NAME, "varName");
    when(nodeTraversal.getParent()).thenReturn(parentVariable);

    varCheck.visit(nodeTraversal, varNode, parentVariable);

    verifyNoMoreInteractions(compiler);

    // Test lp node
    Node lpNode = Node.lparen();
    Node varNode2 = Node.newVar(Token.NAME, "varName");
    when(nodeTraversal.getParent()).thenReturn(lpNode);

    varCheck.visit(nodeTraversal, varNode2, lpNode);

    verifyNoMoreInteractions(compiler);

    // Test other nodes
    Node otherNode1 = Node.literal(1);
    Node otherNode2 = Node.identifier("identifier");
    Node otherNode3 = Node.operator("+");

    varCheck.visit(nodeTraversal, otherNode1, root);
    varCheck.visit(nodeTraversal, otherNode2, root);
    varCheck.visit(nodeTraversal, otherNode3, root);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testCreateSynthesizedExternVar() {
    String varName = "varName";
    Node nameNode = Node.newString(Token.NAME, varName);

    varCheck.createSynthesizedExternVar(varName);

    Node synthesizedExternVar = nodeTraversal.getParent().getLastChild();

    assertEquals("VAR", synthesizedExternVar.getType());
    assertEquals(varName, synthesizedExternVar.getString());
  }
}