package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValueLessThan() {
        EvalContext context = new EvalContext();
        
        CoreOperationLessThan expression = new CoreOperationLessThan(new Constant("2"), new Constant("3"));
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        expression = new CoreOperationLessThan(new Constant("-1"), new Constant("0"));
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        expression = new CoreOperationLessThan(new Constant("5.5"), new Constant("5.5"));
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }
    
    @Test
    public void testComputeValueNotLessThan() {
        EvalContext context = new EvalContext();
        
        CoreOperationLessThan expression = new CoreOperationLessThan(new Constant("3"), new Constant("2"));
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        
        expression = new CoreOperationLessThan(new Constant("0"), new Constant("-1"));
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        
        expression = new CoreOperationLessThan(new Constant("5.5"), new Constant("5.0"));
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Constant("2"), new Constant("3"));
        assertEquals("<", expression.getSymbol());
    }

}