import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testPublicMethods() {
        // Test method getPrecedence()
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        assertEquals(3, expression.getPrecedence());

        // Test method isSymmetric()
        assertEquals(false, expression.isSymmetric());
    }
}