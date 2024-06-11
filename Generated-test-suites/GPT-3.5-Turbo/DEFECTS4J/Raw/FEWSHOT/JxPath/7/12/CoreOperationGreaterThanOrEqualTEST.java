package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        
        // Test case 1: Args compute to >=, return true
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(5);
        arg2.setValue(3);
        operation.setArgs(new Expression[]{arg1, arg2});
        assertTrue((Boolean) operation.computeValue(context));
        
        // Test case 2: Args compute to <, return false
        arg1.setValue(3.6);
        arg2.setValue(4.3);
        operation.setArgs(new Expression[]{arg1, arg2});
        assertFalse((Boolean) operation.computeValue(context));
        
        // Test case 3: Args compute to =, return true
        arg1.setValue(10);
        arg2.setValue(10);
        operation.setArgs(new Expression[]{arg1, arg2});
        assertTrue((Boolean) operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        assertEquals(">=", operation.getSymbol());
    }
}