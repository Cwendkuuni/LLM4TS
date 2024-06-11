import java.util.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test with equal values
        Object left = 10;
        Object right = 10;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        boolean result = expression.computeValue(left, right);
        assertTrue(result);

        // Test with different values
        left = 10;
        right = 20;
        result = expression.computeValue(left, right);
        assertFalse(result);

        // Test with NaN values
        left = Double.NaN;
        right = 20;
        result = expression.computeValue(left, right);
        assertFalse(result);

        // Test with iterator values
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();
        result = expression.computeValue(iterator1, iterator2);
        assertTrue(result);

        // Test with nested iterators
        List<List<Integer>> nestedList1 = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4));
        List<List<Integer>> nestedList2 = Arrays.asList(Arrays.asList(2), Arrays.asList(3, 4));
        Iterator<List<Integer>> nestedIterator1 = nestedList1.iterator();
        Iterator<List<Integer>> nestedIterator2 = nestedList2.iterator();
        result = expression.computeValue(nestedIterator1, nestedIterator2);
        assertTrue(result);

        // Test with InitialContext
        InitialContext context1 = new InitialContext();
        InitialContext context2 = new InitialContext();
        result = expression.computeValue(context1, context2);
        assertFalse(result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        boolean result = expression.evaluateCompare(0);
        assertTrue(result);

        result = expression.evaluateCompare(1);
        assertFalse(result);

        result = expression.evaluateCompare(-1);
        assertFalse(result);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        Object result = expression.reduce(new SelfContext());
        assertTrue(result instanceof SingleNodePointer);

        result = expression.reduce(new ArrayList<>());
        assertTrue(result instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        List<Integer> list = Arrays.asList(1, 2, 3);
        Iterator<Integer> iterator = list.iterator();
        boolean result = expression.containsMatch(iterator, 2);
        assertTrue(result);

        result = expression.containsMatch(iterator, 4);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();
        boolean result = expression.findMatch(iterator1, iterator2);
        assertTrue(result);

        List<List<Integer>> nestedList1 = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4));
        List<List<Integer>> nestedList2 = Arrays.asList(Arrays.asList(2), Arrays.asList(3, 4));
        Iterator<List<Integer>> nestedIterator1 = nestedList1.iterator();
        Iterator<List<Integer>> nestedIterator2 = nestedList2.iterator();
        result = expression.findMatch(nestedIterator1, nestedIterator2);
        assertTrue(result);
    }
}