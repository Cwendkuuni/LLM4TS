import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValueEqual() {
        Expression arg1 = new MockExpression("value");
        Expression arg2 = new MockExpression("value");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new MockEvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueNotEqual() {
        Expression arg1 = new MockExpression("value1");
        Expression arg2 = new MockExpression("value2");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new MockEvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new MockExpression("value1");
        Expression arg2 = new MockExpression("value2");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    @Test
    public void testErrorCaseNullArguments() {
        Expression arg1 = null;
        Expression arg2 = new MockExpression("value");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new MockEvalContext();
        try {
            coreOperationEqual.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEdgeCaseEmptyValues() {
        Expression arg1 = new MockExpression("");
        Expression arg2 = new MockExpression("");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new MockEvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}