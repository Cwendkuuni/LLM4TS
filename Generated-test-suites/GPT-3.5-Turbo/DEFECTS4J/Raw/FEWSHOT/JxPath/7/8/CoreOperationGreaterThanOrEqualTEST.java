package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    
    private CoreOperationGreaterThanOrEqual operation;
    
    @Before
    public void setUp() {
        operation = new CoreOperationGreaterThanOrEqual(null, null);
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(null);
        
        operation.args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 4;
            }
        };
        
        operation.args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };
        
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        operation.args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 4;
            }
        };
        
        operation.args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 4;
            }
        };
        
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        operation.args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };
        
        operation.args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 4;
            }
        };
        
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        assertEquals(">=", operation.getSymbol());
    }
}