import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {

    // Test computeValue() method
    @Test
    public void testComputeValue() {
        EvalContext context = null; // add test input for EvalContext

        // Test simple case where arg1 < arg2
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(10);
        arg2.setValue(20);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue((boolean) result);

        // Test simple case where arg1 >= arg2
        arg1.setValue(30);
        arg2.setValue(20);
        result = operation.computeValue(context);
        assertFalse((boolean) result);

        // Test case where arg1 and arg2 are equal
        arg1.setValue(10);
        arg2.setValue(10);
        result = operation.computeValue(context);
        assertFalse((boolean) result);
    }

    // Test getSymbol() method
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }

}