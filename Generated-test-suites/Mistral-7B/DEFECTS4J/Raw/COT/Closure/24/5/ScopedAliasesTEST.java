package com.google.javascript.jscomp;

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
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Test class for ScopedAliases class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  @InjectMocks
  private ScopedAliases scopedAliases;

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  /**
   * Test method for valid usage of goog.scope with a single parameter.
   */
  @Test
  public void testValidUsageOfGoogScope() {
    Node rootNode = Mockito.mock(Node.class);
    Node scopeCall = Mockito.mock(Node.class);
    Node anonymousFunction = Mockito.mock(Node.class);

    Mockito.when(rootNode.getParent()).thenReturn(null);
    Mockito.when(scopeCall.getParent()).thenReturn(rootNode);
    Mockito.when(scopeCall.getLastChild()).thenReturn(anonymousFunction);
    Mockito.when(anonymousFunction.isFunction()).thenReturn(true);

    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.getAliasUsages().isEmpty());
    assertTrue(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(scopedAliases.getScopeCalls().size() == 1);
  }

  /**
   * Test method for invalid usage of goog.scope with more than one parameter.
   */
  @Test
  public void testInvalidUsageOfGoogScopeWithMultipleParameters() {
    Node rootNode = Mockito.mock(Node.class);
    Node scopeCall = Mockito.mock(Node.class);
    Node anonymousFunction = Mockito.mock(Node.class);
    Node parameter1 = Mockito.mock(Node.class);
    Node parameter2 = Mockito.mock(Node.class);

    Mockito.when(rootNode.getParent()).thenReturn(null);
    Mockito.when(scopeCall.getParent()).thenReturn(rootNode);
    Mockito.when(scopeCall.getLastChild()).thenReturn(anonymousFunction);
    Mockito.when(anonymousFunction.isFunction()).thenReturn(true);
    Mockito.when(scopeCall.getChildCount()).thenReturn(3);
    Mockito.when(scopeCall.getChildAtIndex(1)).thenReturn(parameter1);
    Mockito.when(scopeCall.getChildAtIndex(2)).thenReturn(parameter2);

    scopedAliases.process(null, rootNode);

    assertFalse(scopedAliases.getAliasUsages().isEmpty());
    assertTrue(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(scopedAliases.getScopeCalls().size() == 1);

    Mockito.verify(compiler).report(Mockito.any());
  }

  /**
   * Test method for invalid usage of goog.scope without a parameter.
   */
  @Test
  public void testInvalidUsageOfGoogScopeWithoutParameter() {
    Node rootNode = Mockito.mock(Node.class);
    Node scopeCall = Mockito.mock(Node.class);

    Mockito.when(rootNode.getParent()).thenReturn(null);
    Mockito.when(scopeCall.getParent()).thenReturn(rootNode);

    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.getAliasUsages().isEmpty());
    assertTrue(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(scopedAliases.getScopeCalls().isEmpty());

    Mockito.verify(compiler).report(Mockito.any());
  }

  /**
   * Test method for invalid usage of goog.scope with a non-function body.
   */
  @Test
  public void testInvalidUsageOfGoogScopeWithNonFunctionBody() {
    Node rootNode = Mockito.mock(Node.class);
    Node scopeCall = Mockito.mock(Node.class);
    Node nonFunctionBody = Mockito.mock(Node.class);

    Mockito.when(rootNode.getParent()).thenReturn(null);
    Mockito.when(scopeCall.getParent()).thenReturn(rootNode);
    Mockito.when(scopeCall.getLastChild()).thenReturn(nonFunctionBody);

    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.getAliasUsages().isEmpty());
    assertTrue(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(scopedAliases.getScopeCalls().size() == 1);

    Mockito.verify(compiler).report(Mockito.any());
  }

  /**
   * Test method for invalid usage of goog.scope with a throwing statement.
   */
  @Test
  public void testInvalidUsageOfGoogScopeWithThrowStatement() {
    Node rootNode = Mockito.mock(Node.class);
    Node scopeCall = Mockito.mock(Node.class);
    Node throwStatement = Mockito.mock(Node.class);

    Mockito.when(rootNode.getParent()).thenReturn(null);
    Mockito.when(scopeCall.getParent()).thenReturn(rootNode);
    Mockito.when(scopeCall.getLastChild()).thenReturn(throwStatement);
    Mockito.when(throwStatement.isThrow()).thenReturn(true);

    scopedAliases.process(null, rootNode);

    assertTrue(scopedAliases.getAliasUsages().isEmpty());
    assertTrue(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(scopedAliases.getScopeCalls().isEmpty());

    Mockito.verify(compiler).report(Mockito.any());
  }
}