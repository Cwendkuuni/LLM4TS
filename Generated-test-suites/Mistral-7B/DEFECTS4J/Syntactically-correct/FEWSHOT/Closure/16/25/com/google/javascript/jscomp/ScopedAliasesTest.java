package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ScopedAliasesTest {
    @Test
    public void testScopedAliases() {
        // Test successful scoped aliases
        String sourceCode = "goog.scope(function() {" +
                "  var dom = goog.dom;" +
                "  var DIV = dom.TagName.DIV;" +
                "  goog.dom.createElement(DIV);" +
                "});";

        AbstractCompiler compiler = new AbstractCompiler();
        AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
        HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
        Node root = new Node();
        root.setSource(new SourcePosition("test.js", 1, 1), sourceCode.toCharArray(), 0);
        scopedAliases.process(null, root);
        Traversal traversal = scopedAliases.getTraversal();

        assertEquals(0, traversal.getErrors().size());
        assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
                traversal.getOutputCode());

        // Test errors with improperly used goog.scope
        sourceCode = "goog.scope(function() {" +
                "  goog.scope(function() {" +
                "    var x = 1;" +
                "  });" +
                "});";

        root = new Node();
        root.setSource(new SourcePosition("test.js", 1, 1), sourceCode.toCharArray(), 0);
        scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
        scopedAliases.process(null, root);
        traversal = scopedAliases.getTraversal();

        assertEquals(1, traversal.getErrors().size());
        assertEquals(DiagnosticType.error(
                        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
                        "The call to goog.scope must be alone in a single statement."),
                traversal.getErrors().get(0).getMessage());

        // Test errors with goog.scope having bad parameters
        sourceCode = "goog.scope(function() {" +
                "  goog.scope(function() {}," +
                "  1);" +
                "});";

        root = new Node();
        root.setSource(new SourcePosition("test.js", 1, 1), sourceCode.toCharArray(), 0);
        scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
        scopedAliases.process(null, root);
        traversal = scopedAliases.getTraversal();

        assertEquals(1, traversal.getErrors().size());
        assertEquals(DiagnosticType.error(
                        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
                        "The call to goog.scope must take only a single parameter.  It must" +
                                " be an anonymous function that itself takes no parameters."),
                traversal.getErrors().get(0).getMessage());

        // Test errors with goog.scope referencing 'this'
        sourceCode = "goog.scope(function() {" +
                "  this.x = 1;" +
                "});";

        root = new Node();
        root.setSource(new SourcePosition("test.js", 1, 1), sourceCode.toCharArray(), 0);
        scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
        scopedAliases.process(null, root);
        traversal = scopedAliases.getTraversal();

        assertEquals(1, traversal.getErrors().size());
        assertEquals(DiagnosticType.error(
                        "JSC_GOOG_SCOPE_REFERENCES_THIS",
                        "The body of a goog.scope function cannot reference 'this'."),
                traversal.getErrors().get(0).getMessage());
    }
}