import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValueNotEqual() {
        // Typical case: Check if the computeValue method returns Boolean.TRUE when args are not equal
        Expression arg1 = new CoreOperationVariable();
        Expression arg2 = new CoreOperationVariable();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationNotEqual.computeValue(context));
    }

    @Test
    public void testComputeValueEqual() {
        // Edge case: Check if the computeValue method returns Boolean.FALSE when args are equal
        Expression arg1 = new CoreOperationConstant(5);
        Expression arg2 = new CoreOperationConstant(5);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, coreOperationNotEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        // Typical case: Check if the getSymbol method returns "!="
        Expression arg1 = new CoreOperationVariable();
        Expression arg2 = new CoreOperationVariable();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }
}