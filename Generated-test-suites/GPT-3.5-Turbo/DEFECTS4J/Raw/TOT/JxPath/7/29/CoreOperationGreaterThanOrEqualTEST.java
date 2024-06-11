import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationGreaterThanOrEqualTest {
    
    CoreOperationGreaterThanOrEqual operation;
    Expression arg1;
    Expression arg2;
    
    @Before
    public void setUp() {
        arg1 = new Expression();
        arg2 = new Expression();
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }
    
    @Test
    public void testComputeValue_typicalCase1() {
        // typical case
        arg1.setValue(5);
        arg2.setValue(3);
        
        boolean result = (boolean) operation.computeValue(null);
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_typicalCase2() {
        // typical case
        arg1.setValue(10);
        arg2.setValue(10);
        
        boolean result = (boolean) operation.computeValue(null);
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_typicalCase3() {
        // typical case
        arg1.setValue(5);
        arg2.setValue(7);
        
        boolean result = (boolean) operation.computeValue(null);
        assertFalse(result);
    }
    
    @Test
    public void testComputeValue_edgeCase1() {
        // edge case
        arg1.setValue(Double.MIN_VALUE);
        arg2.setValue(0);
        
        boolean result = (boolean) operation.computeValue(null);
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_edgeCase2() {
        // edge case
        arg1.setValue(Double.MAX_VALUE);
        arg2.setValue(Double.MAX_VALUE);
        
        boolean result = (boolean) operation.computeValue(null);
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_errorCase1() {
        // error case
        arg1.setValue("5");
        arg2.setValue(7);
        
        boolean result = (boolean) operation.computeValue(null);
        assertFalse(result);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        
        assertEquals(">=", symbol);
    }
}