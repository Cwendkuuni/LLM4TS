package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression leftExp = new CoreNumberValueExpression(1.0);
        Expression rightExp = new CoreNumberValueExpression(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreOperationRelationalExpression[]{leftExp, rightExp});

        assertTrue(expression.computeValue(new EvalContext()) == Boolean.FALSE);

        leftExp = new CoreNumberValueExpression(2.0);
        assertTrue(expression.computeValue(new EvalContext()) == Boolean.TRUE);

        leftExp = new CoreNumberValueExpression(1.0);
        rightExp = new CoreNumberValueExpression(1.0);
        assertTrue(expression.computeValue(new EvalContext()) == Boolean.FALSE);

        leftExp = new CoreNumberValueExpression(1.0);
        rightExp = new CoreNumberValueExpression(1.0);
        assertTrue(expression.computeValue(new EvalContext()) == Boolean.FALSE);

        leftExp = new CoreNumberValueExpression(Double.NaN);
        assertFalse(expression.computeValue(new EvalContext()) == Boolean.TRUE);
        assertFalse(expression.computeValue(new EvalContext()) == Boolean.FALSE);
        assertFalse(expression.computeValue(new EvalContext()) instanceof Boolean);

        rightExp = new CoreNumberValueExpression(Double.NaN);
        assertFalse(expression.computeValue(new EvalContext()) == Boolean.TRUE);
        assertFalse(expression.computeValue(new EvalContext()) == Boolean.FALSE);
        assertFalse(expression.computeValue(new EvalContext()) instanceof Boolean);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreOperationRelationalExpression[]{new CoreConstantExpression(1.0), new CoreConstantExpression(2.0)});
        assertTrue(expression.evaluateCompare(-1) == false);
        assertTrue(expression.evaluateCompare(0) == false);
        assertTrue(expression.evaluateCompare(1) == true);

        expression = new CoreOperationGreaterThanOrEqualExpression(new CoreOperationRelationalExpression[]{new CoreConstantExpression(1.0), new CoreConstantExpression(2.0)});
        assertTrue(expression.evaluateCompare(-1) == false);
        assertTrue(expression.evaluateCompare(0) == true);
        assertTrue(expression.evaluateCompare(1) == true);

        expression = new CoreOperationLessThanExpression(new CoreOperationRelationalExpression[]{new CoreConstantExpression(1.0), new CoreConstantExpression(2.0)});
        assertTrue(expression.evaluateCompare(-1) == true);
        assertTrue(expression.evaluateCompare(0) == false);
        assertTrue(expression.evaluateCompare(1) == false);

        expression = new CoreOperationLessThanOrEqualExpression(new CoreOperationRelationalExpression[]{new CoreConstantExpression(1.0), new CoreConstantExpression(2.0)});
        assertTrue(expression.evaluateCompare(-1) == true);
        assertTrue(expression.evaluateCompare(0) == true);
        assertTrue(expression.evaluateCompare(1) == true);
    }

    @Test
    public void testCompute() {
        Expression leftExp = new CoreNumberValueExpression(1.0);
        Expression rightExp = new CoreNumberValueExpression(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreOperationRelationalExpression[]{leftExp, rightExp});

        Object left = new InitialContext();
        Object right = new InitialContext();
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new SelfContext();
        right = new SelfContext();
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreListValueExpression(Arrays.asList(1.0, 2.0));
        right = new CoreListValueExpression(Arrays.asList(1.0, 3.0));
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreListValueExpression(Arrays.asList(1.0, 2.0));
        right = new CoreListValueExpression(Collections.singletonList(3.0));
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreListValueExpression(Collections.singletonList(1.0));
        right = new CoreListValueExpression(Arrays.asList(2.0, 3.0));
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreListValueExpression(Collections.singletonList(1.0));
        right = new CoreListValueExpression(Collections.singletonList(1.0));
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreListValueExpression(Arrays.asList(1.0, 2.0));
        right = new CoreNumberValueExpression(3.0);
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreNumberValueExpression(Double.NaN);
        right = new CoreNumberValueExpression(Double.NaN);
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreNumberValueExpression(Double.NaN);
        right = new CoreNumberValueExpression(1.0);
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreNumberValueExpression(1.0);
        right = new CoreNumberValueExpression(Double.NaN);
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreListValueExpression(Arrays.asList(1.0, 2.0));
        right = new CoreListValueExpression(Arrays.asList(2.0, 3.0));
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreListValueExpression(Arrays.asList(1.0, 1.0));
        right = new CoreListValueExpression(Arrays.asList(2.0, 2.0));
        assertTrue(expression.compute(left, right) instanceof Boolean);

        left = new CoreListValueExpression(Arrays.asList(1.0, 1.0));
        right = new CoreListValueExpression(Arrays.asList(1.0, 2.0));
        assertFalse(expression.compute(left, right) instanceof Boolean);

        left = new CoreListValueExpression(Arrays.asList(1.0, 1.0));
        right = new CoreListValueExpression(Arrays.asList(2.0, 1.0));
        assertTrue(expression.compute(left, right) instanceof Boolean);

        left = new CoreListValueExpression(Arrays.asList(1.0, 1.0));
        right = new CoreListValueExpression(Arrays.asList(1.0, 1.0));
        assertTrue(expression.compute(left, right) instanceof Boolean);
    }

    @Test
    public void testFindMatch() {
        Iterator left = Arrays.asList(1.0, 2.0).iterator();
        Iterator right = Arrays.asList(2.0, 3.0).iterator();
        HashSet leftSet = new HashSet();
        while (left.hasNext()) {
            leftSet.add(left.next());
        }
        assertTrue(CoreOperationRelationalExpression.findMatch(left, right));

        left = Arrays.asList(1.0, 2.0).iterator();
        right = Arrays.asList(1.0, 3.0).iterator();
        HashSet leftSet2 = new HashSet();
        while (left.hasNext()) {
            leftSet2.add(left.next());
        }
        assertFalse(CoreOperationRelationalExpression.findMatch(left, right));

        left = Arrays.asList(1.0, 2.0).iterator();
        right = Arrays.asList(1.0, 1.0).iterator();
        HashSet leftSet3 = new HashSet();
        while (left.hasNext()) {
            leftSet3.add(left.next());
        }
        assertTrue(CoreOperationRelationalExpression.findMatch(left, right));

        left = Arrays.asList(1.0, 1.0).iterator();
        right = Arrays.asList(1.0, 2.0).iterator();
        HashSet leftSet4 = new HashSet();
        while (left.hasNext()) {
            leftSet4.add(left.next());
        }
        assertTrue(CoreOperationRelationalExpression.findMatch(left, right));

        left = new CoreListValueExpression(Arrays.asList(1.0, 2.0)).iterator();
        right = Arrays.asList(2.0, 3.0).iterator();
        assertFalse(CoreOperationRelationalExpression.findMatch(left, right));

        left = new CoreListValueExpression(Arrays.asList(1.0, 1.0)).iterator();
        right = Arrays.asList(2.0, 2.0).iterator();
        assertTrue(CoreOperationRelationalExpression.findMatch(left, right));

        left = new CoreListValueExpression(Arrays.asList(1.0, 1.0)).iterator();
        right = Arrays.asList(2.0, 1.0).iterator();
        assertTrue(CoreOperationRelationalExpression.findMatch(left, right));

        left = new CoreListValueExpression(Arrays.asList(1.0, 1.0)).iterator();
        right = Arrays.asList(1.0, 1.0).iterator();
        assertTrue(CoreOperationRelationalExpression.findMatch(left, right));

        left = Arrays.asList(1.0, 2.0).iterator();
        right = Collections.singletonList(3.0).iterator();
        assertFalse(CoreOperationRelationalExpression.findMatch(left, right));

        left = Collections.singletonList(1.0).iterator();
        right = Arrays.asList(2.0, 3.0).iterator();
        assertFalse(CoreOperationRelationalExpression.findMatch(left, right));

        left = Collections.singletonList(1.0).iterator();
        right = Collections.singletonList(1.0).iterator();
        assertTrue(CoreOperationRelationalExpression.findMatch(left, right));

        left = Collections.singletonList(1.0).iterator();
        right = Collections.singletonList(2.0).iterator();
        assertFalse(CoreOperationRelationalExpression.findMatch(left, right));
    }

    @Test
    public void testContainsMatch() {
        Iterator it = Arrays.asList(1.0, 2.0).iterator();
        assertTrue(CoreOperationRelationalExpression.containsMatch(it, 1.0));
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 3.0));

        it = Arrays.asList(1.0, 1.0).iterator();
        assertTrue(CoreOperationRelationalExpression.containsMatch(it, 1.0));
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 2.0));

        it = Arrays.asList(1.0, 2.0).iterator();
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 3.0));
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 1.0));

        it = new CoreListValueExpression(Arrays.asList(1.0, 2.0)).iterator();
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 3.0));
        assertTrue(CoreOperationRelationalExpression.containsMatch(it, 1.0));

        it = new CoreListValueExpression(Arrays.asList(1.0, 1.0)).iterator();
        assertTrue(CoreOperationRelationalExpression.containsMatch(it, 1.0));
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 2.0));

        it = new CoreListValueExpression(Arrays.asList(1.0, 1.0)).iterator();
        assertTrue(CoreOperationRelationalExpression.containsMatch(it, 1.0));
        assertTrue(CoreOperationRelationalExpression.containsMatch(it, 1.0));

        it = new CoreListValueExpression(Arrays.asList(1.0, 1.0)).iterator();
        assertTrue(CoreOperationRelationalExpression.containsMatch(it, 1.0));
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 2.0));

        it = Arrays.asList(1.0, 2.0).iterator();
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 3.0));
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 1.0));

        it = Collections.singletonList(1.0).iterator();
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 2.0));
        assertFalse(CoreOperationRelationalExpression.containsMatch(it, 1.0));
    }

    @Test
    public void testReduce() {
        Object o = new InitialContext();
        Object reduced = CoreOperationRelationalExpression.reduce(o);
        assertTrue(reduced instanceof EvalContext);

        o = new SelfContext();
        reduced = CoreOperationRelationalExpression.reduce(o);
        assertTrue(reduced instanceof SelfContext);

        o = new CoreListValueExpression(Arrays.asList(1.0, 2.0));
        reduced = CoreOperationRelationalExpression.reduce(o);
        assertTrue(reduced instanceof Iterator);

        o = new CoreListValueExpression(Collections.singletonList(1.0));
        reduced = CoreOperationRelationalExpression.reduce(o);
        assertTrue(reduced instanceof Object);
        assertTrue(reduced instanceof Number);
        assertEquals(1.0, InfoSetUtil.doubleValue(reduced));

        o = new CoreNumberValueExpression(1.0);
        reduced = CoreOperationRelationalExpression.reduce(o);
        assertTrue(reduced instanceof Number);
        assertEquals(1.0, InfoSetUtil.doubleValue(reduced));
    }
}