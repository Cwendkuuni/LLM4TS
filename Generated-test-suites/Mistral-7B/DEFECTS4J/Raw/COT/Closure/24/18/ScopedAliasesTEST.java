package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

import javax.annotation.Nullable;

@RunWith(org.junit.runner.JUnit4.class)
public class ScopedAliasesTest {
  @Nullable
  private AbstractCompiler compiler;
  @Nullable
  private PreprocessorSymbolTable preprocessorSymbolTable;
  @Nullable
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    transformationHandler = Mockito.mock(AliasTransformationHandler.class);
  }

  @Test
  public void testScopedAliases_validAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = RuntimeEnvironment.newJavaScriptSource("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});");
    scopedAliases.process(null, root);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom.createElement", aliasDefinitionsInOrder.get(0).getString());
  }

  @Test
  public void testScopedAliases_multipleAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = RuntimeEnvironment.newJavaScriptSource("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var TAG_NAME = DIV;" +
        "  goog.dom.createElement(TAG_NAME);" +
        "});");
    scopedAliases.process(null, root);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom.createElement", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("div", aliasDefinitionsInOrder.get(1).getString());
  }

  @Test
  public void testScopedAliases_nestedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = RuntimeEnvironment.newJavaScriptSource("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    var divTagName = DIV;" +
        "    goog.dom.createElement(divTagName);" +
        "  });" +
        "});");
    scopedAliases.process(null, root);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(3, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom.createElement", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("div", aliasDefinitionsInOrder.get(1).getString());
    assertEquals("div", aliasDefinitionsInOrder.get(2).getString());
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_wrongNumberOfParameters() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = RuntimeEnvironment.newJavaScriptSource("goog.scope(function() {}," +
        "  function() {" +
        "  });");
    scopedAliases.process(null, root);
    fail("No error reported.");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_functionInBody() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = RuntimeEnvironment.newJavaScriptSource("goog.scope(function() {" +
        "  function test() {}" +
        "  goog.dom.createElement('div');" +
        "});");
    scopedAliases.process(null, root);
    fail("No error reported.");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_thisKeyword() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = RuntimeEnvironment.newJavaScriptSource("goog.scope(function() {" +
        "  this.test = 1;" +
        "  goog.dom.createElement('div');" +
        "});");
    scopedAliases.process(null, root);
    fail("No error reported.");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_returnKeyword() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = RuntimeEnvironment.newJavaScriptSource("goog.scope(function() {" +
        "  return goog.dom.createElement('div');" +
        "});");
    scopedAliases.process(null, root);
    fail("No error reported.");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_throwKeyword() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = RuntimeEnvironment.newJavaScriptSource("goog.scope(function() {" +
        "  throw new Error('Test error.');" +
        "});");
    scopedAliases.process(null, root);
    fail("No error reported.");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_aliasRedefined() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = RuntimeEnvironment.newJavaScriptSource("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var DIV = 'div';" +
        "  goog.dom.createElement(DIV);" +
        "});");
    scopedAliases.process(null, root);
    fail("No error reported.");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliases_nonAliasLocalVariable() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = RuntimeEnvironment.newJavaScriptSource("goog.scope(function() {" +
        "  var test = 1;" +
        "  goog.dom.createElement('div');" +
        "});");
    scopedAliases.process(null, root);
    fail("No error reported.");
  }
}