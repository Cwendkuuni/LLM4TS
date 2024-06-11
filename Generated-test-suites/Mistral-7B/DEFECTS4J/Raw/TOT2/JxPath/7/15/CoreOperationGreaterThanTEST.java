import static org.junit.Assert.assertEquals;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new LiteralExpression(3.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}