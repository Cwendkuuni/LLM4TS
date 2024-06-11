
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual operation;

    @Before
    public void setUp() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.addVariable("arg1", 5);
        context.addVariable("arg2", 10);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals("<=", symbol);
    }

    private class MockExpression extends Expression {
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}
