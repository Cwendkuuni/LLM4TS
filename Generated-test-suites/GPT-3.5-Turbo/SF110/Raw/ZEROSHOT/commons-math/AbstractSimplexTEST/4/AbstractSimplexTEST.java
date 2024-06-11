import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);

        assertNotNull(simplex);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 1.0);

        assertNotNull(steps);
        assertEquals(3, steps.length);
        assertEquals(1.0, steps[0], 0.0001);
        assertEquals(1.0, steps[1], 0.0001);
        assertEquals(1.0, steps[2], 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_Null() {
        AbstractSimplex simplex = new AbstractSimplexImpl(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_ZeroLength() {
        AbstractSimplex simplex = new AbstractSimplexImpl(new double[0]);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_ZeroStep() {
        AbstractSimplex simplex = new AbstractSimplexImpl(new double[]{0.0});
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_EqualVertices() {
        AbstractSimplex simplex = new AbstractSimplexImpl(new double[]{1.0, 0.0, 1.0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_DimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplexImpl(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplex_NotStrictlyPositive() {
        AbstractSimplex simplex = new AbstractSimplexImpl(new double[][]{});
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);

        int size = simplex.getSize();
        
        assertEquals(0, size);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);

        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);

        int size = simplex.getSize();

        assertEquals(4, size);

        for (int i = 0; i < size; i++) {
            PointValuePair point = simplex.getPoint(i);
            double[] coordinates = point.getPoint();

            for (int j = 0; j < simplex.getDimension(); j++) {
                assertEquals(startPoint[j], coordinates[j], 0.0001);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuild_DimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);

        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);

        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);

        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2) + Math.pow(point[2], 2);
            }
        };

        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(evaluationFunction.value(p1.getPoint()), evaluationFunction.value(p2.getPoint()));
            }
        };

        simplex.evaluate(evaluationFunction, comparator);

        int size = simplex.getSize();
        for (int i = 0; i < size - 1; i++) {
            double value1 = evaluationFunction.value(simplex.getPoint(i).getPoint());
            double value2 = evaluationFunction.value(simplex.getPoint(i + 1).getPoint());

            assertTrue(value1 <= value2);
        }
    }

    private class AbstractSimplexImpl extends AbstractSimplex {
        AbstractSimplexImpl(int n) {
            super(n);
        }
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            
        }
    }
}