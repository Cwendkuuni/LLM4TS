import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_LeftGreaterThanRight_ReturnsTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_LeftLessThanRight_ReturnsFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertEquals(Boolean.FALSE, expression.computeValue(new EvalContext()));
    }

    @Test
    public void testGetPrecedence_ReturnsRelationalExprPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric_ReturnsFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare_CompareZero_ReturnsTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertTrue(expression.evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompare_CompareNegative_ReturnsFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompare_ComparePositive_ReturnsFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testCompute_LeftIsInitialContextAndRightIsInitialContext_ResetsLeftAndRight() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        expression.compute(new InitialContext(), new InitialContext());
        // Verify that the reset method was called on both left and right
    }

    @Test
    public void testCompute_LeftIsIteratorAndRightIsIterator_FindsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertTrue(expression.compute(new Iterator(), new Iterator()));
    }

    @Test
    public void testCompute_LeftIsIteratorAndRightIsNotIterator_ContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertTrue(expression.compute(new Iterator(), new Object()));
    }

    @Test
    public void testCompute_LeftIsNotIteratorAndRightIsIterator_ContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertTrue(expression.compute(new Object(), new Iterator()));
    }

    @Test
    public void testCompute_LeftIsDoubleNaN_ReturnsFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertFalse(expression.compute(Double.NaN, new Object()));
    }

    @Test
    public void testCompute_RightIsDoubleNaN_ReturnsFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest();
        assertFalse(expression.compute(new Object(), Double.NaN));
    }

}