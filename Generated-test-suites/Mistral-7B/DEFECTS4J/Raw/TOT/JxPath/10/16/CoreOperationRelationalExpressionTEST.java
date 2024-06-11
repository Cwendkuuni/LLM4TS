import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateIter;
import org.apache.commons.jxpath.ri.iter.Iterate;
import org.apache.commons.jxpath.ri.iter.SingletonIter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    private static CoreOperationRelationalExpression createInstance(Expression left, Expression right) {
        Expression[] args = new Expression[]{left, right};
        return new CoreOperationRelationalExpression(args) {};
    }

    @org.junit.Test
    public void testGreaterThan() {
        // Typical use case
        Expression left1 = new Literal(1);
        Expression right1 = new Literal(2);
        CoreOperationRelationalExpression expr1 = createInstance(left1, right1);
        assertTrue(expr1.computeValue(new EvalContext()));

        // Edge case: checking for greater than between same values
        Expression left2 = new Literal(1);
        Expression right2 = new Literal(1);
        CoreOperationRelationalExpression expr2 = createInstance(left2, right2);
        assertFalse(expr2.computeValue(new EvalContext()));

        // Error scenario: checking for greater than between invalid values
        Expression invalidLeft = new InvalidExpression();
        Expression right3 = new Literal(2);
        CoreOperationRelationalExpression expr3 = createInstance(invalidLeft, right3);
        assertFalse(expr3.computeValue(new EvalContext()));
    }

    @org.junit.Test
    public void testGreaterThanOrEqual() {
        // Typical use case
        Expression left1 = new Literal(1);
        Expression right1 = new Literal(2);
        CoreOperationRelationalExpression expr1 = createInstance(left1, right1);
        assertFalse(expr1.computeValue(new EvalContext()));

        Expression left2 = new Literal(2);
        Expression right2 = new Literal(2);
        CoreOperationRelationalExpression expr2 = createInstance(left2, right2);
        assertTrue(expr2.computeValue(new EvalContext()));

        Expression left3 = new Literal(1);
        Expression right3 = new Literal(2);
        CoreOperationRelationalExpression expr3 = createInstance(left3, right3);
        assertFalse(expr3.computeValue(new EvalContext()));

        Expression left4 = new Literal(2);
        Expression right4 = new Literal(2);
        CoreOperationRelationalExpression expr4 = createInstance(left4, right4);
        assertTrue(expr4.computeValue(new EvalContext()));

        // Edge case: checking for greater than or equal between same values
        Expression left5 = new Literal(1);
        Expression right5 = new Literal(1);
        CoreOperationRelationalExpression expr5 = createInstance(left5, right5);
        assertTrue(expr5.computeValue(new EvalContext()));

        // Error scenario: checking for greater than or equal between invalid values
        Expression invalidLeft = new InvalidExpression();
        Expression right6 = new Literal(2);
        CoreOperationRelationalExpression expr6 = createInstance(invalidLeft, right6);
        assertFalse(expr6.computeValue(new EvalContext()));
    }

    @org.junit.Test
    public void testLessThan() {
        // Typical use case
        Expression left1 = new Literal(2);
        Expression right1 = new Literal(1);
        CoreOperationRelationalExpression expr1 = createInstance(left1, right1);
        assertFalse(expr1.computeValue(new EvalContext()));

        // Edge case: checking for less than between same values
        Expression left2 = new Literal(1);
        Expression right2 = new Literal(1);
        CoreOperationRelationalExpression expr2 = createInstance(left2, right2);
        assertTrue(expr2.computeValue(new EvalContext()));

        // Error scenario: checking for less than between invalid values
        Expression invalidLeft = new InvalidExpression();
        Expression right3 = new Literal(1);
        CoreOperationRelationalExpression expr3 = createInstance(invalidLeft, right3);
        assertFalse(expr3.computeValue(new EvalContext()));
    }

    @org.junit.Test
    public void testLessThanOrEqual() {
        // Typical use case
        Expression left1 = new Literal(1);
        Expression right1 = new Literal(2);
        CoreOperationRelationalExpression expr1 = createInstance(left1, right1);
        assertFalse(expr1.computeValue(new EvalContext()));

        Expression left2 = new Literal(1);
        Expression right2 = new Literal(1);
        CoreOperationRelationalExpression expr2 = createInstance(left2, right2);
        assertTrue(expr2.computeValue(new EvalContext()));

        Expression left3 = new Literal(1);
        Expression right3 = new Literal(2);
        CoreOperationRelationalExpression expr3 = createInstance(left3, right3);
        assertFalse(expr3.computeValue(new EvalContext()));

        Expression left4 = new Literal(1);
        Expression right4 = new Literal(1);
        CoreOperationRelationalExpression expr4 = createInstance(left4, right4);
        assertTrue(expr4.computeValue(new EvalContext()));

        // Edge case: checking for less than or equal between same values
        Expression left5 = new Literal(1);
        Expression right5 = new Literal(1);
        CoreOperationRelationalExpression expr5 = createInstance(left5, right5);
        assertTrue(expr5.computeValue(new EvalContext()));

        // Error scenario: checking for less than or equal between invalid values
        Expression invalidLeft = new InvalidExpression();
        Expression right6 = new Literal(1);
        CoreOperationRelationalExpression expr6 = createInstance(invalidLeft, right6);
        assertFalse(expr6.computeValue(new EvalContext()));
    }

    @org.junit.Test
    public void testCompareWithIterators() {
        // Typical use case: iterators with matching values
        Collection iterable1 = new ArrayList();
        iterable1.add(new Integer(1));
        iterable1.add(new Integer(2));

        Iterator left = iterable1.iterator();
        Iterator right = iterable1.iterator();

        CoreOperationRelationalExpression expr = createInstance(new IterateIter(left), new IterateIter(right));
        assertTrue(expr.computeValue(new EvalContext()));

        // Typical use case: iterators with no matching values
        Collection iterable2 = new ArrayList();
        iterable2.add(new Integer(1));
        iterable2.add(new Integer(3));

        Iterator left2 = iterable2.iterator();
        Iterator right2 = iterable1.iterator();

        CoreOperationRelationalExpression expr2 = createInstance(new IterateIter(left2), new IterateIter(right));
        assertFalse(expr2.computeValue(new EvalContext()));

        // Edge case: iterators with different sizes
        Collection iterable3 = new ArrayList();
        iterable3.add(new Integer(1));
        iterable3.add(new Integer(2));
        iterable3.add(new Integer(3));

        Iterator left3 = iterable3.iterator();
        Iterator right3 = iterable1.iterator();

        CoreOperationRelationalExpression expr3 = createInstance(new IterateIter(left3), new IterateIter(right));
        assertFalse(expr3.computeValue(new EvalContext()));

        // Edge case: iterators with mixed types
        Collection iterable4 = new ArrayList();
        iterable4.add(new Integer(1));
        iterable4.add(new String("two"));

        Iterator left4 = iterable4.iterator();
        Iterator right4 = iterable1.iterator();

        CoreOperationRelationalExpression expr4 = createInstance(new IterateIter(left4), new IterateIter(right));
        assertFalse(expr4.computeValue(new EvalContext()));
    }

    @org.junit.Test
    public void testCompareWithCollections() {
        // Typical use case: collections with matching values
        Collection left = new ArrayList();
        left.add(new Integer(1));
        left.add(new Integer(2));

        Collection right = new ArrayList();
        right.add(new Integer(1));
        right.add(new Integer(2));

        CoreOperationRelationalExpression expr = createInstance(new SingletonIter(left.iterator()), new SingletonIter(right.iterator()));
        assertTrue(expr.computeValue(new EvalContext()));

        // Typical use case: collections with no matching values
        Collection left2 = new ArrayList();
        left2.add(new Integer(1));
        left2.add(new Integer(2));

        Collection right2 = new ArrayList();
        right2.add(new Integer(3));
        right2.add(new Integer(4));

        CoreOperationRelationalExpression expr2 = createInstance(new SingletonIter(left2.iterator()), new SingletonIter(right.iterator()));
        assertFalse(expr2.computeValue(new EvalContext()));

        // Edge case: collections with different sizes
        Collection left3 = new ArrayList();
        left3.add(new Integer(1));
        left3.add(new Integer(2));

        Collection right3 = new ArrayList();
        right3.add(new Integer(1));

        CoreOperationRelationalExpression expr3 = createInstance(new SingletonIter(left3.iterator()), new SingletonIter(right3.iterator()));
        assertFalse(expr3.computeValue(new EvalContext()));

        // Edge case: collections with mixed types
        Collection left4 = new ArrayList();
        left4.add(new Integer(1));
        left4.add(new String("two"));

        Collection right4 = new ArrayList();
        right4.add(new Integer(1));
        right4.add(new Integer(2));

        CoreOperationRelationalExpression expr4 = createInstance(new SingletonIter(left4.iterator()), new SingletonIter(right4.iterator()));
        assertFalse(expr4.computeValue(new EvalContext()));
    }

    private static class InvalidExpression implements Expression {
        @Override
        public Object computeValue(EvalContext context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getPrecedence() {
            return 0;
        }
    }

}