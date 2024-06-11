import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    CoreOperationNotEqual operation = new CoreOperationNotEqual(expression1, expression2);

    @Test
    public void testComputeValue() {
        // Test case 1: arg1 and arg2 are not equal
        EvalContext context1 = new EvalContext();
        context1.setValue(expression1, "value1");
        context1.setValue(expression2, "value2");
        Object result1 = operation.computeValue(context1);
        assertTrue("Expected result to be true", result1.equals(Boolean.TRUE));

        // Test case 2: arg1 and arg2 are equal
        EvalContext context2 = new EvalContext();
        context2.setValue(expression1, "value");
        context2.setValue(expression2, "value");
        Object result2 = operation.computeValue(context2);
        assertTrue("Expected result to be false", result2.equals(Boolean.FALSE));
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals("Expected symbol to be !=", "!=");
    }
}