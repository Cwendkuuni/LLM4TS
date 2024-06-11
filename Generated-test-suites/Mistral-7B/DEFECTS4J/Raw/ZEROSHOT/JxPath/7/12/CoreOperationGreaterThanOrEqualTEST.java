package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Test with valid inputs
        Expression arg1 = new NumberNode(new Double(5.5));
        Expression arg2 = new NumberNode(new Double(4.5));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())).booleanValue());

        arg1 = new NumberNode(new Double(10.5));
        arg2 = new NumberNode(new Double(10.5));
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())).booleanValue());

        arg1 = new NumberNode(new Double(10.5));
        arg2 = new NumberNode(new Double(5.5));
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())).booleanValue());

        // Test with invalid inputs
        arg1 = new SimpleNode("invalidNode");
        arg2 = new NumberNode(new Double(5.5));
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())).booleanValue());

        arg1 = new NumberNode(new Double(5.5));
        arg2 = new SimpleNode("invalidNode");
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())).booleanValue());
    }
}