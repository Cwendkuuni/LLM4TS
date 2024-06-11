import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import static org.junit.Assert.*;

public class ContinuedFractionTest {
    
    @Test
    public void testGetA() {
        // Test case 1
        double result = new DummyContinuedFraction().getA(0, 1);
        assertEquals(1.0, result, 0.0001);

        // Test case 2
        result = new DummyContinuedFraction().getA(2, 2.5);
        assertEquals(5.0, result, 0.0001);
    }
    
    @Test
    public void testGetB() {
        // Test case 1
        double result = new DummyContinuedFraction().getB(0, 1);
        assertEquals(1.0, result, 0.0001);

        // Test case 2
        result = new DummyContinuedFraction().getB(2, 2.5);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testEvaluateDefault() {
        // Test case 1
        double result = new DummyContinuedFraction().evaluate(1);
        assertEquals(2.0, result, 0.0001);

        // Test case 2
        result = new DummyContinuedFraction().evaluate(2.5);
        assertEquals(3.0, result, 0.0001);
    }
    
    @Test
    public void testEvaluateWithEpsilon() {
        // Test case 1
        double result = new DummyContinuedFraction().evaluate(1, 0.001);
        assertEquals(2.0, result, 0.0001);

        // Test case 2
        result = new DummyContinuedFraction().evaluate(2.5, 0.001);
        assertEquals(3.0, result, 0.0001);
    }
    
    @Test
    public void testEvaluateWithMaxIterations() {
        // Test case 1
        double result = new DummyContinuedFraction().evaluate(1, 0.001, 100);
        assertEquals(2.0, result, 0.0001);

        // Test case 2
        result = new DummyContinuedFraction().evaluate(2.5, 0.001, 100);
        assertEquals(3.0, result, 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        // Test case 1
        double result = new DummyContinuedFraction().evaluate(0, 0.001, 100);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfinityDivergence() {
        // Test case 1
        double result = new DummyContinuedFraction().evaluate(2, 0.001, 100);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNanDivergence() {
        // Test case 1
        double result = new DummyContinuedFraction().evaluate(1, 0.001, 100);
        assertEquals(Double.NaN, result, 0.0001);
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateNonConvergent() {
        // Test case 1
        double result = new DummyContinuedFraction().evaluate(1, 0.001, 1);
        assertEquals(2.0, result, 0.0001);
    }
    
    private class DummyContinuedFraction extends ContinuedFraction {
        
        public double getA(int n, double x) {
            if (n == 0) {
                return x;
            } else if (n == 2) {
                return x * 2;
            }
            return 0;
        }
        
        public double getB(int n, double x) {
            if (n == 0) {
                return x;
            } else if (n == 2) {
                return x;
            }
            return 0;
        }
    }
}