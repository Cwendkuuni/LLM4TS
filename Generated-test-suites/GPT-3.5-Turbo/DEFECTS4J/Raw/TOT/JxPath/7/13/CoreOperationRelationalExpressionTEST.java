import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setup() {
        expression = new SampleOperationRelationalExpression();
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

    class SampleOperationRelationalExpression extends CoreOperationRelationalExpression {

        public SampleOperationRelationalExpression() {
            super(null); // args not used in these tests
        }

        @Override
        protected Object performEvaluation() {
            // performEvaluation not relevant for these tests
            return null;
        }

    }

}