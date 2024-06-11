import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationEqualTest {
    
    private CoreOperationEqual operation;
    
    @Before
    public void setup() {
        operation = new CoreOperationEqual(expression1, expression2);
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = createContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        assertTrue(operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        assertEquals("=", operation.getSymbol());
    }
    
    @Test
    public void testEqual() {
        EvalContext context = createContext();
        assertTrue(operation.equal(context, expression1, expression1));
        assertFalse(operation.equal(context, expression1, expression2));
    }
    
    private EvalContext createContext() {
        // TODO: create an instance of EvalContext and return it
    }
}