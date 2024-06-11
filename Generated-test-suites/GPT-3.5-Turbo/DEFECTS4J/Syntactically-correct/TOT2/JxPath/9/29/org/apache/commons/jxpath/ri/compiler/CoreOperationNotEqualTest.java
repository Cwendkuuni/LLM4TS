import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_NotEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression("5"), new Expression("10"));
        EvalContext evalContext = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(evalContext);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression("5"), new Expression("10"));
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    @Test
    public void testComputeValue_Equal() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression("5"), new Expression("5"));
        EvalContext evalContext = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(evalContext);
        assertEquals(Boolean.FALSE, result);
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here

}