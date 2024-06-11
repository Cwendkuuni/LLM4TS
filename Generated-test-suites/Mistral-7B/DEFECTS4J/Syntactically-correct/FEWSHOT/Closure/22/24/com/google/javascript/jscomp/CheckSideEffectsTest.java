package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {
    private static final String PROTECTOR_FN = "JSCOMPILER_PRESERVE";

    @Test
    public void testCheckSideEffects() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        Node root = createNodeFromString("var a = 1;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("function foo() {}");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("var a = 1 + 1;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("var a = 1; a = 2;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("function foo() { return 1; }");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("function foo() { return 1; } foo();");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("for (var i = 0; i < 10; i++) {}");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("for (var i = 0; i < 10; i++) { i++; }");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("var a = 1; eval(\"a = 2\");");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("\"this string is continued on the next line but you forgot the +\";");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("function foo() {} ;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("var a = 1; var b = a;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("function foo() { return 1 + 1; } foo();");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("var a = 1; a = 2; var b = a;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("var a = 1; function foo() { a = 2; } foo();");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
    }

    @Test
    public void testProtectSideEffects() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        Node root = createNodeFromString("var a = 1;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        checkSideEffects.protectSideEffects();
        assertNull(checkSideEffects.problemNodes.get(0));

        root = createNodeFromString("function foo() {}");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        checkSideEffects.protectSideEffects();
        assertNull(checkSideEffects.problemNodes.get(0));

        root = createNodeFromString("var a = 1 + 1;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        checkSideEffects.protectSideEffects();
        assertNull(checkSideEffects.problemNodes.get(0));

        root = createNodeFromString("var a = 1; a = 2;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("function foo() { return 1; }");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        checkSideEffects.protectSideEffects();
        assertNull(checkSideEffects.problemNodes.get(0));

        root = createNodeFromString("function foo() { return 1; } foo();");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("for (var i = 0; i < 10; i++) {}");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        checkSideEffects.protectSideEffects();
        assertNull(checkSideEffects.problemNodes.get(0));

        root = createNodeFromString("for (var i = 0; i < 10; i++) { i++; }");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("var a = 1; eval(\"a = 2\");");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("\"this string is continued on the next line but you forgot the +\";");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("function foo() {} ;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("var a = 1; var b = a;");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        root = createNodeFromString("function foo() { a = 2; } foo();");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
    }

    @Test
    public void testRemoveProtection() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        Node root = createNodeFromString("function foo() { JSCOMPILER_PRESERVE(1 + 1); } foo();");
        CheckSideEffects.process(null, root, checkSideEffects);
        assertFalse(checkSideEffects.problemNodes.isEmpty());
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        stripProtection.process(null, root, null);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
    }

    private Node createNodeFromString(String source) {
        Node root = new IR();
        NodeTraversal.traverse(new AbstractCompiler(), root, new Parser(root, source));
        return root;
    }
}