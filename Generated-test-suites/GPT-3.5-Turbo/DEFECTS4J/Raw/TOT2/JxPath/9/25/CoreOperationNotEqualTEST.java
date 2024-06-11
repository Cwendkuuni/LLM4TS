import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(coreOperationNotEqual);
    }

    @Test
    public void testComputeValue() {
        MockEvalContext context = new MockEvalContext();
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    // Edge cases and exception handling scenarios can be added here

    // Possible additional test cases:

    // @Test
    // public void testComputeValueWithNonEqualArgs() {
    //     MockEvalContext context = new MockEvalContext();
    //     Expression arg1 = new MockExpression(); // Different
    //     Expression arg2 = new MockExpression(); // Different
    //     CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    //     Object result = coreOperationNotEqual.computeValue(context);
    //     assertEquals(Boolean.TRUE, result);
    // }

    // @Test
    // public void testComputeValueWithEqualArgs() {
    //     MockEvalContext context = new MockEvalContext();
    //     Expression arg1 = new MockExpression(); // Same
    //     Expression arg2 = new MockExpression(); // Same
    //     CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    //     Object result = coreOperationNotEqual.computeValue(context);
    //     assertEquals(Boolean.FALSE, result);
    // }

}