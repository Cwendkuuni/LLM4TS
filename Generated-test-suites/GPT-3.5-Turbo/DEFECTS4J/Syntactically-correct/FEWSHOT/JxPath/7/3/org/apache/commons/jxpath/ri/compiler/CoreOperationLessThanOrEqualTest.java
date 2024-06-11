package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test case 1
        CoreOperationLessThanOrEqual expr1 = new CoreOperationLessThanOrEqual(new Expression1(), new Expression2());
        Object result1 = expr1.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result1);
        
        // Test case 2
        CoreOperationLessThanOrEqual expr2 = new CoreOperationLessThanOrEqual(new Expression3(), new Expression4());
        Object result2 = expr2.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result2);
        
        // Test case 3
        CoreOperationLessThanOrEqual expr3 = new CoreOperationLessThanOrEqual(new Expression5(), new Expression6());
        Object result3 = expr3.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result3);
        
        // Test case 4
        CoreOperationLessThanOrEqual expr4 = new CoreOperationLessThanOrEqual(new Expression7(), new Expression8());
        Object result4 = expr4.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result4);
    }

    private class Expression1 extends Expression {
        public Object computeValue(EvalContext context) {
            return 1.0;
        }
    }
    
    private class Expression2 extends Expression {
        public Object computeValue(EvalContext context) {
            return 2.0;
        }
    }
    
    private class Expression3 extends Expression {
        public Object computeValue(EvalContext context) {
            return 3.0;
        }
    }
    
    private class Expression4 extends Expression {
        public Object computeValue(EvalContext context) {
            return 2.5;
        }
    }
    
    private class Expression5 extends Expression {
        public Object computeValue(EvalContext context) {
            return 5.0;
        }
    }
    
    private class Expression6 extends Expression {
        public Object computeValue(EvalContext context) {
            return 2.0;
        }
    }
    
    private class Expression7 extends Expression {
        public Object computeValue(EvalContext context) {
            return 4.0;
        }
    }
    
    private class Expression8 extends Expression {
        public Object computeValue(EvalContext context) {
            return 4.0;
        }
    }
}