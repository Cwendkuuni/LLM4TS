
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    // Test computeValue method
    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        EvalContext context = new EvalContext();
        
        // Test when arguments are equal
        operation.args = new Expression[] {new CoreOperation(), new CoreOperation()};
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Test when arguments are not equal
        operation.args = new Expression[] {new CoreOperation(), new CoreOperation()};
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    // Test getSymbol method
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        Assert.assertEquals("!=", operation.getSymbol());
    }
}
