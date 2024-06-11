package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * CoreOperationGreaterThanTest class contains JUnit test methods for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    /**
     * Test method for CoreOperationGreaterThan constructor, with valid arguments.
     */
    @Test
    public void testCoreOperationGreaterThanConstructor() {
        Expression arg1 = new Path("1.0");
        Expression arg2 = new Path("2.0");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertNotNull(coreOperationGreaterThan);
        assertEquals(2, coreOperationGreaterThan.getOperands().length);
    }

    /**
     * Test method for CoreOperationGreaterThan.computeValue method, when the left operand value is greater than the right operand value.
     */
    @Test
    public void testCoreOperationGreaterThanComputeValueTrue() {
        InfoSet infoSet = new InfoSet();
        Expression arg1 = new Path("3.5");
        Expression arg2 = new Path("2.5");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(infoSet);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);
    }

    /**
     * Test method for CoreOperationGreaterThan.computeValue method, when the left operand value is less than or equal to the right operand value.
     */
    @Test
    public void testCoreOperationGreaterThanComputeValueFalse() {
        InfoSet infoSet = new InfoSet();
        Expression arg1 = new Path("2.5");
        Expression arg2 = new Path("3.5");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(infoSet);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);
    }

    /**
     * Test method for CoreOperationGreaterThan.getSymbol method.
     */
    @Test
    public void testCoreOperationGreaterThanGetSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Path("1.0"), new Path("2.0"));
        String symbol = coreOperationGreaterThan.getSymbol();
        assertEquals(">", symbol);
    }

    /**
     * Test method for CoreOperationGreaterThan.getClass method.
     */
    @Test
    public void testCoreOperationGreaterThanClass() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Path("1.0"), new Path("2.0"));
        assertEquals(CoreOperationGreaterThan.class, coreOperationGreaterThan.getClass());
        assertEquals(CoreOperationRelationalExpression.class, coreOperationGreaterThan.getClass().getSuperclass());
    }
}