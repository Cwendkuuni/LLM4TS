import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    private CoreOperationNotEqual operation;
    private EvalContext context;
    
    @Before
    public void setUp() {
        operation = new CoreOperationNotEqual(null, null);
        context = new EvalContext();
    }
    
    @Test
    public void testComputeValue_equalObjects() {
        Integer arg1 = 5;
        Integer arg2 = 5;
        operation.setArgs(new Expression[]{arg1, arg2});
        
        Object result = operation.computeValue(context);
        
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_unequalObjects() {
        Integer arg1 = 5;
        Integer arg2 = 10;
        operation.setArgs(new Expression[]{arg1, arg2});
        
        Object result = operation.computeValue(context);
        
        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_nullObjects() {
        operation.setArgs(new Expression[]{null, null});
        
        Object result = operation.computeValue(context);
        
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        
        Assert.assertEquals("!=", symbol);
    }
}