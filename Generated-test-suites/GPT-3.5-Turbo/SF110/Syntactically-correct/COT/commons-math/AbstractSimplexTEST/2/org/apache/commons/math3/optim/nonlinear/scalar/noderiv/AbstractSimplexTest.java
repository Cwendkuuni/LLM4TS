import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testConstructor() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        assertEquals(startPoint, simplex.getPoint(0).getPointRef());
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };
        
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair a, PointValuePair b) {
                return Double.compare(a.getValue(), b.getValue());
            }
        };
        
        simplex.evaluate(evaluationFunction, comparator);
        assertEquals(6.0, simplex.getPoint(0).getValue(), 0.0001);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };
        
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair a, PointValuePair b) {
                return Double.compare(a.getValue(), b.getValue());
            }
        };
        
        simplex.evaluate(evaluationFunction, comparator);
        
        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 15.0);
        simplex.replaceWorstPoint(newPoint, comparator);
        assertEquals(newPoint, simplex.getPoint(2));
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(startPoint, points[0].getPointRef());
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        PointValuePair point = simplex.getPoint(1);
        assertEquals(startPoint, point.getPointRef());
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 15.0);
        simplex.setPoint(1, newPoint);
        assertEquals(newPoint, simplex.getPoint(1));
    }
    
    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        PointValuePair[] newPoints = {
            new PointValuePair(new double[]{4.0, 5.0, 6.0}, 15.0),
            new PointValuePair(new double[]{7.0, 8.0, 9.0}, 24.0)
        };
        
        simplex.setPoints(newPoints);
        assertEquals(newPoints, simplex.getPoints());
    }
}