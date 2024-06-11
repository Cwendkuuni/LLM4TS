package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {
  @Mock
  private AbstractCompiler compiler;
  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;
  @Mock
  private AliasTransformationHandler transformationHandler;

  @InjectMocks
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_NoErrors() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    scopedAliases.process(externs, root);

    Mockito.verify(compiler).reportCodeChange();
    Mockito.verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testProcess_HasErrors() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    Traversal traversal = Mockito.mock(Traversal.class);
    Mockito.when(traversal.getAliasDefinitionsInOrder()).thenReturn(Lists.newArrayList());
    Mockito.when(traversal.getAliasUsages()).thenReturn(Lists.newArrayList());
    Mockito.when(traversal.getScopeCalls()).thenReturn(Lists.newArrayList());
    Mockito.when(traversal.hasErrors()).thenReturn(true);

    Mockito.when(compiler.report(Mockito.any(Diagnostic.class))).thenReturn(true);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler) {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
        if (traversal.hasErrors()) {
          throw new RuntimeException("Test error");
        }
      }
    };

    scopedAliases.process(externs, root);

    Mockito.verify(compiler, Mockito.times(1)).report(Mockito.any(Diagnostic.class));
  }

  @Test
  public void testHotSwapScript_NoErrors() {
    Node root = Mockito.mock(Node.class);

    Traversal traversal = Mockito.mock(Traversal.class);
    Mockito.when(traversal.getAliasDefinitionsInOrder()).thenReturn(Lists.newArrayList());
    Mockito.when(traversal.getAliasUsages()).thenReturn(Lists.newArrayList());
    Mockito.when(traversal.getScopeCalls()).thenReturn(Lists.newArrayList());
    Mockito.when(traversal.hasErrors()).thenReturn(false);

    Mockito.when(compiler.reportCodeChange()).thenReturn(true);

    scopedAliases.hotSwapScript(root, null);

    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
  }

  @Test
  public void testHotSwapScript_HasErrors() {
    Node root = Mockito.mock(Node.class);

    Traversal traversal = Mockito.mock(Traversal.class);
    Mockito.when(traversal.getAliasDefinitionsInOrder()).thenReturn(Lists.newArrayList());
    Mockito.when(traversal.getAliasUsages()).thenReturn(Lists.newArrayList());
    Mockito.when(traversal.getScopeCalls()).thenReturn(Lists.newArrayList());
    Mockito.when(traversal.hasErrors()).thenReturn(true);

    Mockito.when(compiler.reportCodeChange()).thenReturn(true);

    scopedAliases.hotSwapScript(root, null);

    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
  }

  @Test
  public void testApplyAlias() {
    Node aliasReference = Mockito.mock(Node.class);
    Node aliasDefinition = Mockito.mock(Node.class);

    AliasedNode aliasedNode = new AliasedNode(aliasReference, aliasDefinition);

    scopedAliases.getScopedAliases().get(0).applyAlias();

    Mockito.verify(aliasReference.getParent()).replaceChild(aliasReference, aliasDefinition);
  }

  @Test
  public void testApplyAliasType() {
    Node aliasReference = Mockito.mock(Node.class);
    String correctedType = "String";

    AliasedTypeNode aliasedTypeNode = new AliasedTypeNode(aliasReference, correctedType);

    scopedAliases.getScopedAliases().get(0).applyAlias();

    Mockito.verify(aliasReference.setString(correctedType));
  }
}