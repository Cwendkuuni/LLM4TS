package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Capture;

public class ScopedAliasesTest {
  private ScopedAliases scopedAliases;
  private AbstractCompiler compilerMock;
  private PreprocessorSymbolTable preprocessorSymbolTableMock;
  private AliasTransformationHandler transformationHandlerMock;

  @Before
  public void setup() {
    compilerMock = mock(AbstractCompiler.class);
    preprocessorSymbolTableMock = mock(PreprocessorSymbolTable.class);
    transformationHandlerMock = mock(AliasTransformationHandler.class);

    scopedAliases = new ScopedAliases(compilerMock, preprocessorSymbolTableMock, transformationHandlerMock);
  }

  @Test
  public void testProcess_NoAliases() {
    Node root = Node.newNode("function test() {}");
    scopedAliases.process(null, root);

    // No checks here as there are no aliases to test
  }

  @Test
  public void testProcess_AliasesIncorrectFunction() {
    Node root = Node.newNode("function test() { goog.scope(function() { goog.dom.createElement('DIV'); }); }");

    ArgumentCaptor<AliasTransformation> transformationCaptor = ArgumentCaptor.forClass(AliasTransformation.class);
    when(transformationHandlerMock.logAliasTransformation(eq("test.js"), any())).thenReturn(transformationCaptor.capture());

    scopedAliases.process(null, root);

    AliasTransformation transformation = transformationCaptor.getValue();
    assertEquals("goog.dom.createElement", transformation.getNewName("DIV"));
  }

  @Test
  public void testProcess_AliasesCorrectFunction() {
    Node root = Node.newNode("function test() { goog.scope(function() { var DIV = goog.dom.TagName.DIV; goog.dom.createElement(DIV); }); }");

    ArgumentCaptor<AliasTransformation> transformationCaptor = ArgumentCaptor.forClass(AliasTransformation.class);
    when(transformationHandlerMock.logAliasTransformation(eq("test.js"), any())).thenReturn(transformationCaptor.capture());

    scopedAliases.process(null, root);

    AliasTransformation transformation = transformationCaptor.getValue();
    assertEquals("goog.dom.TagName.DIV", transformation.getNewName("DIV"));
  }

  @Test(expected = AssertionError.class)
  public void testProcess_FunctionWithReturnStatement() {
    Node root = Node.newNode("function test() { goog.scope(function() { return 42; }); }");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_FunctionWithThisKeyword() {
    Node root = Node.newNode("function test() { goog.scope(function() { this.counter = 0; }); }");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_FunctionWithThrowStatement() {
    Node root = Node.newNode("function test() { goog.scope(function() { throw new Error('Test Error'); }); }");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_AliasRedefined() {
    Node root = Node.newNode("function test() { goog.scope(function() { var DIV = goog.dom.TagName.DIV; goog.dom.createElement(DIV); var DIV = 'new value'; }); }");

    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_NonAliasLocalVariable() {
    Node root = Node.newNode("function test() { goog.scope(function() { var x = 42; }); }");

    scopedAliases.process(null, root);
  }
}