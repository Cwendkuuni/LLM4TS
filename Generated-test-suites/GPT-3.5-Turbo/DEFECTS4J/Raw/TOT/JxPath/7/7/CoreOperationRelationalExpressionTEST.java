import org.junit.*;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression op;

    @Before
    public void setUp() {
        op = new CoreOperationRelationalExpression(new Expression[2]);
    }

    @Test
    public void testGetPrecedence() {
        int result = op.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        boolean result = op.isSymmetric();
        assertFalse(result);
    }

}