import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {
    private CoreOperationLessThan lessThan;

    @Before
    public void setup() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        lessThan = new CoreOperationLessThan(arg1, arg2);
    }

    // Test computeValue method
    @Test
    public void testComputeValue() {
        // Test when l < r, expect true
        EvalContext context1 = new EvalContext();
        context1.setVariableValue("arg1", 3);
        context1.setVariableValue("arg2", 5);
        assertEquals(Boolean.TRUE, lessThan.computeValue(context1));

        // Test when l >= r, expect false
        EvalContext context2 = new EvalContext();
        context2.setVariableValue("arg1", 5);
        context2.setVariableValue("arg2", 3);
        assertEquals(Boolean.FALSE, lessThan.computeValue(context2));
    }

    // Test getSymbol method
    @Test
    public void testGetSymbol() {
        assertEquals("<", lessThan.getSymbol());
    }
}