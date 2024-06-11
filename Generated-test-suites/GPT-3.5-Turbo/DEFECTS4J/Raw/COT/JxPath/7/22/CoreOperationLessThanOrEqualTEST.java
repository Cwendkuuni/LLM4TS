
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Test with arg1 and arg2 both as integers
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(new CoreConstant(-5), new CoreConstant(10));
        assertSame(Boolean.TRUE, expr.computeValue(null));
        
        // Test with arg1 and arg2 both as doubles
        CoreOperationLessThanOrEqual expr2 = new CoreOperationLessThanOrEqual(new CoreConstant(2.5), new CoreConstant(2.5));
        assertSame(Boolean.TRUE, expr2.computeValue(null));
        
        // Test with arg1 and arg2 as integer and double
        CoreOperationLessThanOrEqual expr3 = new CoreOperationLessThanOrEqual(new CoreConstant(2), new CoreConstant(3.75));
        assertSame(Boolean.TRUE, expr3.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(null, null);
        assertEquals("<=", expr.getSymbol());
    }

}
