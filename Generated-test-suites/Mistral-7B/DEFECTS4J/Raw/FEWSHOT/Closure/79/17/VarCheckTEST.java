package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeFactory;

import java.util.Set;

public class VarCheckTest {
    @Test
    public void testVarCheck() {
        AbstractCompiler compiler = new Compiler();
        CompilerPass varCheck = new VarCheck(compiler);

        CompilerInput input = new CompilerInput(
            "test.js",
            "function foo() {\n" +
            "  var x;\n" +
            "  x = 42;\n" +
            "}",
            null);

        Node root = NodeFactory.createRoot();
        NodeTraversal.traverse(compiler, root, varCheck);

        Scope scope = varCheck.compiler.getCurrentScope();
        Scope.Var var = scope.getVar("x");
        assertSame(root, var.node);
        assertFalse(var.isUndeclared);

        input = new CompilerInput(
            "test.js",
            "function foo() {\n" +
            "  var x;\n" +
            "}",
            null);

        root = NodeFactory.createRoot();
        NodeTraversal.traverse(compiler, root, varCheck);

        scope = varCheck.compiler.getCurrentScope();
        var = scope.getVar("x");
        assertNull(var);
        assertTrue(varCheck.varsToDeclareInExterns.contains("x"));

        input = new CompilerInput(
            "test.externs.js",
            "/** @const */ var x = 42;",
            null);

        Node externs = NodeFactory.createRoot();
        NodeTraversal.traverse(compiler, externs, varCheck);

        var = scope.getVar("x");
        assertSame(externs, var.input.getAstRoot(compiler));
        assertFalse(var.isUndeclared);

        input = new CompilerInput(
            "test.js",
            "function foo() {\n" +
            "  var x;\n" +
            "  x = foo.bar;\n" +
            "}",
            null);

        root = NodeFactory.createRoot();
        NodeTraversal.traverse(compiler, root, varCheck);

        Diagnostics diagnostics = compiler.getDiagnostics();
        assertTrue(diagnostics.getErrorCount() == 1);
        assertTrue(diagnostics.getError(0).getMessage().contains("JSC_VIOLATED_MODULE_DEPENDENCY"));
        assertTrue(diagnostics.getError(0).getMessage().contains("foo"));
        assertTrue(diagnostics.getError(0).getMessage().contains("test.js"));
        assertTrue(diagnostics.getError(0).getMessage().contains("test.externs.js"));
        assertTrue(diagnostics.getError(0).getMessage().contains("bar"));

        input = new CompilerInput(
            "test.js",
            "function foo() {\n" +
            "  var x;\n" +
            "  x = module.exports.bar;\n" +
            "}",
            null);

        root = NodeFactory.createRoot();
        NodeTraversal.traverse(compiler, root, varCheck);

        diagnostics = compiler.getDiagnostics();
        assertTrue(diagnostics.getErrorCount() == 1);
        assertTrue(diagnostics.getError(0).getMessage().contains("JSC_MISSING_MODULE_DEPENDENCY"));
        assertTrue(diagnostics.getError(0).getMessage().contains("test.js"));
        assertTrue(diagnostics.getError(0).getMessage().contains("module.exports"));
        assertTrue(diagnostics.getError(0).getMessage().contains("test.externs.js"));
        assertTrue(diagnostics.getError(0).getMessage().contains("bar"));

        Set<String> expected = Sets.newHashSet("x");
        Set<String> actual = Sets.newHashSet(varCheck.varsToDeclareInExterns.toArray(new String[0]));
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }
}