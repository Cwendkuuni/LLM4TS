package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    

    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.newCompilationUnit();
    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Lists.newArrayList(Node.newFunction(null, null, null)));

    root.appendChild(googScopeCall);
    Node functionBody = Node.newBlock(null, null);
    Node divElement = Node.newCall(
        Node.newQualifiedName("goog.dom", "createElement"),
        Lists.newArrayList(Node.newQualifiedName("goog.dom.TagName", "DIV")));
    functionBody.appendChild(divElement);
    Node function = Node.newFunction(null, null, null, null, null, null, functionBody);
    googScopeCall.appendChild(function);

    scopedAliases.process(null, root);

    // Then
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom.createElement", aliasDefinitionsInOrder.get(0).getQualifiedName());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(googScopeCall, scopeCalls.get(0));

    Collection<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals(divElement, aliasUsages.iterator().next());

    
  }

  @Test(expected = AssertionError.class)
  public void testInvalidScopedAliases() {
    

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.newCompilationUnit();
    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Lists.newArrayList(
            Node.newFunction(null, null, Lists.newArrayList(
                Node.newVariable("param1"), Node.newVariable("param2")))));

    root.appendChild(googScopeCall);

    scopedAliases.process(null, root);

    
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasRedefined() {
    

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.newCompilationUnit();
    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Lists.newArrayList(
            Node.newFunction(null, null, null)));

    Node aliasDefinition1 = Node.newVariable("alias", Node.NEW);
    Node aliasUsage1 = Node.newVariable("alias");
    Node assignment1 = Node.newAssignment(aliasUsage1, aliasDefinition1);
    Node aliasDefinition2 = Node.newVariable("alias", Node.NEW);
    Node assignment2 = Node.newAssignment(aliasUsage1, aliasDefinition2);

    root.appendChild(googScopeCall);
    Node functionBody = Node.newBlock(null, null);
    functionBody.appendChild(assignment1);
    functionBody.appendChild(assignment2);
    Node function = Node.newFunction(null, null, null, null, null, null, functionBody);
    googScopeCall.appendChild(function);

    scopedAliases.process(null, root);

    
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasNonAliasLocal() {
    

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.newCompilationUnit();
    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Lists.newArrayList(
            Node.newFunction(null, null, null)));

    Node variable = Node.newVariable("nonAliasLocal");

    root.appendChild(googScopeCall);
    Node functionBody = Node.newBlock(null, null);
    functionBody.appendChild(variable);
    Node function = Node.newFunction(null, null, null, null, null, null, functionBody);
    googScopeCall.appendChild(function);

    scopedAliases.process(null, root);

    
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasRefersToThis() {
    

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.newCompilationUnit();
    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Lists.newArrayList(
            Node.newFunction(null, null, null)));

    root.appendChild(googScopeCall);
    Node functionBody = Node.newBlock(null, null);
    Node thisReference = Node.newThis();
    functionBody.appendChild(thisReference);
    Node function = Node.newFunction(null, null, null, null, null, null, functionBody);
    googScopeCall.appendChild(function);

    scopedAliases.process(null, root);

    
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasUsesReturn() {
    

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.newCompilationUnit();
    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Lists.newArrayList(
            Node.newFunction(null, null, null)));

    Node functionBody = Node.newBlock(null, null);
    Node returnStatement = Node.newExpressionStatement(Node.newReturn(Node.newNumber(1)));
    functionBody.appendChild(returnStatement);
    Node function = Node.newFunction(null, null, null, null, null, null, functionBody);
    googScopeCall.appendChild(function);

    root.appendChild(googScopeCall);

    scopedAliases.process(null, root);

    
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasUsesThrow() {
    

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.newCompilationUnit();
    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Lists.newArrayList(
            Node.newFunction(null, null, null)));

    Node functionBody = Node.newBlock(null, null);
    Node throwStatement = Node.newExpressionStatement(
        Node.newThrow(Node.newIdentifier("error")));
    functionBody.appendChild(throwStatement);
    Node function = Node.newFunction(null, null, null, null, null, null, functionBody);
    googScopeCall.appendChild(function);

    root.appendChild(googScopeCall);

    scopedAliases.process(null, root);

    
  }
}