import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setup() {
        simplex = new AbstractSimplex(3) {};
    }
    
    @Test
    public void testGetDimension() {
        int expected = 3;
        int actual = simplex.getDimension();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSize() {
        int expected = 0;
        int actual = simplex.getSize();
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testIterate() {
        simplex.iterate(null, null);
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] expected = {new PointValuePair(startPoint, Double.NaN), 
                                    new PointValuePair(startPoint, Double.NaN), 
                                    new PointValuePair(startPoint, Double.NaN)};
        PointValuePair[] actual = simplex.getPoints();

        assertArrayEquals(expected, actual);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuild_invalidDimension() {
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };

        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair[] expected = {new PointValuePair(startPoint, 3.0), 
                                    new PointValuePair(startPoint, 3.0), 
                                    new PointValuePair(startPoint, 3.0)};
        PointValuePair[] actual = simplex.getPoints();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReplaceWorstPoint() {
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        PointValuePair newPoint = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 15.0);
        simplex.replaceWorstPoint(newPoint, comparator);

        PointValuePair[] expected = {new PointValuePair(new double[] {1.0, 2.0, 3.0}, 3.0), 
                                    new PointValuePair(new double[] {4.0, 5.0, 6.0}, 15.0), 
                                    new PointValuePair(new double[] {1.0, 2.0, 3.0}, 3.0)};
        PointValuePair[] actual = simplex.getPoints();

        assertArrayEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_invalidIndex() {
        simplex.getPoint(3);
    }

    @Test
    public void testSetPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 15.0);
        simplex.setPoint(1, newPoint);

        PointValuePair expected = newPoint;
        PointValuePair actual = simplex.getPoint(1);

        assertEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_invalidIndex() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 15.0);
        simplex.setPoint(4, newPoint);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPoints_invalidLength() {
        PointValuePair[] points = {new PointValuePair(new double[] {1.0, 2.0, 3.0}, 3.0),
                                   new PointValuePair(new double[] {4.0, 5.0, 6.0}, 15.0)};
        simplex.setPoints(points);
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] expected = {2.0, 2.0, 2.0};
        double[] actual = AbstractSimplex.createHypercubeSteps(3, 2.0);
        assertArrayEquals(expected, actual, 0.0);
    }
}