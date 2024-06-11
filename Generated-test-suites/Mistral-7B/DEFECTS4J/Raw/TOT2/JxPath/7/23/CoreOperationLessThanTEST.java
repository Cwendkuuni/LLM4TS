import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        double l = 5.0;
        double r = 10.0;
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return l;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return r;
            }
        };
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        l = 3.0;
        r = 5.0;
        context = new EvalContext();
        arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return l;
            }
        };
        arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return r;
            }
        };
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}