package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(), new ExpressionStub());
        
        EvalContext context = new EvalContext(false);
        context.getContext().setValue("foo", 5);
        context.getContext().setValue("bar", 10);
        
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(), new ExpressionStub());
        
        assertEquals(">=", expression.getSymbol());
    }
    
    // Stub class for Expression
    private class ExpressionStub extends Expression {
        public Object computeValue(EvalContext evalContext) {
            return null;
        }
        
        public String toString() {
            return null;
        }
    }
}