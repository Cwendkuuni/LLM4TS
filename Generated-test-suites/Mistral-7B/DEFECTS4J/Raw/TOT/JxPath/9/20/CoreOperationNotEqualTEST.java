package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link CoreOperationNotEqual}.
 */
public class CoreOperationNotEqualTest {

    /**
     * Test method for {@link CoreOperationNotEqual#CoreOperationNotEqual(Expression, Expression)}.
     */
    @Test
    public void testCoreOperationNotEqualConstruction() {
        Expression arg1 = new SimpleNode(new NodeInfo(1));
        Expression arg2 = new SimpleNode(new NodeInfo(2));
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(operation);
        assertEquals("!=", operation.getSymbol());
        assertEquals(2, operation.getArgs().length);
        assertEquals(arg1, operation.getArgs()[0]);
        assertEquals(arg2, operation.getArgs()[1]);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvaluationContext)}.
     */
    @Test
    public void testComputeValue() {
        EvaluationContext context = new EvaluationContext();
        SimpleNode arg1 = new SimpleNode(new NodeInfo(1));
        SimpleNode arg2 = new SimpleNode(new NodeInfo("2"));
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(operation.computeValue(context).booleanValue());

        arg1 = new SimpleNode(new NodeInfo(1));
        arg2 = new SimpleNode(new NodeInfo(1));
        operation = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(operation.computeValue(context).booleanValue());

        arg1 = new SimpleNode(new NodeInfo(null));
        arg2 = new SimpleNode(new NodeInfo(1));
        operation = new CoreOperationNotEqual(arg1, arg2);
        assertNull(operation.computeValue(context));

        arg1 = new SimpleNode(new NodeInfo(1));
        arg2 = new SimpleNode(new NodeInfo(null));
        operation = new CoreOperationNotEqual(arg1, arg2);
        assertNull(operation.computeValue(context));
    }
}