import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.test.BaseCompilerTest;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

/**
 * Test ScopedAliases class
 */
public class ScopedAliasesTest extends BaseCompilerTest {

  @Test
  public void testScopedAliases() {
    // Test case for valid scoped alias usage
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
    AbstractCompiler compiler = new AbstractCompiler(options, null);

    List<String> errors = compile(compiler, sourceCode);
    assertTrue(errors.isEmpty());

    // Test case for improper usage of goog.scope
    sourceCode = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "});" +
        "});";

    errors = compile(compiler, sourceCode, null, GOOG_SCOPE_USED_IMPROPERLY);
    assertFalse(errors.isEmpty());
    assertEquals(1, errors.size());

    // Test case for goog.scope with bad parameters
    sourceCode = "goog.scope(function() {" +
        "function goo() {}" +
        "goog.scope(goo);" +
        "});";

    errors = compile(compiler, sourceCode, null, GOOG_SCOPE_HAS_BAD_PARAMETERS);
    assertFalse(errors.isEmpty());
    assertEquals(1, errors.size());

    // Test case for goog.scope references 'this'
    sourceCode = "goog.scope(function() {" +
        "this.test = 1;" +
        "});";

    errors = compile(compiler, sourceCode, null, GOOG_SCOPE_REFERENCES_THIS);
    assertFalse(errors.isEmpty());
    assertEquals(1, errors.size());

    // Test case for goog.scope uses return
    sourceCode = "goog.scope(function() {" +
        "return 1;" +
        "});";

    errors = compile(compiler, sourceCode, null, GOOG_SCOPE_USES_RETURN);
    assertFalse(errors.isEmpty());
    assertEquals(1, errors.size());

    // Test case for goog.scope uses throw
    sourceCode = "goog.scope(function() {" +
        "throw new Error('Test error');" +
        "});";

    errors = compile(compiler, sourceCode, null, GOOG_SCOPE_USES_THROW);
    assertFalse(errors.isEmpty());
    assertEquals(1, errors.size());

    // Test case for alias redefined
    sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "var dom = goog.dom;" +
        "});";

    errors = compile(compiler, sourceCode, null, GOOG_SCOPE_ALIAS_REDEFINED);
    assertFalse(errors.isEmpty());
    assertEquals(1, errors.size());

    // Test case for non-alias local variable
    sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var test = 1;" +
        "});";

    errors = compile(compiler, sourceCode, null, GOOG_SCOPE_NON_ALIAS_LOCAL);
    assertFalse(errors.isEmpty());
    assertEquals(1, errors.size());

    // Test case for valid scoped alias usage with JSDoc
    sourceCode = "/** @type {goog.dom.TagName} */" +
        "var DIV = goog.dom.TagName.DIV;" +
        "goog.scope(function() {" +
        "   goog.dom.createElement(DIV);" +
        "});";

    errors = compile(compiler, sourceCode, null);
    assertTrue(errors.isEmpty());

    // Test case for valid scoped alias usage with multiple aliases
    sourceCode = "goog.scope(function() {" +
        "   var dom = goog.dom;" +
        "   var DIV = dom.TagName.DIV;" +
        "   var NAME = goog.STRINGS.NAME;" +
        "   goog.dom.createElement(DIV);" +
        "   goog.dom.createElement(NAME);" +
        "});";

    errors = compile(compiler, sourceCode, null);
    assertTrue(errors.isEmpty());

    // Test case for valid scoped alias usage with transitive aliasing
    sourceCode = "goog.scope(function() {" +
        "   var g = goog;" +
        "   var d = g.dom;" +
        "   d.createElement(g.dom.TagName.DIV);" +
        "});";

    errors = compile(compiler, sourceCode, null);
    assertTrue(errors.isEmpty());

    // Test case for valid scoped alias usage with type aliasing
    sourceCode = "goog.scope(function() {" +
        "   /** @type {goog.dom.TagName} */" +
        "   var DIV = goog.dom.TagName.DIV;" +
        "   goog.dom.createElement(DIV);" +
        "});";

    errors = compile(compiler, sourceCode, null);
    assertTrue(errors.isEmpty());
  }

  @Test
  public void testScopedAliasesWithException() {
    // Test case for goog.scope with exception
    String sourceCode = "goog.scope(function() {" +
        "try {" +
        "throw new Error('Test error');" +
        "} catch (e) {" +
        "}" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
    AbstractCompiler compiler = new ScopedAliases(null, null, null);

    List<String> errors = compile(compiler, sourceCode, null);
    assertFalse(errors.isEmpty());
    assertEquals(1, errors.size());

    errors = compile(compiler, sourceCode, null, GOOG_SCOPE_USES_THROW);
    assertFalse(errors.isEmpty());
    assertEquals(1, errors.size());
  }

  @Test
  public void testScopedAliasesWithMultipleAliasDefinitions() {
    // Test case for goog.scope with multiple alias definitions
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = goog.dom.TagName.DIV;" +
        "var DIV2 = goog.dom.TagName.DIV;" +
        "goog.dom.createElement(DIV);" +
        "goog.dom.createElement(DIV2);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
    AbstractCompiler compiler = new ScopedAliases(null, null, null);

    List<String> errors = compile(compiler, sourceCode, null, GOOG_SCOPE_ALIAS_REDEFINED);
    assertFalse(errors.isEmpty());
    assertEquals(1, errors.size());
  }

  @Test
  public void testScopedAliasesWithNestedScope() {
    // Test case for goog.scope with nested scopes
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {" +
        "   dom.createElement(goog.dom.TagName.DIV);" +
        "});" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
    AbstractCompiler compiler = new ScopedAliases(null, null, null);

    List<String> errors = compile(compiler, sourceCode, null);
    assertTrue(errors.isEmpty());
  }
}