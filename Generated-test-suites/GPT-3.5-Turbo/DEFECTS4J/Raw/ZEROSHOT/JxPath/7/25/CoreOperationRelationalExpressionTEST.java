import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setup() {
        expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            public Object computeValue(JXPathContext context) {
                return null;
            }
        };
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean isSymmetric = expression.isSymmetric();
        assertFalse(isSymmetric);
    }
}