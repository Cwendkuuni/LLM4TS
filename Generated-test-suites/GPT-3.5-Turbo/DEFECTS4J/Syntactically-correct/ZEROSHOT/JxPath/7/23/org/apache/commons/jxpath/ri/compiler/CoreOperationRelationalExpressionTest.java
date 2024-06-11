import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null);
        int precedence = expr.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null);
        boolean isSymmetric = expr.isSymmetric();
        assertFalse(isSymmetric);
    }

    // Add more test methods here...

}