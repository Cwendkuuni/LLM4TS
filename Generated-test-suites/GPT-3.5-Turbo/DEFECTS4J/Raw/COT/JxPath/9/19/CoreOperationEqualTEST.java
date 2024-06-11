import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
      
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
      
        EvalContext context = new EvalContext();
     
        Object result = operationEqual.computeValue(context);
      
        Assert.assertEquals(Boolean.class, result.getClass());
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
      
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
      
        String symbol = operationEqual.getSymbol();
      
        Assert.assertEquals("=", symbol);
    }
    
    
    // Additional test cases for edge cases and exceptions:
    
    @Test
    public void testComputeValue_withNullArg1() {
        Expression arg1 = null;
        Expression arg2 = new Expression();
      
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
      
        EvalContext context = new EvalContext();
     
        Object result = operationEqual.computeValue(context);
      
        Assert.assertEquals(Boolean.class, result.getClass());
    }
    
    @Test
    public void testComputeValue_withNullArg2() {
        Expression arg1 = new Expression();
        Expression arg2 = null;
      
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
      
        EvalContext context = new EvalContext();
     
        Object result = operationEqual.computeValue(context);
      
        Assert.assertEquals(Boolean.class, result.getClass());
    }
    
    @Test
    public void testComputeValue_withDifferentTypes() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
      
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        arg1.setValue(obj1);
        arg2.setValue(obj2);
      
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
      
        EvalContext context = new EvalContext();
     
        Object result = operationEqual.computeValue(context);
      
        Assert.assertEquals(Boolean.class, result.getClass());
    }
    
    @Test
    public void testComputeValue_withEqualObjects() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
      
        Object obj1 = new Object();
        Object obj2 = obj1;
        
        arg1.setValue(obj1);
        arg2.setValue(obj2);
      
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
      
        EvalContext context = new EvalContext();
     
        Object result = operationEqual.computeValue(context);
      
        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_withNotEqualObjects() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
      
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        arg1.setValue(obj1);
        arg2.setValue(obj2);
      
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
      
        EvalContext context = new EvalContext();
     
        Object result = operationEqual.computeValue(context);
      
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    
}