package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestingUtility;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {
    @SuppressWarnings("unchecked")
    private static final List<Node> EMPTY_NODE_LIST = (List<Node>) Lists.newArrayList();

    private static Compiler getCompiler(boolean protectSideEffectFreeCode) {
        CompilerOptions options = new CompilerOptions();
        options.setWarFile("CheckSideEffectsTest-compiled.js");
        Compiler compiler = new Compiler(options);
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, protectSideEffectFreeCode);
        compiler.addPass(checkSideEffects);
        compiler.addPass(new CheckSideEffects.StripProtection(compiler));
        return compiler;
    }

    @Test
    public void testCheckSideEffects() {
        Compiler compiler = getCompiler(false);
        Node root = ParserTestingUtility.parseJavaScript("var x = 1;\n" +
                "x = foo();\n" +
                "foo();;\n", compiler);
        NodeTraversal.traverse(compiler, root, null);
        compiler.reportCodeChange();
        Node compiledRoot = compiler.getOutput();
        Node xNode = NodeUtil.findChildByType(compiledRoot, Token.VAR);
        Node xAssignmentNode = NodeUtil.findChildByType(xNode, Token.ASSIGN);
        Node fooCallNode = NodeUtil.findChildByType(xAssignmentNode, Token.CALL);

        // Check that the warnings were generated.
        List<Node> warnings = (List<Node>) compiler.getDiagnostics().getAllDiagnostics();
        assertThat(warnings).hasSize(3);
        for (Node warning : warnings) {
            String message = warning.getFirstChild().getString();
            if (message.contains("Suspicious code. x = foo")) {
                assertThat(warning.getLineNumber()).isEqualTo(1);
            } else if (message.contains("Suspicious code. foo();;")) {
                assertThat(warning.getLineNumber()).isEqualTo(2);
            } else if (message.contains("Suspicious code. foo();")) {
                assertThat(warning.getLineNumber()).isEqualTo(3);
            }
        }

        // Check that the code was changed as expected.
        Node x = xNode.getFirstChild();
        Node foo = NodeFactory.createName("foo");
        Node fooCall = NodeFactory.createCall(foo, EMPTY_NODE_LIST);
        Node xAssignment = NodeFactory.createAssign(x, fooCall);
        Node expectedRoot = NodeFactory.createBlock(xAssignment, null);
        NodeUtil.assertEqualsSubtree(expectedRoot, compiledRoot);
    }

    @Test
    public void testProtectSideEffectFreeCode() {
        Compiler compiler = getCompiler(true);
        Node root = ParserTestingUtility.parseJavaScript("var x = 1 + 1;\n" +
                "x = 2 + 2;\n", compiler);
        NodeTraversal.traverse(compiler, root, null);
        compiler.reportCodeChange();
        Node compiledRoot = compiler.getOutput();

        // Check that no warnings were generated.
        List<Node> warnings = (List<Node>) compiler.getDiagnostics().getAllDiagnostics();
        assertTrue(warnings.isEmpty());

        // Check that the code was not changed.
        Node x = NodeUtil.findChildByType(compiledRoot, Token.VAR);
        Node xAssignment = NodeUtil.findChildByType(x, Token.ASSIGN);
        Node left = NodeUtil.findChildByType(xAssignment, Token.PLUS);
        Node right1 = left.getLastChild();
        Node right2 = NodeUtil.findChildByType(right1, Token.NUM);
        int xValueBefore = Integer.parseInt(right1.getString());
        int xValueAfter = Integer.parseInt(NodeUtil.getChildValue(xAssignment, 1).getString());
        assertThat(xValueBefore).isEqualTo(1);
        assertThat(xValueAfter).isEqualTo(2);
    }
}