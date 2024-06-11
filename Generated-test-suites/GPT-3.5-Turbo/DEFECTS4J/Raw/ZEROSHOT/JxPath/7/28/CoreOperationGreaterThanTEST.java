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
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.setContextNode(arg1);
        double l = InfoSetUtil.doubleValue(arg1.computeValue(context));
        double r = InfoSetUtil.doubleValue(arg2.computeValue(context));
        assertEquals(l > r ? Boolean.TRUE : Boolean.FALSE, operationGreaterThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals(">", operationGreaterThan.getSymbol());
    }
}