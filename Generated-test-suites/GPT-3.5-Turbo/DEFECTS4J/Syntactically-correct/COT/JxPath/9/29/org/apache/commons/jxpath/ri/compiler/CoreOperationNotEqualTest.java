
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new CoreVariable("arg1");
        Expression arg2 = new CoreVariable("arg2");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        // Test when arg1 and arg2 are equal
        EvalContext context1 = new EvalContext();
        context1.setVariable("arg1", 10);
        context1.setVariable("arg2", 10);
        Object result1 = operation.computeValue(context1);
        assertEquals(Boolean.FALSE, result1);

        // Test when arg1 and arg2 are not equal
        EvalContext context2 = new EvalContext();
        context2.setVariable("arg1", "hello");
        context2.setVariable("arg2", "world");
        Object result2 = operation.computeValue(context2);
        assertEquals(Boolean.TRUE, result2);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new CoreVariable("arg1");
        Expression arg2 = new CoreVariable("arg2");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }

}
