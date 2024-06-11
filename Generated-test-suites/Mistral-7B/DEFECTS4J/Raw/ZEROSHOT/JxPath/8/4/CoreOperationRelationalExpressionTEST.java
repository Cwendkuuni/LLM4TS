import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionArg1;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionArg2;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.InitialContext;
import org.apache.commons.jxpath.ri.compiler.SelfContext;
import org.apache.commons.jxpath.ri.evaluator.EvalContext;
import org.apache.commons.jxpath.ri.iter.CollectionIter;
import org.apache.commons.jxpath.ri.iter.Iterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testGreaterThan() {
        Expression left = new CoreOperationRelationalExpressionArg1(new SelfContext());
        Expression right = new CoreOperationRelationalExpressionArg2(new SelfContext());
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] {left, right});

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Double(10.0));
        Object leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(5.0));
        Object rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(15.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(10.0));
        rightValue = expression.computeValue(context);
        assertFalse(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(10.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(10.0));
        rightValue = expression.computeValue(context);
        assertFalse(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(10.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(11.0));
        rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));
    }

    @org.junit.Test
    public void testGreaterThanOrEqual() {
        Expression left = new CoreOperationRelationalExpressionArg1(new SelfContext());
        Expression right = new CoreOperationRelationalExpressionArg2(new SelfContext());
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] {left, right});

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Double(10.0));
        Object leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(5.0));
        Object rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(10.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(10.0));
        rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(10.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(11.0));
        rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(5.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(10.0));
        rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));
    }

    @org.junit.Test
    public void testLessThan() {
        Expression left = new CoreOperationRelationalExpressionArg1(new SelfContext());
        Expression right = new CoreOperationRelationalExpressionArg2(new SelfContext());
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperation[] {left, right});

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Double(5.0));
        Object leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(10.0));
        Object rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(10.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(10.0));
        rightValue = expression.computeValue(context);
        assertFalse(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(10.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(5.0));
        rightValue = expression.computeValue(context);
        assertFalse(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(5.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(5.0));
        rightValue = expression.computeValue(context);
        assertFalse(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(5.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(4.0));
        rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));
    }

    @org.junit.Test
    public void testLessThanOrEqual() {
        Expression left = new CoreOperationRelationalExpressionArg1(new SelfContext());
        Expression right = new CoreOperationRelationalExpressionArg2(new SelfContext());
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreOperation[] {left, right});

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Double(5.0));
        Object leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(10.0));
        Object rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(5.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(5.0));
        rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(5.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(4.0));
        rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(10.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(5.0));
        rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));

        context.setSingleNodePointer(new Double(10.0));
        leftValue = expression.computeValue(context);
        context.setSingleNodePointer(new Double(10.0));
        rightValue = expression.computeValue(context);
        assertTrue(expression.compute(leftValue, rightValue));
    }

    @org.junit.Test
    public void testCompareDoubleValues() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] {new CoreOperationRelationalExpressionArg1(new SelfContext()), new CoreOperationRelationalExpressionArg2(new SelfContext())});
        Expression left = new CoreOperationRelationalExpressionArg1(new SelfContext());
        Expression right = new CoreOperationRelationalExpressionArg2(new SelfContext());
        assertEquals(expression, left.getClass().cast(left.getExpression()).getParent(), "Parent relation expression is not set correctly for left operand.");
        assertEquals(expression, right.getClass().cast(right.getExpression()).getParent(), "Parent relation expression is not set correctly for right operand.");

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Double(10.0));

        Object leftValue = expression.computeValue(context);
        Object rightValue = expression.computeValue(context);

        Double leftDouble = (Double) leftValue;
        Double rightDouble = (Double) rightValue;

        expression.evaluateCompare(0);
        assertFalse(expression.compute(leftValue, rightValue));

        expression.evaluateCompare(1);
        assertTrue(expression.compute(leftValue, rightValue));

        expression.evaluateCompare(-1);
        assertFalse(expression.compute(leftValue, rightValue));
    }

    @org.junit.Test
    public void testCompareIterators() {
        CollectionIter<String> leftIter = new CollectionIter<>(new ArrayList<>());
        leftIter.add("a");
        leftIter.add("b");
        leftIter.add("c");

        CollectionIter<String> rightIter = new CollectionIter<>(new ArrayList<>());
        rightIter.add("a");
        rightIter.add("b");
        rightIter.add("d");

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] {new CoreOperationRelationalExpressionArg1(new IteratorContext(leftIter)), new CoreOperationRelationalExpressionArg2(new IteratorContext(rightIter))});

        Iterator leftIterator = expression.getClass().cast(leftIter.getExpression()).getExpression();
        Iterator rightIterator = expression.getClass().cast(rightIter.getExpression()).getExpression();

        assertEquals(expression, leftIterator.getExpression().getParent(), "Parent relation expression is not set correctly for left iterator.");
        assertEquals(expression, rightIterator.getExpression().getParent(), "Parent relation expression is not set correctly for right iterator.");

        expression.compute(leftIterator, rightIterator);
        assertFalse(expression.compute(leftIterator, rightIterator));
    }

    @org.junit.Test
    public void testContainsMatchIteratorAndCollection() {
        CollectionIter<String> iter = new CollectionIter<>(new ArrayList<>());
        iter.add("a");
        iter.add("b");
        iter.add("c");

        CoreOperationRelationalExpression expression = new CoreOperationContains(new CoreOperation[] {new CoreOperationRelationalExpressionArg1(new IteratorContext(iter)), new CoreOperationRelationalExpressionArg2(new SingletonIterator("b"))});

        Iterator leftIterator = expression.getClass().cast(iter.getExpression()).getExpression();
        Expression rightExpression = expression.getClass().cast(new SingletonIterator("b").getExpression());

        assertEquals(expression, leftIterator.getExpression().getParent(), "Parent relation expression is not set correctly for left iterator.");
        assertEquals(expression, rightExpression.getExpression().getParent(), "Parent relation expression is not set correctly for right expression.");

        expression.compute(leftIterator, rightExpression);
        assertTrue(expression.compute(leftIterator, rightExpression));
    }

    @org.junit.Test
    public void testFindMatchIteratorAndIterator() {
        CollectionIter<String> lit = new CollectionIter<>(new ArrayList<>());
        lit.add("a");
        lit.add("b");
        lit.add("c");

        CollectionIter<String> rit = new CollectionIter<>(new ArrayList<>());
        rit.add("a");
        rit.add("b");
        rit.add("d");

        CoreOperationRelationalExpression expression = new CoreOperationFindMatch(new CoreOperation[] {new CoreOperationRelationalExpressionArg1(new IteratorContext(lit)), new CoreOperationRelationalExpressionArg2(new IteratorContext(rit))});

        Iterator leftIterator = expression.getClass().cast(lit.getExpression()).getExpression();
        Iterator rightIterator = expression.getClass().cast(rit.getExpression()).getExpression();

        assertEquals(expression, leftIterator.getExpression().getParent(), "Parent relation expression is not set correctly for left iterator.");
        assertEquals(expression, rightIterator.getExpression().getParent(), "Parent relation expression is not set correctly for right iterator.");

        expression.compute(leftIterator, rightIterator);
        assertFalse(expression.compute(leftIterator, rightIterator));
    }
}