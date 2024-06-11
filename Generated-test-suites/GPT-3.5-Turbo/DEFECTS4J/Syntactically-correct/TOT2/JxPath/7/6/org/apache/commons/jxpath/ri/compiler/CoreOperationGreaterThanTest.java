import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue_basicCase() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new SomeExpression(), new SomeExpression());
        EvalContext context = new EvalContext();
        context.setVariable("var1", 5.0);
        context.setVariable("var2", 3.0);
        assertTrue((Boolean)operation.computeValue(context));
    }

    @Test
    public void testComputeValue_equalValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new SomeExpression(), new SomeExpression());
        EvalContext context = new EvalContext();
        context.setVariable("var1", 5.0);
        context.setVariable("var2", 5.0);
        assertFalse((Boolean)operation.computeValue(context));
    }

    @Test
    public void testComputeValue_invalidValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new SomeExpression(), new SomeExpression());
        EvalContext context = new EvalContext();
        context.setVariable("var1", "invalid");
        context.setVariable("var2", 3.0);
        // Handle illegal argument exception or other handling in CoreOperationGreaterThan
        // assertThrows(Exception.class, () -> operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new SomeExpression(), new SomeExpression());
        assertEquals(">", operation.getSymbol());
    }

    // Add more test cases for edge cases and exceptions as needed

    // SomeExpression class can be mocked for testing purposes
    private class SomeExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return context.getVariable("var1");
        }

        @Override
        public String toString() {
            return "SomeExpression";
        }
    }
}