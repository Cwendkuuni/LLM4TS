package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext(context, null));
        
        assertFalse(expression.computeValue(context));
        
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        args[1] = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare != 0;
            }
        };
        
        assertFalse(expression.computeValue(context));
        
        args[0] = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        args[1] = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        
        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testReduceWithSelfContext() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        EvalContext context = new EvalContext();
        
        SelfContext selfContext = new SelfContext(context, null);
        context.setSingleNodePointer(selfContext);
        
        Object result = expression.reduce(selfContext);
        
        assertEquals(selfContext, result);
    }

    @Test
    public void testReduceWithCollection() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        EvalContext context = new EvalContext();
        
        Object result = expression.reduce(new SelfContext(context, null));
        
        assertEquals(result, context.getSingleNodePointer());
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        EvalContext context = new EvalContext();
        
        assertFalse(expression.containsMatch(context.getSingleNodePointer(), context.getSingleNodePointer()));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        InitialContext left = new InitialContext(context);
        left.reset();
        
        assertFalse(expression.findMatch(left, context.getSingleNodePointer()));
    }

}