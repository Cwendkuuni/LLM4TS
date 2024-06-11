package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.NodeUtil;

import java.util.List;

public class CheckSideEffectsTest {
    private static final String PROTECTOR_FN = "JSCOMPILER_PRESERVE";

    @Test
    public void testCheckSideEffects() {
        CompilerOptions options = new CompilerOptions();
        options.setCheckSideEffects(true);
        options.setDiagnostics(new Diagnostics());

        AbstractCompiler compiler = new Compiler(options);
        Checker checker = new Checker(compiler, null);

        String sourceCode = "function foo() { return 1; } " +
                "var x = 0; " +
                "var s = \"this string is \" + \"continued on the next line but you forgot the +\";" +
                "x = foo(); " +
                "foo();;" +
                "var y = { get bar() { return 1; } }; " +
                "var z = y.bar; " +
                "var a = [1, 2]; " +
                "var b = a.map(function(x) { return x + 1; }); " +
                "for (var i = 0; i < b.length; i++) {" +
                " var c = b[i]; " +
                "}";

        Node root = checker.parse(sourceCode, "<source>", null);
        NodeTraversal.traverse(compiler, root, new CheckSideEffects(compiler, WARNING, false));

        List<Node> problemNodes = ((CheckSideEffects) root.getUserData(CheckSideEffects.class)).problemNodes;
        assertEquals(6, problemNodes.size());

        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(0)), 3, 13);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(1)), 4, 1);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(2)), 5, 1);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(3)), 6, 12);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(4)), 6, 23);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(5)), 7, 3);
    }

    @Test
    public void testProtectSideEffects() {
        CompilerOptions options = new CompilerOptions();
        options.setCheckSideEffects(true);
        options.setProtectSideEffectFreeCode(true);
        options.setDiagnostics(new Diagnostics());

        AbstractCompiler compiler = new Compiler(options);
        Checker checker = new Checker(compiler, null);

        String sourceCode = "function foo() { return 1; } " +
                "var x = 0; " +
                "var s = \"this string is \" + \"continued on the next line but you forgot the +\";" +
                "x = foo(); " +
                "foo();;" +
                "var y = { get bar() { return 1; } }; " +
                "var z = y.bar; ";

        Node root = checker.parse(sourceCode, "<source>", null);
        NodeTraversal.traverse(compiler, root, new CheckSideEffects(compiler, WARNING, true));

        List<Node> problemNodes = ((CheckSideEffects) root.getUserData(CheckSideEffects.class)).problemNodes;
        assertEquals(5, problemNodes.size());

        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(0)), 3, 13);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(1)), 4, 1);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(2)), 5, 1);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(3)), 6, 12);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(4)), 6, 23);

        // Check that side-effect free code has been protected
        for (Node node : problemNodes) {
            Node protectorFnNode = null;
            for (Node child = node.getFirstChild(); child != null; child = child.getNextSib()) {
                if (child.isName() && child.getString().equals(PROTECTOR_FN)) {
                    protectorFnNode = child;
                    break;
                }
            }
            assertNotNull(protectorFnNode);
        }
    }

    @Test
    public void testRemoveProtection() {
        CompilerOptions options = new CompilerOptions();
        options.setCheckSideEffects(true);
        options.setProtectSideEffectFreeCode(true);
        options.setDiagnostics(new Diagnostics());

        AbstractCompiler compiler = new Compiler(options);
        Checker checker = new Checker(compiler, null);

        String sourceCode = "function foo() { return 1; } " +
                "var x = 0; " +
                "var s = \"this string is \" + \"continued on the next line but you forgot the +\";" +
                "x = foo(); " +
                "foo();;" +
                "var y = { get bar() { return 1; } }; " +
                "var z = y.bar; ";

        Node root = checker.parse(sourceCode, "<source>", null);
        NodeTraversal.traverse(compiler, root, new CheckSideEffects(compiler, WARNING, true));

        List<Node> problemNodes = ((CheckSideEffects) root.getUserData(CheckSideEffects.class)).problemNodes;
        assertEquals(5, problemNodes.size());

        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
        NodeTraversal.traverse(compiler, root, new StripProtection(compiler));

        problemNodes = ((CheckSideEffects) root.getUserData(CheckSideEffects.class)).problemNodes;
        assertEquals(3, problemNodes.size());

        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(0)), 3, 13);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(1)), 4, 1);
        assertEquals(NodeUtil.getLineAndCharacterNumber(root, problemNodes.get(2)), 6, 12);

        // Check that side-effect free code is no longer protected
        for (Node node : problemNodes) {
            Node protectorFnNode = null;
            for (Node child = node.getFirstChild(); child != null; child = child.getNextSib()) {
                if (child.isName() && child.getString().equals(PROTECTOR_FN)) {
                    protectorFnNode = child;
                    break;
                }
            }
            assertNull(protectorFnNode);
        }
    }
}