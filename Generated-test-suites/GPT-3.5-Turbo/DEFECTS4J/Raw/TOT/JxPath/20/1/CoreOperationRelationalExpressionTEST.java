
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_GreaterThanOperator_EvaluatesCorrectly() {
        Expression[] args = {new ConstantExpression(5), new ConstantExpression(3)};
        CoreOperationRelationalExpression expression = new GreaterThanOperator(args);

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_GreaterThanOrEqualOperator_EvaluatesCorrectly() {
        Expression[] args = {new ConstantExpression(5), new ConstantExpression(5)};
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualOperator(args);

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_LessThanOperator_EvaluatesCorrectly() {
        Expression[] args = {new ConstantExpression(5), new ConstantExpression(10)};
        CoreOperationRelationalExpression expression = new LessThanOperator(args);

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_LessThanOrEqualOperator_EvaluatesCorrectly() {
        Expression[] args = {new ConstantExpression(5), new ConstantExpression(5)};
        CoreOperationRelationalExpression expression = new LessThanOrEqualOperator(args);

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEvaluateCompare_GreaterThanOperator_ReturnsTrueForPositiveComparison() {
        Expression[] args = {new ConstantExpression(10), new ConstantExpression(5)};
        GreaterThanOperator operator = new GreaterThanOperator(args);

        boolean result = operator.evaluateCompare(1);

        Assert.assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_GreaterThanOperator_ReturnsFalseForEqualComparison() {
        Expression[] args = {new ConstantExpression(5), new ConstantExpression(5)};
        GreaterThanOperator operator = new GreaterThanOperator(args);

        boolean result = operator.evaluateCompare(0);

        Assert.assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_GreaterThanOperator_ReturnsFalseForNegativeComparison() {
        Expression[] args = {new ConstantExpression(5), new ConstantExpression(10)};
        GreaterThanOperator operator = new GreaterThanOperator(args);

        boolean result = operator.evaluateCompare(-1);

        Assert.assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_LessThanOperator_ReturnsTrueForNegativeComparison() {
        Expression[] args = {new ConstantExpression(5), new ConstantExpression(10)};
        LessThanOperator operator = new LessThanOperator(args);

        boolean result = operator.evaluateCompare(-1);

        Assert.assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_LessThanOperator_ReturnsFalseForEqualComparison() {
        Expression[] args = {new ConstantExpression(5), new ConstantExpression(5)};
        LessThanOperator operator = new LessThanOperator(args);

        boolean result = operator.evaluateCompare(0);

        Assert.assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_LessThanOperator_ReturnsFalseForPositiveComparison() {
        Expression[] args = {new ConstantExpression(10), new ConstantExpression(5)};
        LessThanOperator operator = new LessThanOperator(args);

        boolean result = operator.evaluateCompare(1);

        Assert.assertFalse(result);
    }

    @Test
    public void testReduce_ObjectIsSelfContext_ReturnsSingleNodePointer() {
        EvalContext evalContext = new EvalContext();
        evalContext.setSingleNodePointer(new Object());

        CoreOperationRelationalExpression operation = new DummyOperation();

        Object result = operation.reduce(evalContext);

        Assert.assertNotNull(result);
        Assert.assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testReduce_ObjectIsCollection_ReturnsIterator() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        CoreOperationRelationalExpression operation = new DummyOperation();

        Object result = operation.reduce(list);

        Assert.assertNotNull(result);
        Assert.assertEquals(Iterator.class, result.getClass());
    }

    @Test
    public void testReduce_ObjectIsNotSelfContextOrCollection_ReturnsUnchangedObject() {
        Object testObject = new Object();
        CoreOperationRelationalExpression operation = new DummyOperation();

        Object result = operation.reduce(testObject);

        Assert.assertEquals(testObject, result);
    }

    @Test
    public void testContainsMatch_IteratorContainsMatch_ReturnsTrue() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        Iterator<Integer> iterator = list.iterator();
        CoreOperationRelationalExpression operation = new DummyOperation();

        boolean result = operation.containsMatch(iterator, 2);

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsMatch_IteratorDoesNotContainMatch_ReturnsFalse() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        Iterator<Integer> iterator = list.iterator();
        CoreOperationRelationalExpression operation = new DummyOperation();

        boolean result = operation.containsMatch(iterator, 4);

        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch_NoIntersectionBetweenIterators_ReturnsFalse() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();
        CoreOperationRelationalExpression operation = new DummyOperation();

        boolean result = operation.findMatch(iterator1, iterator2);

        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch_IntersectionBetweenIterators_ReturnsTrue() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(3, 4, 5));
        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();
        CoreOperationRelationalExpression operation = new DummyOperation();

        boolean result = operation.findMatch(iterator1, iterator2);

        Assert.assertTrue(result);
    }
    
    private class DummyOperation extends CoreOperationRelationalExpression {
        public DummyOperation() {
            super(null);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return false;
        }

        @Override
        protected int getPrecedence() {
            return 0;
        }

        @Override
        protected boolean isSymmetric() {
            return false;
        }
    }
}
