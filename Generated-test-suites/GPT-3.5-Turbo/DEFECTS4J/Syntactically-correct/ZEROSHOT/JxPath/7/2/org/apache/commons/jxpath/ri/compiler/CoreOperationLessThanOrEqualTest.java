import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanOrEqualTest {
    
    private CoreOperationLessThanOrEqual operation;
    
    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test case 1: arg1 and arg2 both equal to 0
        double arg1Value = 0;
        double arg2Value = 0;
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(arg1Value);
        arg2.setValue(arg2Value);
        double expectedValue1 = InfoSetUtil.doubleValue(arg1.computeValue(context));
        double expectedValue2 = InfoSetUtil.doubleValue(arg2.computeValue(context));
        Object expected = expectedValue1 <= expectedValue2 ? Boolean.TRUE : Boolean.FALSE;
        Object actual = operation.computeValue(context);
        Assert.assertEquals(expected, actual);
        
        // Test case 2: arg1 is less than arg2
        arg1Value = -1;
        arg2Value = 1;
        arg1.setValue(arg1Value);
        arg2.setValue(arg2Value);
        expectedValue1 = InfoSetUtil.doubleValue(arg1.computeValue(context));
        expectedValue2 = InfoSetUtil.doubleValue(arg2.computeValue(context));
        expected = expectedValue1 <= expectedValue2 ? Boolean.TRUE : Boolean.FALSE;
        actual = operation.computeValue(context);
        Assert.assertEquals(expected, actual);
        
        // Test case 3: arg1 is greater than arg2
        arg1Value = 100;
        arg2Value = 50;
        arg1.setValue(arg1Value);
        arg2.setValue(arg2Value);
        expectedValue1 = InfoSetUtil.doubleValue(arg1.computeValue(context));
        expectedValue2 = InfoSetUtil.doubleValue(arg2.computeValue(context));
        expected = expectedValue1 <= expectedValue2 ? Boolean.TRUE : Boolean.FALSE;
        actual = operation.computeValue(context);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSymbol() {
        String expected = "<=";
        String actual = operation.getSymbol();
        Assert.assertEquals(expected, actual);
    }
}