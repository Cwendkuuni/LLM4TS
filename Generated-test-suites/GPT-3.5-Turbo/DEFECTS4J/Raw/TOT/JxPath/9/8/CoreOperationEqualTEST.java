import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setUp() {
        arg1 = new Expression();
        arg2 = new Expression();
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        context = new EvalContext();
    }
    
    @Test
    public void testComputeValue() {
        // Test when both arguments are equal
        arg1.setValue(5);
        arg2.setValue(5);
        assertTrue((boolean) coreOperationEqual.computeValue(context));

        // Test when both arguments are not equal
        arg1.setValue(3);
        arg2.setValue(5);
        assertFalse((boolean) coreOperationEqual.computeValue(context));
        assertFalse((boolean) coreOperationEqual.computeValue(context));

        // Test when both arguments are null
        arg1.setValue(null);
        arg2.setValue(null);
        assertTrue((boolean) coreOperationEqual.computeValue(context));

        // Test when arg1 is null and arg2 is not null
        arg1.setValue(null);
        arg2.setValue(5);
        assertFalse((boolean) coreOperationEqual.computeValue(context));

        // Test when arg1 is not null and arg2 is null
        arg1.setValue(5);
        arg2.setValue(null);
        assertFalse((boolean) coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}