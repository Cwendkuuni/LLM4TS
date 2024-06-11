package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        Expression[] expressions = new Expression[2];
        expressions[0] = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return 5;
            }
        };

        expressions[1] = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return 3;
            }
        };

        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertTrue(coreOperationRelationalExpression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] expressions = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(coreOperationRelationalExpression.getPrecedence(), 6);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] expressions = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] expressions = new Expression[2];
        expressions[0] = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return 5;
            }
        };

        expressions[1] = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return 3;
            }
        };


        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertTrue(coreOperationRelationalExpression.compute(null, null));

        expressions[0] = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return 3.5;
            }
        };

        expressions[1] = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return 3.0;
            }
        };

        assertTrue(coreOperationRelationalExpression.compute(null, null));

        expressions[0] = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return 2.3;
            }
        };

        expressions[1] = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return 3.7;
            }
        };

        assertFalse(coreOperationRelationalExpression.compute(null, null));
    }

    @Test
    public void testReduce() {
        Expression[] expressions = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        EvalContext evalContext = new EvalContext(null);
        SelfContext selfContext = new SelfContext();
        InitialContext initialContext = new InitialContext(evalContext);

        selfContext.reset();
        initialContext.reset();

        Object reducedSelfContext = coreOperationRelationalExpression.reduce(selfContext);
        Object reducedInitialContext = coreOperationRelationalExpression.reduce(initialContext);

        assertTrue(reducedSelfContext instanceof SelfContext);
        assertTrue(reducedInitialContext instanceof InitialContext);

        Object collection = new HashSet<String>();
        Object reducedCollection = coreOperationRelationalExpression.reduce(collection);

        assertTrue(reducedCollection instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        Expression[] expressions = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Iterator iterator = new HashSet<String>().iterator();
        Object value = "test";

        assertFalse(coreOperationRelationalExpression.containsMatch(iterator, value));

        iterator = new HashSet<String>().iterator();
        iterator.hasNext();

        assertTrue(coreOperationRelationalExpression.containsMatch(iterator, value));
    }

    @Test
    public void testFindMatch() {
        Expression[] expressions = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Iterator leftIterator = new HashSet<String>().iterator();
        Iterator rightIterator = new HashSet<String>().iterator();
        assertFalse(coreOperationRelationalExpression.findMatch(leftIterator, rightIterator));

    }
}