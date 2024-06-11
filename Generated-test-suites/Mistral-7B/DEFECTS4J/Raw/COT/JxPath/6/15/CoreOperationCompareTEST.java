package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression(new ConstantExpression(1));
        Expression arg2 = new Expression(new ConstantExpression(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNull(coreOperationCompare.getNext());
        assertEquals(2, coreOperationCompare.getOperands().length);
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression left = new VariableExpression("var1");
        Expression right = new ConstantExpression(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluateToBoolean(context));

        left = new VariableExpression("var1");
        right = new VariableExpression("var1");
        coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(1);
        right = new ConstantExpression(1);
        coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        left = new ConstantExpression(1);
        right = new ConstantExpression(2);
        coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqual_Collection_Null() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression left = new VariableExpression("var1");
        Expression right = new ConstantExpression(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqual_Collection_Empty() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression left = new VariableExpression("var1");
        Expression right = new ConstantExpression(new ArrayList<>());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqual_Collection_SameElements() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression left = new VariableExpression("var1");
        Expression right = new ConstantExpression(new ArrayList<>());
        ((ArrayList) right).add(new Integer(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqual_Collection_DifferentElements() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression left = new VariableExpression("var1");
        Expression right = new ConstantExpression(new ArrayList<>());
        ((ArrayList) right).add(new Integer(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqual_Collection_SameIterator() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression left = new VariableExpression("var1");
        Expression right = new VariableExpression("var2");
        ((EvalContext) left.getParent()).setVariable("var1", new ArrayList<>());
        ((EvalContext) left.getParent()).setVariable("var2", new ArrayList<>());
        ((ArrayList) ((EvalContext) left.getParent()).getVariable("var1")).add(new Integer(1));
        ((ArrayList) ((EvalContext) left.getParent()).getVariable("var2")).add(new Integer(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqual_Collection_DifferentIterators() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression left = new VariableExpression("var1");
        Expression right = new VariableExpression("var2");
        ((EvalContext) left.getParent()).setVariable("var1", new ArrayList<>());
        ((EvalContext) left.getParent()).setVariable("var2", new ArrayList<>());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        coreOperationCompare.evaluateToBoolean(context);
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression it = new VariableExpression("var1");
        Expression value = new ConstantExpression(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(it, value);

        ArrayList list = new ArrayList();
        list.add(new Integer(1));
        ((EvalContext) it.getParent()).setVariable("var1", list.iterator());

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testContains_Null() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression it = new VariableExpression("var1");
        Expression value = new ConstantExpression(null);

        Iterator iterator = new ArrayList<Object>().iterator();
        ((EvalContext) it.getParent()).setVariable("var1", iterator);

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression lit = new VariableExpression("var1");
        Expression rit = new VariableExpression("var2");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        ArrayList list1 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));
        ((EvalContext) lit.getParent()).setVariable("var1", list1.iterator());

        ArrayList list2 = new ArrayList();
        list2.add(new Integer(1));
        list2.add(new Integer(2));
        list2.add(new Integer(1));
        ((EvalContext) rit.getParent()).setVariable("var2", list2.iterator());

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testFindMatch_EmptyList() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression lit = new VariableExpression("var1");
        Expression rit = new VariableExpression("var2");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        ArrayList list1 = new ArrayList();
        ((EvalContext) lit.getParent()).setVariable("var1", list1.iterator());

        ArrayList list2 = new ArrayList();
        ((EvalContext) rit.getParent()).setVariable("var2", list2.iterator());

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testFindMatch_NullList() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression lit = new VariableExpression("var1");
        Expression rit = new VariableExpression("var2");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        ArrayList list1 = null;
        ((EvalContext) lit.getParent()).setVariable("var1", list1);

        ArrayList list2 = new ArrayList();
        ((EvalContext) rit.getParent()).setVariable("var2", list2.iterator());

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testFindMatch_SameIterator() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression lit = new VariableExpression("var1");
        Expression rit = new VariableExpression("var2");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        ArrayList list1 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));
        ((EvalContext) lit.getParent()).setVariable("var1", list1.iterator());

        ArrayList list2 = new ArrayList();
        list2.add(new Integer(1));
        list2.add(new Integer(2));
        ((EvalContext) rit.getParent()).setVariable("var2", list2.iterator());

        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testFindMatch_DifferentIterators() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression lit = new VariableExpression("var1");
        Expression rit = new VariableExpression("var2");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        ArrayList list1 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));
        ((EvalContext) lit.getParent()).setVariable("var1", list1.iterator());

        ArrayList list2 = new ArrayList();
        list2.add(new Integer(2));
        ((EvalContext) rit.getParent()).setVariable("var2", list2.iterator());

        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMatch_NullIterator() {
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}));
        Expression lit = new VariableExpression("var1");
        Expression rit = new VariableExpression("var2");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        ArrayList list1 = new ArrayList();
        ((EvalContext) lit.getParent()).setVariable("var1", list1.iterator());

        Iterator iterator = null;
        ((EvalContext) rit.getParent()).setVariable("var2", iterator);

        coreOperationCompare.evaluateToBoolean(context);
    }

}