package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodeContext;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;

public class CoreOperationNotEqualTest {

    @org.junit.Before
    public void setUp() {
        // Set up any necessary preconditions or initialize any non-static variables here
    }

    @org.junit.After
    public void tearDown() {
        // Release any allocated resources or perform any clean-up actions here
    }

    @org.junit.Test
    public void testComputeValue_whenArgumentsAreBothNull_thenResultIsFalse() {
        Expression arg1 = new CoreConstantExpression(null);
        Expression arg2 = new CoreConstantExpression(null);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext(new NodeContext(new SimpleNode("root")));
        assertFalse(operation.computeValue(context).toString().equals("true"));
    }

    @org.junit.Test
    public void testComputeValue_whenArgumentsAreEqual_thenResultIsFalse() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext(new NodeContext(new SimpleNode("root")));
        assertFalse(operation.computeValue(context).toString().equals("true"));
    }

    @org.junit.Test
    public void testComputeValue_whenArgumentsAreDifferent_thenResultIsTrue() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(2);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext(new NodeContext(new SimpleNode("root")));
        assertTrue(operation.computeValue(context).toString().equals("true"));
    }

    @org.junit.Test
    public void testComputeValue_whenArgumentsAreDifferentTypes_thenResultIsBoolean() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression("two");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext(new NodeContext(new SimpleNode("root")));
        assertTrue(operation.computeValue(context) instanceof Boolean);
    }

    @org.junit.Test
    public void testGetSymbol_shouldReturnNotEqualSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        assertEquals("!=", operation.getSymbol());
    }
}