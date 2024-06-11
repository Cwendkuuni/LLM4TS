import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args);
        int precedence = exp.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression exp = new CoreOperationRelationalExpression(args);
        boolean isSymmetric = exp.isSymmetric();
        assertFalse(isSymmetric);
    }
}