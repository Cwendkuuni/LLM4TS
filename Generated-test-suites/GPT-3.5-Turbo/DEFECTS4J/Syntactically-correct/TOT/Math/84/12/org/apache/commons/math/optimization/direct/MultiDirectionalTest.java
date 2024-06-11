import static org.junit.Assert.*;
import java.util.Comparator;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;


public class MultiDirectionalTest {
    
    private static final double EPSILON = 1e-10;
    
    private MultiDirectional optimizer;
    
    @Before
    public void setup() {
        optimizer = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex_whenComparatorReturnsPositive_thenReturnBestPointInSimplex() throws Exception {
        // Arrange
        Comparator<RealPointValuePair> comparator = new CompareByX();
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};
        double[] point5 = {9.0, 10.0};
        double[] expectedPoint = {1.0, 2.0};
        RealPointValuePair[] simplex = {
            new RealPointValuePair(point1, 1.0),
            new RealPointValuePair(point2, 2.0),
            new RealPointValuePair(point3, 3.0),
            new RealPointValuePair(point4, 4.0),
            new RealPointValuePair(point5, 5.0)
        };
        optimizer.setSimplex(simplex);
        
        // Act
        optimizer.iterateSimplex(comparator);
        RealPointValuePair actualPoint = optimizer.getSimplex()[0];
        
        // Assert
        assertArrayEquals(expectedPoint, actualPoint.getPointRef(), EPSILON);
    }
    
    @Test
    public void testIterateSimplex_whenComparatorReturnsZero_thenReturnBestPointInSimplex() throws Exception {
        // Arrange
        Comparator<RealPointValuePair> comparator = new CompareByX();
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] expectedPoint = {1.0, 2.0};
        RealPointValuePair[] simplex = {
            new RealPointValuePair(point1, 1.0),
            new RealPointValuePair(point2, 2.0)
        };
        optimizer.setSimplex(simplex);
        
        // Act
        optimizer.iterateSimplex(comparator);
        RealPointValuePair actualPoint = optimizer.getSimplex()[0];
        
        // Assert
        assertArrayEquals(expectedPoint, actualPoint.getPointRef(), EPSILON);
    }
    
    @Test
    public void testIterateSimplex_whenComparatorReturnsNegative_thenReturnBestPointInSimplex() throws Exception {
        // Arrange
        Comparator<RealPointValuePair> comparator = new CompareByX();
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] expectedPoint = {3.0, 4.0};
        RealPointValuePair[] simplex = {
            new RealPointValuePair(point1, 2.0),
            new RealPointValuePair(point2, 1.0)
        };
        optimizer.setSimplex(simplex);
        
        // Act
        optimizer.iterateSimplex(comparator);
        RealPointValuePair actualPoint = optimizer.getSimplex()[0];
        
        // Assert
        assertArrayEquals(expectedPoint, actualPoint.getPointRef(), EPSILON);
    }
    
    @Test
    public void testEvaluateNewSimplex_whenCoeffIsOne_thenReturnBestPointInTransformedSimplex() throws Exception {
        // Arrange
        Comparator<RealPointValuePair> comparator = new CompareByX();
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};
        double[] point5 = {9.0, 10.0};
        double[] expectedPoint = {1.0, 2.0};
        RealPointValuePair[] original = {
            new RealPointValuePair(point1, 1.0),
            new RealPointValuePair(point2, 2.0),
            new RealPointValuePair(point3, 3.0),
            new RealPointValuePair(point4, 4.0),
            new RealPointValuePair(point5, 5.0)
        };
        
        // Act
        RealPointValuePair actualPoint = optimizer.evaluateNewSimplex(original, 1.0, comparator);
        
        // Assert
        assertArrayEquals(expectedPoint, actualPoint.getPointRef(), EPSILON);
    }
    
    @Test
    public void testEvaluateNewSimplex_whenCoeffIsZero_thenReturnBestPointInTransformedSimplex() throws Exception {
        // Arrange
        Comparator<RealPointValuePair> comparator = new CompareByX();
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};
        double[] point5 = {9.0, 10.0};
        double[] expectedPoint = {9.0, 10.0};
        RealPointValuePair[] original = {
            new RealPointValuePair(point1, 1.0),
            new RealPointValuePair(point2, 2.0),
            new RealPointValuePair(point3, 3.0),
            new RealPointValuePair(point4, 4.0),
            new RealPointValuePair(point5, 5.0)
        };
        
        // Act
        RealPointValuePair actualPoint = optimizer.evaluateNewSimplex(original, 0.0, comparator);
        
        // Assert
        assertArrayEquals(expectedPoint, actualPoint.getPointRef(), EPSILON);
    }
    
    @Test
    public void testEvaluateNewSimplex_whenCoeffIsGreaterThanZero_thenReturnBestPointInTransformedSimplex() throws Exception {
        // Arrange
        Comparator<RealPointValuePair> comparator = new CompareByX();
        double[] point1 = {1.0, 2.0};
        double[] point2 = {3.0, 4.0};
        double[] point3 = {5.0, 6.0};
        double[] point4 = {7.0, 8.0};
        double[] point5 = {9.0, 10.0};
        double[] expectedPoint = {1.0, 2.0};
        RealPointValuePair[] original = {
            new RealPointValuePair(point1, 1.0),
            new RealPointValuePair(point2, 2.0),
            new RealPointValuePair(point3, 3.0),
            new RealPointValuePair(point4, 4.0),
            new RealPointValuePair(point5, 5.0)
        };
        
        // Act
        RealPointValuePair actualPoint = optimizer.evaluateNewSimplex(original, 0.5, comparator);
        
        // Assert
        assertArrayEquals(expectedPoint, actualPoint.getPointRef(), EPSILON);
    }
    
    private static class CompareByX implements Comparator<RealPointValuePair> {

        @Override
        public int compare(RealPointValuePair o1, RealPointValuePair o2) {
            double x1 = o1.getPointRef()[0];
            double x2 = o2.getPointRef()[0];
            return Double.compare(x1, x2);
        }
        
    }

}