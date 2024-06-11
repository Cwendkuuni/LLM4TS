package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.rhino.SourceFile;

public class ScopedAliasesTest extends CompilationTestBase {

  @Test
  public void testScopedAliases() {
    String source = ""
        + "goog.scope(function() {"
        + "  var dom = goog.dom;"
        + "  var DIV = dom.TagName.DIV;"
        + "  goog.dom.createElement(DIV);"
        + "});";

    SourceFile input = new SourceFile("Test.java", source, null, null);
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALIASES_AS_CLASSES);
    options.setAliasTransformationHandler(new IdentityAliasTransformationHandler());

    compile(input, options);
    assertEquals(0, options.getDiagnostics().length);
    assertEquals("java.lang.String", getClassFromDeclaration(options.getOutput(), "DIV").getTypeName());
  }

  @Test
  public void testScopedAliasesWithMultipleAliases() {
    String source = ""
        + "goog.scope(function() {"
        + "  var dom = goog.dom;"
        + "  var DIV = dom.TagName.DIV;"
        + "  var GOOG = goog;"
        + "  var DOM = GOOG.dom;"
        + "  DOM.createElement(DIV);"
        + "});";

    SourceFile input = new SourceFile("Test.java", source, null, null);
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALIASES_AS_CLASSES);
    options.setAliasTransformationHandler(new IdentityAliasTransformationHandler());

    compile(input, options);
    assertEquals(0, options.getDiagnostics().length);
    assertEquals("java.lang.String", getClassFromDeclaration(options.getOutput(), "DIV").getTypeName());
    assertEquals("com.google.javascript.jscomp.GoogleDom", getClassFromDeclaration(options.getOutput(), "DOM").getTypeName());
  }

  @Test
  public void testScopedAliasesWithNonAliasedLocalVariable() {
    String source = ""
        + "goog.scope(function() {"
        + "  var dom = goog.dom;"
        + "  var DIV = dom.TagName.DIV;"
        + "  var result = goog.dom.createElement(DIV);"
        + "});";

    SourceFile input = new SourceFile("Test.java", source, null, null);
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALIASES_AS_CLASSES);
    options.setAliasTransformationHandler(new IdentityAliasTransformationHandler());

    compile(input, options);
    assertEquals(1, options.getDiagnostics().length);
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", options.getDiagnostics()[0].getCode());
    assertEquals("result", options.getDiagnostics()[0].getMessage());
  }

  @Test
  public void testScopedAliasesWithInvalidScopeCall() {
    String source = ""
        + "goog.scope(function() {"
        + "  goog.scope(function() {"
        + "    var DIV = goog.dom.TagName.DIV;"
        + "  });"
        + "  goog.dom.createElement(DIV);"
        + "});";

    SourceFile input = new SourceFile("Test.java", source, null, null);
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALIASES_AS_CLASSES);
    options.setAliasTransformationHandler(new IdentityAliasTransformationHandler());

    compile(input, options);
    assertEquals(1, options.getDiagnostics().length);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", options.getDiagnostics()[0].getCode());
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    String source = ""
        + "goog.scope(function() {"
        + "  var dom = goog.dom;"
        + "  return goog.dom.createElement(dom.TagName.DIV);"
        + "});";

    SourceFile input = new SourceFile("Test.java", source, null, null);
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALIASES_AS_CLASSES);
    options.setAliasTransformationHandler(new IdentityAliasTransformationHandler());

    compile(input, options);
    assertEquals(1, options.getDiagnostics().length);
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", options.getDiagnostics()[0].getCode());
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    String source = ""
        + "goog.scope(function() {"
        + "  try {"
        + "    goog.dom.createElement(goog.dom.TagName.DIV);"
        + "  } catch (e) {"
        + "    throw new Error('Test')";
        + "  }"
        + "});";

    SourceFile input = new SourceFile("Test.java", source, null, null);
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALIASES_AS_CLASSES);
    options.setAliasTransformationHandler(new IdentityAliasTransformationHandler());

    compile(input, options);
    assertEquals(1, options.getDiagnostics().length);
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", options.getDiagnostics()[0].getCode());
  }

}