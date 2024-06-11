package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTableMock;
  private AliasTransformationHandler transformationHandlerMock;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    preprocessorSymbolTableMock = Mockito.mock(PreprocessorSymbolTable.class);
    transformationHandlerMock = Mockito.mock(AliasTransformationHandler.class);
    compiler = Mockito.mock(AbstractCompiler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTableMock, transformationHandlerMock);
  }

  @Test
  public void testProcess_NoErrors() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);
    scopedAliases.process(externs, root);
    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_HasErrors() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);
    Traversal traversalMock = Mockito.mock(Traversal.class);
    Mockito.when(compiler.reportCodeChange()).thenReturn(traversalMock);
    Mockito.when(traversalMock.getAliasDefinitionsInOrder()).thenReturn(Lists.newArrayList());
    Mockito.when(traversalMock.getAliasUsages()).thenReturn(Lists.newArrayList());
    Mockito.when(traversalMock.getScopeCalls()).thenReturn(Lists.newArrayList());
    Mockito.when(traversalMock.hasErrors()).thenReturn(true);

    scopedAliases.process(externs, root);
    Mockito.verify(compiler, Mockito.atLeastOnce()).reportCodeChange();
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_NullCompiler() {
    AbstractCompiler compiler = null;
    Traversal traversalMock = Mockito.mock(Traversal.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTableMock, transformationHandlerMock);
    scopedAliases.process(Mockito.mock(Node.class), Mockito.mock(Node.class));
  }

  @Test(expected = NullPointerException.class)
  public void testProcess_NullPreprocessorSymbolTable() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    AliasTransformationHandler transformationHandlerMock =
        Mockito.mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases =
        new ScopedAliases(compiler, null, transformationHandlerMock);
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);
    scopedAliases.process(externs, root);
  }

  @Test(expected = NullPointerException.class)
  public void testProcess_NullTransformationHandler() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTableMock =
        Mockito.mock(PreprocessorSymbolTable.class);
    ScopedAliases scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTableMock, null);
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);
    scopedAliases.process(externs, root);
  }

  @Test
  public void testHotSwapScript_NoChanges() {
    Node root = Mockito.mock(Node.class);
    Traversal traversalMock = Mockito.mock(Traversal.class);
    Mockito.when(traversalMock.getAliasDefinitionsInOrder()).thenReturn(Lists.newArrayList());
    Mockito.when(traversalMock.getAliasUsages()).thenReturn(Lists.newArrayList());
    Mockito.when(traversalMock.getScopeCalls()).thenReturn(Lists.newArrayList());
    Mockito.when(traversalMock.hasErrors()).thenReturn(false);

    Node oldRoot = Mockito.mock(Node.class);
    scopedAliases.hotSwapScript(root, oldRoot);
    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testHotSwapScript_HasChanges() {
    Node root = Mockito.mock(Node.class);
    Node oldRoot = Mockito.mock(Node.class);
    Traversal traversalMock = Mockito.mock(Traversal.class);
    Mockito.when(traversalMock.hasErrors()).thenReturn(false);
    Mockito.when(traversalMock.getAliasDefinitionsInOrder()).thenReturn(Lists.newArrayList());
    Mockito.when(traversalMock.getAliasUsages()).thenReturn(Lists.newArrayList());
    Mockito.when(traversalMock.getScopeCalls()).thenReturn(Lists.newArrayList());
    Mockito.when(compiler.reportCodeChange()).thenReturn(traversalMock);

    scopedAliases.hotSwapScript(root, oldRoot);
    Mockito.verify(compiler, Mockito.atLeastOnce()).reportCodeChange();
  }

  @Test(expected = NullPointerException.class)
  public void testHotSwapScript_NullCompiler() {
    Node root = Mockito.mock(Node.class);
    Node oldRoot = Mockito.mock(Node.class);
    ScopedAliases scopedAliases = new ScopedAliases(null, preprocessorSymbolTableMock, transformationHandlerMock);
    scopedAliases.hotSwapScript(root, oldRoot);
  }

  @Test(expected = NullPointerException.class)
  public void testHotSwapScript_NullRoot() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTableMock =
        Mockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandlerMock =
        Mockito.mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTableMock, transformationHandlerMock);
    Node oldRoot = Mockito.mock(Node.class);
    scopedAliases.hotSwapScript(null, oldRoot);
  }

  @Test
  public void testApplyAlias_AliasExists() {
    Node aliasReference = Mockito.mock(Node.class);
    Node aliasDefinition = Mockito.mock(Node.class);
    AliasedNode aliasedNode = new AliasedNode(aliasReference, aliasDefinition);

    scopedAliases.applyAlias(aliasedNode);

    Node expected = aliasDefinition;
    assertEquals(expected, aliasReference.getParent().getFirstChild());
  }

  @Test
  public void testApplyAlias_AliasDoesNotExist() {
    Node aliasReference = Mockito.mock(Node.class);
    AliasedNode aliasedNode = new AliasedNode(aliasReference, null);

    scopedAliases.applyAlias(aliasedNode);

    // No change to the aliasReference should be made
    assertNull(aliasReference.getParent().getFirstChild());
  }

  @Test(expected = NullPointerException.class)
  public void testApplyAlias_NullAliasedNode() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTableMock, transformationHandlerMock);
    scopedAliases.applyAlias(null);
  }

  @Test
  public void testFixTypeNode_NoAliases() {
    Node typeNode = Mockito.mock(Node.class);
    scopedAliases.fixTypeNode(typeNode);

    // No change to the typeNode should be made
    assertEquals(typeNode, Mockito.mock(Node.class));
  }

  @Test
  public void testFixTypeNode_HasAliases() {
    String baseName = "google";
    String aliasName = "g";
    Node baseNode = Mockito.mock(Node.class);
    Node aliasNode = Mockito.mock(Node.class);
    Var aliasVar = Mockito.mock(Var.class);
    Var baseVar = Mockito.mock(Var.class);

    Mockito.when(baseNode.getString()).thenReturn(baseName);
    Mockito.when(aliasNode.getString()).thenReturn(aliasName);
    Mockito.when(aliasVar.getName()).thenReturn(aliasName);
    Mockito.when(aliasVar.getInitialValue()).thenReturn(baseNode);
    Mockito.when(baseVar.getName()).thenReturn(baseName);
    Mockito.when(baseVar.getInitialValue()).thenReturn(aliasNode);
    Mockito.when(preprocessorSymbolTableMock.getVar(baseName)).thenReturn(baseVar);

    Node typeNode = Mockito.mock(Node.class);
    Mockito.when(typeNode.getType()).thenReturn(Token.STRING);
    Mockito.when(typeNode.getString()).thenReturn(baseName + "." + aliasName);

    scopedAliases.fixTypeNode(typeNode);

    AliasedTypeNode aliasedTypeNode = new AliasedTypeNode(typeNode, aliasName);

    assertEquals(aliasedTypeNode, Mockito.mock(AliasUsage.class));

    assertEquals(aliasNode, typeNode.getParent().getFirstChild());
  }

  @Test(expected = NullPointerException.class)
  public void testFixTypeNode_NullTypeNode() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTableMock, transformationHandlerMock);
    scopedAliases.fixTypeNode(null);
  }
}