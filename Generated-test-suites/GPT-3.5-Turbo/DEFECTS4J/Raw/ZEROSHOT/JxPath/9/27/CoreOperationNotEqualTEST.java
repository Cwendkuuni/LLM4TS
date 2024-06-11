import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);
        
        // Test case 1
        EvalContext context1 = new EvalContext();
        context1.setVariable("arg1", "value1");
        context1.setVariable("arg2", "value2");
        Object result1 = op.computeValue(context1);
        assertEquals(true, result1);
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        context2.setVariable("arg1", "value1");
        context2.setVariable("arg2", "value1");
        Object result2 = op.computeValue(context2);
        assertEquals(false, result2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);
        String symbol = op.getSymbol();
        assertEquals("!=", symbol);
    }

}