
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        // Test for l < r
        double l = 10.5;
        double r = 20.0;
        EvalContext context = new EvalContext();
        context.setValue("arg1", l);
        context.setValue("arg2", r);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test for l >= r
        double l2 = 50.0;
        double r2 = 30.5;
        EvalContext context2 = new EvalContext();
        context2.setValue("arg1", l2);
        context2.setValue("arg2", r2);
        Object result2 = operation.computeValue(context2);
        assertTrue(result2 instanceof Boolean);
        assertFalse((Boolean) result2);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }
}
