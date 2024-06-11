package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for CoreOperationCompare
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = new LiteralExpression("1");
        Expression right = new LiteralExpression("1");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqual() {
        Expression left = new LiteralExpression("1");
        Expression right = new LiteralExpression("2");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualBoolean() {
        Expression left = new LiteralExpression(Boolean.TRUE);
        Expression right = new LiteralExpression(Boolean.TRUE);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualBoolean() {
        Expression left = new LiteralExpression(Boolean.TRUE);
        Expression right = new LiteralExpression(Boolean.FALSE);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualString() {
        Expression left = new LiteralExpression("abc");
        Expression right = new LiteralExpression("abc");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualString() {
        Expression left = new LiteralExpression("abc");
        Expression right = new LiteralExpression("def");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualNumber() {
        Expression left = new LiteralExpression(1.0);
        Expression right = new LiteralExpression(1.0);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualNumber() {
        Expression left = new LiteralExpression(1.0);
        Expression right = new LiteralExpression(2.0);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualCollectionSameElements() {
        Expression left = new AxisExpression("/list[1]/a", new EvalContext());
        Expression right = new AxisExpression("/list[1]/a", new EvalContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualCollectionDifferentOrder() {
        Expression left = new AxisExpression("/list[1]/a", new EvalContext());
        Expression right = new AxisExpression("/list[1]/b[1]", new EvalContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualCollectionSameElementsNoOrder() {
        Expression left = new AxisExpression("/list[1]/a", new EvalContext());
        Expression right = new AxisExpression("/list[1]/b", new EvalContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualCollectionDifferentElements() {
        Expression left = new AxisExpression("/list[1]/a", new EvalContext());
        Expression right = new AxisExpression("/list[1]/b", new EvalContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualCollectionEmptyAndEmpty() {
        Expression left = new AxisExpression("/list[1]", new EvalContext());
        Expression right = new AxisExpression("/list[1]", new EvalContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualCollectionEmptyAndNonEmpty() {
        Expression left = new AxisExpression("/list[1]", new EvalContext());
        Expression right = new AxisExpression("/list[1]/a", new EvalContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualCollectionNonEmptyAndEmpty() {
        Expression left = new AxisExpression("/list[1]/a", new EvalContext());
        Expression right = new AxisExpression("/list[1]", new EvalContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualPointer() {
        Expression left = new AxisExpression("/a[1]", new EvalContext());
        Expression right = new AxisExpression("/a[1]", new EvalContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualPointer() {
        Expression left = new AxisExpression("/a[1]", new EvalContext());
        Expression right = new AxisExpression("/b[1]", new EvalContext());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualPointerSameValue() {
        Expression left = new AxisExpression("/a[1]", new EvalContext());
        Expression right = new LiteralExpression("value");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        Pointer pointer = (Pointer) left.compute(context);
        Object value = pointer.getValue();

        right = new LiteralExpression(value);

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualPointerDifferentValues() {
        Expression left = new AxisExpression("/a[1]", new EvalContext());
        Expression right = new LiteralExpression("otherValue");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualInitialContext() {
        Expression left = new InitialContext();
        Expression right = new InitialContext();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualInitialContext() {
        Expression left = new InitialContext();
        Expression right = new SelfContext();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqualSelfContext() {
        Expression left = new SelfContext();
        Expression right = new InitialContext();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }
}