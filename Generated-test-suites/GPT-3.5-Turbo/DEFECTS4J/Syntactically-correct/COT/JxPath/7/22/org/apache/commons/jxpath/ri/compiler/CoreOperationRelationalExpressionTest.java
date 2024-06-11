
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression(null) {
            @Override
            protected Object computeValue(EvaluationContext context) {
                return null;
            }
        };
        int result = instance.getPrecedence();
        Assert.assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression instance = new CoreOperationRelationalExpression(null) {
            @Override
            protected Object computeValue(EvaluationContext context) {
                return null;
            }
        };
        boolean result = instance.isSymmetric();
        Assert.assertFalse(result);
    }

}
