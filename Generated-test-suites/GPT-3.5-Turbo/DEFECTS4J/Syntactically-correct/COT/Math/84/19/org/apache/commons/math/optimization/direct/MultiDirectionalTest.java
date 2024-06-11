
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {
    
    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Initialize the MultiDirectional optimizer
        MultiDirectional optimizer = new MultiDirectional();
        
        // Set up the simplex for testing
        RealPointValuePair[] simplex = new RealPointValuePair[3];
        simplex[0] = new RealPointValuePair(new double[]{1, 2, 3}, 0.0);
        simplex[1] = new RealPointValuePair(new double[]{4, 5, 6}, 0.0);
        simplex[2] = new RealPointValuePair(new double[]{7, 8, 9}, 0.0);
        optimizer.setSimplex(simplex);
        
        // Set up the comparator
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        
        // Perform the iteration
        optimizer.iterateSimplex(comparator);
        
        // Check the results
        RealPointValuePair[] newSimplex = optimizer.getSimplex();
        assertEquals(3, newSimplex.length);
        assertEquals(0.0, newSimplex[0].getValue(), 0.0);
        assertEquals(0.0, newSimplex[1].getValue(), 0.0);
        assertEquals(0.0, newSimplex[2].getValue(), 0.0);
    }
    
    // Add test cases for each public method in the MultiDirectional class
    
    // Test case for constructor with default coefficients
    @Test
    public void testConstructorDefault() {
        MultiDirectional optimizer = new MultiDirectional();
        
        // Check the default coefficients
        assertEquals(2.0, optimizer.getKhi(), 0.0);
        assertEquals(0.5, optimizer.getGamma(), 0.0);
    }
    
    // Test case for constructor with specified coefficients
    @Test
    public void testConstructorWithCoefficients() {
        MultiDirectional optimizer = new MultiDirectional(1.0, 2.0);
        
        // Check the specified coefficients
        assertEquals(1.0, optimizer.getKhi(), 0.0);
        assertEquals(2.0, optimizer.getGamma(), 0.0);
    }
    
    // Add more test cases for other methods in the MultiDirectional class
    
}

