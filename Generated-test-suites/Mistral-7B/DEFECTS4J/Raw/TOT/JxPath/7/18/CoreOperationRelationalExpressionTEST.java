package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        // Given
        CoreOperation leftOperand = new CoreOperation("5");
        CoreOperation rightOperand = new CoreOperation("6");
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(leftOperand, rightOperand);

        // When
        boolean result = expression.evaluate(new Context());

        // Then
        assertTrue("Expected true as result", result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        // Given
        CoreOperation leftOperand = new CoreOperation("5");
        CoreOperation rightOperand = new CoreOperation("5");
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(leftOperand, rightOperand);

        // When
        boolean result = expression.evaluate(new Context());

        // Then
        assertTrue("Expected true as result", result);
    }

    @Test
    public void testLessThan() {
        // Given
        CoreOperation leftOperand = new CoreOperation("5");
        CoreOperation rightOperand = new CoreOperation("6");
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(leftOperand, rightOperand);

        // When
        boolean result = expression.evaluate(new Context());

        // Then
        assertFalse("Expected false as result", result);
    }

    @Test
    public void testLessThanOrEqual() {
        // Given
        CoreOperation leftOperand = new CoreOperation("5");
        CoreOperation rightOperand = new CoreOperation("5");
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(leftOperand, rightOperand);

        // When
        boolean result = expression.evaluate(new Context());

        // Then
        assertTrue("Expected true as result", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        // Given
        CoreOperation leftOperand = new CoreOperation("5");
        CoreOperation rightOperand = null;
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(leftOperand, rightOperand);

        // When & Then
        expression.evaluate(new Context()); // Exception should be thrown in this line
    }

    @Test
    public void testSymmetricOperations() {
        // Given
        CoreOperation leftOperand1 = new CoreOperation("5");
        CoreOperation rightOperand1 = new CoreOperation("6");
        CoreOperationRelationalExpression expression1 = new CoreOperationGreaterThan(leftOperand1, rightOperand1);

        CoreOperation leftOperand2 = new CoreOperation("6");
        CoreOperation rightOperand2 = new CoreOperation("5");
        CoreOperationRelationalExpression expression2 = new CoreOperationLessThan(leftOperand2, rightOperand2);

        // Then
        assertFalse("Expected false as result for expression1", expression1.isSymmetric());
        assertFalse("Expected false as result for expression2", expression2.isSymmetric());
    }

}