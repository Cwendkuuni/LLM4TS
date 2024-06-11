import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue_typicalCase() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();

        arg1.setValue(5);
        arg2.setValue(3);
        boolean result = (Boolean)op.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_edgeCase() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();

        arg1.setValue(Double.MAX_VALUE - 1);
        arg2.setValue(Double.MAX_VALUE);
        boolean result = (Boolean)op.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeValue_errorCase() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();

        arg1.setValue("5");
        arg2.setValue(3);
        boolean result = (Boolean)op.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        String symbol = op.getSymbol();

        assertEquals(">", symbol);
    }
}