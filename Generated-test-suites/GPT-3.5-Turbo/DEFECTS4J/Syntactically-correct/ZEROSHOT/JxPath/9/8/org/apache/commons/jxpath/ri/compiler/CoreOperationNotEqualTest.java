import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_equalArguments_returnFalse() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        boolean result = operation.computeValue(context);

        assertEquals(false, result);
    }

    @Test
    public void testGetSymbol_shouldReturnNotEqualSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        String symbol = operation.getSymbol();

        assertEquals("!=", symbol);
    }
}

class Expression {
    // Add necessary code for Expression if needed
}