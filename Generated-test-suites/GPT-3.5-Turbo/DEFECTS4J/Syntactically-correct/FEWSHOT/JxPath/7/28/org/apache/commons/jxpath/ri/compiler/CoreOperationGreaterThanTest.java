package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNode(new Object());
        
        // Test case 1
        context.getContextNode().setProperty("property1", 10);
        context.getContextNode().setProperty("property2", 5);
        operation.args[0] = new ExpressionMock(10);
        operation.args[1] = new ExpressionMock(5);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test case 2
        context.getContextNode().setProperty("property1", 5);
        context.getContextNode().setProperty("property2", 10);
        operation.args[0] = new ExpressionMock(5);
        operation.args[1] = new ExpressionMock(10);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        assertEquals(">", operation.getSymbol());
    }
    
    private static class ExpressionMock extends Expression {
        private Object value;
        
        public ExpressionMock(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}