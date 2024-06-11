import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {
    
    private CoreOperationNotEqual operation;
    private EvalContext context;
    
    @Before
    public void setUp() {
        operation = new CoreOperationNotEqual(null, null);
        context = new EvalContext(null);
    }
    
    @Test
    public void computeValue_notEqual_shouldReturnTrue() {
        // Arrange
        Object arg1 = "Hello";
        Object arg2 = "World";
        operation.setArgs(new Expression[]{new Constant(arg1), new Constant(arg2)});
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }
    
    @Test
    public void computeValue_equal_shouldReturnFalse() {
        // Arrange
        Object arg1 = 10;
        Object arg2 = 10;
        operation.setArgs(new Expression[]{new Constant(arg1), new Constant(arg2)});
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }
    
    @Test
    public void computeValue_nullValue_shouldReturnTrue() {
        // Arrange
        Object arg1 = null;
        Object arg2 = null;
        operation.setArgs(new Expression[]{new Constant(arg1), new Constant(arg2)});
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }
    
    @Test
    public void computeValue_differentTypes_shouldReturnTrue() {
        // Arrange
        Object arg1 = 10;
        Object arg2 = "10";
        operation.setArgs(new Expression[]{new Constant(arg1), new Constant(arg2)});
        
        // Act
        Object result = operation.computeValue(context);
        
        // Assert
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }
    
    @Test
    public void getSymbol_shouldReturnNotEqualSymbol() {
        // Arrange
        
        // Act
        String symbol = operation.getSymbol();
        
        // Assert
        assertEquals("!=", symbol);
    }
}