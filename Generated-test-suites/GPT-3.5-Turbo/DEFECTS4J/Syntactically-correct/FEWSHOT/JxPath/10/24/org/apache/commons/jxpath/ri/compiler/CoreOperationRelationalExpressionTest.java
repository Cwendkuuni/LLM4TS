package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    private final class TestExpression extends CoreOperationRelationalExpression {
        protected TestExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare < 0;
        }
    }

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression(5);
        args[1] = new DummyExpression(3);
        CoreOperationRelationalExpression expression = new TestExpression(args);
        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression(5);
        args[1] = new DummyExpression(3);
        CoreOperationRelationalExpression expression = new TestExpression(args);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression(5);
        args[1] = new DummyExpression(3);
        CoreOperationRelationalExpression expression = new TestExpression(args);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute_LeftIsInitialContext() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression(new InitialContext(null));
        args[1] = new DummyExpression(3);
        CoreOperationRelationalExpression expression = new TestExpression(args);
        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testCompute_RightIsInitialContext() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression(5);
        args[1] = new DummyExpression(new InitialContext(null));
        CoreOperationRelationalExpression expression = new TestExpression(args);
        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testCompute_BothAreIterator() {
        List<Object> list = new ArrayList<Object>();
        list.add(1);
        list.add(2);
        list.add(3);

        Expression[] args = new Expression[2];
        args[0] = new DummyExpression(list.iterator());
        args[1] = new DummyExpression(list.iterator());
        CoreOperationRelationalExpression expression = new TestExpression(args);
        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testCompute_LeftIsIterator() {
        List<Object> list = new ArrayList<Object>();
        list.add(1);
        list.add(2);
        list.add(3);

        Expression[] args = new Expression[2];
        args[0] = new DummyExpression(list.iterator());
        args[1] = new DummyExpression(2);
        CoreOperationRelationalExpression expression = new TestExpression(args);
        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testCompute_RightIsIterator() {
        List<Object> list = new ArrayList<Object>();
        list.add(1);
        list.add(2);
        list.add(3);

        Expression[] args = new Expression[2];
        args[0] = new DummyExpression(2);
        args[1] = new DummyExpression(list.iterator());
        CoreOperationRelationalExpression expression = new TestExpression(args);
        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testCompute_LeftAndRightAreIterator() {
        List<Object> leftList = new ArrayList<Object>();
        leftList.add(1);
        leftList.add(2);
        leftList.add(3);

        List<Object> rightList = new ArrayList<Object>();
        rightList.add(4);
        rightList.add(5);
        rightList.add(2);

        Expression[] args = new Expression[2];
        args[0] = new DummyExpression(leftList.iterator());
        args[1] = new DummyExpression(rightList.iterator());
        CoreOperationRelationalExpression expression = new TestExpression(args);
        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testCompute_NaN() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression(0.0 / 0.0);
        args[1] = new DummyExpression(2);
        CoreOperationRelationalExpression expression = new TestExpression(args);
        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        args[0] = new DummyExpression(2);
        args[1] = new DummyExpression(0.0 / 0.0);
        expression = new TestExpression(args);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    private final class DummyExpression extends Expression {

        private final Object value;

        public DummyExpression(Object value) {
            super();
            this.value = value;
        }

        @Override
        public Iterator iterate(EvalContext context) {
            return null;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}