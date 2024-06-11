import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {
    
    @Test
    public void TestAbstractSimplexConstructor1() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void TestAbstractSimplexConstructor2() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0);
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void TestAbstractSimplexConstructor3() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void TestAbstractSimplexConstructor4() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
    }
    
    @Test
    public void TestGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(5);
        assertEquals(5, simplex.getDimension());
    }
    
    @Test
    public void TestGetSize() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void TestIterate() {
        AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.iterate(new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        });
    }
    
    @Test
    public void TestBuild() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
    }
    
    @Test
    public void TestEvaluate() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        });
    }
    
    @Test
    public void TestReplaceWorstPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
        simplex.replaceWorstPoint(new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0), new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        });
    }
    
    @Test
    public void TestGetPoints() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(4, points.length);
    }
    
    @Test
    public void TestGetPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
        PointValuePair point = simplex.getPoint(0);
        assertNotNull(point);
    }
    
    @Test
    public void TestSetPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
        simplex.setPoint(0, new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0));
    }
    
    @Test
    public void TestSetPoints() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
        PointValuePair[] points = {new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0),
                                   new PointValuePair(new double[]{7.0, 8.0, 9.0}, 0.0)};
        simplex.setPoints(points);
    }
    
    // 
    // Generated test cases
    @Test
    public void testAllMethods() {
        TestAbstractSimplexConstructor1();
        TestAbstractSimplexConstructor2();
        TestAbstractSimplexConstructor3();
        TestAbstractSimplexConstructor4();
        TestGetDimension();
        TestGetSize();
        TestIterate();
        TestBuild();
        TestEvaluate();
        TestReplaceWorstPoint();
        TestGetPoints();
        TestGetPoint();
        TestSetPoint();
        TestSetPoints();
    }
    // 
}