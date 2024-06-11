package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testExpressionIsContextDependent() {
        AbstractExpression expression = new ConstantExpression(new Integer(1));
        assertTrue(expression.isContextDependent());

        expression = new VariableExpression("testVar");
        assertTrue(expression.isContextDependent());

        expression = new IndexExpression(new VariableExpression("testList"), new IntExpression(0));
        assertTrue(expression.isContextDependent());

        expression = new ArithmeticExpression(new AdditionExpression(new IntExpression(1), new IntExpression(1)), new IntExpression(2));
        assertFalse(expression.isContextDependent());

        expression = new AndExpression(new BooleanExpression(new EqualsExpression(new VariableExpression("x"), new IntExpression(1))),
                new BooleanExpression(new EqualsExpression(new VariableExpression("y"), new IntExpression(2))));
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testExpressionComputeValue() {
        Expression expression = new ConstantExpression(new Integer(1));
        assertEquals(1, ((Number) expression.computeValue(new EvalContext())).intValue());

        expression = new VariableExpression("testVar");
        assertNull(expression.computeValue(new EvalContext()));

        expression = new IndexExpression(new VariableExpression("testList"), new IntExpression(0));
        NodePointer nodePointer = new NodePointer(new QName("testNode"), new Object(), Locale.ENGLISH);
        EvalContext context = new EvalContext(nodePointer, null);
        Expression testList = new ConstantExpression(new NodePointer[]{nodePointer});
        assertEquals(nodePointer, ((NodePointer) expression.computeValue(context)).getFirstPointer());

        expression = new ArithmeticExpression(new AdditionExpression(new IntExpression(1), new IntExpression(1)), new IntExpression(2));
        assertEquals(4, ((Number) expression.computeValue(new EvalContext())).intValue());

        expression = new AndExpression(new BooleanExpression(new EqualsExpression(new VariableExpression("x"), new IntExpression(1))),
                new BooleanExpression(new EqualsExpression(new VariableExpression("y"), new IntExpression(2))));
        EvalContext contextWithVariables = new EvalContext(new NodePointer[]{nodePointer}, new Object[]{1, 2});
        assertTrue((boolean) expression.computeValue(contextWithVariables));
    }

    @Test
    public void testExpressionIterate() {
        Expression expression = new ConstantExpression(new Integer(1));
        Iterator iterator = expression.iterate(new EvalContext());
        assertFalse(iterator.hasNext());

        expression = new VariableExpression("testVar");
        iterator = expression.iterate(new EvalContext());
        assertFalse(iterator.hasNext());

        expression = new IndexExpression(new VariableExpression("testList"), new IntExpression(0));
        EvalContext context = new EvalContext(new NodePointer[]{new NodePointer(new QName("testNode"), null, Locale.ENGLISH)}, null);
        Expression testList = new ConstantExpression(new NodePointer[]{new NodePointer(new QName("testNode1"), null, Locale.ENGLISH),
                new NodePointer(new QName("testNode2"), null, Locale.ENGLISH)});
        iterator = expression.iterate(context);
        assertTrue(iterator.hasNext());
        assertEquals(new NodePointer(new QName("testNode1"), null, Locale.ENGLISH), iterator.next());

        expression = new ArithmeticExpression(new AdditionExpression(new IntExpression(1), new IntExpression(1)), new IntExpression(2));
        iterator = expression.iterate(new EvalContext());
        assertTrue(iterator.hasNext());
        assertEquals(3, ((Number) iterator.next()).intValue());

        expression = new AndExpression(new BooleanExpression(new EqualsExpression(new VariableExpression("x"), new IntExpression(1))),
                new BooleanExpression(new EqualsExpression(new VariableExpression("y"), new IntExpression(2))));
        EvalContext contextWithVariables = new EvalContext(new NodePointer[]{new NodePointer(new QName("testNode"), null, Locale.ENGLISH)},
                new Object[]{1, 2});
        iterator = expression.iterate(contextWithVariables);
        assertTrue(iterator.hasNext());
        assertTrue((boolean) iterator.next());
    }
}