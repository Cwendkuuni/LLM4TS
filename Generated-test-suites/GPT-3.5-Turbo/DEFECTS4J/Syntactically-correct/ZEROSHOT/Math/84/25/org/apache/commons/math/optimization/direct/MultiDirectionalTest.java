import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
    
    private MultiDirectional multiDirectional;
    
    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }
    
    @Test
    public void testIterateSimplex() {
        // Test case 1
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 4.0, 6.0}, 0.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 6.0, 9.0}, 0.0);
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        try {
            multiDirectional.iterateSimplex(comparator);
            RealPointValuePair[] simplex = multiDirectional.getSimplex();
            
            // Assert the new simplex after iteration
            assertEquals(3, simplex.length);
            assertEquals(new double[]{1.0, 2.0, 3.0}, simplex[0].getPointRef(), 0.0);
            assertEquals(new double[]{2.0, 3.0, 4.0}, simplex[1].getPointRef(), 0.0);
            assertEquals(new double[]{3.0, 4.0, 5.0}, simplex[2].getPointRef(), 0.0);
            
        } catch (Exception e) {
            fail("Exception thrown while testing iterateSimplex method: " + e.getMessage());
        }
        
        // Test case 2
        original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{2.0, 4.0, 6.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 6.0, 9.0}, 0.0);
        try {
            multiDirectional.iterateSimplex(comparator);
            RealPointValuePair[] simplex = multiDirectional.getSimplex();
            
            // Assert the new simplex after iteration
            assertEquals(3, simplex.length);
            assertEquals(new double[]{1.0, 2.0, 3.0}, simplex[0].getPointRef(), 0.0);
            assertEquals(new double[]{1.0, 2.0, 3.0}, simplex[1].getPointRef(), 0.0);
            assertEquals(new double[]{3.0, 6.0, 9.0}, simplex[2].getPointRef(), 0.0);
            
        } catch (Exception e) {
            fail("Exception thrown while testing iterateSimplex method: " + e.getMessage());
        }
    }
    
    @Test
    public void testEvaluateNewSimplex() {
        // Test case 1
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 4.0, 6.0}, 0.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 6.0, 9.0}, 0.0);
        double coeff = 0.5;
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        try {
            RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, coeff, comparator);
            RealPointValuePair[] simplex = multiDirectional.getSimplex();
            
            // Assert the new simplex after evaluation
            assertEquals(3, simplex.length);
            assertEquals(new double[]{1.5, 3.0, 4.5}, simplex[0].getPointRef(), 0.0);
            assertEquals(new double[]{2.0, 4.0, 6.0}, simplex[1].getPointRef(), 0.0);
            assertEquals(new double[]{3.0, 6.0, 9.0}, simplex[2].getPointRef(), 0.0);
            
            // Assert the result
            assertEquals(new double[]{1.5, 3.0, 4.5}, result.getPointRef(), 0.0);
            
        } catch (Exception e) {
            fail("Exception thrown while testing evaluateNewSimplex method: " + e.getMessage());
        }
        
        // Test case 2
        original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{2.0, 4.0, 6.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 6.0, 9.0}, 0.0);
        coeff = 0.5;
        try {
            RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, coeff, comparator);
            RealPointValuePair[] simplex = multiDirectional.getSimplex();
            
            // Assert the new simplex after evaluation
            assertEquals(3, simplex.length);
            assertEquals(new double[]{1.5, 3.0, 4.5}, simplex[0].getPointRef(), 0.0);
            assertEquals(new double[]{1.0, 2.0, 3.0}, simplex[1].getPointRef(), 0.0);
            assertEquals(new double[]{3.0, 6.0, 9.0}, simplex[2].getPointRef(), 0.0);
            
            // Assert the result
            assertEquals(new double[]{1.5, 3.0, 4.5}, result.getPointRef(), 0.0);
            
        } catch (Exception e) {
            fail("Exception thrown while testing evaluateNewSimplex method: " + e.getMessage());
        }
    }
}