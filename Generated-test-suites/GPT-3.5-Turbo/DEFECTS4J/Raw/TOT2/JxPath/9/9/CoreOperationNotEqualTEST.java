import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNodePointer(new NodePointer());
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        // Typical use case
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        String symbol = coreOperationNotEqual.getSymbol();

        assertTrue(symbol.equals("!="));
    }

    @Test
    public void testComputeValueEdgeCases() {
        // Edge case 1: When args are equal
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNodePointer(new NodePointer());
        context.getVariables().declareVariable(null, "var1", null);
        context.getVariables().declareVariable(null, "var2", null);
        
        // Equal variables as args
        context.getVariables().declareVariable(null, "var1", "value");
        context.getVariables().declareVariable(null, "var2", "value");

        Object result = coreOperationNotEqual.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgs() {
        // Error scenario: Passing null args
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, null);
        coreOperationNotEqual.computeValue(new EvalContext());
    }

}