package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

/**
 * JUnit test cases for {@link CoreOperationRelationalExpression} class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = new Integer(1);
        Object right = new Integer(2);
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new NumberExpression(left), new NumberExpression(right));
        assertEquals(Boolean.FALSE, expr.computeValue(new EvalContext()));

        left = new Integer(3);
        right = new Integer(2);
        expr = new GreaterThanOrEqualExpression(new NumberExpression(left), new NumberExpression(right));
        assertTrue(expr.computeValue(new EvalContext()));

        left = new Integer(2);
        right = new Integer(3);
        expr = new LessThanExpression(new NumberExpression(left), new NumberExpression(right));
        assertFalse(expr.computeValue(new EvalContext()));

        left = new Integer(2);
        right = new Integer(2);
        expr = new LessThanOrEqualExpression(new NumberExpression(left), new NumberExpression(right));
        assertTrue(expr.computeValue(new EvalContext()));

        left = new Integer(1);
        right = new Integer(1);
        expr = new EqualsExpression(new NumberExpression(left), new NumberExpression(right));
        assertTrue(expr.computeValue(new EvalContext()));

        left = new Integer(1);
        right = new Integer(2);
        expr = new NotEqualsExpression(new NumberExpression(left), new NumberExpression(right));
        assertFalse(expr.computeValue(new EvalContext()));
    }

    @Test
    public void testFindMatch() {
        Iterator it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        Object value = new Integer(2);

        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).findMatch(it, value).booleanValue());

        it = Collections.singletonList(new Integer(3)).iterator();
        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).findMatch(it, value).booleanValue());

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(3);

        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).findMatch(it, value).booleanValue());

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(1);

        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN_OR_EQUAL).findMatch(it, value).booleanValue());

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(2);

        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN_OR_EQUAL).findMatch(it, value).booleanValue());

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(4);

        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN_OR_EQUAL).findMatch(it, value).booleanValue());
    }

    @Test
    public void testContainsMatch() {
        Iterator it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        Object value = new Integer(2);

        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).containsMatch(it, value));

        it = Collections.singletonList(new Integer(3)).iterator();
        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).containsMatch(it, value));

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(1);

        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).containsMatch(it, value));

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(2);

        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN_OR_EQUAL).containsMatch(it, value));

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(3);

        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN_OR_EQUAL).containsMatch(it, value));

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(1);

        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN).containsMatch(it, value));

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(2);

        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN).containsMatch(it, value));

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(1);

        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN_OR_EQUAL).containsMatch(it, value));

        it = Arrays.asList(new Integer[] { 1, 2, 3 }).iterator();
        value = new Integer(2);

        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN_OR_EQUAL).containsMatch(it, value));
    }

    @Test
    public void testEvaluateCompare() {
        assertEquals(Boolean.FALSE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).evaluateCompare(-1));
        assertEquals(Boolean.FALSE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).evaluateCompare(0));
        assertEquals(Boolean.TRUE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).evaluateCompare(1));

        assertEquals(Boolean.FALSE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN_OR_EQUAL).evaluateCompare(-1));
        assertEquals(Boolean.TRUE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN_OR_EQUAL).evaluateCompare(0));
        assertEquals(Boolean.TRUE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN_OR_EQUAL).evaluateCompare(1));

        assertEquals(Boolean.FALSE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN).evaluateCompare(-1));
        assertEquals(Boolean.FALSE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN).evaluateCompare(0));
        assertEquals(Boolean.FALSE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN).evaluateCompare(1));

        assertEquals(Boolean.TRUE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN_OR_EQUAL).evaluateCompare(-1));
        assertEquals(Boolean.FALSE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN_OR_EQUAL).evaluateCompare(0));
        assertEquals(Boolean.TRUE, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN_OR_EQUAL).evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        Object left = new Integer(1);
        Object right = new Integer(2);

        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).compute(left, right).booleanValue());
        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN_OR_EQUAL).compute(left, right).booleanValue());

        left = new Integer(2);
        right = new Integer(1);

        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).compute(left, right).booleanValue());
        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN).compute(left, right).booleanValue());

        left = new Integer(1);
        right = new Integer(1);

        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).compute(left, right).booleanValue());
        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.LESS_THAN_OR_EQUAL).compute(left, right).booleanValue());

        left = new Integer(2);
        right = new Integer(2);

        assertTrue(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.EQUALS).compute(left, right).booleanValue());
        assertFalse(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.NOT_EQUALS).compute(left, right).booleanValue());
    }

    @Test
    public void testReduce() {
        Object left = new InitialContext();
        assertEquals(left, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).reduce(left));

        left = new HashSet<>();
        assertEquals(left, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).reduce(left));

        left = new Iterator() {};
        assertEquals(left, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).reduce(left));

        left = new SelfContext();
        assertEquals(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.SELF).computeValue(new EvalContext()), CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).reduce(left));

        left = new Integer(1);
        assertEquals(left, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).reduce(left));

        left = new Double(1.0);
        assertEquals(left, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).reduce(left));

        left = new Boolean(true);
        assertEquals(left, CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).reduce(left));

        left = "foo";
        assertEquals("foo", CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).reduce(left));

        left = null;
        assertNull(CoreOperationRelationalExpression.getInstance(CoreOperationRelationalExpression.GREATER_THAN).reduce(left));
    }

}