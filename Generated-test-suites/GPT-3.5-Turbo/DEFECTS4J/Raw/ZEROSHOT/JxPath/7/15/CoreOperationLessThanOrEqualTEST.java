import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValueLessThanOrEqual() {
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(null, null);
        
        double l = 5.0;
        double r = 10.0;
        EvalContext context = createContext(l, r);
        
        Object result = lessThanOrEqual.computeValue(context);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValueNotLessThanOrEqual() {
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(null, null);
        
        double l = 10.0;
        double r = 5.0;
        EvalContext context = createContext(l, r);
        
        Object result = lessThanOrEqual.computeValue(context);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(null, null);
        
        String symbol = lessThanOrEqual.getSymbol();
        
        Assert.assertNotNull(symbol);
        Assert.assertEquals("<=", symbol);
    }
    
    private EvalContext createContext(double l, double r) {
        EvalContext context = new EvalContext();
        
        context.setExpression(new Expression());
        context.getExpression().setArgs(new Expression[] { new Expression(), new Expression() });
        
        InfoSetUtil.setValue(context, l, 0);
        InfoSetUtil.setValue(context, r, 1);
        
        return context;
    }
}