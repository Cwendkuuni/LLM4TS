package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test cases for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expr;
    private EvalContext context;

    @Before
    public void setup() {
        context = new EvalContext();
        expr = new CoreOperationRelationalExpression(new Expression[]{
                new CoreConstantExpression(new Constant(1.0)),
                new CoreVariableExpression("$x")
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue() {
        Object left = expr.getArgs()[0].compute(context);
        Object right = expr.getArgs()[1].compute(context);
        double leftValue = InfoSetUtil.doubleValue(left);
        double rightValue = InfoSetUtil.doubleValue(right);

        // Test with valid values
        InfoSetUtil.setDoubleValue(left, 2.0);
        InfoSetUtil.setDoubleValue(right, 1.5);
        assertTrue(expr.computeValue(context) instanceof Boolean &&
                ((Boolean) expr.computeValue(context)).booleanValue());

        InfoSetUtil.setDoubleValue(left, 1.5);
        InfoSetUtil.setDoubleValue(right, 2.0);
        assertFalse(expr.computeValue(context) instanceof Boolean);

        // Test with null values
        left = null;
        right = null;
        assertNull(expr.computeValue(context));

        // Test with invalid values (one is a SelfContext)
        left = new SelfContext();
        assertNull(expr.computeValue(context));

        left = new InitialContext();
        right = new InitialContext();
        assertNull(expr.computeValue(context));

        left = new InitialContext();
        right = new SelfContext();
        assertNull(expr.computeValue(context));

        left = new SelfContext();
        right = new InitialContext();
        assertNull(expr.computeValue(context));

        // Test with invalid values (one is an Iterator)
        left = Collections.singletonList(new Double(1.0)).iterator();
        right = new Double(2.0);
        assertFalse(expr.computeValue(context) instanceof Boolean);

        left = new Double(1.0);
        right = Collections.singletonList(new Double(2.0)).iterator();
        assertFalse(expr.computeValue(context) instanceof Boolean);
    }

    @Test
    public void testFindMatch() {
        List<Double> leftList = new ArrayList<>();
        List<Double> rightList = new ArrayList<>();

        leftList.add(1.0);
        leftList.add(2.0);
        rightList.add(1.5);
        rightList.add(2.5);

        Iterator leftIterator = leftList.iterator();
        Iterator rightIterator = rightList.iterator();

        assertTrue(expr.compute(leftIterator.next(), rightIterator.next()));
        assertTrue(expr.compute(leftIterator.next(), rightIterator.next()));
        assertFalse(expr.compute(leftIterator.next(), rightIterator.next()));
        assertFalse(expr.compute(leftIterator.next(), rightIterator.next()));
    }

    @Test
    public void testContainsMatch() {
        List<Double> list = new ArrayList<>();

        list.add(1.0);
        list.add(2.0);
        list.add(3.0);

        Iterator iterator = list.iterator();

        Object value = new Double(2.0);
        assertTrue(expr.containsMatch(iterator, value));
        assertFalse(expr.containsMatch(iterator, new Double(4.0)));

        Object value2 = new CoreVariableExpression("$x");
        assertFalse(expr.containsMatch(iterator, value2));
    }
}