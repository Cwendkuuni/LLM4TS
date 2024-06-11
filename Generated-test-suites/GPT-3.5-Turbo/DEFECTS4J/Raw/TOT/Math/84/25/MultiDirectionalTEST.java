import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;

public class MultiDirectionalTest {
    
    @Test
    public void testIterateSimplex_PositiveCase() {
        // Create MultiDirectional object
        MultiDirectional multiDir = new MultiDirectional();
        
        // Create the comparator
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
              return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        
        try {
            // Call the 'iterateSimplex' method
            multiDir.iterateSimplex(comparator);
            
            // Check if the iterations counter is incremented
            assertEquals(1, multiDir.getIterations());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testIterateSimplex_ExceptionCase() {
        // Create MultiDirectional object
        MultiDirectional multiDir = new MultiDirectional();
        
        // Create the comparator
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
              return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        
        try {
            // Call the 'iterateSimplex' method with an invalid comparator
            multiDir.iterateSimplex(null);
            
            // The method should throw an IllegalArgumentException
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            // The exception should be thrown
            assertEquals("Comparator must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getClass().getSimpleName());
        }
    }
    
    @Test
    public void testEvaluateNewSimplex_PositiveCase() {
        // Create MultiDirectional object
        MultiDirectional multiDir = new MultiDirectional();
        
        // Create the original simplex
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{3.0, 4.0}, 0.5);
        original[2] = new RealPointValuePair(new double[]{5.0, 6.0}, 1.0);
        
        try {
            // Call the 'evaluateNewSimplex' method
            RealPointValuePair result = multiDir.evaluateNewSimplex(original, 0.5, new StandardComparator());
            
            // Check if the first element of the result is equal to the first element of original
            assertArrayEquals(original[0].getPointRef(), result.getPointRef(), 0.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testEvaluateNewSimplex_ExceptionCase() {
        // Create MultiDirectional object
        MultiDirectional multiDir = new MultiDirectional();
        
        // Create the original simplex
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{3.0, 4.0}, 0.5);
        original[2] = new RealPointValuePair(new double[]{5.0, 6.0}, 1.0);
        
        try {
            // Call the 'evaluateNewSimplex' method with an invalid coefficient
            multiDir.evaluateNewSimplex(original, -1.0, new StandardComparator());
            
            // The method should throw an IllegalArgumentException
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            // The exception should be thrown
            assertEquals("Coefficient must be positive.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getClass().getSimpleName());
        }
    }
    
    @Test
    public void testEvaluateNewSimplex_ExceptionCase2() {
        // Create MultiDirectional object
        MultiDirectional multiDir = new MultiDirectional();
        
        // Create the original simplex
        RealPointValuePair[] original = null;
        
        try {
            // Call the 'evaluateNewSimplex' method with null original simplex
            multiDir.evaluateNewSimplex(original, 0.5, new StandardComparator());
            
            // The method should throw an IllegalArgumentException
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            // The exception should be thrown
            assertEquals("Original simplex must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getClass().getSimpleName());
        }
    }
}