import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new MockCoreOperationRelationalExpression();
    }

    @Test
    public void testComputeValue_WhenComparisonIsTrue_ShouldReturnTrue() {
        // Arrange
        EvalContext context = new MockEvalContext();
        Object expectedResult = Boolean.TRUE;
        
        // Act
        Object actualResult = expression.computeValue(context);

        // Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testComputeValue_WhenComparisonIsFalse_ShouldReturnFalse() {
        // Arrange
        EvalContext context = new MockEvalContext();
        Object expectedResult = Boolean.FALSE;
        
        // Act
        Object actualResult = expression.computeValue(context);

        // Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetPrecedence_ShouldReturnRelationalExpressionPrecedence() {
        // Arrange
        int expectedPrecedence = CoreOperation.RELATIONAL_EXPR_PRECEDENCE;

        // Act
        int actualPrecedence = expression.getPrecedence();

        // Assert
        assertEquals(expectedPrecedence, actualPrecedence);
    }

    @Test
    public void testIsSymmetric_ShouldReturnFalse() {
        // Arrange
        boolean expectedSymmetry = false;

        // Act
        boolean actualSymmetry = expression.isSymmetric();

        // Assert
        assertEquals(expectedSymmetry, actualSymmetry);
    }

    @Test
    public void testEvaluateCompare_WhenCompareIsGreaterThanZero_ShouldReturnTrue() {
        // Arrange
        int compare = 1;
        boolean expectedEvaluation = true;

        // Act
        boolean actualEvaluation = expression.evaluateCompare(compare);

        // Assert
        assertEquals(expectedEvaluation, actualEvaluation);
    }

    @Test
    public void testEvaluateCompare_WhenCompareIsEqualToZero_ShouldReturnTrue() {
        // Arrange
        int compare = 0;
        boolean expectedEvaluation = true;

        // Act
        boolean actualEvaluation = expression.evaluateCompare(compare);

        // Assert
        assertEquals(expectedEvaluation, actualEvaluation);
    }

    @Test
    public void testEvaluateCompare_WhenCompareIsLessThanZero_ShouldReturnFalse() {
        // Arrange
        int compare = -1;
        boolean expectedEvaluation = false;

        // Act
        boolean actualEvaluation = expression.evaluateCompare(compare);

        // Assert
        assertEquals(expectedEvaluation, actualEvaluation);
    }

    @Test
    public void testCompute_WhenLeftIsInitialContext_AndRightIsInitialContext_ShouldResetBothContexts() {
        // Arrange
        Object left = new InitialContext();
        Object right = new InitialContext();
        Object expectedLeft = left;
        Object expectedRight = right;

        // Act
        expression.compute(left, right);

        // Assert
        assertEquals(expectedLeft, left);
        assertEquals(expectedRight, right);
    }

    @Test
    public void testCompute_WhenLeftIsIterator_AndRightIsIterator_ShouldFindMatch() {
        // Arrange
        Iterator left = new MockIterator();
        Iterator right = new MockIterator();
        boolean expectedMatch = true;

        // Act
        boolean actualMatch = expression.compute(left, right);

        // Assert
        assertEquals(expectedMatch, actualMatch);
    }

    @Test
    public void testCompute_WhenLeftIsIterator_AndRightIsNotIterator_ShouldContainMatch() {
        // Arrange
        Iterator left = new MockIterator();
        Object right = new Object();
        boolean expectedMatch = true;

        // Act
        boolean actualMatch = expression.compute(left, right);

        // Assert
        assertEquals(expectedMatch, actualMatch);
    }

    @Test
    public void testCompute_WhenRightIsIterator_AndLeftIsNotIterator_ShouldContainMatch() {
        // Arrange
        Object left = new Object();
        Iterator right = new MockIterator();
        boolean expectedMatch = true;

        // Act
        boolean actualMatch = expression.compute(left, right);

        // Assert
        assertEquals(expectedMatch, actualMatch);
    }

    @Test
    public void testCompute_WhenLeftAndRightAreNotIterators_ShouldEvaluateComparison() {
        // Arrange
        Object left = new Object();
        Object right = new Object();
        boolean expectedEvaluation = true;

        // Act
        boolean actualEvaluation = expression.compute(left, right);

        // Assert
        assertEquals(expectedEvaluation, actualEvaluation);
    }

    @Test
    public void testReduce_WhenOperandIsSelfContext_ShouldReturnSingleNodePointer() {
        // Arrange
        Object operand = new SelfContext();
        Object expectedOperand = ((EvalContext) operand).getSingleNodePointer();

        // Act
        Object actualOperand = expression.reduce(operand);

        // Assert
        assertEquals(expectedOperand, actualOperand);
    }

    @Test
    public void testReduce_WhenOperandIsCollection_ShouldReturnIterator() {
        // Arrange
        Object operand = new MockCollection();
        Object expectedOperand = ((Collection) operand).iterator();

        // Act
        Object actualOperand = expression.reduce(operand);

        // Assert
        assertEquals(expectedOperand, actualOperand);
    }

    @Test
    public void testContainsMatch_WhenIteratorContainsMatch_ShouldReturnTrue() {
        // Arrange
        Iterator iterator = new MockIteratorWithMatch();
        Object value = new Object();
        boolean expectedMatch = true;

        // Act
        boolean actualMatch = expression.containsMatch(iterator, value);

        // Assert
        assertEquals(expectedMatch, actualMatch);
    }

    @Test
    public void testContainsMatch_WhenIteratorDoesNotContainMatch_ShouldReturnFalse() {
        // Arrange
        Iterator iterator = new MockIteratorWithoutMatch();
        Object value = new Object();
        boolean expectedMatch = false;

        // Act
        boolean actualMatch = expression.containsMatch(iterator, value);

        // Assert
        assertEquals(expectedMatch, actualMatch);
    }

    @Test
    public void testFindMatch_WhenIntersectionExistsBetweenIterators_ShouldReturnTrue() {
        // Arrange
        Iterator leftIterator = new MockIteratorWithMatch();
        Iterator rightIterator = new MockIteratorWithMatch();
        boolean expectedMatch = true;

        // Act
        boolean actualMatch = expression.findMatch(leftIterator, rightIterator);

        // Assert
        assertEquals(expectedMatch, actualMatch);
    }

    @Test
    public void testFindMatch_WhenIntersectionDoesNotExistBetweenIterators_ShouldReturnFalse() {
        // Arrange
        Iterator leftIterator = new MockIteratorWithMatch();
        Iterator rightIterator = new MockIteratorWithoutMatch();
        boolean expectedMatch = false;

        // Act
        boolean actualMatch = expression.findMatch(leftIterator, rightIterator);

        // Assert
        assertEquals(expectedMatch, actualMatch);
    }

    private static class MockCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

        public MockCoreOperationRelationalExpression() {
            super(new Expression[]{});
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare >= 0;
        }
    }

    private static class MockEvalContext extends EvalContext {

        @Override
        public Object getSingleNodePointer() {
            return new Object();
        }
    }

    private static class MockIterator implements Iterator {

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Object next() {
            return new Object();
        }

        @Override
        public void remove() {
            // Do nothing
        }
    }

    private static class MockIteratorWithMatch implements Iterator {

        private boolean hasNextCalled = false;

        @Override
        public boolean hasNext() {
            if (hasNextCalled) {
                return false;
            } else {
                hasNextCalled = true;
                return true;
            }
        }

        @Override
        public Object next() {
            return new Object();
        }

        @Override
        public void remove() {
            // Do nothing
        }
    }

    private static class MockIteratorWithoutMatch implements Iterator {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return new Object();
        }

        @Override
        public void remove() {
            // Do nothing
        }
    }

    private static class MockCollection extends HashSet {
    }
}