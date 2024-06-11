import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpressionMock(new Expression[]{});
    }

    @Test
    public void testComputeValue_NullInputs() {
        // Arrange
        EvalContext context = null;

        // Act
        Object result = expression.computeValue(context);

        // Assert
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        // Act
        int result = expression.getPrecedence();

        // Assert
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        // Act
        boolean result = expression.isSymmetric();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_EqualValues() {
        // Act
        boolean result = expression.evaluateCompare(0);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_LesserValue() {
        // Act
        boolean result = expression.evaluateCompare(-1);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_GreaterValue() {
        // Act
        boolean result = expression.evaluateCompare(1);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCompute_NullInputs() {
        // Arrange
        Object left = null;
        Object right = null;

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCompute_LeftInitialContext() {
        // Arrange
        Object left = new InitialContext();
        Object right = new Integer(5);

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCompute_LeftAndRightInitialContext() {
        // Arrange
        Object left = new InitialContext();
        Object right = new InitialContext();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCompute_LeftAndRightIterators_WithoutMatch() {
        // Arrange
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(4, 5, 6).iterator();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCompute_LeftAndRightIterators_WithMatch() {
        // Arrange
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(3, 4, 5).iterator();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCompute_LeftIterator_ContainsMatch() {
        // Arrange
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Object right = new Integer(3);

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCompute_RightIterator_ContainsMatch() {
        // Arrange
        Object left = new Integer(3);
        Iterator right = Arrays.asList(1, 2, 3).iterator();

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCompute_LeftAndRightDifferentValues() {
        // Arrange
        Object left = new Double(3.5);
        Object right = new Double(2.5);

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCompute_LeftAndRightEqualValues() {
        // Arrange
        Object left = new Double(3.5);
        Object right = new Double(3.5);

        // Act
        boolean result = expression.compute(left, right);

        // Assert
        assertTrue(result);
    }

    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionMock(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }
}