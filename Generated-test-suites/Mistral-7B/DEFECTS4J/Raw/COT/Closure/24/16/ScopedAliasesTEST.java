package com.google.javascript.jscomp.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private CompilationTestBase testBase;
  private ScopedAliases scopedAliases;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() throws Exception {
    testBase = new CompilationTestBase() {};
    testBase.initEnvironment();

    CompilerOptions options = testBase.getOptions();
    options.aliasTransformations = List.of(CompilerOptions.AliasTransformation.GOOG_SCOPE);
    scopedAliases = new ScopedAliases(testBase.compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_NoErrors() throws Exception {
    Node root = testBase.parseTestFile("ScopedAliasesTest.js");
    scopedAliases.process(testBase.getExterns(), root);
    testBase.assertErrorsEmpty();
  }

  @Test
  public void testHotSwapScript_NoErrors() throws Exception {
    Node root = testBase.parseTestFile("ScopedAliasesTest.js");
    scopedAliases.hotSwapScript(root, null);
    testBase.assertErrorsEmpty();
  }

  @Test
  public void testProcess_GoogScopeUsedImproperly() throws Exception {
    String code = "goog.scope(function(){\n" +
        "  goog.scope(function(){\n" +
        "  });\n" +
        "});";
    Node root = testBase.parse(code);
    scopedAliases.process(testBase.getExterns(), root);

    List<String> errors = testBase.getErrors();
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", errors.get(0).getType());
  }

  @Test
  public void testProcess_GoogScopeHasBadParameters() throws Exception {
    String code = "goog.scope(function(arg1, arg2){});";
    Node root = testBase.parse(code);
    scopedAliases.process(testBase.getExterns(), root);

    List<String> errors = testBase.getErrors();
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", errors.get(0).getType());
  }

  @Test
  public void testProcess_GoogScopeRefersToThis() throws Exception {
    String code = "goog.scope(function(){\n" +
        "  this.test = 1;\n" +
        "});";
    Node root = testBase.parse(code);
    scopedAliases.process(testBase.getExterns(), root);

    List<String> errors = testBase.getErrors();
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", errors.get(0).getType());
  }

  @Test
  public void testProcess_GoogScopeUsesReturn() throws Exception {
    String code = "goog.scope(function(){\n" +
        "  return 1;\n" +
        "});";
    Node root = testBase.parse(code);
    scopedAliases.process(testBase.getExterns(), root);

    List<String> errors = testBase.getErrors();
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("JSC_GOOG_SCOPE_USES_RETURN", errors.get(0).getType());
  }

  @Test
  public void testProcess_GoogScopeUsesThrow() throws Exception {
    String code = "goog.scope(function(){\n" +
        "  throw new Error('Test Error');\n" +
        "});";
    Node root = testBase.parse(code);
    scopedAliases.process(testBase.getExterns(), root);

    List<String> errors = testBase.getErrors();
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("JSC_GOOG_SCOPE_USES_THROW", errors.get(0).getType());
  }

  @Test
  public void testProcess_AliasRedefined() throws Exception {
    String code = "goog.scope(function(){\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +
        "});";
    Node root = testBase.parse(code);
    scopedAliases.process(testBase.getExterns(), root);

    List<String> errors = testBase.getErrors();
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", errors.get(0).getType());
  }

  @Test
  public void testProcess_NonAliasLocal() throws Exception {
    String code = "goog.scope(function(){\n" +
        "  var a = 1;\n" +
        "  goog.scope(function(){\n" +
        "    var a = 2;\n" +
        "  });\n" +
        "});";
    Node root = testBase.parse(code);
    scopedAliases.process(testBase.getExterns(), root);

    List<String> errors = testBase.getErrors();
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", errors.get(0).getType());
  }

  @Test
  public void testProcess_HotSwapScript_MultipleAliases() throws Exception {
    String code = "goog.scope(function(){\n" +
        "  var goog = {\n" +
        "    dom: function(){\n" +
        "      this.createElement = function(tagName) {\n" +
        "        return document.createElement(tagName);\n" +
        "      };\n" +
        "    }\n" +
        "  };\n" +
        "});";
    Node root = testBase.parse(code);
    scopedAliases.process(testBase.getExterns(), root);

    testBase.assertErrorsEmpty();

    Node goog = testBase.getGlobalScope().getChildByName("goog");
    Assert.assertNotNull(goog);
    Node dom = testBase.getGlobalScope().getChildByName("goog.dom");
    Assert.assertNotNull(dom);
    Node createElement = dom.getChildByName("createElement");
    Assert.assertNotNull(createElement);

    String newCode = "goog.scope(function(){\n" +
        "  var goog = {\n" +
        "    dom: function(){\n" +
        "      this.createElement = function(tagName, id) {\n" +
        "        var elem = document.createElement(tagName);\n" +
        "        elem.id = id;\n" +
        "        return elem;\n" +
        "      };\n" +
        "    }\n" +
        "  };\n" +
        "});";
    Node updatedRoot = testBase.parse(newCode);
    scopedAliases.hotSwapScript(updatedRoot, root);

    testBase.assertErrorsEmpty();

    Node updatedGoog = testBase.getGlobalScope().getChildByName("goog");
    Node updatedDom = testBase.getGlobalScope().getChildByName("goog.dom");
    Node updatedCreateElement = updatedDom.getChildByName("createElement");
    Node idNode = updatedCreateElement.getChildAtIndex(1);
    Assert.assertNotNull(idNode);
    Assert.assertEquals(Token.ID, idNode.getType());

    Node updatedElement = updatedCreateElement.getLastChild();
    Assert.assertEquals(Token.GETPROP, updatedElement.getType());
    Assert.assertEquals("id", updatedElement.getString());
  }
}