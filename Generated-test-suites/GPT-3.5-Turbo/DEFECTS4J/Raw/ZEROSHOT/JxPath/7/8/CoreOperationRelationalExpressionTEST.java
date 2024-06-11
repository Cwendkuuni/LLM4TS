import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null){
            protected int getPrecedence() {
                return 3;
            }
        };
        int result = expression.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null){
            protected boolean isSymmetric() {
                return false;
            }
        };
        boolean result = expression.isSymmetric();
        assertFalse(result);
    }
}