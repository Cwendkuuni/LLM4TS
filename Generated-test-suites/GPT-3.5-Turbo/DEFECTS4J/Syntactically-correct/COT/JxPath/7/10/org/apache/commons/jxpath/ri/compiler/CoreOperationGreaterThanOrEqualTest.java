
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        // Test case 1: arg1 >= arg2 should return true
        EvalContext context1 = new EvalContext();
        context1.setVariable("arg1", 5);
        context1.setVariable("arg2", 3);
        assertEquals(true, expression.computeValue(context1));
        
        // Test case 2: arg1 >= arg2 should return false
        EvalContext context2 = new EvalContext();
        context2.setVariable("arg1", 3);
        context2.setVariable("arg2", 5);
        assertEquals(false, expression.computeValue(context2));
    }

}
