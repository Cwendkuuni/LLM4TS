import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual operation = new CoreOperationEqual(new MockExpression(2), new MockExpression(2));
        assertTrue((Boolean) operation.computeValue(null));

        operation = new CoreOperationEqual(new MockExpression("test"), new MockExpression("test"));
        assertTrue((Boolean) operation.computeValue(null));

        operation = new CoreOperationEqual(new MockExpression(2), new MockExpression(3));
        assertFalse((Boolean) operation.computeValue(null));

        operation = new CoreOperationEqual(new MockExpression("test"), new MockExpression("different"));
        assertFalse((Boolean) operation.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(null, null);
        assertEquals("=", operation.getSymbol());
    }

    // Additional test cases for edge cases and exceptions can be added here

}

class MockExpression extends Expression {
    private Object value;

    public MockExpression(Object value) {
        this.value = value;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return value;
    }

    @Override
    public boolean computeContextDependent() {
        return false;
    }
}