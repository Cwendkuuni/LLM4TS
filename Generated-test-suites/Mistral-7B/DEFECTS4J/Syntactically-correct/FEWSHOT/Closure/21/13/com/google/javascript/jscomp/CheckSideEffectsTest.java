package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {
    private static AbstractCompiler compiler;
    private static NodeFactory nodeFactory;

    @ org.junit.BeforeClass
    public static void setUpBeforeClass() throws Exception {
        compiler = new TestCompiler();
        nodeFactory = compiler.getFactory();
    }

    @Test
    public void testCheckSideEffects() {
        CheckLevel level = CheckLevel.WARNING;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);
        CompilerInput input = NodeTraversal.buildTestInput(nodeFactory, "test.js");

        Node root = input.getAstRoot(compiler);
        NodeTraversal.traverse(compiler, root, checkSideEffects);

        List<Node> problemNodes = checkSideEffects.problemNodes;
        assertThat(problemNodes).isEmpty();

        compiler.reportAllErrors();
        List<String> errors = compiler.getDiagnostics();
        assertThat(errors).isEmpty();
    }

    @Test
    public void testCheckSideEffects_simpleOp() {
        CheckLevel level = CheckLevel.WARNING;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);

        Node root = nodeFactory.createProgram();
        Node a = nodeFactory.createNumber(1);
        Node b = nodeFactory.createNumber(2);
        Node c = nodeFactory.createAdd(a, b);

        root.addChildToBack(c);

        NodeTraversal.traverse(compiler, root, checkSideEffects);

        List<Node> problemNodes = checkSideEffects.problemNodes;
        assertThat(problemNodes).isEmpty();

        compiler.reportAllErrors();
        List<String> errors = compiler.getDiagnostics();
        assertThat(errors).isEmpty();

        NodeUtil.assertTypeEquals(Token.EXPR_RESULT, c.getType(), "Expected EXPR_RESULT");
    }

    @Test
    public void testCheckSideEffects_missingPlus() {
        CheckLevel level = CheckLevel.WARNING;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);

        Node root = nodeFactory.createProgram();
        Node a = nodeFactory.createString("foo");
        Node b = nodeFactory.createString("bar");

        root.addChildToBack(nodeFactory.createCall(null, "concat", a, b));

        NodeTraversal.traverse(compiler, root, checkSideEffects);

        List<Node> problemNodes = checkSideEffects.problemNodes;
        Node expected = problemNodes.get(0);
        assertThat(expected).isNotNull();
        assertThat(expected.getString()).isEqualTo("Expected a '+' on the previous line");

        compiler.reportAllErrors();
        List<String> errors = compiler.getDiagnostics();
        String expectedError = "Unexpected concatenation in a string literal";
        assertThat(errors).containsExactly(expectedError);
    }

    @Test
    public void testCheckSideEffects_protectSideEffectFreeCode() {
        CheckLevel level = CheckLevel.WARNING;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);

        Node root = nodeFactory.createProgram();
        Node a = nodeFactory.createNumber(1);
        Node b = nodeFactory.createNumber(2);
        Node c = nodeFactory.createAdd(a, b);

        root.addChildToBack(c);

        NodeTraversal.traverse(compiler, root, checkSideEffects);

        List<Node> problemNodes = checkSideEffects.problemNodes;
        assertThat(problemNodes).isEmpty();

        // Check that the protected code was not removed
        Node protectedNode = checkSideEffects.problemNodes.get(0);
        Node expectedProtectedNode = root.getChild(0);
        assertThat(expectedProtectedNode).isEqualTo(protectedNode);

        compiler.reportAllErrors();
        List<String> errors = compiler.getDiagnostics();
        assertThat(errors).isEmpty();

        NodeUtil.assertTypeEquals(Token.EXPR_RESULT, c.getType(), "Expected EXPR_RESULT");
    }

    @Test
    public void testCheckSideEffects_protectSideEffectFreeCode_withErrors() {
        CheckLevel level = CheckLevel.WARNING;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);

        Node root = nodeFactory.createProgram();
        Node a = nodeFactory.createNumber(1);
        Node b = nodeFactory.createString("foo");
        Node c = nodeFactory.createAdd(a, b);

        root.addChildToBack(c);

        NodeTraversal.traverse(compiler, root, checkSideEffects);

        List<Node> problemNodes = checkSideEffects.problemNodes;
        Node expected = problemNodes.get(0);
        assertThat(expected).isNotNull();
        assertThat(expected.getString()).isEqualTo("Is there a missing '+' on the previous line?");

        compiler.reportAllErrors();
        List<String> errors = compiler.getDiagnostics();
        String expectedError = "Unexpected concatenation in a string literal";
        assertThat(errors).containsExactly(expectedError);

        // Check that the protected code was not removed
        Node protectedNode = checkSideEffects.problemNodes.get(0);
        Node expectedProtectedNode = root.getChild(0);
        assertThat(expectedProtectedNode).isEqualTo(protectedNode);
    }

    @Test
    public void testStripProtection() {
        CheckLevel level = CheckLevel.WARNING;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);
        StripProtection stripProtection = new StripProtection(compiler);

        Node root = nodeFactory.createProgram();
        Node a = nodeFactory.createName("JSCOMPILER_PRESERVE");
        Node b = nodeFactory.createCall(a, null);
        Node c = nodeFactory.createNumber(1);

        Node util = new JSDocInfoBuilder(true)
            .build(nodeFactory.createQualifiedName("Util"))
            .setAccess("static");
        JSDocInfo info = new JSDocInfoBuilder()
            .build(nodeFactory.createQualifiedName("JSCOMPILER_PRESERVE"))
            .setAccess("private")
            .setInfo(util)
            .build(b);
        b.setJSDocInfo(info);

        root.addChildToBack(b);
        root.addChildToBack(c);

        NodeTraversal.traverse(compiler, root, checkSideEffects);
        NodeTraversal.traverse(compiler, root, stripProtection);

        List<Node> problemNodes = checkSideEffects.problemNodes;
        Node expected = problemNodes.get(0);
        assertNull(expected);

        compiler.reportAllErrors();
        List<String> errors = compiler.getDiagnostics();
        assertThat(errors).isEmpty();

        Node expectedNode = root.getChild(0);
        Node actualNode = root.getChild(1);
        assertThat(expectedNode).isEqualTo(actualNode);
        assertThat(actualNode.getType()).isEqualTo(Token.EXPR_RESULT);
    }
}