package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AbstractSimplexTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testAbstractSimplex_withDimensionConstructor() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Implement iterate method if needed for testing
            }
        };
        
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testAbstractSimplex_withDimensionAndSideLengthConstructor() {
        AbstractSimplex simplex = new AbstractSimplex(3, 5.0) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Implement iterate method if needed for testing
            }
        };
        
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testAbstractSimplex_withStepsConstructor() {
        double[] steps = {1.0, 2.0, 3.0};
        
        AbstractSimplex simplex = new AbstractSimplex(steps) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Implement iterate method if needed for testing
            }
        };
        
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testAbstractSimplex_withReferenceSimplexConstructor() {
        double[][] referenceSimplex = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Implement iterate method if needed for testing
            }
        };
        
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());
    }
    
    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Implement iterate method if needed for testing
            }
        };
        
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        
        PointValuePair point = simplex.getPoint(0);
        assertArrayEquals(startPoint, point.getPoint(), 0.0);
        
        exception.expect(OutOfRangeException.class);
        simplex.getPoint(-1);
    }
    
    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Implement iterate method if needed for testing
            }
        };
        
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        
        PointValuePair newPoint = new PointValuePair(new double[] {3.0, 4.0}, 5.0);
        simplex.setPoint(0, newPoint);
        
        PointValuePair updatedPoint = simplex.getPoint(0);
        assertArrayEquals(newPoint.getPoint(), updatedPoint.getPoint(), 0.0);
        
        exception.expect(OutOfRangeException.class);
        simplex.setPoint(-1, newPoint);
    }
    
    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Implement iterate method if needed for testing
            }
        };
        
        PointValuePair[] points = {new PointValuePair(new double[] {1.0, 2.0}, 3.0)};
        simplex.setPoints(points);
        
        PointValuePair[] updatedPoints = simplex.getPoints();
        
        assertEquals(1, updatedPoints.length);
        assertArrayEquals(points, updatedPoints);
        
        exception.expect(DimensionMismatchException.class);
        simplex.setPoints(new PointValuePair[3]);
    }
    
    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Implement iterate method if needed for testing
            }
        };
        
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        
        simplex.evaluate(evaluationFunction, comparator);
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        
        assertEquals(3.0, points[0].getValue(), 0.0);
        assertEquals(3.0, points[1].getValue(), 0.0);
        assertEquals(3.0, points[2].getValue(), 0.0);
        assertEquals(3.0, points[3].getValue(), 0.0);
    }
    
    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Implement iterate method if needed for testing
            }
        };
        
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        
        int worstIndex = 3;
        PointValuePair worstPoint = simplex.getPoint(worstIndex);
        
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        
        simplex.replaceWorstPoint(worstPoint, comparator);
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(worstPoint, points[2]);
        assertEquals(worstPoint, points[3]);
        
        exception.expect(OutOfRangeException.class);
        simplex.replaceWorstPoint(worstPoint, comparator); // worstIndex is out of range
    }
    
    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Implement iterate method if needed for testing
            }
        };
        
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        
        assertArrayEquals(startPoint, points[0].getPoint(), 0.0);
        assertArrayEquals(new double[] {2.0, 2.0}, points[1].getPoint(), 0.0);
        assertArrayEquals(new double[] {1.0, 3.0}, points[2].getPoint(), 0.0);
        assertArrayEquals(new double[] {2.0, 3.0}, points[3].getPoint(), 0.0);
        
        exception.expect(DimensionMismatchException.class);
        simplex.build(new double[] {1.0, 2.0, 3.0}); // startPoint has incorrect dimension
    }
  
    @Test
    public void testCreateHypercubeSteps() {
        int dimension = 3;
        double sideLength = 5.0;
        
        double[] steps = AbstractSimplex.createHypercubeSteps(dimension, sideLength);
        
        assertEquals(dimension, steps.length);
        assertTrue(Arrays.stream(steps).allMatch(step -> step == sideLength));
    }
    
}