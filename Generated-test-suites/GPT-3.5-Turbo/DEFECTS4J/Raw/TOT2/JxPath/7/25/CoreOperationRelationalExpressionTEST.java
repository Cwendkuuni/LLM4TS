import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression() {
            @Override
            protected int getPrecedence() {
                return 3;
            }
        };

        assertEquals(3, core.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression core = new CoreOperationRelationalExpression() {
            @Override
            protected boolean isSymmetric() {
                return false;
            }
        };

        assertFalse(core.isSymmetric());
    }

}