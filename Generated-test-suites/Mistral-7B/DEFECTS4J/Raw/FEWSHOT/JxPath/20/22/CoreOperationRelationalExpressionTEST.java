package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.apache.commons.jxpath.ri.iter.SingletonIter;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        Expression[] args = new Expression[2];
        Object left = new SingletonIter(1.0);
        Object right = new SingletonIter(2.0);
        args[0] = new CoreNumber(1.0);
        args[1] = new CoreNumber(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(args);
        assertTrue(expression.evaluateCompare(1));
        assertFalse(expression.evaluateCompare(0));
    }

    @Test
    public void testCoreOperationRelationalExpressionWithIterators() {
        Iterator<Object> left = new ArrayIter(new Object[]{1.0, 2.0, 3.0});
        Iterator<Object> right = new ArrayIter(new Object[]{2.0, 3.0, 4.0});
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreNumber(2.0), new CoreNumber(1.0));
        assertTrue(expression.compute(left, right));
        assertFalse(expression.compute(left, new SingletonIter(1.0)));
    }

    @Test
    public void testCoreOperationRelationalExpressionWithSelfContext() {
        Expression[] args = new Expression[2];
        Object left = new SelfContext();
        Object right = new SingletonIter(1.0);
        args[0] = new CoreNumber(1.0);
        args[1] = new CoreNumber(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(args);
        EvalContext context = new EvalContext(new Object[]{new SelfContext()});
        assertFalse(expression.compute(left, right));
    }

    @Test
    public void testCoreOperationRelationalExpressionWithCollections() {
        Collection<Object> left = new HashSet<>();
        Collection<Object> right = new HashSet<>();
        left.add(1.0);
        left.add(2.0);
        right.add(2.0);
        right.add(3.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreNumber(2.0), new CoreNumber(1.0));
        assertTrue(expression.compute(left.iterator(), right.iterator()));
        assertFalse(expression.compute(left.iterator(), new SingletonIter(1.0).iterator()));
    }

    @Test
    public void testCoreOperationRelationalExpressionWithInitialContext() {
        Expression[] args = new Expression[2];
        Object left = new InitialContext();
        Object right = new SingletonIter(1.0);
        args[0] = new CoreNumber(1.0);
        args[1] = new CoreNumber(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(args);
        assertFalse(expression.compute(left, right));
    }
}