import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_TypicalUseCase() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        arg1.setValue(5);
        arg2.setValue(10);
        assertEquals(Boolean.TRUE, coreOperation.computeValue(context));
    }

    @Test
    public void testComputeValue_BothArgumentsEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        arg1.setValue(10);
        arg2.setValue(10);
        assertEquals(Boolean.TRUE, coreOperation.computeValue(context));
    }

    @Test
    public void testComputeValue_FirstArgumentGreater() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        arg1.setValue(15);
        arg2.setValue(10);
        assertEquals(Boolean.FALSE, coreOperation.computeValue(context));
    }

    @Test
    public void testComputeValue_ExceptionalScenario() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        arg1.setValue(null);
        arg2.setValue(10);
        Exception exception = assertThrows(NullPointerException.class,
                () -> coreOperation.computeValue(context));
        assertEquals("arg1 is null", exception.getMessage());

        arg1.setValue(20);
        arg2.setValue(null);
        exception = assertThrows(NullPointerException.class,
                () -> coreOperation.computeValue(context));
        assertEquals("arg2 is null", exception.getMessage());
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("<=", coreOperation.getSymbol());
    }
}