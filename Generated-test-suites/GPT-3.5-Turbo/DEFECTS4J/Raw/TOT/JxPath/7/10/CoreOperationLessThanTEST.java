import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Typical use case: arg1 < arg2
        CoreOperationLessThan operation = new CoreOperationLessThan(new TestExpression(5), new TestExpression(10));
        assertEquals(Boolean.TRUE, operation.computeValue(null));

        // Typical use case: arg1 >= arg2
        operation = new CoreOperationLessThan(new TestExpression(10), new TestExpression(5));
        assertEquals(Boolean.FALSE, operation.computeValue(null));

        // Edge case: arg1 = arg2
        operation = new CoreOperationLessThan(new TestExpression(10), new TestExpression(10));
        assertEquals(Boolean.FALSE, operation.computeValue(null));

        // Edge case: arg1 < arg2 with decimal values
        operation = new CoreOperationLessThan(new TestExpression(3.14), new TestExpression(3.1415));
        assertEquals(Boolean.TRUE, operation.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        assertEquals("<", operation.getSymbol());
    }

    // Additional test cases for edge cases and error scenarios can be added here

}

class TestExpression extends Expression {
    private Object value;

    public TestExpression(Object value) {
        this.value = value;
    }

    public Object computeValue(EvalContext context) {
        return value;
    }
}