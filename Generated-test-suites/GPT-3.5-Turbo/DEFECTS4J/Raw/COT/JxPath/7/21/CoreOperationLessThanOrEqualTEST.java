import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    private Expression arg1;
    private Expression arg2;
    private CoreOperationLessThanOrEqual coreOperationLessThanOrEqual;

    @Before
    public void setUp() {
        arg1 = new Expression();
        arg2 = new Expression();
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_arg1LessThanarg2_returnsTrue() {
        arg1.setValue(1);
        arg2.setValue(2);
        EvalContext context = new EvalContext();
        assertTrue((boolean)coreOperationLessThanOrEqual.computeValue(context));
    }
    
    @Test
    public void testComputeValue_arg1Equalsarg2_returnsTrue() {
        arg1.setValue(2);
        arg2.setValue(2);
        EvalContext context = new EvalContext();
        assertTrue((boolean)coreOperationLessThanOrEqual.computeValue(context));
    }
    
    @Test
    public void testComputeValue_arg1GreaterThanarg2_returnsFalse() {
        arg1.setValue(3);
        arg2.setValue(2);
        EvalContext context = new EvalContext();
        assertFalse((boolean)coreOperationLessThanOrEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol_returnsLessThanOrEqualSymbol() {
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());
    }

}