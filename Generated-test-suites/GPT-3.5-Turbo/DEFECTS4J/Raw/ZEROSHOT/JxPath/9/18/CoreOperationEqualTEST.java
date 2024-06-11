import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        Assert.assertTrue(result instanceof Boolean);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        Assert.assertEquals("=", symbol);
    }
}