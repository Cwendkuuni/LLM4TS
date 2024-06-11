import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValueWithEqualArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ExpressionMock("arg1"), new ExpressionMock("arg1"));
        assertTrue((Boolean) coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValueWithDifferentArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ExpressionMock("arg1"), new ExpressionMock("arg2"));
        assertFalse((Boolean) coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ExpressionMock("arg1"), new ExpressionMock("arg2"));
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    // Edge cases and exception handling scenarios

    @Test
    public void testComputeValueWithNullArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, new ExpressionMock("arg2"));
        try {
            coreOperationEqual.computeValue(context);
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

}

class ExpressionMock extends Expression {
    private String name;

    public ExpressionMock(String name) {
        this.name = name;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return null; // Dummy implementation
    }

    @Override
    public String toString() {
        return name;
    }
}

class EvalContext {
    // Dummy class for context simulation
}