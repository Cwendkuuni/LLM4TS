import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Typical Use Case
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operator = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();

        // Create mocks and set expected values for doubleValue
        InfoSetUtil infoSetUtilMock = mock(InfoSetUtil.class);
        when(infoSetUtilMock.doubleValue(arg1.computeValue(context))).thenReturn(5.0);
        when(infoSetUtilMock.doubleValue(arg2.computeValue(context))).thenReturn(10.0);

        // Test for l < r
        Object result = operator.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test for l = r
        when(infoSetUtilMock.doubleValue(arg2.computeValue(context))).thenReturn(5.0);
        result = operator.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test for l > r
        when(infoSetUtilMock.doubleValue(arg2.computeValue(context))).thenReturn(1.0);
        result = operator.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Edge Case: arg1.computeValue(context) returns null
        when(infoSetUtilMock.doubleValue(arg1.computeValue(context))).thenReturn(null);
        result = operator.computeValue(context);
        assertNull(result);

        // Edge Case: arg2.computeValue(context) returns null
        when(infoSetUtilMock.doubleValue(arg1.computeValue(context))).thenReturn(5.0);
        when(infoSetUtilMock.doubleValue(arg2.computeValue(context))).thenReturn(null);
        result = operator.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        // Typical Use Case
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operator = new CoreOperationLessThan(arg1, arg2);

        // Test for "<" symbol
        String symbol = operator.getSymbol();
        assertEquals("<", symbol);

        // Edge Case: arg1.computeValue(context) returns null
        operator = new CoreOperationLessThan(null, arg2);
        symbol = operator.getSymbol();
        assertEquals("<", symbol);

        // Edge Case: arg2.computeValue(context) returns null
        operator = new CoreOperationLessThan(arg1, null);
        symbol = operator.getSymbol();
        assertEquals("<", symbol);
    }

}