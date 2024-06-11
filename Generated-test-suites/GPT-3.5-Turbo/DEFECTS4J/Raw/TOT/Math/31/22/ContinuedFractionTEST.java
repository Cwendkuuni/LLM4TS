import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ContinuedFractionTest {

    private final double epsilon = 1e-9;
    
    @Test
    void testGetA() {
        // Mock the subclass of ContinuedFraction
        ContinuedFraction continuedFraction = spy(ContinuedFraction.class);
        doReturn(1.0).when(continuedFraction).getA(0, 1.0);
        
        double result = continuedFraction.getA(0, 1.0);
        
        assertEquals(1.0, result);
    }
    
    @Test
    void testGetB() {
        // Mock the subclass of ContinuedFraction
        ContinuedFraction continuedFraction = spy(ContinuedFraction.class);
        doReturn(2.0).when(continuedFraction).getB(0, 1.0);
        
        double result = continuedFraction.getB(0, 1.0);
        
        assertEquals(2.0, result);
    }
    
    @Test
    void testEvaluate() {
        // Create a concrete implementation of ContinuedFraction for testing
        ContinuedFractionImpl continuedFraction = new ContinuedFractionImpl();
        
        // Test typical case
        double result = continuedFraction.evaluate(1.0);
        assertEquals(1.0, result, epsilon);
        
        // Test edge case
        result = continuedFraction.evaluate(0.0);
        assertEquals(0.0, result, epsilon);
        
        // Test error scenario
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(Double.NaN));
    }
    
    class ContinuedFractionImpl extends ContinuedFraction {
        
        @Override
        protected double getA(int n, double x) {
            return 1.0;
        }
        
        @Override
        protected double getB(int n, double x) {
            return 2.0;
        }
        
    }
}