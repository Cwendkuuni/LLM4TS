import org.junit.Test;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.optimization.direct.RealPointValuePair;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.FunctionEvaluationException;

import java.util.Comparator;
import org.apache.commons.math.optimization.OptimizationException;

import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        MultiDirectional optimizer = new MultiDirectional();
        
        // Test case 1: Simplex is not empty
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};
        double[] point5 = {9.0, 10.0};
        
        RealPointValuePair[] simplex = {new RealPointValuePair(point1, 1.0),
                                        new RealPointValuePair(point2, 2.0),
                                        new RealPointValuePair(point3, 3.0),
                                        new RealPointValuePair(point4, 4.0),
                                        new RealPointValuePair(point5, 5.0)};
        
        optimizer.setSimplex(simplex);
        optimizer.iterateSimplex(comparator);
        
        // Verify that the best point is correctly updated
        RealPointValuePair bestPoint = simplex[0];
        assertEquals(5.0, bestPoint.getValue(), 0.0001);
        
        // Test case 2: Simplex is empty
        RealPointValuePair[] emptySimplex = {};
        optimizer.setSimplex(emptySimplex);
        
        try {
            optimizer.iterateSimplex(comparator);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (Exception e) {
            fail("Wrong exception type thrown");
        }
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        MultiDirectional optimizer = new MultiDirectional();
        
        // Test case 1: Original simplex is not empty
        double coeff = 2.0;
        
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};
        double[] point5 = {9.0, 10.0};
        
        RealPointValuePair[] originalSimplex = {new RealPointValuePair(point1, 1.0),
                                                new RealPointValuePair(point2, 2.0),
                                                new RealPointValuePair(point3, 3.0),
                                                new RealPointValuePair(point4, 4.0),
                                                new RealPointValuePair(point5, 5.0)};
        
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        
        RealPointValuePair bestPoint = optimizer.evaluateNewSimplex(originalSimplex, coeff, comparator);
        
        // Verify that the transformed simplex is correctly created
        RealPointValuePair[] simplex = optimizer.getSimplex();
        assertEquals(originalSimplex[0], simplex[0]);
        assertEquals(originalSimplex.length, simplex.length);
        
        // Verify that the best point is correctly updated
        assertEquals(5.0, bestPoint.getValue(), 0.0001);
        
        // Test case 2: Original simplex is empty
        double[] emptyPoint = {};
        RealPointValuePair[] emptyOriginalSimplex = {new RealPointValuePair(emptyPoint, Double.NaN)};
        
        try {
            optimizer.evaluateNewSimplex(emptyOriginalSimplex, coeff, comparator);
            fail("Expected FunctionEvaluationException to be thrown");
        } catch (FunctionEvaluationException e) {
            // Test passed
        } catch (Exception e) {
            fail("Wrong exception type thrown");
        }
    }

}