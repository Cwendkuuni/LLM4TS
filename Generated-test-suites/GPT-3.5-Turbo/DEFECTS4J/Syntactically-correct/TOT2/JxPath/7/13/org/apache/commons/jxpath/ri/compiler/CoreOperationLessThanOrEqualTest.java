import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue_basic() {
        CoreOperationLessThanOrEqual coreOp = new CoreOperationLessThanOrEqual(new MockExpression(5), new MockExpression(3));
        assertEquals(Boolean.FALSE, coreOp.computeValue(null));
    }
    
    @Test
    public void testComputeValue_negative() {
        CoreOperationLessThanOrEqual coreOp = new CoreOperationLessThanOrEqual(new MockExpression(-2), new MockExpression(0));
        assertEquals(Boolean.TRUE, coreOp.computeValue(null));
    }
    
    @Test
    public void testComputeValue_equal() {
        CoreOperationLessThanOrEqual coreOp = new CoreOperationLessThanOrEqual(new MockExpression(7), new MockExpression(7));
        assertEquals(Boolean.TRUE, coreOp.computeValue(null));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOp = new CoreOperationLessThanOrEqual(new MockExpression(5), new MockExpression(3));
        assertEquals("<=", coreOp.getSymbol());
    }

    // Edge cases and exceptions to be tested:
    // Null arguments
    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationLessThanOrEqual coreOp = new CoreOperationLessThanOrEqual(null, null);
        coreOp.computeValue(null);
    }
    
    // Large values 
    @Test
    public void testLargeValues() {
        CoreOperationLessThanOrEqual coreOp = new CoreOperationLessThanOrEqual(new MockExpression(Double.MAX_VALUE), new MockExpression(Double.MIN_VALUE));
        assertEquals(Boolean.FALSE, coreOp.computeValue(null));
    }
}

class MockExpression extends Expression {
    
    private Object value;
    
    public MockExpression(Object value) {
        this.value = value;
    }

    public Object computeValue(EvalContext context) {
        return value;
    }
}