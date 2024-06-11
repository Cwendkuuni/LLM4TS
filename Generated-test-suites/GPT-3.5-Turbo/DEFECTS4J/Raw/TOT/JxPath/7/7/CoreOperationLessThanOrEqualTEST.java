import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual coreOperationLessThanOrEqual;
    private MockEvalContext mockEvalContext;

    @Before
    public void setUp() {
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(null, null);
        mockEvalContext = new MockEvalContext();
    }

    // Test computeValue() method

    @Test
    public void testComputeValue_LeftValueLessThanRightValue_ReturnsTrue() {
        double leftValue = 5.0;
        double rightValue = 10.0;
        mockEvalContext.setComputedValues(leftValue, rightValue);
        
        Object result = coreOperationLessThanOrEqual.computeValue(mockEvalContext);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftValueEqualToRightValue_ReturnsTrue() {
        double leftValue = 5.0;
        double rightValue = 5.0;
        mockEvalContext.setComputedValues(leftValue, rightValue);
        
        Object result = coreOperationLessThanOrEqual.computeValue(mockEvalContext);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LeftValueGreaterThanRightValue_ReturnsFalse() {
        double leftValue = 10.0;
        double rightValue = 5.0;
        mockEvalContext.setComputedValues(leftValue, rightValue);
        
        Object result = coreOperationLessThanOrEqual.computeValue(mockEvalContext);
        
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullLeftValue_ThrowsNullPointerException() {
        double rightValue = 10.0;
        mockEvalContext.setComputedValues(null, rightValue);
        
        coreOperationLessThanOrEqual.computeValue(mockEvalContext);
    }
    
    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullRightValue_ThrowsNullPointerException() {
        double leftValue = 5.0;
        mockEvalContext.setComputedValues(leftValue, null);
        
        coreOperationLessThanOrEqual.computeValue(mockEvalContext);
    }
    
    // Test getSymbol() method
    
    @Test
    public void testGetSymbol_ReturnsLessThanOrEqualSymbol() {
        String symbol = coreOperationLessThanOrEqual.getSymbol();
        
        assertEquals("<=", symbol);
    }
}