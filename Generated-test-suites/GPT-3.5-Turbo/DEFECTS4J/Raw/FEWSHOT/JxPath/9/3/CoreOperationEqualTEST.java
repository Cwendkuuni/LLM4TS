package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationEqualTest {
    @Test
    public void testConstructor() {
        Expression arg1 = new MyExpression();
        Expression arg2 = new MyExpression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertSame(arg1, operation.getArg1());
        assertSame(arg2, operation.getArg2());
    }
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new MyExpression();
        Expression arg2 = new MyExpression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(null);
        assertTrue((Boolean) operation.computeValue(context));
        
        arg1 = new MyExpression();
        arg2 = new MyExpression();
        operation = new CoreOperationEqual(arg1, arg2);
        context = new EvalContext(null);
        assertFalse((Boolean) operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new MyExpression();
        Expression arg2 = new MyExpression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertEquals("=", operation.getSymbol());
    }
    
    private class MyExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}