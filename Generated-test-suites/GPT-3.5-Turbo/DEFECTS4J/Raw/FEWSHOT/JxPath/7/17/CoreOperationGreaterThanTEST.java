package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        // Test with l > r
        arg1.setValue(10);
        arg2.setValue(5);
        assertEquals(Boolean.TRUE, operation.computeValue(null));
        
        // Test with l < r
        arg1.setValue(5);
        arg2.setValue(10);
        assertEquals(Boolean.FALSE, operation.computeValue(null));
        
        // Test with l = r
        arg1.setValue(15);
        arg2.setValue(15);
        assertEquals(Boolean.FALSE, operation.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        assertEquals(">", operation.getSymbol());
    }
}