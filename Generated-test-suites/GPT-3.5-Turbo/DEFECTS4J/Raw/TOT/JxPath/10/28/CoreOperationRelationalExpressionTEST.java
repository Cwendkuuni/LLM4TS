import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test when both arguments are equal
        Expression[] args1 = {new Literal(5), new Literal(5)};
        CoreOperationRelationalExpression coreOp1 = new ConcreteCoreOperationRelationalExpression(args1);
        assertEquals(Boolean.FALSE, coreOp1.computeValue(new EvalContext()));

        // Test when left argument is less than right argument
        Expression[] args2 = {new Literal(3), new Literal(5)};
        CoreOperationRelationalExpression coreOp2 = new ConcreteCoreOperationRelationalExpression(args2);
        assertEquals(Boolean.TRUE, coreOp2.computeValue(new EvalContext()));

        // Test when left argument is greater than right argument
        Expression[] args3 = {new Literal(10), new Literal(7)};
        CoreOperationRelationalExpression coreOp3 = new ConcreteCoreOperationRelationalExpression(args3);
        assertEquals(Boolean.FALSE, coreOp3.computeValue(new EvalContext()));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = {new Literal(5), new Literal(10)};
        CoreOperationRelationalExpression coreOp = new ConcreteCoreOperationRelationalExpression(args);
        assertEquals(3, coreOp.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {new Literal(5), new Literal(10)};
        CoreOperationRelationalExpression coreOp = new ConcreteCoreOperationRelationalExpression(args);
        assertFalse(coreOp.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = {new Literal(5), new Literal(10)};
        CoreOperationRelationalExpression coreOp = new ConcreteCoreOperationRelationalExpression(args);
        assertTrue(coreOp.evaluateCompare(-1));
        assertFalse(coreOp.evaluateCompare(0));
        assertFalse(coreOp.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        Expression[] args = {new Literal(5), new Literal(10)};
        CoreOperationRelationalExpression coreOp = new ConcreteCoreOperationRelationalExpression(args);
        assertFalse(coreOp.compute(5, 5));
        assertTrue(coreOp.compute(3, 5));
        assertFalse(coreOp.compute(10, 7));
    }

    @Test
    public void testReduce() {
        Expression[] args = {new Literal(5), new Literal(10)};
        CoreOperationRelationalExpression coreOp = new ConcreteCoreOperationRelationalExpression(args);
        assertEquals(5, coreOp.reduce(5));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = {new Literal(5), new Literal(10)};
        CoreOperationRelationalExpression coreOp = new ConcreteCoreOperationRelationalExpression(args);

        // Test when iterator is not empty and contains a match
        assertTrue(coreOp.containsMatch(getIterator(new int[]{1, 2, 3}), 2));

        // Test when iterator is not empty but does not contain a match
        assertFalse(coreOp.containsMatch(getIterator(new int[]{1, 2, 3}), 4));

        // Test when iterator is empty
        assertFalse(coreOp.containsMatch(getIterator(new int[]{}), 2));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = {new Literal(5), new Literal(10)};
        CoreOperationRelationalExpression coreOp = new ConcreteCoreOperationRelationalExpression(args);

        // Test when the left iterator contains a match with an element in the right iterator
        assertTrue(coreOp.findMatch(getIterator(new int[]{1, 2, 3}), getIterator(new int[]{2, 4, 6})));

        // Test when the left iterator contains no match with any element in the right iterator
        assertFalse(coreOp.findMatch(getIterator(new int[]{1, 2, 3}), getIterator(new int[]{4, 6})));

        // Test when the right iterator is empty
        assertFalse(coreOp.findMatch(getIterator(new int[]{1, 2, 3}), getIterator(new int[]{})));
    }

    private static Iterator<Integer> getIterator(int[] array) {
        HashSet<Integer> set = new HashSet<>();
        for (int i : array) {
            set.add(i);
        }
        return set.iterator();
    }
}

class ConcreteCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
    
    public ConcreteCoreOperationRelationalExpression(Expression[] args) {
        super(args);
    }

    protected boolean evaluateCompare(int compare) {
        return compare < 0;
    }
}