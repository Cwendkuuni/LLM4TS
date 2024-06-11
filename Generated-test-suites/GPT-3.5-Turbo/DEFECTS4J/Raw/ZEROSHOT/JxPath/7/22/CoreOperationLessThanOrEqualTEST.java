import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue_WhenArg1IsLessThanOrEqualToArg2_ReturnsTrue() {
        // Arrange
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        double arg1Value = 5.0;
        double arg2Value = 10.0;
        InfoSetUtil.doubleValue(arg1Value);
        InfoSetUtil.doubleValue(arg2Value);
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        Assert.assertEquals(true, result);
    }
    
    @Test
    public void testComputeValue_WhenArg1IsGreaterThanArg2_ReturnsFalse() {
        // Arrange
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        double arg1Value = 10.0;
        double arg2Value = 5.0;
        InfoSetUtil.doubleValue(arg1Value);
        InfoSetUtil.doubleValue(arg2Value);
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testGetSymbol_ReturnsLessThanOrEqualSymbol() {
        // Arrange
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        // Act
        String symbol = operation.getSymbol();
        
        // Assert
        Assert.assertEquals("<=", symbol);
    }
}