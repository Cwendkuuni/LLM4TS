import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {

    private CoreOperationEqual operationEqual;

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        operationEqual = new CoreOperationEqual(arg1, arg2);
        
        // Test when equal
        arg1.setValue("value");
        arg2.setValue("value");
        assertTrue((Boolean) operationEqual.computeValue(context));

        // Test when not equal
        arg1.setValue("value");
        arg2.setValue("different");
        assertFalse((Boolean) operationEqual.computeValue(context));
    }


    @Test
    public void testGetSymbol() {
        operationEqual = new CoreOperationEqual(null, null);
        assertEquals("=", operationEqual.getSymbol());
    }
}