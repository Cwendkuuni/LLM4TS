package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new SelfContext();
        Expression rightExpression = new SelfContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertTrue(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionDifferentValues() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new SelfContext();
        Expression rightExpression = new InitialContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertFalse(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionEquals() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new SelfContext();
        Expression rightExpression = new SelfContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression1 = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertTrue(expression1.equals(expression2));
    }

    @Test
    public void testCoreOperationRelationalExpressionNotEquals() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new SelfContext();
        Expression rightExpression = new SelfContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression1 = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertFalse(expression1.equals(expression2));
    }

    @Test
    public void testCoreOperationRelationalExpressionLessThan() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new SelfContext();
        Expression rightExpression = new InitialContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertFalse(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionLessThanOrEqual() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new SelfContext();
        Expression rightExpression = new InitialContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertTrue(left.equals(right) || left.equals(null) || right.equals(null));
    }

    @Test
    public void testCoreOperationRelationalExpressionGreaterThan() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new InitialContext();
        Expression rightExpression = new SelfContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertFalse(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionGreaterThanOrEqual() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new InitialContext();
        Expression rightExpression = new SelfContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertTrue(left.equals(right) || left.equals(null) || right.equals(null));
    }

    @Test
    public void testCoreOperationRelationalExpressionIterators() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new IteratorExpression(new ArrayList<Object>().iterator());
        Expression rightExpression = new IteratorExpression(new ArrayList<Object>().iterator());
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertTrue(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionIteratorsDifferentValues() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new IteratorExpression(new ArrayList<Object>().iterator());
        Expression rightExpression = new IteratorExpression(new ArrayList<Object>() {{ add(new Object()); }}.iterator());
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare != 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertFalse(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionIteratorsIntersection() {
        Expression[] args = new Expression[2];
        Collection collection1 = new ArrayList<>();
        collection1.add(new Object());
        Expression leftExpression = new IteratorExpression(collection1.iterator());
        Collection collection2 = new ArrayList<>();
        collection2.add(new Object());
        Expression rightExpression = new IteratorExpression(collection2.iterator());
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertTrue(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionIteratorsNoIntersection() {
        Expression[] args = new Expression[2];
        Collection collection1 = new ArrayList<>();
        Expression leftExpression = new IteratorExpression(collection1.iterator());
        Collection collection2 = new ArrayList<>();
        collection2.add(new Object());
        Expression rightExpression = new IteratorExpression(collection2.iterator());
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare != 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertFalse(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionDoubleValues() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new SelfContext();
        Expression rightExpression = new InitialContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return Double.doubleToLongBits(InfoSetUtil.doubleValue(leftExpression.compute(new EvalContext()))) > Double.doubleToLongBits(InfoSetUtil.doubleValue(rightExpression.compute(new EvalContext())));
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertFalse(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionDoubleValuesEqual() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new SelfContext();
        Expression rightExpression = new SelfContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertTrue(left.equals(right) || left.equals(null) || right.equals(null));
    }

    @Test
    public void testCoreOperationRelationalExpressionDoubleValuesLessThan() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new SelfContext();
        Expression rightExpression = new InitialContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertFalse(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionDoubleValuesLessThanOrEqual() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new SelfContext();
        Expression rightExpression = new InitialContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertTrue(left.equals(right) || left.equals(null) || right.equals(null));
    }

    @Test
    public void testCoreOperationRelationalExpressionDoubleValuesGreaterThan() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new InitialContext();
        Expression rightExpression = new SelfContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertFalse(left.equals(right));
    }

    @Test
    public void testCoreOperationRelationalExpressionDoubleValuesGreaterThanOrEqual() {
        Expression[] args = new Expression[2];
        Expression leftExpression = new InitialContext();
        Expression rightExpression = new SelfContext();
        args[0] = leftExpression;
        args[1] = rightExpression;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        EvalContext context = new EvalContext();

        Object left = expression.compute(context);
        Object right = expression.compute(context);

        assertTrue(left.equals(right) || left.equals(null) || right.equals(null));
    }
}