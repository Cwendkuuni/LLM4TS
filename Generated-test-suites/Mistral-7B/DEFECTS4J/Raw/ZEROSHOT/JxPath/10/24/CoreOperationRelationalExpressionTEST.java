package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.IteratorSupport;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression(5), new LiteralExpression(3)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        InfoSet infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.EMPTY_ITERATOR;
            }
        };

        EvalContext context = new EvalContext(new InitialContext(infoSet));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(3.0, 5.0));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @org.junit.Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression(5), new LiteralExpression(3)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        InfoSet infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.EMPTY_ITERATOR;
            }
        };

        EvalContext context = new EvalContext(new InitialContext(infoSet));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(3.0, 5.0));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(3.0, 6.0));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression(3), new LiteralExpression(5)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        InfoSet infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.EMPTY_ITERATOR;
            }
        };

        EvalContext context = new EvalContext(new InitialContext(infoSet));
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(3.0, 5.0));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(2.0, 5.0));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression(3), new LiteralExpression(5)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        InfoSet infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.EMPTY_ITERATOR;
            }
        };

        EvalContext context = new EvalContext(new InitialContext(infoSet));
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(3.0, 5.0));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(3.0, 5.0));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(3.0, 4.0));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testEquals() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression("foo"), new LiteralExpression("foo")) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        InfoSet infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.EMPTY_ITERATOR;
            }
        };

        EvalContext context = new EvalContext(new InitialContext(infoSet));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList("foo", "foo"));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList("foo", "bar"));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testNotEquals() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression("foo"), new LiteralExpression("foo")) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare != 0;
            }
        };

        InfoSet infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.EMPTY_ITERATOR;
            }
        };

        EvalContext context = new EvalContext(new InitialContext(infoSet));
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList("foo", "foo"));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList("foo", "bar"));
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testContains() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new LiteralExpression(5), new LiteralExpression(new ArrayList<>(Arrays.asList(3.0, 5.0)))) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        InfoSet infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.EMPTY_ITERATOR;
            }
        };

        EvalContext context = new EvalContext(new InitialContext(infoSet));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(3.0, 5.0)).iterator();
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(2.0, 5.0)).iterator();
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new IteratorExpression(new SingletonIterator(5)), new IteratorExpression(new SingletonIterator(3))) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        InfoSet infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.EMPTY_ITERATOR;
            }
        };

        EvalContext context = new EvalContext(new InitialContext(infoSet));
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(3, 5)).iterator();
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return IteratorSupport.fromIterable(Arrays.asList(3, 4)).iterator();
            }
        };

        context = new EvalContext(new InitialContext(infoSet));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}