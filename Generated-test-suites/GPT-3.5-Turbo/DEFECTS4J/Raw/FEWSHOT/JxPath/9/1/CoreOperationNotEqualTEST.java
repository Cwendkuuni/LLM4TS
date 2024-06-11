package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationNotEqualTest {
    @Test
    public void testComputeValue() {
        Expression arg1 = new SomeExpression();
        Expression arg2 = new SomeExpression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        assertTrue((Boolean) operation.computeValue(null));
        
        arg1 = new SomeExpression();
        arg2 = new SomeExpression();
        operation = new CoreOperationNotEqual(arg1, arg2);
        assertFalse((Boolean) operation.computeValue(null));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        assertEquals("!=", operation.getSymbol());
    }
}

class SomeExpression implements Expression {
    public Object computeValue(EvalContext context) {
        return null;
    }
}