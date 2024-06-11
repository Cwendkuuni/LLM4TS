package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual_PrimitiveTypes() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{1, 2, 3}));
        CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression(1), new ConstantExpression(1));
        assertTrue(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{1, 2, 3}));
        compare = new CoreOperationCompare(new ConstantExpression(1), new ConstantExpression(2));
        assertFalse(compare.evaluate(context).getBooleanValue());
    }

    @Test
    public void testEqual_Objects() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{new Integer(1), new Integer(1)}));
        CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(1)));
        assertTrue(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{new Integer(1), new Integer(2)}));
        compare = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new Integer(2)));
        assertFalse(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{new String("foo"), new String("foo")}));
        compare = new CoreOperationCompare(new ConstantExpression(new String("foo")), new ConstantExpression(new String("foo")));
        assertTrue(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{new String("foo"), new String("bar")}));
        compare = new CoreOperationCompare(new ConstantExpression(new String("foo")), new ConstantExpression(new String("bar")));
        assertFalse(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{new Pointer(new Object[]{1, 1}), new Pointer(new Object[]{1, 1})}));
        compare = new CoreOperationCompare(new ConstantExpression(new Pointer(new Object[]{1, 1})), new ConstantExpression(new Pointer(new Object[]{1, 1})));
        assertTrue(compare.evaluate(context).getBooleanValue());
    }

    @Test
    public void testEqual_Collections() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{List.of(1, 2, 3), List.of(1, 2, 3)}));
        CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression(List.of(1, 2, 3)), new ConstantExpression(List.of(1, 2, 3)));
        assertTrue(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{List.of(1, 2, 3), List.of(1, 2, 4)}));
        compare = new CoreOperationCompare(new ConstantExpression(List.of(1, 2, 3)), new ConstantExpression(List.of(1, 2, 4)));
        assertFalse(compare.evaluate(context).getBooleanValue());
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{List.of(1, 2, 3), 3}));
        CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression(List.of(1, 2, 3).iterator()), new ConstantExpression(3));
        assertTrue(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{List.of(1, 2, 3), 4}));
        compare = new CoreOperationCompare(new ConstantExpression(List.of(1, 2, 3).iterator()), new ConstantExpression(4));
        assertFalse(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{List.of(1, 2, 3), List.of(3, 2, 1)}));
        compare = new CoreOperationCompare(new ConstantExpression(List.of(1, 2, 3).iterator()), new ConstantExpression(List.of(3, 2, 1).iterator()));
        assertFalse(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{List.of(1, 2, 3), List.of(3)}));
        compare = new CoreOperationCompare(new ConstantExpression(List.of(1, 2, 3).iterator()), new ConstantExpression(List.of(3).iterator()));
        assertTrue(compare.evaluate(context).getBooleanValue());
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{List.of(1, 2, 3), List.of(3, 2, 1)}));
        CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression(List.of(1, 2, 3).iterator()), new ConstantExpression(List.of(3, 2, 1).iterator()));
        assertFalse(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{List.of(1, 2, 3), List.of(1, 3, 2)}));
        compare = new CoreOperationCompare(new ConstantExpression(List.of(1, 2, 3).iterator()), new ConstantExpression(List.of(1, 3, 2).iterator()));
        assertTrue(compare.evaluate(context).getBooleanValue());
    }

    @Test
    public void testEqual_NullPointer() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{null, null}));
        CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression(null), new ConstantExpression(null));
        assertTrue(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{null, new Integer(1)}));
        compare = new CoreOperationCompare(new ConstantExpression(null), new ConstantExpression(new Integer(1)));
        assertFalse(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{new Integer(1), null}));
        compare = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(null));
        assertFalse(compare.evaluate(context).getBooleanValue());
    }

    @Test
    public void testEqual_MixedTypes() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{new Integer(1), new String("1")}));
        CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(new String("1")));
        assertTrue(compare.evaluate(context).getBooleanValue());

        context = new EvalContext(new InitialContext(new Object[]{new String("1"), new Integer(1)}));
        compare = new CoreOperationCompare(new ConstantExpression(new String("1")), new ConstantExpression(new Integer(1)));
        assertFalse(compare.evaluate(context).getBooleanValue());
    }
}