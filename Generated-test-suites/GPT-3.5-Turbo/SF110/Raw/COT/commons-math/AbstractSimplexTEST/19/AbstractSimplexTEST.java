
import org.junit.Test;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.analysis.*;

import java.util.Arrays;
import java.util.Comparator;

public class AbstractSimplexTest {
    @Test
    public void testAbstractSimplex() {
        // Test AbstractSimplex() constructor
        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {}
        };

        assert(simplex.getDimension() == 2);

        // Test AbstractSimplex(int n, double sideLength) constructor
        AbstractSimplex simplex2 = new AbstractSimplex(3, 1.5) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {}
        };

        assert(simplex2.getDimension() == 3);

        // Test AbstractSimplex(double[] steps) constructor
        double[] steps = {1.5, 2.0};
        AbstractSimplex simplex3 = new AbstractSimplex(steps) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {}
        };

        assert(simplex3.getDimension() == 2);

        // Test AbstractSimplex(double[][] referenceSimplex) constructor
        double[][] referenceSimplex = {{1.5, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        AbstractSimplex simplex4 = new AbstractSimplex(referenceSimplex) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {}
        };

        assert(simplex4.getDimension() == 1);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {}
        };
        
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        
        assert(simplex.getSize() == 3);
        assert(Arrays.equals(simplex.getPoint(0).getPoint(), startPoint));
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {}
        };
        
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        
        // Test evaluate() method
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[1];
            }
        }, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        
        assert(Math.abs(simplex.getPoint(0).getValue() - 2.0) < 1e-10);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {}
        };
        
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);

        // Test replaceWorstPoint() method
        PointValuePair pointValuePair = new PointValuePair(new double[] {3.0, 4.0}, 10.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        simplex.replaceWorstPoint(pointValuePair, comparator);
        
        assert(simplex.getPoint(2).getValue() == 10.0);
    }
}
