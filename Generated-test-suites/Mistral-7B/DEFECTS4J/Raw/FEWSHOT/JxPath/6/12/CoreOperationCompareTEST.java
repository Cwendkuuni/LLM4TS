package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare_Boolean_equal() {
        EvalContext context = new EvalContext(new Object[] { true, true });
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(true),
            new ConstantExpression(true)
        );
        assertTrue(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_Boolean_notEqual() {
        EvalContext context = new EvalContext(new Object[] { true, false });
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(true),
            new ConstantExpression(false)
        );
        assertFalse(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_Number_equal() {
        EvalContext context = new EvalContext(new Object[] { 1.0, 1.0 });
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(1.0),
            new ConstantExpression(1.0)
        );
        assertTrue(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_Number_notEqual() {
        EvalContext context = new EvalContext(new Object[] { 1.0, 2.0 });
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(1.0),
            new ConstantExpression(2.0)
        );
        assertFalse(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_String_equal() {
        EvalContext context = new EvalContext(new Object[] {"foo", "foo"});
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression("foo"),
            new ConstantExpression("foo")
        );
        assertTrue(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_String_notEqual() {
        EvalContext context = new EvalContext(new Object[]{"foo", "bar"});
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression("foo"),
            new ConstantExpression("bar")
        );
        assertFalse(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_Pointer_equal() {
        EvalContext context = new EvalContext(new Object[] {
            new Pointer("foo"), new Pointer("foo")
        });
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Pointer("foo")),
            new ConstantExpression(new Pointer("foo"))
        );
        assertTrue(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_Pointer_notEqual() {
        EvalContext context = new EvalContext(new Object[] {
            new Pointer("foo"), new Pointer("bar")
        });
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Pointer("foo")),
            new ConstantExpression(new Pointer("bar"))
        );
        assertFalse(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_Collection_contains() {
        EvalContext context = new EvalContext(new Object[] {
            new ArrayList<>(Collections.singletonList(1)), new Integer(1)
        });
        CoreOperationCompare op = new CoreOperationCompare(
            new VariableExpression("$a"), new ConstantExpression(1)
        );
        assertTrue(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_Collection_notContains() {
        EvalContext context = new EvalContext(new Object[] {
            new ArrayList<>(Collections.singletonList(1)), new Integer(2)
        });
        CoreOperationCompare op = new CoreOperationCompare(
            new VariableExpression("$a"), new ConstantExpression(2)
        );
        assertFalse(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_Collection_containsAll() {
        EvalContext context = new EvalContext(new Object[] {
            new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(1)))),
            new ArrayList<>(Collections.singletonList(1))
        });
        CoreOperationCompare op = new CoreOperationCompare(
            new VariableExpression("$a"), new VariableExpression("$b")
        );
        assertTrue(op.booleanValue(context));
    }

    @Test
    public void testCoreOperationCompare_Collection_notContainsAll() {
        EvalContext context = new EvalContext(new Object[] {
            new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(1)))),
            new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(2))))
        });
        CoreOperationCompare op = new CoreOperationCompare(
            new VariableExpression("$a"), new VariableExpression("$b")
        );
        assertFalse(op.booleanValue(context));
    }

}