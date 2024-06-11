import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.util.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexConstructor1() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            assertEquals(3, simplex.getDimension());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAbstractSimplexConstructor2() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3, 2.5);
            assertEquals(3, simplex.getDimension());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAbstractSimplexConstructor3() {
        try {
            double[] steps = {2.0, 1.5, 1.0};
            AbstractSimplex simplex = new AbstractSimplex(steps);
            assertEquals(3, simplex.getDimension());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAbstractSimplexConstructor4() {
        try {
            double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
            AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
            assertEquals(2, simplex.getDimension());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetDimension() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            assertEquals(3, simplex.getDimension());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetSize() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            simplex.build(new double[] {1.0, 2.0, 3.0});
            assertEquals(4, simplex.getSize());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testIterate() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            // Not possible to test iterate() method without concrete implementation of AbstractSimplex
            // As AbstractSimplex is an abstract class, it cannot be instantiated directly
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testBuild() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            simplex.build(new double[] {1.0, 2.0, 3.0});
            
            // Test if simplex array is correctly initialized
            assertNotNull(simplex.getPoints());
            assertEquals(4, simplex.getPoints().length);
            assertEquals(3, simplex.getPoints()[0].getPointRef().length);
            
            // Test if the first point corresponds to the input startPoint
            double[] startPoint = simplex.getPoints()[0].getPointRef();
            assertArrayEquals(new double[] {1.0, 2.0, 3.0}, startPoint, 0.001);
            
            // Test if the other points are correctly constructed based on the startConfiguration
            double[][] startConfiguration = simplex.startConfiguration;
            for (int i = 0; i < startConfiguration.length; i++) {
                double[] vertexI = simplex.getPoints()[i+1].getPointRef();
                for (int j = 0; j < vertexI.length; j++) {
                    assertEquals(startPoint[j] + startConfiguration[i][j], vertexI[j], 0.001);
                }
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testEvaluate() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            simplex.build(new double[] {1.0, 2.0, 3.0});
            final MultivariateFunction evaluationFunction = new MultivariateFunction() {
                public double value(double[] point) {
                    return point[0] + point[1] + point[2];
                }
            };
            final Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
                public int compare(PointValuePair p1, PointValuePair p2) {
                    return Double.compare(p1.getValue(), p2.getValue());
                }
            };
            
            simplex.evaluate(evaluationFunction, comparator);
            
            // Test if simplex array is correctly sorted
            PointValuePair[] points = simplex.getPoints();
            for (int i = 0; i < points.length - 1; i++) {
                assertTrue(points[i+1].getValue() >= points[i].getValue());
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testReplaceWorstPoint() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            simplex.build(new double[] {1.0, 2.0, 3.0});
            final Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
                public int compare(PointValuePair p1, PointValuePair p2) {
                    return Double.compare(p1.getValue(), p2.getValue());
                }
            };
            
            PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
            simplex.replaceWorstPoint(new PointValuePair(new double[] {4.0, 5.0, 6.0}, 20.0), comparator);
            
            // Test if worst point is indeed replaced
            assertEquals(20.0, simplex.getPoint(simplex.getSize() - 1).getValue(), 0.001);
            assertArrayEquals(new double[] {4.0, 5.0, 6.0}, simplex.getPoint(simplex.getSize() - 1).getPointRef(), 0.001);
            
            // Test if the other points remain unchanged
            for (int i = 0; i < simplex.getSize() - 1; i++) {
                assertSame(worstPoint, simplex.getPoints()[i]);
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetPoints() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            simplex.build(new double[] {1.0, 2.0, 3.0});
            
            // Test if getPoints() returns a copy of the simplex array
            assertNotSame(simplex.getPoints(), simplex.simplex);
            assertArrayEquals(simplex.getPoints(), simplex.simplex);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetPoint() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            simplex.build(new double[] {1.0, 2.0, 3.0});
            
            // Test if getPoint() returns the correct point at the given index
            assertSame(simplex.getPoint(0), simplex.getPoints()[0]);
            assertSame(simplex.getPoint(1), simplex.getPoints()[1]);
            assertArrayEquals(new double[] {1.0, 2.0, 3.0}, simplex.getPoint(0).getPointRef(), 0.001);
            assertArrayEquals(new double[] {2.0, 3.0, 4.0}, simplex.getPoint(1).getPointRef(), 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetPoint() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            simplex.build(new double[] {1.0, 2.0, 3.0});
            
            PointValuePair point = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 20.0);
            simplex.setPoint(1, point);
            
            // Test if setPoint() correctly replaces the point at the given index
            assertSame(point, simplex.getPoint(1));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetPoints() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            simplex.build(new double[] {1.0, 2.0, 3.0});
            
            PointValuePair[] points = new PointValuePair[4];
            points[0] = new PointValuePair(new double[] {7.0, 8.0, 9.0}, 30.0);
            points[1] = new PointValuePair(new double[] {10.0, 11.0, 12.0}, 40.0);
            points[2] = new PointValuePair(new double[] {13.0, 14.0, 15.0}, 50.0);
            points[3] = new PointValuePair(new double[] {16.0, 17.0, 18.0}, 60.0);
            
            simplex.setPoints(points);
            
            // Test if setPoints() correctly replaces the simplex array
            assertArrayEquals(points, simplex.getPoints());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateHypercubeSteps() {
        try {
            double[] steps = AbstractSimplex.createHypercubeSteps(3, 2.5);
            
            // Test if correct hypercube steps array is generated
            assertArrayEquals(new double[] {2.5, 2.5, 2.5}, steps, 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}