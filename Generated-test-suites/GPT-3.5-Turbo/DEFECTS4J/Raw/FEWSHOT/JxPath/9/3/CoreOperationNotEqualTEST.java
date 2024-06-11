package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        // Test case: arg1 and arg2 are equal
        arg1.setValue("foo");
        arg2.setValue("foo");
        assertFalse((boolean) operation.computeValue(context));

        // Test case: arg1 and arg2 are equal but of different types
        arg1.setValue("1");
        arg2.setValue(1);
        assertFalse((boolean) operation.computeValue(context));

        // Test case: arg1 and arg2 are not equal
        arg1.setValue("foo");
        arg2.setValue("bar");
        assertTrue((boolean) operation.computeValue(context));

        // Test case: arg1 and arg2 are not equal but of different types
        arg1.setValue("1");
        arg2.setValue(2);
        assertTrue((boolean) operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        assertEquals("!=", operation.getSymbol());
    }
}