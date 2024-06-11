package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        EvalContext context = new EvalContext();
        
        // Case 1: l > r
        operation.args[0] = new ExpressionStub(5);
        operation.args[1] = new ExpressionStub(3);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Case 2: l = r
        operation.args[0] = new ExpressionStub(5);
        operation.args[1] = new ExpressionStub(5);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Case 3: l < r
        operation.args[0] = new ExpressionStub(3);
        operation.args[1] = new ExpressionStub(5);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        assertEquals(">", operation.getSymbol());
    }
    
    private static class ExpressionStub extends Expression {
        private Object value;
        
        public ExpressionStub(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}