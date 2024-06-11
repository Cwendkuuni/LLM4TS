import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testGreaterThanOrEqual() {
        // Given
        Expression arg1 = new NumberNode(5.0);
        Expression arg2 = new NumberNode(5.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = Mockito.spy(EvalContext.class);

        // When
        Object result = operation.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNotGreaterThanOrEqual() {
        // Given
        Expression arg1 = new NumberNode(5.0);
        Expression arg2 = new NumberNode(6.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = PowerMockito.mock(EvalContext.class);

        // When
        Object result = operation.computeValue(context);

        // Then
        assertFalse(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentTypes() {
        // Given
        Expression arg1 = new NumberNode(5.0);
        Expression arg2 = new StringNode("5");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When and Then
        operation.computeValue(Mockito.mock(EvalContext.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArgument() {
        // Given
        Expression arg1 = null;
        Expression arg2 = new NumberNode(5.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When and Then
        operation.computeValue(Mockito.mock(EvalContext.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRightArgument() {
        // Given
        Expression arg1 = new NumberNode(5.0);
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When and Then
        operation.computeValue(Mockito.mock(EvalContext.class));
    }

    @Test
    public void testEdgeCases() {
        // Given
        Expression arg1 = new NumberNode(Double.MIN_VALUE);
        Expression arg2 = new NumberNode(Double.MIN_VALUE);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = Mockito.spy(EvalContext.class);

        // When
        Object result = operation.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new NumberNode(Double.MIN_VALUE);
        arg2 = new NumberNode(Double.MIN_VALUE + 1);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new NumberNode(Double.MAX_VALUE);
        arg2 = new NumberNode(Double.MAX_VALUE);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new NumberNode(Double.MAX_VALUE);
        arg2 = new NumberNode(Double.MAX_VALUE - 1);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}