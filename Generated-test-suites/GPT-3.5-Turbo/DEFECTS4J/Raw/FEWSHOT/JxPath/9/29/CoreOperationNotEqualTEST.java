import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    private CoreOperationNotEqual operation;
    private EvalContext context;
    
    @Before
    public void setUp() {
        operation = new CoreOperationNotEqual(null, null);
        context = new EvalContext();
    }
    
    @Test
    public void testComputeValue_equalArguments_returnFalse() {
        context.setNodeSet(new Object[] {"abc"});
        operation.setArguments(new Expression[] {new Constant("abc"), new Constant("abc")});
        assertFalse((boolean) operation.computeValue(context));
    }
    
    @Test
    public void testComputeValue_notEqualArguments_returnTrue() {
        context.setNodeSet(new Object[] {"abc"});
        operation.setArguments(new Expression[] {new Constant("abc"), new Constant("def")});
        assertTrue((boolean) operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        assertEquals("!=", operation.getSymbol());
    }
}