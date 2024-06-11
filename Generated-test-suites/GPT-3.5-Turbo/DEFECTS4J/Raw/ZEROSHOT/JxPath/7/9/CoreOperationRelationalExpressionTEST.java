import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void getPrecedenceTest() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        int result = expression.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void isSymmetricTest() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        boolean result = expression.isSymmetric();
        assertFalse(result);
    }
}