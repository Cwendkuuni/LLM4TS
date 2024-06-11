import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.util.Pair;
import java.util.Arrays;
import java.util.Comparator;

public class AbstractSimplexTest {
    
    @Test
    public void testAbstractSimplex() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        
        Assert.assertEquals(3, abstractSimplex.getDimension());
        Assert.assertEquals(0, abstractSimplex.getSize());
    }
    
    @Test
    public void testAbstractSimplexWithSideLength() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3, 2.0);
        
        Assert.assertEquals(3, abstractSimplex.getDimension());
        Assert.assertEquals(0, abstractSimplex.getSize());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexWithNullSteps() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(null);
    }
    
    @Test(expected = ZeroException.class)
    public void testAbstractSimplexWithEmptySteps() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(new double[]{});
    }
    
    @Test
    public void testAbstractSimplexWithSteps() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(new double[]{1.0, 2.0, 3.0});
        
        Assert.assertEquals(3, abstractSimplex.getDimension());
        Assert.assertEquals(0, abstractSimplex.getSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexWithReferenceSimplexEmpty() {
        double[][] referenceSimplex = new double[][]{};
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(referenceSimplex);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexWithReferenceSimplexDimensionMismatch() {
        double[][] referenceSimplex = new double[][]{{1.0, 2.0}, {2.0, 3.0}};
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(referenceSimplex);
    }
    
    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexWithEqualVertices() {
        double[][] referenceSimplex = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {1.0, 2.0}};
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(referenceSimplex);
    }
    
    @Test
    public void testGetDimension() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        
        Assert.assertEquals(3, abstractSimplex.getDimension());
    }
    
    @Test
    public void testGetSize() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        
        Assert.assertEquals(0, abstractSimplex.getSize());
    }
    
    @Test
    public void testIterate() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        abstractSimplex.iterate(new MultivariateFunction() {
            public double value(double[] point) {
                return 0;
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        });
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testBuildWithDifferentDimension() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        double[] startPoint = new double[]{1.0, 2.0};
        abstractSimplex.build(startPoint);
    }
    
    @Test
    public void testBuild() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        
        Assert.assertEquals(4, abstractSimplex.getSize());
    }
    
    @Test
    public void testEvaluate() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        
        abstractSimplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return 0;
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        });
    }
    
    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        
        PointValuePair pointValuePair = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        abstractSimplex.replaceWorstPoint(pointValuePair, new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        });
    }
    
    @Test
    public void testGetPoints() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        
        PointValuePair[] points = abstractSimplex.getPoints();
        
        Assert.assertEquals(4, points.length);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetPointWithInvalidIndex() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        abstractSimplex.getPoint(4);
    }
    
    @Test
    public void testGetPoint() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        
        PointValuePair point = abstractSimplex.getPoint(0);
        
        Assert.assertNotNull(point);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetPointWithInvalidIndex() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        abstractSimplex.setPoint(4, point);
    }
    
    @Test
    public void testSetPoint() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        abstractSimplex.setPoint(0, point);
        
        Assert.assertEquals(point, abstractSimplex.getPoint(0));
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsWithDifferentSize() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        PointValuePair[] points = new PointValuePair[]{new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0)};
        
        abstractSimplex.setPoints(points);
    }
    
    @Test
    public void testSetPoints() {
        AbstractSimplex abstractSimplex = new AbstractSimplexImpl(3);
        PointValuePair[] points = new PointValuePair[]{new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0)};
        
        abstractSimplex.build(new double[]{1.0, 2.0, 3.0});
        abstractSimplex.setPoints(points);
        
        Assert.assertEquals(points[0], abstractSimplex.getPoint(0));
    }
    
    private class AbstractSimplexImpl extends AbstractSimplex {
        
        public AbstractSimplexImpl(int n) {
            super(n);
        }
        
        public AbstractSimplexImpl(int n, double sideLength) {
            super(n, sideLength);
        }
        
        public AbstractSimplexImpl(double[] steps) {
            super(steps);
        }
        
        public AbstractSimplexImpl(double[][] referenceSimplex) {
            super(referenceSimplex);
        }
        
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // Test implementation
        }
    }
}