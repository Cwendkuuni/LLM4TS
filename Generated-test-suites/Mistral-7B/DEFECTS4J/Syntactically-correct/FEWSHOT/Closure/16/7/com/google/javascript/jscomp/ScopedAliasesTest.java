package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test a valid scoped alias
    Node root = NodeFactory.createRootNode(
        "function foo() {" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  dom.createElement(DIV);" +
            "});" +
            "console.log(goog.dom.createElement(goog.dom.TagName.DIV));" +
            "}");
    scopedAliases.process(null, root);

    // Check that the alias was applied
    NodeUtil.assertTreeEquals(
        "function foo() {" +
            "console.log(goog.dom.createElement(goog.dom.TagName.DIV));" +
            "}",
        root);

    // Check that the alias was not applied to another call
    Node root2 = NodeFactory.createRootNode(
        "function bar() {" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "});" +
            "console.log(goog.dom.createElement(goog.dom.TagName.DIV));" +
            "console.log(goog.dom.createElement(goog.dom.TagName.DIV));" +
            "}"
    );
    scopedAliases.process(null, root2);

    // Check that the second call to goog.dom.createElement does not use the alias
    NodeUtil.assertTreeEquals(
        "function bar() {" +
            "console.log(goog.dom.createElement(goog.dom.TagName.DIV));" +
            "console.log(dom.createElement(dom.TagName.DIV));" +
            "}",
        root2);

    // Test that errors are reported for invalid usage of goog.scope
    Node root3 = NodeFactory.createRootNode(
        "function baz() {" +
            "goog.scope(function() {" +
            "});" +
            "console.log(goog.dom.createElement(goog.dom.TagName.DIV));" +
            "}"
    );
    scopedAliases.process(null, root3);

    List<DiagnosticType> errors = Lists.newArrayList(compiler.getDiagnostics());

    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        errors.get(0).getTypeName());

    // Test that errors are reported for multiple statements in a goog.scope
    Node root4 = NodeFactory.createRootNode(
        "function qux() {" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  dom.createElement(DIV);" +
            "  var foo = 1;" +
            "});" +
            "console.log(goog.dom.createElement(goog.dom.TagName.DIV));" +
            "}"
    );
    scopedAliases.process(null, root4);

    errors = Lists.newArrayList(compiler.getDiagnostics());

    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        errors.get(0).getTypeName());

    // Test that errors are reported for non-function parameters
    Node root5 = NodeFactory.createRootNode(
        "function quux() {" +
            "goog.scope('foo');" +
            "}"
    );
    scopedAliases.process(null, root5);

    errors = Lists.newArrayList(compiler.getDiagnostics());

    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        errors.get(0).getTypeName());

    // Test that errors are reported for 'this' references
    Node root6 = NodeFactory.createRootNode(
        "function quux() {" +
            "goog.scope(function() {" +
            "  this.foo = 1;" +
            "});" +
            "}"
    );
    scopedAliases.process(null, root6);

    errors = Lists.newArrayList(compiler.getDiagnostics());

    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_REFERENCES_THIS",
        errors.get(0).getTypeName());

    // Test that errors are reported for return statements
    Node root7 = NodeFactory.createRootNode(
        "function quux() {" +
            "goog.scope(function() {" +
            "  return 1;" +
            "});" +
            "}"
    );
    scopedAliases.process(null, root7);

    errors = Lists.newArrayList(compiler.getDiagnostics());

    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_USES_RETURN",
        errors.get(0).getTypeName());

    // Test that errors are reported for throw statements
    Node root8 = NodeFactory.createRootNode(
        "function quux() {" +
            "goog.scope(function() {" +
            "  throw new Error('foo');" +
            "});" +
            "}"
    );
    scopedAliases.process(null, root8);

    errors = Lists.newArrayList(compiler.getDiagnostics());

    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_USES_THROW",
        errors.get(0).getTypeName());

    // Test that errors are reported for alias redefinition
    Node root9 = NodeFactory.createRootNode(
        "function quux() {" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.scope(function() {" +
            "    var DIV = 'foo';" +
            "  });" +
            "  console.log(goog.dom.createElement(DIV));" +
            "});" +
            "}"
    );
    scopedAliases.process(null, root9);

    errors = Lists.newArrayList(compiler.getDiagnostics());

    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        errors.get(0).getTypeName());

    // Test that errors are reported for non-alias local variables in a goog.scope
    Node root10 = NodeFactory.createRootNode(
        "function quux() {" +
            "goog.scope(function() {" +
            "  var foo = 1;" +
            "});" +
            "console.log(foo);" +
            "}"
    );
    scopedAliases.process(null, root10);

    errors = Lists.newArrayList(compiler.getDiagnostics());

    assertEquals(1, errors.size());
    assertEquals(
        "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        errors.get(0).getTypeName());
  }
}