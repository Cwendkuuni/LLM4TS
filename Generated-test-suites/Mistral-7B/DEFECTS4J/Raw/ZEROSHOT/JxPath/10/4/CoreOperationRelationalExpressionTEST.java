import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.iter.Iterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        EvalContext context = new EvalContext(new Object[]{"value1", "value2", "value3"});

        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThan(new CoreConstantExpression(2), new CoreConstantExpression(1));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(2), new CoreConstantExpression(1));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThan(new CoreConstantExpression(2), new CoreConstantExpression(1));
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(2), new CoreConstantExpression(1));
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        boolean symmetric = expression.isSymmetric();
        assertEquals(false, symmetric);

        expression = new CoreOperationLessThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        symmetric = expression.isSymmetric();
        assertEquals(false, symmetric);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        symmetric = expression.isSymmetric();
        assertEquals(false, symmetric);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        symmetric = expression.isSymmetric();
        assertEquals(true, symmetric);
    }

    @org.junit.Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(1));
        boolean result = expression.evaluateCompare(0);
        assertEquals(true, result);

        result = expression.evaluateCompare(1);
        assertEquals(true, result);

        result = expression.evaluateCompare(-1);
        assertEquals(false, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        result = expression.evaluateCompare(0);
        assertEquals(false, result);

        result = expression.evaluateCompare(1);
        assertEquals(false, result);

        result = expression.evaluateCompare(-1);
        assertEquals(true, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(2), new CoreConstantExpression(1));
        result = expression.evaluateCompare(0);
        assertEquals(false, result);

        result = expression.evaluateCompare(1);
        assertEquals(true, result);

        result = expression.evaluateCompare(-1);
        assertEquals(false, result);
    }

    @org.junit.Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        Object left = new Integer(1);
        Object right = new Integer(2);
        boolean result = expression.compute(left, right);
        assertEquals(false, result);

        left = new Integer(2);
        right = new Integer(1);
        result = expression.compute(left, right);
        assertEquals(false, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(1));
        result = expression.compute(left, right);
        assertEquals(true, result);

        left = new Integer(1);
        right = new Integer(1);
        result = expression.compute(left, right);
        assertEquals(true, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        result = expression.compute(left, right);
        assertEquals(false, result);

        left = new Integer(2);
        right = new Integer(1);
        result = expression.compute(left, right);
        assertEquals(true, result);

        expression = new CoreOperationLessThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        result = expression.compute(left, right);
        assertEquals(false, result);

        left = new Integer(2);
        right = new Integer(1);
        result = expression.compute(left, right);
        assertEquals(false, result);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(1));
        result = expression.compute(left, right);
        assertEquals(true, result);

        left = new Integer(1);
        right = new Integer(1);
        result = expression.compute(left, right);
        assertEquals(true, result);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        result = expression.compute(left, right);
        assertEquals(false, result);

        left = new Integer(2);
        right = new Integer(1);
        result = expression.compute(left, right);
        assertEquals(true, result);
    }

    @org.junit.Test
    public void testReduce() {
        Object o = new ArrayList();
        Object result = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression(new CoreConstantExpression(1), new CoreConstantExpression(2)).reduce(o));
        assertEquals(o, result);

        o = new InitialContext();
        result = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression(new CoreConstantExpression(1), new CoreConstantExpression(2)).reduce(o));
        assertNull(result);

        o = new Iterator() {};
        result = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression(new CoreConstantExpression(1), new CoreConstantExpression(2)).reduce(o));
        assertEquals(o, result);

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
        result = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression(new CoreConstantExpression(1), new CoreConstantExpression(2)).reduce(it));
        assertEquals(it, result);
    }

    @org.junit.Test
    public void testContainsMatch() {
        Iterator it = new Iterator() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count++ < 3;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };
        Object value = new Object();
        boolean result = new CoreOperationRelationalExpression(new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2)), it).containsMatch(it, value);
        assertEquals(false, result);

        result = new CoreOperationRelationalExpression(new CoreOperationLessThan(new CoreConstantExpression(1), new CoreConstantExpression(2)), it).containsMatch(it, value);
        assertEquals(false, result);

        result = new CoreOperationRelationalExpression(new CoreOperationLessThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2)), it).containsMatch(it, value);
        assertEquals(true, result);

        result = new CoreOperationRelationalExpression(new CoreOperationLessThanOrEqual(new CoreConstantExpression(1), it), value).containsMatch(it, value);
        assertEquals(false, result);

        result = new CoreOperationRelationalExpression(new CoreOperationLessThanOrEqual(it, new CoreConstantExpression(1)), value).containsMatch(it, value);
        assertEquals(false, result);

        result = new CoreOperationRelationalExpression(new CoreOperationLessThanOrEqual(it, it), value).containsMatch(it, value);
        assertEquals(true, result);

        result = new CoreOperationRelationalExpression(new CoreOperationLessThanOrEqual(it, it), new CoreConstantExpression(1)).containsMatch(it, value);
        assertEquals(false, result);

        result = new CoreOperationRelationalExpression(new CoreOperationLessThanOrEqual(it, it), new CoreConstantExpression(2)).containsMatch(it, value);
        assertEquals(false, result);
    }

    @org.junit.Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Object());
                list.add(new Object());
                list.add(new Object());
            }

            @Override
            public boolean hasNext() {
                return list.size() > 0;
            }

            @Override
            public Object next() {
                Object object = list.remove(0);
                return object;
            }
        };
        Iterator rit = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Object());
                list.add(new Object());
                list.add(new Object());
            }

            @Override
            public boolean hasNext() {
                return list.size() > 0;
            }

            @Override
            public Object next() {
                Object object = list.remove(0);
                return object;
            }
        };
        boolean result = new CoreOperationRelationalExpression(new CoreOperationGreaterThanOrEqual(lit, rit), lit, rit).findMatch();
        assertEquals(false, result);

        result = new CoreOperationRelationalExpression(new CoreOperationLessThanOrEqual(lit, rit), lit, rit).findMatch();
        assertEquals(true, result);
    }
}