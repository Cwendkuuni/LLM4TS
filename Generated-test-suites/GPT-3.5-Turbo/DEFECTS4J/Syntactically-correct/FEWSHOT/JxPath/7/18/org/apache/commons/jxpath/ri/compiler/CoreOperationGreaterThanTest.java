
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CoreOperationGreaterThanTest {

    @Mock
    private EvalContext context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testComputeValue_LeftGreaterThanRight_ReturnsTrue() {
        // Arrange
        double left = 5.0;
        double right = 3.0;
        Expression arg1 = new NumberExpression(left);
        Expression arg2 = new NumberExpression(right);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        // Act
        Object result = operation.computeValue(context);

        // Assert
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_RightGreaterThanLeft_ReturnsFalse() {
        // Arrange
        double left = 3.0;
        double right = 5.0;
        Expression arg1 = new NumberExpression(left);
        Expression arg2 = new NumberExpression(right);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        // Act
        Object result = operation.computeValue(context);

        // Assert
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftEqualsRight_ReturnsFalse() {
        // Arrange
        double value = 3.0;
        Expression arg1 = new NumberExpression(value);
        Expression arg2 = new NumberExpression(value);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        // Act
        Object result = operation.computeValue(context);

        // Assert
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanSymbol() {
        // Arrange
        Expression arg1 = new NumberExpression(1.0);
        Expression arg2 = new NumberExpression(2.0);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        // Act
        String symbol = operation.getSymbol();

        // Assert
        assertEquals(">", symbol);
    }
}
