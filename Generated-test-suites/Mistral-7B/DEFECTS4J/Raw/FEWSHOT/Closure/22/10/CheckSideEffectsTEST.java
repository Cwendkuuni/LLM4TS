package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TreeShakeUtils;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Nodeops;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Test class for {@link CheckSideEffects}.
 */
public class CheckSideEffectsTest {
    @Test
    public void testCheckSideEffects() {
        // Given
        String sourceCode = "function foo() {}\n" +
                "var x = 1;\n" +
                "var y = 2;\n" +
                "var z = 3;\n" +
                "var s = \"this string is continued on the next line but you forgot the +\";\n" +
                "x = foo();\n" +
                "foo();;\n" +
                "var a = function() {};\n" +
                "var b = function() {};\n" +
                "var c = function() {};\n" +
                "var d = function() {};\n" +
                "var e = function() {};\n" +
                "var f = function() {};\n" +
                "var g = function() {};\n" +
                "var h = function() {};\n" +
                "var i = function() {};\n" +
                "var j = function() {};\n" +
                "var k = function() {};\n" +
                "var l = function() {};\n" +
                "function bar() {\n" +
                "  x = 4;\n" +
                "  y = 5;\n" +
                "  z = 6;\n" +
                "}\n" +
                "bar();\n";

        List<Node> ast = AbstractCompiler.createCompiler(sourceCode).getAst();
        CheckSideEffects checkSideEffects = new CheckSideEffects(new NoopCompiler(), CheckLevel.WARNING, false);

        // When
        checkSideEffects.process(null, ast.get(0));

        // Then
        checkSideEffects.problemNodes.forEach(node -> {
            assertFalse("Unexpected side-effect found", NodeUtil.isExpressionResultUsed(node));
            assertTrue("Unexpectedly simple operator found", NodeUtil.isSimpleOperatorType(node.getType()));
            assertTrue("Unexpected side-effect not protected", TreeShakeUtils.hasSideEffects(node, checkSideEffects.compiler));
        });

        checkSideEffects.problemNodes.clear();

        // Given
        sourceCode = "function foo() { return 42; }\n" +
                "var x = foo();\n" +
                "x += 1;\n";

        ast = AbstractCompiler.createCompiler(sourceCode).getAst();
        checkSideEffects = new CheckSideEffects(new NoopCompiler(), CheckLevel.WARNING, false);

        // When
        checkSideEffects.process(null, ast.get(0));

        // Then
        assertTrue("Unexpected side-effect not detected", TreeShakeUtils.hasSideEffects(ast.get(3), checkSideEffects.compiler));

        // Given
        sourceCode = "function foo() { return 42; }\n" +
                "var x = foo();\n" +
                "delete x;\n";

        ast = AbstractCompiler.createCompiler(sourceCode).getAst();
        checkSideEffects = new CheckSideEffects(new NoopCompiler(), CheckLevel.WARNING, false);

        // When
        checkSideEffects.process(null, ast.get(0));

        // Then
        assertNull("Unexpected problem node found", checkSideEffects.problemNodes.get(0));

        // Given
        sourceCode = "function foo() { return 42; }\n" +
                "var x = foo();\n" +
                "x *= 2;\n";

        ast = AbstractCompiler.createCompiler(sourceCode).getAst();
        checkSideEffects = new CheckSideEffects(new NoopCompiler(), CheckLevel.WARNING, false);

        // When
        checkSideEffects.process(null, ast.get(0));

        // Then
        assertNull("Unexpected problem node found", checkSideEffects.problemNodes.get(0));
    }

    @Test
    public void testProtectSideEffects() {
        // Given
        String sourceCode = "function foo() {}\n" +
                "var x = 1;\n" +
                "x = foo();\n" +
                "foo();;\n";

        List<Node> ast = AbstractCompiler.createCompiler(sourceCode).getAst();
        CheckSideEffects checkSideEffects = new CheckSideEffects(new NoopCompiler(), CheckLevel.WARNING, true);

        // When
        checkSideEffects.process(null, ast.get(0));

        // Then
        assertEquals("Expected 1 problem node, found " + checkSideEffects.problemNodes.size(), 1, checkSideEffects.problemNodes.size());
        Node problemNode = checkSideEffects.problemNodes.get(0);
        Node protectedNode = IR.name(CheckSideEffects.PROTECTOR_FN).srcref(problemNode);
        Node callNode = IR.call(protectedNode).srcref(problemNode);

        // Then
        assertFalse("Unexpected side-effect not protected", TreeShakeUtils.hasSideEffects(problemNode, checkSideEffects.compiler));
        assertTrue("Unexpectedly no call node found", callNode != null);
    }
}

class NoopCompiler extends Compiler {
    @Override
    public void processCode(Node root) {}
}