import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(3.5),
                new CoreConstantExpression(4.2)
        );

        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }
}