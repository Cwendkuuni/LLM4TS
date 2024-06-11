import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        EvalContext context = new EvalContext(null);
        Object result = coreOperationEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }
}