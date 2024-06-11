import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setup() {
        expression = new CoreOperationRelationalExpression(new Expression[]{});
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testExpressionConstructor() {
        assertNotNull(expression);
    }

    @Test
    public void testExpressionArgs() {
        Expression[] args = expression.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);
    }

}