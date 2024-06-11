import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operationGreaterThan;

    @Before
    public void setUp() {
        // Initialize the CoreOperationGreaterThan instance
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test
    public void testComputeValueGreaterThan() {
        // Test when the first argument is greater than the second argument
        EvalContext context = new EvalContext();
        context.setContextNode(new Object()); // Set the context node to some object
        double arg1Value = 5.0;
        double arg2Value = 2.0;
        InfoSetUtil.putValue(context, arg1Value);
        InfoSetUtil.putValue(context, arg2Value);
        Object result = operationGreaterThan.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueNotGreaterThan() {
        // Test when the first argument is not greater than the second argument
        EvalContext context = new EvalContext();
        context.setContextNode(new Object()); // Set the context node to some object
        double arg1Value = 2.0;
        double arg2Value = 5.0;
        InfoSetUtil.putValue(context, arg1Value);
        InfoSetUtil.putValue(context, arg2Value);
        Object result = operationGreaterThan.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        // Test the getSymbol method
        String symbol = operationGreaterThan.getSymbol();
        assertEquals(">", symbol);
    }
}