import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        Assert.assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        Assert.assertFalse(expression.isSymmetric());
    }

}