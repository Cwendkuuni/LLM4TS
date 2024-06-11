package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        // write assertions here
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        // write assertions here
    }
}