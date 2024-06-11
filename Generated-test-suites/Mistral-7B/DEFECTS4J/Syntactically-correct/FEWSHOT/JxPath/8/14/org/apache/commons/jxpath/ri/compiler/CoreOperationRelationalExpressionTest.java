package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IteratingIterator;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.axes.SelfAxis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object leftValue = new SelfAxis("self::node()[1]", new EvalContext());
        Object rightValue = new SelfAxis("self::node()[2]", new EvalContext());

        CoreOperationRelationalExpression expression = new GreaterThanExpression(new CoreOperation[] { new VariableExpression("left"), new VariableExpression("right") });
        Object result = expression.computeValue(new EvalContext());

        assertFalse(result instanceof Boolean);

        leftValue = new SelfAxis("self::node()[1]", new EvalContext());
        rightValue = new SelfAxis("self::node()[2]", new EvalContext());

        leftValue = new Integer(5);
        rightValue = new Integer(4);

        expression = new GreaterThanExpression(new CoreOperation[] { new ConstantExpression(leftValue), new ConstantExpression(rightValue) });
        result = expression.computeValue(new EvalContext());

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        leftValue = new SelfAxis("self::node()[1]", new EvalContext());
        rightValue = new SelfAxis("self::node()[2]", new EvalContext());

        leftValue = new Double(5.5);
        rightValue = new Double(4.5);

        expression = new GreaterThanExpression(new CoreOperation[] { new ConstantExpression(leftValue), new ConstantExpression(rightValue) });
        result = expression.computeValue(new EvalContext());

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        leftValue = new SelfAxis("self::node()[1]", new EvalContext());
        rightValue = new SelfAxis("self::node()[2]", new EvalContext());

        leftValue = new IteratingIterator(Axis.CHILD) {
            @Override
            public Iterator iterator() {
                return new NodeIterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return new Object();
                    }
                };
            }
        };

        rightValue = new IteratingIterator(Axis.CHILD) {
            @Override
            public Iterator iterator() {
                return new NodeIterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return new Object();
                    }
                };
            }
        };

        expression = new GreaterThanExpression(new CoreOperation[] { leftValue, rightValue });
        result = expression.computeValue(new EvalContext());

        assertFalse(result instanceof Boolean);

        leftValue = new IteratingIterator(Axis.CHILD) {
            @Override
            public Iterator iterator() {
                Collection<Object> values = new ArrayList<>();
                values.add(new Integer(5));
                values.add(new Integer(6));
                values.add(new Integer(7));
                return values.iterator();
            }
        };

        rightValue = new IteratingIterator(Axis.CHILD) {
            @Override
            public Iterator iterator() {
                Collection<Object> values = new ArrayList<>();
                values.add(new Integer(4));
                values.add(new Integer(5));
                values.add(new Integer(6));
                values.add(new Integer(7));
                return values.iterator();
            }
        };

        expression = new GreaterThanExpression(new CoreOperation[] { leftValue, rightValue });
        result = expression.computeValue(new EvalContext());

        assertFalse(result instanceof Boolean);

        leftValue = new IteratingIterator(Axis.CHILD) {
            @Override
            public Iterator iterator() {
                Collection<Object> values = new ArrayList<>();
                values.add(new Integer(5));
                values.add(new Integer(6));
                values.add(new Integer(7));
                return values.iterator();
            }
        };

        rightValue = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        expression = new GreaterThanExpression(new CoreOperation[] { leftValue, rightValue });
        result = expression.computeValue(new EvalContext());

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new CoreOperation[] {});
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new CoreOperation[] {});
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new CoreOperation[] {});
        assertTrue(expression.evaluateCompare(1) == -1);
        assertTrue(expression.evaluateCompare(0) == -1);
        assertTrue(expression.evaluateCompare(-1) == 1);
        assertTrue(expression.evaluateCompare(0) == 0);
        assertTrue(expression.evaluateCompare(1) == 0);
    }

    @Test
    public void testReduce() {
        Object o = new SelfAxis("self::node()[1]", new EvalContext());
        Object reduced = CoreOperationRelationalExpression.reduce(o);
        assertTrue(reduced instanceof Object);

        o = new SelfAxis("self::node()[1]", new EvalContext());
        o = new Collection<Object>() {
            @Override
            public boolean add(Object e) {
                return false;
            }

            @Override
            public boolean addAll(Collection c) {
                return false;
            }

            @Override
            public void clear() {
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection c) {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator iterator() {
                return null;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean removeAll(Collection c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection c) {
                return false;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public Object[] toArray(Type type) {
                return new Object[0];
            }
        };
        reduced = CoreOperationRelationalExpression.reduce(o);
        assertTrue(reduced instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };

        Object value = new Object();
        boolean result = CoreOperationRelationalExpression.containsMatch(it, value);
        assertFalse(result);

        it = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };

        result = CoreOperationRelationalExpression.containsMatch(it, value);
        assertFalse(result);

        it = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                Object element = super.next();
                if (CoreOperationRelationalExpression.compute(element, value)) {
                    return element;
                }
                return super.next();
            }
        };

        result = CoreOperationRelationalExpression.containsMatch(it, value);
        assertTrue(result);

        it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        result = CoreOperationRelationalExpression.containsMatch(it, value);
        assertFalse(result);

        it = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        result = CoreOperationRelationalExpression.containsMatch(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };

        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };

        boolean result = CoreOperationRelationalExpression.findMatch(lit, rit);
        assertFalse(result);

        lit = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                Object element = super.next();
                if (CoreOperationRelationalExpression.containsMatch(lit, element)) {
                    return element;
                }
                return super.next();
            }
        };

        rit = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                Object element = super.next();
                if (CoreOperationRelationalExpression.containsMatch(rit, element)) {
                    return element;
                }
                return super.next();
            }
        };

        result = CoreOperationRelationalExpression.findMatch(lit, rit);
        assertTrue(result);

        lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        result = CoreOperationRelationalExpression.findMatch(lit, rit);
        assertFalse(result);

        lit = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        result = CoreOperationRelationalExpression.findMatch(lit, rit);
        assertFalse(result);
    }
}