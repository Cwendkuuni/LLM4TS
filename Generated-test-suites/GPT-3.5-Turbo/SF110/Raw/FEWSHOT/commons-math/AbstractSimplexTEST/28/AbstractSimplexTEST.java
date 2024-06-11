package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AbstractSimplexTest {
    
    @Test
    public void testAbstractSimplexConstructor() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testAbstractSimplexConstructorWithSideLength() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0);
        
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexConstructorWithNullSteps() {
        AbstractSimplex simplex = new AbstractSimplex(null);
    }
    
    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorWithEmptySteps() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{});
    }
    
    @Test
    public void testAbstractSimplexConstructorWithSteps() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexConstructorWithReferenceSimplex() {
        double[][] referenceSimplex = {};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorWithMismatchedReferenceSimplex() {
        double[][] referenceSimplex = {{1.0, 2.0}, {2.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }
    
    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexConstructorWithEqualVertices() {
        double[][] referenceSimplex = {{1.0, 2.0}, {1.0, 2.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }
    
    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        assertEquals(startPoint.length, simplex.getPoint(0).getPoint().length);
        
        for (int i = 1; i < simplex.getSize(); i++) {
            double[] vertexI = simplex.getPoint(i).getPoint();
            
            for (int j = 0; j < startPoint.length; j++) {
                assertEquals(startPoint[j] + i, vertexI[j], 0.0001);
            }
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testBuildWithMismatchedStartPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
    }
    
    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        simplex.evaluate(evaluationFunction, comparator);
        
        for (int i = 0; i < simplex.getSize(); i++) {
            PointValuePair vertex = simplex.getPoint(i);
            
            assertTrue(!Double.isNaN(vertex.getValue()));
        }
    }
    
    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        PointValuePair pointValuePair = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0);
        
        simplex.replaceWorstPoint(pointValuePair, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        
        assertEquals(pointValuePair, simplex.getPoint(3));
    }
    
    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        PointValuePair[] points = simplex.getPoints();
        
        assertEquals(simplex.getSize(), points.length);
        assertEquals(startPoint.length, points[0].getPoint().length);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        
        simplex.getPoint(4);
    }
    
    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0);
        
        simplex.setPoint(1, point);
        
        assertEquals(point, simplex.getPoint(1));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0);
        
        simplex.setPoint(4, point);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsWithMismatchedSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[5];
        
        simplex.setPoints(points);
    }
}