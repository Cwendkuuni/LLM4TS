import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Const(1.0);
        Expression arg2 = new Const(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new Const(1.0);
        arg2 = new Const(1.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new Const(1.0);
        arg2 = new Const(0.5);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}