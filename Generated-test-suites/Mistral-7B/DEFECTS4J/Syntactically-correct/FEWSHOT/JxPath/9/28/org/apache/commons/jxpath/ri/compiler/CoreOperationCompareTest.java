package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {
    @Test
    public void testEquals() {
        Integer left = 5;
        Integer right = 5;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertTrue(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testNotEquals() {
        Integer left = 5;
        Integer right = 6;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertFalse(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testEqualsWithNull() {
        String left = null;
        String right = null;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertTrue(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testNotEqualsWithNull() {
        String left = null;
        String right = "foo";
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertFalse(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testEqualsWithBool() {
        boolean left = true;
        boolean right = true;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertTrue(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testNotEqualsWithBool() {
        boolean left = true;
        boolean right = false;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertFalse(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testEqualsWithNumber() {
        double left = 5.0;
        double right = 5.0;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertTrue(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testNotEqualsWithNumber() {
        double left = 5.0;
        double right = 6.0;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertFalse(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testEqualsWithString() {
        String left = "foo";
        String right = "foo";
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertTrue(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testNotEqualsWithString() {
        String left = "foo";
        String right = "bar";
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertFalse(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testEqualsWithCollections() {
        List<Integer> left = Arrays.asList(1, 2, 3);
        List<Integer> right = Arrays.asList(1, 2, 3);
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertTrue(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testNotEqualsWithCollections() {
        List<Integer> left = Arrays.asList(1, 2, 3);
        List<Integer> right = Arrays.asList(1, 2, 4);
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertFalse(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testEqualsWithIterators() {
        Iterator<Integer> left = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> right = Arrays.asList(1, 2, 3).iterator();
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertTrue(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testNotEqualsWithIterators() {
        Iterator<Integer> left = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> right = Arrays.asList(1, 2, 4).iterator();
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertFalse(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testEqualsWithPointer() {
        Pointer left = new Pointer(new Object(), new Object());
        Pointer right = new Pointer(new Object(), new Object());
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertTrue(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testNotEqualsWithPointer() {
        Pointer left = new Pointer(new Object(), new Object());
        Pointer right = new Pointer(new Object(), new Object());
        EvalContext context = new EvalContext(new Object[] {});

        // Change the values of the pointers to be different
        left.setValue(new Object() {});
        right.setValue(new Object() {});

        CoreOperationCompare operation = new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right));
        assertFalse(operation.equal(context, operation.getArguments()[0], operation.getArguments()[1]));
    }

    @Test
    public void testFindMatch() {
        Integer[] left = {1, 2, 3};
        Integer[] right = {2, 3, 4};
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ArrayExpression(new ConstantExpression(left), new AxisName(".")), new ConstantExpression(right));
        assertTrue(operation.findMatch(context.getIterator(new Object[] {left}), context.getIterator(new Object[] {right})));
    }

    @Test
    public void testFindMatchNull() {
        Integer[] left = null;
        Integer[] right = {1, 2, 3};
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ArrayExpression(new ConstantExpression(left), new AxisName(".")), new ConstantExpression(right));
        assertFalse(operation.findMatch(context.getIterator(new Object[] {left}), context.getIterator(new Object[] {right})));
    }

    @Test
    public void testFindMatchEmpty() {
        Integer[] left = {};
        Integer[] right = {1, 2, 3};
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ArrayExpression(new ConstantExpression(left), new AxisName(".")), new ConstantExpression(right));
        assertFalse(operation.findMatch(context.getIterator(new Object[] {left}), context.getIterator(new Object[] {right})));
    }

    @Test
    public void testFindMatchSelf() {
        Integer[] left = {1, 2, 3};
        Integer[] right = {1, 2, 3};
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ArrayExpression(new ConstantExpression(left), new AxisName(".")), new ArrayExpression(new SelfContext(), new AxisName(".")));
        assertTrue(operation.findMatch(context.getIterator(new Object[] {left}), context.getIterator(new Object[] {right})));
    }

    @Test
    public void testContains() {
        Integer[] array = {1, 2, 3};
        Integer value = 2;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ArrayExpression(new ConstantExpression(array), new AxisName(".")), new ConstantExpression(value));
        assertTrue(operation.contains(context.getIterator(new Object[] {array}), value));
    }

    @Test
    public void testContainsNull() {
        Integer[] array = null;
        Integer value = 2;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ArrayExpression(new ConstantExpression(array), new AxisName(".")), new ConstantExpression(value));
        assertFalse(operation.contains(context.getIterator(new Object[] {array}), value));
    }

    @Test
    public void testContainsEmpty() {
        Integer[] array = {};
        Integer value = 2;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ArrayExpression(new ConstantExpression(array), new AxisName(".")), new ConstantExpression(value));
        assertFalse(operation.contains(context.getIterator(new Object[] {array}), value));
    }

    @Test
    public void testContainsSelf() {
        Integer[] array = {1, 2, 3};
        Integer value = 2;
        EvalContext context = new EvalContext(new Object[] {});

        CoreOperationCompare operation = new CoreOperationCompare(new ArrayExpression(new ConstantExpression(array), new AxisName(".")), new ConstantExpression(value));
        SelfContext selfContext = new SelfContext();
        Pointer pointer = new Pointer(value, selfContext);

        assertTrue(operation.contains(context.getIterator(new Object[] {array}), pointer));
    }
}