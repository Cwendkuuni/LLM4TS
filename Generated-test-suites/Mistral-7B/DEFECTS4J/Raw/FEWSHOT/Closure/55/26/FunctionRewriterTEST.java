package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.FunctionRewriter;
import org.apache.commons.cli.Node;
import org.apache.commons.cli.NodeUtil;
import org.apache.commons.cli.compiler.AbstractCompiler;
import org.apache.commons.cli.compiler.CompilerPass;
import org.apache.commons.cli.compiler.NodeTraversal;
import org.apache.commons.cli.compiler.Callback;
import org.apache.commons.cli.compiler.InlineCostEstimator;
import org.apache.commons.cli.compiler.NodeUtil;
import org.junit.Test;

import java.util.List;

public class FunctionRewriterTest {

    @Test
    public void testEmptyFunctionReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        AbstractCompiler compiler = new TestAbstractCompiler();
        Node root = compiler.parseTestCode("EmptyFunctionReducerTest");
        functionRewriter.process(null, root);

        Node emptyFunctionNode = NodeUtil.getFunctionByName(root, "EmptyFunctionReducerTest_emptyFunction");
        assertEquals(1, emptyFunctionNode.getChildCount());
        Node helperNode = emptyFunctionNode.getChild(0);
        assertEquals(1, helperNode.getChildCount());
        assertEquals("emptyFn", helperNode.getString());
    }

    @Test
    public void testIdentityReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        AbstractCompiler compiler = new TestAbstractCompiler();
        Node root = compiler.parseTestCode("IdentityReducerTest");
        functionRewriter.process(null, root);

        Node identityFunctionNode = NodeUtil.getFunctionByName(root, "IdentityReducerTest_identityFunction");
        assertEquals(1, identityFunctionNode.getChildCount());
        Node helperNode = identityFunctionNode.getChild(0);
        assertEquals(1, helperNode.getChildCount());
        assertEquals("identityFn", helperNode.getString());

        Node identityFunctionCallNode = NodeUtil.getFunctionCallByName(root, "identityFn");
        assertEquals(1, identityFunctionCallNode.getChildCount());
        assertEquals("a_", identityFunctionCallNode.getChild(0).getString());

        Node identityFunctionValueNode = identityFunctionCallNode.getChild(0);
        assertEquals("a", identityFunctionValueNode.getString());
    }

    @Test
    public void testReturnConstantReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        AbstractCompiler compiler = new TestAbstractCompiler();
        Node root = compiler.parseTestCode("ReturnConstantReducerTest");
        functionRewriter.process(null, root);

        Node returnConstantFunctionNode = NodeUtil.getFunctionByName(root, "ReturnConstantReducerTest_returnConstantFunction");
        assertEquals(1, returnConstantFunctionNode.getChildCount());
        Node helperNode = returnConstantFunctionNode.getChild(0);
        assertEquals(1, helperNode.getChildCount());
        assertEquals("returnArg", helperNode.getString());

        Node returnConstantFunctionCallNode = NodeUtil.getFunctionCallByName(root, "returnArg");
        assertEquals(0, returnConstantFunctionCallNode.getChildCount());
        assertEquals(10, InlineCostEstimator.getCost(returnConstantFunctionCallNode));
    }

    @Test
    public void testGetterReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        AbstractCompiler compiler = new TestAbstractCompiler();
        Node root = compiler.parseTestCode("GetterReducerTest");
        functionRewriter.process(null, root);

        Node getterFunctionNode = NodeUtil.getFunctionByName(root, "GetterReducerTest_getterFunction");
        assertEquals(1, getterFunctionNode.getChildCount());
        Node helperNode = getterFunctionNode.getChild(0);
        assertEquals(1, helperNode.getChildCount());
        assertEquals("getter", helperNode.getString());

        Node getterFunctionCallNode = NodeUtil.getFunctionCallByName(root, "getter");
        assertEquals(1, getterFunctionCallNode.getChildCount());
        assertEquals("b_", getterFunctionCallNode.getChild(0).getString());

        Node getterFunctionValueNode = getterFunctionCallNode.getChild(0);
        assertEquals("b", getterFunctionValueNode.getString());
    }

    @Test
    public void testSetterReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        AbstractCompiler compiler = new TestAbstractCompiler();
        Node root = compiler.parseTestCode("SetterReducerTest");
        functionRewriter.process(null, root);

        Node setterFunctionNode = NodeUtil.getFunctionByName(root, "SetterReducerTest_setterFunction");
        assertEquals(1, setterFunctionNode.getChildCount());
        Node helperNode = setterFunctionNode.getChild(0);
        assertEquals(1, helperNode.getChildCount());
        assertEquals("setter", helperNode.getString());

        Node setterFunctionCallNode = NodeUtil.getFunctionCallByName(root, "setter");
        assertEquals(1, setterFunctionCallNode.getChildCount());
        assertEquals("b_", setterFunctionCallNode.getChild(0).getString());
        assertEquals(1, setterFunctionCallNode.getChild(1).getChildCount());
        assertEquals(11, setterFunctionCallNode.getChild(1).getChild(0).getChildCount());
        assertEquals(10, InlineCostEstimator.getCost(setterFunctionCallNode));

        Node setterFunctionValueNode = setterFunctionCallNode.getChild(1).getChild(0);
        assertEquals(11, setterFunctionValueNode.getChildCount());
        assertEquals("this", setterFunctionValueNode.getChild(0).getString());
        assertEquals("b_", setterFunctionValueNode.getChild(1).getString());
        assertEquals(10, setterFunctionValueNode.getChild(2).getChild(0).getChildCount());
        assertEquals(10, InlineCostEstimator.getCost(setterFunctionValueNode.getChild(2).getChild(0)));
    }

    @Test
    public void testNoReduction() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        AbstractCompiler compiler = new TestAbstractCompiler();
        Node root = compiler.parseTestCode("NoReductionTest");
        functionRewriter.process(null, root);

        Node noReductionFunctionNode = NodeUtil.getFunctionByName(root, "NoReductionTest_noReductionFunction");
        assertNull(noReductionFunctionNode);
    }

    private static class TestAbstractCompiler extends AbstractCompiler {
        @Override
        public Node parseSyntheticCode(String name, String code) {
            return null;
        }
    }
}

class InlineCostEstimator {
    public static int getCost(Node node) {
        return 1;
    }
}