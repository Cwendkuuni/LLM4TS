import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    // Test case for constructor
    @Test
    public void testCoreOperationEqualConstructor() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertNotNull(coreOperationEqual);
    }

    // Test case for computeValue method - typical case where args are equal
    @Test
    public void testComputeValueEqualArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new MockExpression("test"), new MockExpression("test"));
        assertTrue((boolean) coreOperationEqual.computeValue(null));
    }

    // Test case for computeValue method - typical case where args are not equal
    @Test
    public void testComputeValueNotEqualArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new MockExpression("test"), new MockExpression("test2"));
        assertFalse((boolean) coreOperationEqual.computeValue(null));
    }

    // Test case for getSymbol method
    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new MockExpression(), new MockExpression());
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    // Edge case: testing with null arguments
    @Test
    public void testComputeValueNullArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        assertFalse((boolean) coreOperationEqual.computeValue(null));
    }

    // Edge case: testing with numbers as arguments
    @Test
    public void testComputeValueNumberArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new MockExpression(5), new MockExpression(5));
        assertTrue((boolean) coreOperationEqual.computeValue(null));
    }

    // Error scenario: testing with non-matching argument types
    @Test(expected = ClassCastException.class)
    public void testComputeValueNonMatchingArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new MockExpression("test"), new MockExpression(5));
        coreOperationEqual.computeValue(null);
    }

}

class MockExpression extends Expression {
    private Object value;

    public MockExpression() {
        this.value = null;
    }

    public MockExpression(Object value) {
        this.value = value;
    }

    public Object computeValue(EvalContext context) {
        return value;
    }
}