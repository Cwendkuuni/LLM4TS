package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {
    private AbstractCompiler compiler;
    private CheckSideEffects checkSideEffects;

    @Test
    public void testCheckSideEffects() {
        compiler = new CompilerOptions().setPrintStackFrames(false).toCompiler();
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        Node jsCode = createJsCode("function test() {\n" +
                                   "  var s = \"this string is \";\n" +
                                   "  s += ' continued on the next line but you forgot the +\";\n" +
                                   "  x = foo(); \n" +
                                   "  foo();;\n" +
                                   "  var a = [1, 2, 3];\n" +
                                   "}");

        checkSideEffects.process(null, jsCode);

        List<Node> problemNodes = checkSideEffects.problemNodes;
        int numProblemNodes = problemNodes.size();
        assertSame("Expected 1 problem node.", 1, numProblemNodes);

        Node problemNode = problemNodes.get(0);
        int problemNodeType = problemNode.getType();
        assertSame("Expected a call node.", Token.CALL, problemNodeType);

        Node problemNodeTarget = problemNode.getFirstChild();
        String problemNodeTargetName = problemNodeTarget.getString();
        assertSame("Expected 'x ='.", "x =", problemNodeTargetName);

        Node problemNodeRightChild = problemNode.getLastChild();
        assertNull("Expected null right child.", problemNodeRightChild);

        Node problemNodeParent = problemNode.getParent();
        int problemNodeParentType = problemNodeParent.getType();
        assertSame("Expected a statement node.", Token.BLOCK, problemNodeParentType);

        checkSideEffects.protectSideEffects();
        assertFalse("Expected no problem nodes after protection.", checkSideEffects.problemNodes.isEmpty());

        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.OFF, false);
        checkSideEffects.process(null, jsCode);
        assertFalse("Expected no problem nodes when level is OFF.", checkSideEffects.problemNodes.isEmpty());

        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        jsCode = createJsCode("function test() {\n" +
                              "  var x = document.getElementById('foo').offsetWidth;\n" +
                              "}");
        checkSideEffects.process(null, jsCode);
        assertFalse("Expected no problem nodes with side-effect free code protection.", checkSideEffects.problemNodes.isEmpty());
    }

    private Node createJsCode(String code) {
        CompilerInput input = new CompilerInput("test.js", code);
        Node externs = input.getAstRoot(compiler);
        Node root = compiler.parseTree(input, false, null);
        return root;
    }
}

class NodeUtilTest extends CheckSideEffectsTest {
    @Test
    public void testIsExpressionResultUsed() {
        Node node = createJsCode("function test() {\n" +
                                 "  return (1 + 2);\n" +
                                 "}").getLastChild();
        assertTrue("Expected node to be an expression result.", NodeUtil.isExpressionResult(node));
        assertTrue("Expected node to be used.", NodeUtil.isExpressionResultUsed(node));

        node = createJsCode("function test() {\n" +
                             "  return;\n" +
                             "}").getLastChild();
        assertTrue("Expected node to be an expression result.", NodeUtil.isExpressionResult(node));
        assertFalse("Expected node to not be used.", NodeUtil.isExpressionResultUsed(node));

        node = createJsCode("function test() {\n" +
                             "  return 42;\n" +
                             "}").getLastChild();
        assertTrue("Expected node to be an expression result.", NodeUtil.isExpressionResult(node));
        assertTrue("Expected node to be used.", NodeUtil.isExpressionResultUsed(node));

        node = createJsCode("function test() {\n" +
                             "  return;\n" +
                             "  return 42;\n" +
                             "}").getLastChild();
        assertTrue("Expected node to be an expression result.", NodeUtil.isExpressionResult(node));
        assertTrue("Expected node to be used.", NodeUtil.isExpressionResultUsed(node));

        node = createJsCode("function test() {\n" +
                             "  return;\n" +
                             "  return;\n" +
                             "  return 42;\n" +
                             "}").getLastChild();
        assertTrue("Expected node to be an expression result.", NodeUtil.isExpressionResult(node));
        assertTrue("Expected node to be used.", NodeUtil.isExpressionResultUsed(node));

        node = createJsCode("function test() {\n" +
                             "  return;\n" +
                             "  return;\n" +
                             "  return;\n" +
                             "  return 42;\n" +
                             "}").getLastChild();
        assertTrue("Expected node to be an expression result.", NodeUtil.isExpressionResult(node));
        assertTrue("Expected node to be used.", NodeUtil.isExpressionResultUsed(node));

        node = createJsCode("function test() {\n" +
                             "  return 42;\n" +
                             "  return;\n" +
                             "}").getLastChild();
        assertTrue("Expected node to be an expression result.", NodeUtil.isExpressionResult(node));
        assertFalse("Expected node to not be used.", NodeUtil.isExpressionResultUsed(node));
    }

    @Test
    public void testMayHaveSideEffects() {
        Node node = createJsCode("function test() {\n" +
                                 "  return 1 + 2;\n" +
                                 "}").getLastChild();
        assertFalse("Expected node to not have side effects.", NodeUtil.mayHaveSideEffects(node, compiler));

        node = createJsCode("function test() {\n" +
                             "  document.write('Hello World');\n" +
                             "}").getLastChild();
        assertTrue("Expected node to have side effects.", NodeUtil.mayHaveSideEffects(node, compiler));

        node = createJsCode("function test() {\n" +
                             "  document.write('Hello World');\n" +
                             "  return 1 + 2;\n" +
                             "}").getLastChild();
        assertTrue("Expected node to have side effects.", NodeUtil.mayHaveSideEffects(node, compiler));

        node = createJsCode("function test() {\n" +
                             "  document.write('Hello World');\n" +
                             "  return 1 + 2;\n" +
                             "  return;\n" +
                             "}").getLastChild();
        assertTrue("Expected node to have side effects.", NodeUtil.mayHaveSideEffects(node, compiler));
    }
}

class CheckSideEffectsProtectionTest extends CheckSideEffectsTest {
    @Test
    public void testProtectSideEffects() {
        CompilerInput input = new CompilerInput("test.js",
                                                "function test() {\n" +
                                                "  var s = \"this string is \";\n" +
                                                "  s += ' continued on the next line but you forgot the +\";\n" +
                                                "  x = foo(); \n" +
                                                "}");
        AbstractCompiler compiler = new CompilerOptions().setPrintStackFrames(false).toCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
        checkSideEffects.process(null, input.getAstRoot(compiler));
        int numProblemNodes = checkSideEffects.problemNodes.size();
        assertSame("Expected 1 problem node.", 1, numProblemNodes);

        checkSideEffects.protectSideEffects();
        numProblemNodes = checkSideEffects.problemNodes.size();
        assertSame("Expected 0 problem nodes after protection.", 0, numProblemNodes);

        input.setCode(compiler.getSourceCode());
        compiler.dispose();
        compiler = new CompilerOptions().setPrintStackFrames(false).toCompiler();
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
        checkSideEffects.process(null, input.getAstRoot(compiler));
        numProblemNodes = checkSideEffects.problemNodes.size();
        assertSame("Expected 1 problem node.", 1, numProblemNodes);

        checkSideEffects.protectSideEffects();
        numProblemNodes = checkSideEffects.problemNodes.size();
        assertSame("Expected 0 problem nodes after protection.", 0, numProblemNodes);

        input.setCode(compiler.getSourceCode());
        compiler.dispose();
        compiler = new CompilerOptions().setPrintStackFrames(false).toCompiler();
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checkSideEffects.process(null, input.getAstRoot(compiler));
        numProblemNodes = checkSideEffects.problemNodes.size();
        assertSame("Expected 1 problem node.", 1, numProblemNodes);

        checkSideEffects.protectSideEffects();
        numProblemNodes = checkSideEffects.problemNodes.size();
        assertSame("Expected 0 problem nodes after protection.", 0, numProblemNodes);
    }
}

class StripProtectionTest extends CheckSideEffectsTest {
    @Test
    public void testStripProtection() {
        CompilerInput input = new CompilerInput("test.js",
                                                "function test() {\n" +
                                                "  JSCOMPILER_PROTECTED_SIDE_EFFECT_FREE_NODE;\n" +
                                                "}");
        AbstractCompiler compiler = new CompilerOptions().setPrintStackFrames(false).toCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
        checkSideEffects.process(null, input.getAstRoot(compiler));
        int numProblemNodes = checkSideEffects.problemNodes.size();
        assertSame("Expected 1 problem node.", 1, numProblemNodes);

        StripProtection stripProtection = new StripProtection(compiler);
        stripProtection.process(null, input.getAstRoot(compiler));
        numProblemNodes = checkSideEffects.problemNodes.size();
        assertSame("Expected 0 problem nodes after protection.", 0, numProblemNodes);

        input.setCode(compiler.getSourceCode());
        compiler.dispose();
        compiler = new CompilerOptions().setPrintStackFrames(false).toCompiler();
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
        checkSideEffects.process(null, input.getAstRoot(compiler));
        numProblemNodes = checkSideEffects.problemNodes.size();
        assertSame("Expected 1 problem node.", 1, numProblemNodes);

        stripProtection = new StripProtection(compiler);
        stripProtection.process(null, input.getAstRoot(compiler));
        numProblemNodes = checkSideEffects.problemNodes.size();
        assertSame("Expected 0 problem nodes after protection.", 0, numProblemNodes);
    }
}