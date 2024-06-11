package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLessThanOrEqual;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Tests for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new CoreExpressionNode(new VariableExpression("a")),
                new CoreExpressionNode(new VariableExpression("b")));
        Object left = new Integer(5);
        Object right = new Integer(3);
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Double(5.5);
        right = new Double(3.5);
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new String("5");
        right = new String("3");
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Double("5.5");
        right = new Double("3.5");
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new ArrayList();
        right = new ArrayList();
        left.add(new Integer(5));
        right.add(new Integer(3));
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(3);
            }
        };
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        right = new SelfContext();
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new SelfContext();
        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(3);
            }
        };
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new SelfContext();
        right = new SelfContext();
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new InitialContext();
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        try {
            expression.computeValue(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNode(new VariableExpression("a")),
                new CoreExpressionNode(new VariableExpression("b")));
        Object left = new Integer(5);
        Object right = new Integer(3);
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Double(5.5);
        right = new Double(3.5);
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new String("5");
        right = new String("3");
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Double("5.5");
        right = new Double("3.5");
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new ArrayList();
        right = new ArrayList();
        left.add(new Integer(5));
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(3);
            }
        };
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        right = new SelfContext();
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new SelfContext();
        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(3);
            }
        };
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new SelfContext();
        right = new SelfContext();
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new InitialContext();
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        try {
            expression.computeValue(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }

        left = new Integer(5);
        right = new Integer(5);
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new CoreExpressionNode(new VariableExpression("a")),
                new CoreExpressionNode(new VariableExpression("b")));
        Object left = new Integer(5);
        Object right = new Integer(3);
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Double(5.5);
        right = new Double(3.5);
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new String("5");
        right = new String("3");
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Double("5.5");
        right = new Double("3.5");
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new ArrayList();
        right = new ArrayList();
        left.add(new Integer(5));
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(3);
            }
        };
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        right = new SelfContext();
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new SelfContext();
        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(3);
            }
        };
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new SelfContext();
        right = new SelfContext();
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new InitialContext();
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        try {
            expression.computeValue(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new CoreExpressionNode(new VariableExpression("a")),
                new CoreExpressionNode(new VariableExpression("b")));
        Object left = new Integer(5);
        Object right = new Integer(3);
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Double(5.5);
        right = new Double(3.5);
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new String("5");
        right = new String("3");
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Double("5.5");
        right = new Double("3.5");
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new ArrayList();
        right = new ArrayList();
        left.add(new Integer(5));
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(3);
            }
        };
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        right = new SelfContext();
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new SelfContext();
        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(3);
            }
        };
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new SelfContext();
        right = new SelfContext();
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new InitialContext();
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        try {
            expression.computeValue(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }

        left = new Integer(5);
        right = new Integer(5);
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));
    }
}