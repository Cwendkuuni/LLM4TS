import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setVariable("var1", "value");
        context.setVariable("var2", "value");

        Object result = operation.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        assertEquals("=", operation.getSymbol());
    }

    @Test
    public void testComputeValueWithDifferentValues() {
        Expression arg1 = new Expression();
        arg1.setValue("value1");
        Expression arg2 = new Expression();
        arg2.setValue("value2");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithNullArguments() {
        CoreOperationEqual operation = new CoreOperationEqual(null, null);

        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }
}