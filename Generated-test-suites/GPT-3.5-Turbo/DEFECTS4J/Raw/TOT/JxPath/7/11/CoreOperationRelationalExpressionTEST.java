import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        int expectedPrecedence = 3;
        int result = expression.getPrecedence();
        assertEquals(expectedPrecedence, result);
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        boolean expectedResult = false;
        boolean result = expression.isSymmetric();
        assertEquals(expectedResult, result);
    }

}