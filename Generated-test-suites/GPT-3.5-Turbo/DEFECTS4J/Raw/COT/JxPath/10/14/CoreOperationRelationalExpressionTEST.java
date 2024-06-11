
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression core;

    @Before
    public void setUp() {
        core = new CoreOperationRelationalExpressionMock();
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer("left");
        Object result = core.computeValue(context);
        assertEquals(Boolean.class, result.getClass());
    }

    @Test
    public void testGetPrecedence() {
        int result = core.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        boolean result = core.isSymmetric();
        assertFalse(result);
    }
}

class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
    public CoreOperationRelationalExpressionMock() {
        super(null);
    }

    protected boolean evaluateCompare(int compare) {
        return true;
    }
}
