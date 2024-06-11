import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_equalValues_returnsTrue() {
        // Arrange
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // Act
        Object result = coreOperationEqual.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_notEqualValues_returnsFalse() {
        // Arrange
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        // Modify arg2 to be different from arg1
        arg2.setValue(5);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // Act
        Object result = coreOperationEqual.computeValue(context);

        // Assert
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol_returnsEqualSymbol() {
        // Arrange
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Act
        String symbol = coreOperationEqual.getSymbol();

        // Assert
        Assert.assertEquals("=", symbol);
    }
}