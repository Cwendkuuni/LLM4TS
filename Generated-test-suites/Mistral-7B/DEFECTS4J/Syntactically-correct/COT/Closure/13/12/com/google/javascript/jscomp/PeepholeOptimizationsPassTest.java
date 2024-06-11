package com.google.javascript.jscomp.test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.test.compilers.SimpleCompiler;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeepholeOptimizationsPassTest {
    abstract static class TestPeepholeOptimizationsPass extends PeepholeOptimizationsPass {
        TestPeepholeOptimizationsPass(SimpleCompiler compiler,
                AbstractPeepholeOptimization... optimizations) {
            super(compiler, optimizations);
        }

        @Override
        public void process(Node externs, Node root) {
            // In test cases, just manually call the methods
            processTest(externs, root);
        }
    }

    SimpleCompiler compiler;
    TestPeepholeOptimizationsPass pass;
    Node rootNode;

    @Before
    public void setUp() {
        compiler = new SimpleCompiler();
        pass = new TestPeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new ConstFoldingPeepholeOptimization());
        rootNode = Node.objectLiteral();
    }

    @After
    public void tearDown() {
        compiler = null;
        pass = null;
    }

    @Test
    public void testConstructor() {
        AbstractCompiler compiler = new SimpleCompiler();
        AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);
        Assert.assertNotNull(pass);
    }

    @Test
    public void testProcess() {
        pass.process(Node.objectLiteral(), rootNode);
    }

    @Test(expected = NullPointerException.class)
    public void testProcessWithNullCompiler() {
        AbstractCompiler compiler = null;
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization());
        pass.process(Node.objectLiteral(), rootNode);
    }

    @Test(expected = NullPointerException.class)
    public void testProcessWithNullRoot() {
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization());
        pass.process(null, rootNode);
    }

    @Test(expected = NullPointerException.class)
    public void testProcessWithNullExterns() {
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization());
        pass.process(null, rootNode);
    }

    @Test
    public void testShouldVisitSimpleFunction() {
        Node functionNode = Node.function("func", Node.parameterList(), Node.block(Lists.newArrayList(Node.returnStatement(Node.stringLiteral("result")))));
        Assert.assertTrue(pass.shouldVisit(functionNode));
    }

    @Test
    public void testShouldVisitSimpleScript() {
        Node scriptNode = Node.script("var result = 1;");
        Assert.assertTrue(pass.shouldVisit(scriptNode));
    }

    @Test
    public void testShouldNotVisitSimpleFunctionWhenTraversalDisabled() {
        pass.traversalState.peek().traverseChildScopes = false;
        Node functionNode = Node.function("func", Node.parameterList(), Node.block(Lists.newArrayList(Node.returnStatement(Node.stringLiteral("result")))));
        Assert.assertFalse(pass.shouldVisit(functionNode));
    }

    @Test
    public void testShouldNotVisitSimpleScriptWhenTraversalDisabled() {
        pass.traversalState.peek().traverseChildScopes = false;
        Node scriptNode = Node.script("var result = 1;");
        Assert.assertFalse(pass.shouldVisit(scriptNode));
    }

    @Test
    public void testTraverseNullNode() {
        pass.traverse(null);
    }

    @Test
    public void testVisitSimpleNode() {
        pass.visit(Node.numberLiteral(1));
    }

    @Test(expected = NullPointerException.class)
    public void testVisitNullNode() {
        pass.visit(null);
    }

    @Test
    public void testVisitFunctionWithChildFunction() {
        Node functionNode = Node.function("func", Node.parameterList(), Node.block(Lists.newArrayList(
                Node.variableDeclaration("childFunc", Node.function("childFunc", Node.parameterList(), Node.block(Lists.newArrayList(Node.returnStatement(Node.stringLiteral("child result")))))),
                Node.returnStatement(Node.numberLiteral(2)))));
        pass.processTest(Node.objectLiteral(), functionNode);
        Assert.assertTrue(functionNode.isFunction());
        Assert.assertTrue(functionNode.getFirstChild().isFunction());
    }

    @Test
    public void testVisitScriptWithFunction() {
        Node scriptNode = Node.script("function func() { return 1; }");
        pass.processTest(Node.objectLiteral(), scriptNode);
        Assert.assertTrue(scriptNode.isScript());
        Assert.assertTrue(scriptNode.getFirstChild().isFunction());
    }

    @Test
    public void testVisitFunctionWithVariableDeclaration() {
        Node functionNode = Node.function("func", Node.parameterList(), Node.block(Lists.newArrayList(
                Node.variableDeclaration("x", Node.numberLiteral(1)),
                Node.returnStatement(Node.numberLiteral(2)))));
        pass.processTest(Node.objectLiteral(), functionNode);
        Assert.assertEquals(2, functionNode.getChildrenList().size());
        Assert.assertTrue(functionNode.getChildrenList().get(0).isVariableDeclaration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVisitFunctionWithNonFunctionChild() {
        Node functionNode = Node.function("func", Node.parameterList(), Node.block(Lists.newArrayList(Node.numberLiteral(1))));
        Node childNode = Node.stringLiteral("not a function");
        functionNode.addChild(childNode);
        pass.processTest(Node.objectLiteral(), functionNode);
    }

    @Test
    public void testVisitScriptWithVariableDeclaration() {
        Node scriptNode = Node.script("var x = 1;");
        pass.processTest(Node.objectLiteral(), scriptNode);
        Assert.assertEquals(2, scriptNode.getChildrenList().size());
        Assert.assertTrue(scriptNode.getChildrenList().get(0).isVariableDeclaration());
    }

    @Test
    public void testVisitScriptWithMultipleVariableDeclarations() {
        Node scriptNode = Node.script("var x = 1, y = 2;");
        pass.processTest(Node.objectLiteral(), scriptNode);
        Assert.assertEquals(3, scriptNode.getChildrenList().size());
        Assert.assertTrue(scriptNode.getChildrenList().get(0).isVariableDeclaration());
        Assert.assertTrue(scriptNode.getChildrenList().get(1).isVariableDeclaration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVisitScriptWithNonScriptChild() {
        Node scriptNode = Node.script("1");
        Node childNode = Node.numberLiteral(1);
        scriptNode.addChild(childNode);
        pass.processTest(Node.objectLiteral(), scriptNode);
    }

    private static class NoOpPeepholeOptimization implements AbstractPeepholeOptimization {
        @Override
        public Node optimizeSubtree(Node node) {
            return node;
        }

        @Override
        public void beginTraversal(AbstractCompiler compiler) {
            // no-op
        }

        @Override
        public void endTraversal(AbstractCompiler compiler) {
            // no-op
        }
    }

    private static class ConstFoldingPeepholeOptimization implements AbstractPeepholeOptimization {
        @Override
        public Node optimizeSubtree(Node node) {
            if (node.isNumberLiteral() || node.isStringLiteral()) {
                return node;
            }
            return null;
        }

        @Override
        public void beginTraversal(AbstractCompiler compiler) {
            // no-op
        }

        @Override
        public void endTraversal(AbstractCompiler compiler) {
            // no-op
        }
    }
}