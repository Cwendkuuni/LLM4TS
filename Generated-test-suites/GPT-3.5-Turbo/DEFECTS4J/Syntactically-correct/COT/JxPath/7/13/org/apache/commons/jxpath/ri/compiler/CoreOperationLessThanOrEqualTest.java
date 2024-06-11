
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    // Test computeValue method
    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are equal
        Double arg1 = 5.0;
        Double arg2 = 5.0;
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test when arg1 is less than arg2
        arg1 = 3.0;
        arg2 = 5.0;
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test when arg1 is greater than arg2
        arg1 = 7.0;
        arg2 = 5.0;
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Test when arg1 and arg2 are not comparable
        arg1 = "hello";
        arg2 = 5.0;
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    // Test getSymbol method
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        Assert.assertEquals("<=", operation.getSymbol());
    }
}
