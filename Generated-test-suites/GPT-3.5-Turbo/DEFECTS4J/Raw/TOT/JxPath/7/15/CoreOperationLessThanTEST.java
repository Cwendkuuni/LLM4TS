import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Test with two integer values
        Expression arg1 = new IntegerExpression(5);
        Expression arg2 = new IntegerExpression(10);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Assert.assertEquals(Boolean.TRUE, lessThan.computeValue(context));

        // Test with two double values
        Expression arg3 = new DoubleExpression(1.5);
        Expression arg4 = new DoubleExpression(2.5);
        CoreOperationLessThan lessThan2 = new CoreOperationLessThan(arg3, arg4);
        
        EvalContext context2 = new EvalContext();
        Assert.assertEquals(Boolean.TRUE, lessThan2.computeValue(context2));

        // Test with one integer value and one double value
        Expression arg5 = new IntegerExpression(5);
        Expression arg6 = new DoubleExpression(9.5);
        CoreOperationLessThan lessThan3 = new CoreOperationLessThan(arg5, arg6);
        
        EvalContext context3 = new EvalContext();
        Assert.assertEquals(Boolean.TRUE, lessThan3.computeValue(context3));

        // Test with two string values
        Expression arg7 = new StringExpression("apple");
        Expression arg8 = new StringExpression("banana");
        CoreOperationLessThan lessThan4 = new CoreOperationLessThan(arg7, arg8);
        
        EvalContext context4 = new EvalContext();
        Assert.assertEquals(Boolean.TRUE, lessThan4.computeValue(context4));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new IntegerExpression(5);
        Expression arg2 = new IntegerExpression(10);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        
        Assert.assertEquals("<", lessThan.getSymbol());
    }
   
}