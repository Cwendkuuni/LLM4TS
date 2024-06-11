package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexConstructorWithDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testAbstractSimplexConstructorWithDimensionAndSideLength() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testAbstractSimplexConstructorWithSteps() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testAbstractSimplexConstructorWithReferenceSimplex() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        assertEquals(2, simplex.getDimension());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexConstructorWithEmptyReferenceSimplex() {
        double[][] referenceSimplex = {};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorWithMismatchedReferenceSimplex() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {2.0, 3.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorWithMismatchedStartPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(2) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        simplex.build(startPoint);
    }
    
    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(startPoint.length + 1, points.length);
        for (int i = 0; i < startPoint.length; i++) {
            assertEquals(startPoint[i], points[i + 1].getPoint()[i], 0.0);
        }
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetPointWithInvalidIndex() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        simplex.build(startPoint);
        simplex.getPoint(-1);
    }
    
    @Test
    public void testSetPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        simplex.build(startPoint);
        
        double[] newPoint = {4.0, 5.0, 6.0};
        PointValuePair newPointValuePair = new PointValuePair(newPoint, 1.0);
        
        simplex.setPoint(0, newPointValuePair);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(newPoint, points[0].getPoint());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointWithInvalidIndex() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        simplex.build(startPoint);
        
        double[] newPoint = {4.0, 5.0, 6.0};
        PointValuePair newPointValuePair = new PointValuePair(newPoint, 1.0);
        
        simplex.setPoint(4, newPointValuePair);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsWithMismatchedDimensions() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        simplex.build(startPoint);
        
        double[] newPoint = {4.0, 5.0};
        PointValuePair[] newPoints = {new PointValuePair(newPoint, 1.0)};
        
        simplex.setPoints(newPoints);
    }
    
    @Test
    public void testEvaluate() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        simplex.build(startPoint);
        
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };
        
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair pv1, PointValuePair pv2) {
                if (pv1.getValue() < pv2.getValue()) {
                    return -1;
                }
                else if (pv1.getValue() > pv2.getValue()) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        };
        
        simplex.evaluate(evaluationFunction, comparator);
        
        PointValuePair[] points = simplex.getPoints();
        for (PointValuePair point : points) {
            assertFalse(Double.isNaN(point.getValue()));
        }
    }
    
    @Test
    public void testReplaceWorstPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            }
        };
        
        simplex.build(startPoint);
        
        double[] newPoint = {4.0, 5.0, 6.0};
        PointValuePair newPointValuePair = new PointValuePair(newPoint, 1.0);
        
        simplex.replaceWorstPoint(newPointValuePair, null);
        
        assertEquals(newPoint, simplex.getPoint(2).getPoint());
    }
}