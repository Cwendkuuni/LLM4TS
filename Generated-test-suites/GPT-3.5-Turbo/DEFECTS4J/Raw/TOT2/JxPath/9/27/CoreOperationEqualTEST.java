import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_EqualArgs() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(new TestExpression(5), new TestExpression(5));
        assertTrue((boolean) operationEqual.computeValue(null));
    }

    @Test
    public void testComputeValue_NotEqualArgs() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(new TestExpression(5), new TestExpression(10));
        assertFalse((boolean) operationEqual.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(null, null);
        assertEquals("=", operationEqual.getSymbol());
    }

    // Additional test cases for edge cases and exception handling scenarios
    @Test
    public void testComputeValue_NullArgs() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(null, null);
        assertFalse((boolean) operationEqual.computeValue(null));
    }
}

class TestExpression extends Expression {
    private final Object value;

    TestExpression(Object value) {
        this.value = value;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return value;
    }

    @Override
    public String getExpression() {
        return null;
    }
}