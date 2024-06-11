import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operation;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test
    public void testComputeValue_LessThan_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setValue(arg1, 5);
        context.setValue(arg2, 10);
        assertFalse((boolean) operation.computeValue(context));
    }

    @Test
    public void testComputeValue_GreaterThan_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setValue(arg1, 10);
        context.setValue(arg2, 5);
        assertTrue((boolean) operation.computeValue(context));
    }

    @Test
    public void testComputeValue_Equal_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setValue(arg1, 10);
        context.setValue(arg2, 10);
        assertFalse((boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanSymbol() {
        assertEquals(">", operation.getSymbol());
    }
}