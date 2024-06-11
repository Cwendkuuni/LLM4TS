
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        // Test when arg1 is greater than arg2
        EvalContext context = new EvalContext();
        arg1.setValue(5);
        arg2.setValue(2);
        assertTrue((Boolean) op.computeValue(context));
        
        // Test when arg1 is equal to arg2
        context = new EvalContext();
        arg1.setValue(5);
        arg2.setValue(5);
        assertTrue((Boolean) op.computeValue(context));
        
        // Test when arg1 is less than arg2
        context = new EvalContext();
        arg1.setValue(2);
        arg2.setValue(5);
        assertFalse((Boolean) op.computeValue(context));
        
        // Add more test cases here for all the edge cases and exceptions
        // ...
        
    }

    // Add more test cases for other public methods here
    
}
