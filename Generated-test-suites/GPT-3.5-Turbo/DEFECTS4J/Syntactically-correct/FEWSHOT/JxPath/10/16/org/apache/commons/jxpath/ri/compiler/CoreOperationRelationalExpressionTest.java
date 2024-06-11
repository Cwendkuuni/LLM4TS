
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    private static CoreOperationRelationalExpression coreOperationRelationalExpression;

    @BeforeClass
    public static void setUp() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }
    
    @Test
    public void testComputeValue_CorrectArguments() {
        EvalContext context = new EvalContext();
        context.setNativeContext("test");
        Object result = coreOperationRelationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_IncorrectArguments() {
        EvalContext context = new EvalContext();
        context.setNativeContext(1);
        Object result = coreOperationRelationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testGetPrecedence() {
        int result = coreOperationRelationalExpression.getPrecedence();
        assertEquals(3, result);
    }
    
    @Test
    public void testIsSymmetric() {
        boolean result = coreOperationRelationalExpression.isSymmetric();
        assertFalse(result);
    }
    
    private static class TestExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return context.getNativeContext();
        }
    }

}
