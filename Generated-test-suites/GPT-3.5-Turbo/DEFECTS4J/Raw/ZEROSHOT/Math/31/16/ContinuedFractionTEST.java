import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {
    
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 3 * n + x;
            }
            
            protected double getB(int n, double x) {
                return 2 * n - x;
            }
        };
        
        assertEquals(5, cf.getA(1, 2), 0);
        assertEquals(-2, cf.getA(3, -1), 0);
    }
    
    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 3 * n + x;
            }
            
            protected double getB(int n, double x) {
                return 2 * n - x;
            }
        };
        
        assertEquals(1, cf.getB(1, 2), 0);
        assertEquals(5, cf.getB(3, -1), 0);
    }
    
    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 3 * n + x;
            }
            
            protected double getB(int n, double x) {
                return 2 * n - x;
            }
        };
        
        assertEquals(2.0, cf.evaluate(1), 0);
        assertEquals(1.5, cf.evaluate(2), 0);
        assertEquals(2.6, cf.evaluate(3), 0);
    }
    
    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 3 * n + x;
            }
            
            protected double getB(int n, double x) {
                return 2 * n - x;
            }
        };
        
        assertEquals(2.0, cf.evaluate(1, 10e-9), 0);
        assertEquals(1.5, cf.evaluate(2, 10e-9), 0);
        assertEquals(2.6, cf.evaluate(3, 10e-9), 0);
    }
    
    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 3 * n + x;
            }
            
            protected double getB(int n, double x) {
                return 2 * n - x;
            }
        };
        
        assertEquals(2.0, cf.evaluate(1, 10e-9, 100), 0);
        assertEquals(1.5, cf.evaluate(2, 10e-9, 100), 0);
        assertEquals(2.6, cf.evaluate(3, 10e-9, 100), 0);
    }
    
    @Test
    public void testEvaluateWithMaxIterationsAndConvergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 3 * n + x;
            }
            
            protected double getB(int n, double x) {
                return 2 * n - x;
            }
        };
        
        try {
            cf.evaluate(1, 10e-9, 1);
            fail("ConvergenceException not thrown");
        } catch (ConvergenceException e) {
            // success
        }
    }
}