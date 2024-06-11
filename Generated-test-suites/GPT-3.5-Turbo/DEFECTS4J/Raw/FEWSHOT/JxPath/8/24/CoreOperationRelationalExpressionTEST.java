package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        args[0] = new DummyExpression(false);
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        args[0] = new DummyExpression();
        args[1] = new DummyExpression(false);
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        args[0] = new DummyExpression(false);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);
        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        Object trueResult = expression.compute("true", "true");
        assertTrue((boolean) trueResult);

        Object falseResult = expression.compute("false", "true");
        assertFalse((boolean) falseResult);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        Object initialContext = expression.reduce(new InitialContext());
        assertTrue(initialContext instanceof InitialContext);

        Object collection = expression.reduce("collection");
        assertEquals("collection", collection);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        DummyIterator iterator = new DummyIterator();
        Object value1 = "value1";
        Object value2 = "value2";
        iterator.add(value1);
        iterator.add(value2);

        assertTrue(expression.containsMatch(iterator, value1));
        assertFalse(expression.containsMatch(iterator, "value3"));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        DummyIterator lit = new DummyIterator();
        DummyIterator rit = new DummyIterator();
        Object value1 = "value1";
        Object value2 = "value2";
        lit.add(value1);
        lit.add(value2);
        rit.add(value2);

        assertTrue(expression.findMatch(lit, rit));

        DummyIterator lit2 = new DummyIterator();
        lit2.add(value1);
        assertFalse(expression.findMatch(lit2, rit));
    }
}

class DummyExpression implements Expression {
    private boolean result;

    public DummyExpression() {
        this.result = true;
    }

    public DummyExpression(boolean result) {
        this.result = result;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return result;
    }
}

class DummyCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
    public DummyCoreOperationRelationalExpression(Expression[] args) {
        super(args);
    }

    @Override
    protected boolean evaluateCompare(int compare) {
        return compare == 0;
    }
}

class DummyIterator implements Iterator<Object> {
    private int currentIndex = -1;
    private Object[] values;

    public DummyIterator() {
        values = new Object[0];
    }

    public DummyIterator(Object... values) {
        this.values = values;
    }

    public void add(Object value) {
        Object[] newValues = new Object[values.length + 1];
        System.arraycopy(values, 0, newValues, 0, values.length);
        newValues[values.length] = value;
        values = newValues;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < values.length - 1;
    }

    @Override
    public Object next() {
        currentIndex++;
        return values[currentIndex];
    }
}