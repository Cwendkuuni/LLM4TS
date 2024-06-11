package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl();
        
        // Test case 1: left operand is null, right operand is null
        EvalContext context1 = new EvalContext(null, null);
        assertEquals(Boolean.FALSE, expression.computeValue(context1));
        
        // Test case 2: left operand is null, right operand is "foo"
        EvalContext context2 = new EvalContext(null, "foo");
        assertEquals(Boolean.FALSE, expression.computeValue(context2));
        
        // Test case 3: left operand is "foo", right operand is null
        EvalContext context3 = new EvalContext("foo", null);
        assertEquals(Boolean.FALSE, expression.computeValue(context3));
        
        // Test case 4: left operand is "100", right operand is "200"
        EvalContext context4 = new EvalContext("100", "200");
        assertEquals(Boolean.TRUE, expression.computeValue(context4));
        
        // Test case 5: left operand is "abc", right operand is "ABC"
        EvalContext context5 = new EvalContext("abc", "ABC");
        assertEquals(Boolean.FALSE, expression.computeValue(context5));
    }
    
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl();
        assertEquals(3, expression.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl();
        assertEquals(false, expression.isSymmetric());
    }
    
    private class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {
        
        protected CoreOperationRelationalExpressionImpl() {
            super(null);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare < 0;
        }
    }
}