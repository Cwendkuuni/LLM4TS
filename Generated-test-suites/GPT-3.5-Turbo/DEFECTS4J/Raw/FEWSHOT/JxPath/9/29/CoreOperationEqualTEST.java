import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationEqualTest {

    private CoreOperationEqual operationEqual;

    @Before
    public void setUp() {
        operationEqual = new CoreOperationEqual(new Expression(), new Expression());
    }

    @Test
    public void testComputeValue_returnsTrue_whenEqual() {
        // Arrange
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        
        // Act
        Object result = operationEqual.computeValue(context);
        
        // Assert
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_returnsFalse_whenNotEqual() {
        // Arrange
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        
        // Act
        Object result = operationEqual.computeValue(context);
        
        // Assert
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testGetSymbol_returnsEqualSymbol() {
        // Act
        String symbol = operationEqual.getSymbol();
        
        // Assert
        assertEquals("=", symbol);
    }
}