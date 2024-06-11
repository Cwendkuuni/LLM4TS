import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.util.MathUtils;

import java.util.Arrays;
import java.util.Comparator;

public class AbstractSimplexTest {
    
    private class DummySimplex extends AbstractSimplex {
        
        public DummySimplex(int n) {
            super(n);
        }
        
        public DummySimplex(int n, double sideLength) {
            super(n, sideLength);
        }
        
        public DummySimplex(double[] steps) {
            super(steps);
        }
        
        public DummySimplex(double[][] referenceSimplex) {
            super(referenceSimplex);
        }
        
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // Implementation not necessary for testing
        }
    }
    
    private DummySimplex simplex;
    
    @Before
    public void setup() {
        simplex = new DummySimplex(3);
    }
    
    @Test
    public void testConstructor1() {
        assertEquals(3, simplex.getDimension());
        assertEquals(0, simplex.getSize());
    }
    
    @Test
    public void testConstructor2() {
        simplex = new DummySimplex(3, 2.0);
        assertEquals(3, simplex.getDimension());
        assertEquals(0, simplex.getSize());
    }
    
    @Test
    public void testConstructor3() {
        double[] steps = {1.0, 2.0, 3.0};
        simplex = new DummySimplex(steps);
        assertEquals(3, simplex.getDimension());
        assertEquals(0, simplex.getSize());
    }
    
    @Test
    public void testConstructor4() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        simplex = new DummySimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        assertEquals(0, simplex.getSize());
    }
    
    @Test
    public void testGetDimension() {
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testGetSize() {
        assertEquals(0, simplex.getSize());
    }
    
    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        assertEquals(4, simplex.getSize());
        assertEquals(startPoint, simplex.getPoint(0).getPoint());
    }
    
    @Test(expected=DimensionMismatchException.class)
    public void testBuildDimensionMismatch() {
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
    }
    
    @Test
    public void testEvaluate() {
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return MathUtils.sum(point);
            }
        };
        
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, null);
        
        assertEquals(startPoint.length, simplex.getSize());
        for (int i = 0; i < simplex.getSize(); i++) {
            assertEquals(startPoint[i] + 6, simplex.getPoint(i).getValue(), 0.0001);
        }
    }
    
    @Test
    public void testReplaceWorstPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 15.0);
        simplex.replaceWorstPoint(newPoint, null);
        
        PointValuePair lastPoint = simplex.getPoint(simplex.getSize() - 1);
        assertEquals(newPoint, lastPoint);
    }
    
    @Test(expected=OutOfRangeException.class)
    public void testInvalidIndexGetPoint() {
        simplex.getPoint(2);
    }
    
    @Test(expected=OutOfRangeException.class)
    public void testInvalidIndexSetPoint() {
        simplex.setPoint(2, new PointValuePair(new double[]{1.0, 2.0, 3.0}, 10.0));
    }
    
    @Test(expected=DimensionMismatchException.class)
    public void testSetPointsDimensionMismatch() {
        PointValuePair[] points = {
            new PointValuePair(new double[]{1.0, 2.0, 3.0}, 10.0),
            new PointValuePair(new double[]{4.0, 5.0, 6.0}, 15.0),
            new PointValuePair(new double[]{7.0, 8.0, 9.0}, 20.0)
        };
        simplex.setPoints(points);
    }
    
    @Test
    public void testGetPoints() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        PointValuePair[] points = simplex.getPoints();
        
        assertEquals(startPoint.length + 1, points.length);
        for (int i = 0; i < points.length; i++) {
            assertEquals(startPoint[i], points[i].getPoint()[i], 0.0001);
        }
    }
}