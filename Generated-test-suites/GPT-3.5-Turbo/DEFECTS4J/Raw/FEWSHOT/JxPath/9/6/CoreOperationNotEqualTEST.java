import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
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
    public void testComputeValue() {
        operation.setArguments(new Expression[] {new Constant("foo"), new Constant("bar")});
        assertFalse((Boolean) operation.computeValue(context));
        
        operation.setArguments(new Expression[] {new Constant("foo"), new Constant("foo")});
        assertTrue((Boolean) operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        assertEquals("!=", operation.getSymbol());
    }
}