package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testGT() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreConstantExpression(5.0)),
                new CoreNumberExpression(new CoreConstantExpression(4.0)));

        EvalContext context = new EvalContext(Arrays.asList(new Object[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }));
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testGTWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNumberExpression(new CoreVariableExpression("y")));

        EvalContext context = new EvalContext(new Object[] { new LinkedList<Object>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
                .iterator(), new LinkedList<Object>(Arrays.asList(1.0, 2.0, 3.0, 4.5, 5.0, 6.0)).iterator() });

        assertTrue(expression.computeValue(context));
    }

    @org.junit.Test
    public void testGTWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNumberExpression(new CoreVariableExpression("y")));

        EvalContext context = new EvalContext(new Object[] { new InitialContext(), new InitialContext() });
        context.setSingleNodePointer(1.0);
        context.setSingleNodePointer(2.0);

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));

        context.getSingleNodePointer(0).setValue(5.0);
        context.getSingleNodePointer(1).setValue(4.0);

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testGTE() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreConstantExpression(5.0)),
                new CoreNumberExpression(new CoreConstantExpression(5.0)));

        EvalContext context = new EvalContext(Arrays.asList(new Object[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }));
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testLTE() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreConstantExpression(4.0)),
                new CoreNumberExpression(new CoreConstantExpression(5.0)));

        EvalContext context = new EvalContext(Arrays.asList(new Object[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }));
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testLTEWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNumberExpression(new CoreVariableExpression("y")));

        EvalContext context = new EvalContext(new Object[] { new LinkedList<Object>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
                .iterator(), new LinkedList<Object>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)).iterator() });

        assertTrue(expression.computeValue(context));
    }

    @org.junit.Test
    public void testLTEWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNumberExpression(new CoreVariableExpression("y")));

        EvalContext context = new EvalContext(new Object[] { new InitialContext(), new InitialContext() });
        context.setSingleNodePointer(1.0);
        context.setSingleNodePointer(2.0);

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        context.getSingleNodePointer(0).setValue(4.0);
        context.getSingleNodePointer(1).setValue(5.0);

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testLT() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreConstantExpression(4.0)),
                new CoreNumberExpression(new CoreConstantExpression(5.0)));

        EvalContext context = new EvalContext(Arrays.asList(new Object[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }));
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testLTWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNumberExpression(new CoreVariableExpression("y")));

        EvalContext context = new EvalContext(new Object[] { new LinkedList<Object>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
                .iterator(), new LinkedList<Object>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)).iterator() });

        assertFalse(expression.computeValue(context));
    }

    @org.junit.Test
    public void testLTWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNumberExpression(new CoreVariableExpression("y")));

        EvalContext context = new EvalContext(new Object[] { new InitialContext(), new InitialContext() });
        context.setSingleNodePointer(1.0);
        context.setSingleNodePointer(2.0);

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));

        context.getSingleNodePointer(0).setValue(4.0);
        context.getSingleNodePointer(1).setValue(5.0);

        assertTrue(expression.computeValue(context).equals(Boolean.FALSE));
    }

    @org.junit.Test
    public void testEquals() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreConstantExpression(5.0)),
                new CoreNumberExpression(new CoreConstantExpression(5.0)));

        EvalContext context = new EvalContext(Arrays.asList(new Object[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }));
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testEqualsWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNumberExpression(new CoreVariableExpression("y")));

        EvalContext context = new EvalContext(new Object[] { new LinkedList<Object>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
                .iterator(), new LinkedList<Object>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)).iterator() });

        Iterator<Object> iterator1 = context.getSingleNodePointer(0);
        Iterator<Object> iterator2 = context.getSingleNodePointer(1);

        Object next1 = iterator1.next();
        Object next2 = iterator2.next();

        assertTrue(expression.computeValue(context));

        iterator1.next();
        iterator2.next();

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));

        while (iterator1.hasNext()) {
            next1 = iterator1.next();
            next2 = iterator2.next();

            assertTrue(expression.computeValue(context));
        }

        while (iterator2.hasNext()) {
            next2 = iterator2.next();
            assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
        }
    }

    @org.junit.Test
    public void testEqualsWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNumberExpression(new CoreVariableExpression("y")));

        EvalContext context = new EvalContext(new Object[] { new InitialContext(), new InitialContext() });

        context.setSingleNodePointer(0, 5.0);
        context.setSingleNodePointer(1, 5.0);

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        context.getSingleNodePointer(0).setValue(7.0);
        context.getSingleNodePointer(1).setValue(5.0);

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testNotEquals() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreConstantExpression(5.0)),
                new CoreNumberExpression(new CoreConstantExpression(6.0)));

        EvalContext context = new EvalContext(Arrays.asList(new Object[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }));
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testNotEqualsWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNumberExpression(new CoreVariableExpression("y")));

        EvalContext context = new EvalContext(new Object[] { new LinkedList<Object>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
                .iterator(), new LinkedList<Object>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 7.0)).iterator() });

        Iterator<Object> iterator1 = context.getSingleNodePointer(0);
        Iterator<Object> iterator2 = context.getSingleNodePointer(1);

        Object next1 = iterator1.next();
        Object next2 = iterator2.next();

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));

        while (iterator1.hasNext()) {
            next1 = iterator1.next();
            next2 = iterator2.next();

            assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
        }

        while (iterator2.hasNext()) {
            next2 = iterator2.next();

            assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
        }
    }

    @org.junit.Test
    public void testNotEqualsWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNumberExpression(new CoreVariableExpression("y")));

        EvalContext context = new EvalContext(new Object[] { new InitialContext(), new InitialContext() });

        context.setSingleNodePointer(0, 5.0);
        context.setSingleNodePointer(1, 6.0);

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testNull() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreConstantExpression(5.0)),
                new CoreNullExpression());

        EvalContext context = new EvalContext(Collections.emptyList());

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @org.junit.Test
    public void testNullWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNullExpression());

        EvalContext context = new EvalContext(new Object[] { new LinkedList<Object>(Arrays.asList(new SelfContext())).iterator() });

        assertFalse(expression.computeValue(context));
    }

    @org.junit.Test
    public void testNullWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(new CoreVariableExpression("x")),
                new CoreNullExpression());

        EvalContext context = new EvalContext(new Object[] { new InitialContext(), new InitialContext() });

        context.setSingleNodePointer(0, new SelfContext());
        context.setSingleNodePointer(1, null);

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

}