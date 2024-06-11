import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {
  
    private CoreOperationNotEqual coreOpNotEqual;
    private EvalContext evalContext;
  
    @Before
    public void setUp() {
        coreOpNotEqual = new CoreOperationNotEqual(null, null);
        evalContext = new EvalContext(null);
    }
  
    @Test
    public void testComputeValue_WhenEqual_ReturnsFalse() {
        // Arrange
        Object arg1 = new Object();
        Object arg2 = new Object();
        CoreOperationNotEqual coreOpNotEqual = new CoreOperationNotEqual(null, null);
      
        // Act
        evalContext.setNode(arg1);
        evalContext.setPosition(1);
        evalContext.setSize(10);
        coreOpNotEqual.computeValue(evalContext);

        // Assert
        assertEquals(Boolean.FALSE, coreOpNotEqual.computeValue(evalContext));
    }
    
    @Test
    public void testComputeValue_WhenNotEqual_ReturnsTrue() {
        // Arrange
        Object arg1 = new Object();
        Object arg2 = new Object();
        CoreOperationNotEqual coreOpNotEqual = new CoreOperationNotEqual(null, null);
      
        // Act
        evalContext.setNode(arg1);
        evalContext.setPosition(1);
        evalContext.setSize(10);
        coreOpNotEqual.computeValue(evalContext);

        // Assert
        assertEquals(Boolean.TRUE, coreOpNotEqual.computeValue(evalContext));
    }
    
    @Test
    public void testGetSymbol_ReturnsNotEqualSymbol() {
        // Arrange
        CoreOperationNotEqual coreOpNotEqual = new CoreOperationNotEqual(null, null);
      
        // Act
        String symbol = coreOpNotEqual.getSymbol();

        // Assert
        assertEquals("!=", symbol);
    }
}