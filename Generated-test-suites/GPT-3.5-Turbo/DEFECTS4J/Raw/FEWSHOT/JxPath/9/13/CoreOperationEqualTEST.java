package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationEqualTest {
    
    private CoreOperationEqual operation;
    private EvalContext context;
    
    @Before
    public void setUp() {
        operation = new CoreOperationEqual(new Expression(), new Expression());
        context = new EvalContext();
    }
    
    @Test
    public void testComputeValue() {
        operation.getArgs()[0] = "foo";
        operation.getArgs()[1] = "foo";
        assertTrue((Boolean) operation.computeValue(context));
        
        operation.getArgs()[0] = "foo";
        operation.getArgs()[1] = "bar";
        assertFalse((Boolean) operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        assertEquals("=", operation.getSymbol());
    }
}