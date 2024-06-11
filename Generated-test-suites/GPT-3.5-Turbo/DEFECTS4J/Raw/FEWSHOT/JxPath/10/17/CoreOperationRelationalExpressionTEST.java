package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression operation;

    @Test
    public void testComputeValue() {
        operation = new CoreOperationRelationalExpression(new Expression[]{null, null}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        operation = new CoreOperationRelationalExpression(new Expression[]{null, null}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        int precedence = operation.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        operation = new CoreOperationRelationalExpression(new Expression[]{null, null}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        boolean isSymmetric = operation.isSymmetric();
        assertEquals(false, isSymmetric);
    }

    @Test
    public void testCompute() {
        operation = new CoreOperationRelationalExpression(new Expression[]{null, null}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        boolean result = operation.compute(null, null);
        assertEquals(false, result);
    }

    @Test
    public void testReduce() {
        operation = new CoreOperationRelationalExpression(new Expression[]{null, null}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object result = operation.reduce(null);
        assertNull(result);
    }

    @Test
    public void testContainsMatch() {
        operation = new CoreOperationRelationalExpression(new Expression[]{null, null}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        boolean result = operation.containsMatch(null, null);
        assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        operation = new CoreOperationRelationalExpression(new Expression[]{null, null}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        boolean result = operation.findMatch(null, null);
        assertEquals(false, result);
    }
}