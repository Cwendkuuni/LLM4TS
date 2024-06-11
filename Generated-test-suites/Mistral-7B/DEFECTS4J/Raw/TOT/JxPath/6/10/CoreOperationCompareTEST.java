package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = new ConstantExpression("1");
        Expression right = new ConstantExpression("1");
        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvaluationContext context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("2");
        right = new ConstantExpression("1");
        coreOperationCompare = new EqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("'abc'");
        right = new ConstantExpression("'abc'");
        coreOperationCompare = new EqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("'abc'");
        right = new ConstantExpression("'def'");
        coreOperationCompare = new EqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(new Integer(1));
        right = new ConstantExpression(new Integer(1));
        coreOperationCompare = new EqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(new Integer(1));
        right = new ConstantExpression(new Integer(2));
        coreOperationCompare = new EqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(new Float(1.0));
        right = new ConstantExpression(new Float(1.0));
        coreOperationCompare = new EqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(new Float(1.0));
        right = new ConstantExpression(new Float(2.0));
        coreOperationCompare = new EqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("null");
        right = new ConstantExpression("null");
        coreOperationCompare = new EqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("null");
        right = new ConstantExpression(new Integer(0));
        coreOperationCompare = new EqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(new Integer(0));
        right = new ConstantExpression("null");
        coreOperationCompare = new EqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqual() {
        Expression left = new ConstantExpression("1");
        Expression right = new ConstantExpression("1");
        CoreOperationCompare coreOperationCompare = new NotEqualExpression(left, right);
        EvaluationContext context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("1");
        right = new ConstantExpression("2");
        coreOperationCompare = new NotEqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("'abc'");
        right = new ConstantExpression("'abc'");
        coreOperationCompare = new NotEqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("'abc'");
        right = new ConstantExpression("'def'");
        coreOperationCompare = new NotEqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(new Integer(1));
        right = new ConstantExpression(new Integer(1));
        coreOperationCompare = new NotEqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(new Integer(1));
        right = new ConstantExpression(new Integer(2));
        coreOperationCompare = new NotEqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(new Float(1.0));
        right = new ConstantExpression(new Float(1.0));
        coreOperationCompare = new NotEqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(new Float(1.0));
        right = new ConstantExpression(new Float(2.0));
        coreOperationCompare = new NotEqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("null");
        right = new ConstantExpression("null");
        coreOperationCompare = new NotEqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("null");
        right = new ConstantExpression(new Integer(0));
        coreOperationCompare = new NotEqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(new Integer(0));
        right = new ConstantExpression("null");
        coreOperationCompare = new NotEqualExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testContains() {
        Expression left = new ConstantExpression(Arrays.asList(1, 2, 3));
        Expression right = new ConstantExpression(new Integer(2));
        CoreOperationCompare coreOperationCompare = new ContainsExpression(left, right);
        EvaluationContext context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(Arrays.asList(1, 2, 3));
        right = new ConstantExpression(new Integer(4));
        coreOperationCompare = new ContainsExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(Arrays.asList("abc", "def", "ghi"));
        right = new ConstantExpression("def");
        coreOperationCompare = new ContainsExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(Arrays.asList("abc", "def", "ghi"));
        right = new ConstantExpression("xyz");
        coreOperationCompare = new ContainsExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("1,2,3");
        right = new ConstantExpression(new Integer(2));
        coreOperationCompare = new ContainsExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("1,2,3");
        right = new ConstantExpression(new Integer(4));
        coreOperationCompare = new ContainsExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("abc,def,ghi");
        right = new ConstantExpression("def");
        coreOperationCompare = new ContainsExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression("abc,def,ghi");
        right = new ConstantExpression("xyz");
        coreOperationCompare = new ContainsExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testFindMatch() {
        Expression left = new ConstantExpression(Arrays.asList(1, 2, 3, 4));
        Expression right = new ConstantExpression(Arrays.asList(2, 3, 4));
        CoreOperationCompare coreOperationCompare = new FindMatchExpression(left, right);
        EvaluationContext context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(Arrays.asList(1, 2, 3, 4));
        right = new ConstantExpression(Arrays.asList(3, 4, 5));
        coreOperationCompare = new FindMatchExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(Arrays.asList("abc", "def", "ghi", "jkl"));
        right = new ConstantExpression(Arrays.asList("def", "ghi"));
        coreOperationCompare = new FindMatchExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(Arrays.asList("abc", "def", "ghi", "jkl"));
        right = new ConstantExpression(Arrays.asList("xyz", "ghi"));
        coreOperationCompare = new FindMatchExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(Arrays.asList(1, 2, 3));
        right = new ConstantExpression(Arrays.asList(1, 2, 3, 4));
        coreOperationCompare = new FindMatchExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(Arrays.asList(1, 2, 3));
        right = new ConstantExpression(Arrays.asList(3, 4, 5));
        coreOperationCompare = new FindMatchExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(Arrays.asList("abc", "def", "ghi"));
        right = new ConstantExpression(Arrays.asList("def", "xyz"));
        coreOperationCompare = new FindMatchExpression(left, right);
        context = new EvaluationContext(new InitialContext());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testErrorScenario1() {
        Expression left = new ConstantExpression(null);
        Expression right = new ConstantExpression("1");
        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvaluationContext context = new EvaluationContext(new InitialContext());
        assertThrows(NullPointerException.class, () -> {
            coreOperationCompare.evaluateToBoolean(context);
        });
    }

    @Test
    public void testErrorScenario2() {
        Expression left = new ConstantExpression("1");
        Expression right = new ConstantExpression(null);
        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvaluationContext context = new EvaluationContext(new InitialContext());
        assertThrows(NullPointerException.class, () -> {
            coreOperationCompare.evaluateToBoolean(context);
        });
    }
}