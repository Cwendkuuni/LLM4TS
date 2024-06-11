
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        // Arrange
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // Act
        Object result = operation.computeValue(context);

        // Assert
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        // Arrange
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        // Act
        String symbol = operation.getSymbol();

        // Assert
        assertEquals("=", symbol);
    }
}
