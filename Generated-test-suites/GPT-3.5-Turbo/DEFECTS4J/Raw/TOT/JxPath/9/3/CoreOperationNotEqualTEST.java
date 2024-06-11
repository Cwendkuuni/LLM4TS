

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_WithEqualValues_ShouldReturnFalse() {
        // Arrange
        EvalContext context = new EvalContext();
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant("abc"), new Constant("abc"));
        
        // Act
        Object result = notEqual.computeValue(context);
        
        // Assert
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_WithUnequalValues_ShouldReturnTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(123), new Constant(456));
        
        // Act
        Object result = notEqual.computeValue(context);
        
        // Assert
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_WithSameReference_ShouldReturnFalse() {
        // Arrange
        EvalContext context = new EvalContext();
        Object obj = new Object();
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(obj), new Constant(obj));
        
        // Act
        Object result = notEqual.computeValue(context);
        
        // Assert
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_WithDifferentReferences_ShouldReturnTrue() {
        // Arrange
        EvalContext context = new EvalContext();
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(new Object()), new Constant(new Object()));
        
        // Act
        Object result = notEqual.computeValue(context);
        
        // Assert
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_WithNullValue_ShouldThrowNullPointerException() {
        // Arrange
        EvalContext context = new EvalContext();
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(null), new Constant(null));
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            notEqual.computeValue(context);
        });
    }
    
    @Test
    public void testGetSymbol_ShouldReturnNotEqualSymbol() {
        // Arrange
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant("abc"), new Constant("def"));
        
        // Act
        String symbol = notEqual.getSymbol();
        
        // Assert
        assertEquals("!=", symbol);
    }
}

