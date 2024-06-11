import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected Object computeValue(EvaluationContext context) {
                return null;
            }
        };
        int expectedPrecedence = 3;
        int actualPrecedence = expression.getPrecedence();
        assertEquals(expectedPrecedence, actualPrecedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected Object computeValue(EvaluationContext context) {
                return null;
            }
        };
        boolean expectedSymmetric = false;
        boolean actualSymmetric = expression.isSymmetric();
        assertEquals(expectedSymmetric, actualSymmetric);
    }

    @Test
    public void testComputeValue_NullArgs() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected Object computeValue(EvaluationContext context) {
                return null;
            }
        };
        Object expectedValue = null;
        Object actualValue = expression.computeValue(null);
        assertEquals(expectedValue, actualValue);
    }

}