import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    private final CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, null);

    @Test
    public void testComputeValue() {
        // Typical case: Left value less than right value
        EvalContext context1 = new EvalContext();
        context1.setContextNodePointer(new NodePointer());
        context1.getJXPathContext().getContextBeanInfo().getVariables().declareVariable("var1", 10);
        context1.getJXPathContext().getContextBeanInfo().getVariables().declareVariable("var2", 20);
        operation.args[0] = new Variable("var1");
        operation.args[1] = new Variable("var2");
        assertTrue((Boolean) operation.computeValue(context1));

        // Edge case: Left value equals right value
        EvalContext context2 = new EvalContext();
        context2.setContextNodePointer(new NodePointer());
        context2.getJXPathContext().getContextBeanInfo().getVariables().declareVariable("var3", 30);
        operation.args[0] = new Variable("var3");
        operation.args[1] = new Variable("var3");
        assertTrue((Boolean) operation.computeValue(context2));

        // Error scenario: Invalid data type
        EvalContext context3 = new EvalContext();
        context3.setContextNodePointer(new NodePointer());
        context3.getJXPathContext().getContextBeanInfo().getVariables().declareVariable("var4", "string");
        context3.getJXPathContext().getContextBeanInfo().getVariables().declareVariable("var5", 10);
        operation.args[0] = new Variable("var4");
        operation.args[1] = new Variable("var5");
        try {
            operation.computeValue(context3);
            fail("Exception not thrown for invalid data type");
        } catch (Exception e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testGetSymbol() {
        // Typical case: Check if symbol is "<="
        assertEquals("<=", operation.getSymbol());
    }

}