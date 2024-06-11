import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new Constant(new Double(5.0)),
                new Constant(new Double(3.0))
        );
        EvalContext context = TestUtils.newContext();
        InfoSet infoSet = TestUtils.newInfoSet();
        context.setInfoSet(infoSet);
        Boolean result = (Boolean) operation.computeValue(context);
        assertTrue(result);
    }
}