
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(5), new ExpressionStub(3));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        CoreOperationGreaterThanOrEqual expression2 = new CoreOperationGreaterThanOrEqual(new ExpressionStub(3), new ExpressionStub(5));
        EvalContext context2 = new EvalContext();
        Object result2 = expression2.computeValue(context2);
        assertTrue(result2 instanceof Boolean);
        assertFalse((Boolean) result2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new ExpressionStub(5), new ExpressionStub(3));
        assertEquals(">=", expression.getSymbol());

        CoreOperationGreaterThanOrEqual expression2 = new CoreOperationGreaterThanOrEqual(new ExpressionStub(3), new ExpressionStub(5));
        assertEquals(">=", expression2.getSymbol());
    }

    // Additional test cases for potential edge cases and exceptions can be added here

}

// Helper class for testing
class ExpressionStub implements Expression {
    private Object value;

    public ExpressionStub(Object value) {
        this.value = value;
    }

    public Object computeValue(EvalContext context) {
        return value;
    }
}
