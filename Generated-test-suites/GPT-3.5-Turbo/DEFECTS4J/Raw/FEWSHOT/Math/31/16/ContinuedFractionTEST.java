import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class ContinuedFractionTest {
    
    private static final double EPSILON = 1e-9;
    private static final int MAX_ITERATIONS = 100;
    
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }
            @Override
            protected double getB(int n, double x) {
                return 2 * n;
            }
        };
        assertEquals(0, cf.getA(0, 0), EPSILON);
        assertEquals(1, cf.getA(1, 0), EPSILON);
        assertEquals(2, cf.getA(2, 0), EPSILON);
        assertEquals(3, cf.getA(3, 0), EPSILON);
    }
    
    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }
            @Override
            protected double getB(int n, double x) {
                return 2 * n;
            }
        };
        assertEquals(0, cf.getB(0, 0), EPSILON);
        assertEquals(2, cf.getB(1, 0), EPSILON);
        assertEquals(4, cf.getB(2, 0), EPSILON);
        assertEquals(6, cf.getB(3, 0), EPSILON);
    }
    
    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }
            @Override
            protected double getB(int n, double x) {
                return 2 * n;
            }
        };
        
        assertEquals(0.0, cf.evaluate(0.0, EPSILON, MAX_ITERATIONS), EPSILON);
        assertEquals(1.0, cf.evaluate(1.0, EPSILON, MAX_ITERATIONS), EPSILON);
        assertEquals(0.5, cf.evaluate(0.5, EPSILON, MAX_ITERATIONS), EPSILON);
        assertEquals(0.3333333333333333, cf.evaluate(0.333333, EPSILON, MAX_ITERATIONS), EPSILON);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0;
            }
            @Override
            protected double getB(int n, double x) {
                return 0;
            }
        };
        
        cf.evaluate(1.0, EPSILON, MAX_ITERATIONS);
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }
            @Override
            protected double getB(int n, double x) {
                return 2 * n;
            }
        };
        
        cf.evaluate(1.0, EPSILON, 0); // Max iterations set to 0, should trigger exception
    }
    
    // Additional test cases can be added to further test the evaluate() method.
    
}