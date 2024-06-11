import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        
        // Typical use case
        context.setArg("arg1", 5);
        context.setArg("arg2", 5);
        assertFalse((boolean) operation.computeValue(context));
        
        // Edge case
        context.setArg("arg1", null);
        context.setArg("arg2", null);
        assertFalse((boolean) operation.computeValue(context));
        
        // Error scenario
        context.setArg("arg1", "five");
        context.setArg("arg2", 5);
        assertTrue((boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        assertEquals("!=", operation.getSymbol());
    }
}