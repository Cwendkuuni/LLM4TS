package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        }, new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 3;
            }
        });
        
        assertEquals(Boolean.TRUE, expression.computeValue(null));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(null, null);
        
        assertEquals(">", expression.getSymbol());
    }
}