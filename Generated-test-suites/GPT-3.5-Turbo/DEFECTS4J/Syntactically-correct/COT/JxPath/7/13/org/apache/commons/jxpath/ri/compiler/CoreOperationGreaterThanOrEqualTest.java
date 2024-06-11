
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValueGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.set("arg1", 5);
        context.set("arg2", 3);
        
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }
}
