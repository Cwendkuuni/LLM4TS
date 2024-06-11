import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionMapper;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.nsga2.NSGA2Optimizer;
import org.apache.commons.math3.optim.univariate.nlp.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.univariate.nlp.LevenbergMarquardtOptimizer.Options;
import org.apache.commons.math3.optim.univariate.nlp.MoreThuenteOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SimplexOptimizerTest {

    private AbstractSimplex simplex;
    private MultivariateFunction function;
    private SimplexOptimizer optimizer;

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {new IdentityFunction(), new double[]{0.0}},
                {new SineFunction(), new double[]{0.0}},
                {new RastriginFunction(), new double[]{1.0, 1.0}}
        };
        return Arrays.asList(data);
    }

    public SimplexOptimizerTest(MultivariateFunction function, double[] startPoint) {
        this.function = function;
        this.startPoint = startPoint;
    }

    @Before
    public void setUp() {
        simplex = new Simplex(new SimpleValueChecker(1e-6, 1e-6));
        optimizer = new SimplexOptimizer(simplex);
    }

    @Test
    public void testBasicFunctionality() {
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        Assert.assertNotNull(optimizer.getOptimum());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        optimizer.optimize(100, null, GoalType.MINIMIZE, startPoint);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        optimizer.optimize(100, function, GoalType.MINIMIZE, null);
    }

    @Test
    public void testSetSimplex() {
        optimizer.setSimplex(simplex);
        Assert.assertEquals(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        optimizer.setSimplex(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSimplexWithWrongType() {
        optimizer.setSimplex(new UnivariateOptimizer() {
            @Override
            public double optimize(UnivariateFunction f, double initialGuess, Options options) {
                return 0.0;
            }
        });
    }

    @Test
    public void testEmptySimplex() {
        optimizer.setSimplex(new EmptySimplex());
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        Assert.assertNotNull(optimizer.getOptimum());
    }

    @Test
    public void testFixedPoint() {
        optimizer.setSimplex(new FixedPointSimplex(new double[]{1.0}));
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        Assert.assertNotNull(optimizer.getOptimum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSimplexWithWrongSize() {
        optimizer.setSimplex(new AbstractSimplex() {
            @Override
            public int getSize() {
                return 3;
            }
        });
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
    }

    @Test
    public void testSetSimplexWithWrongFunctionType() {
        optimizer.setSimplex(new UnivariateFunctionMapper() {
            @Override
            public MultivariateFunction map(UnivariateFunction univariateFunction) {
                return null;
            }
        });
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
    }

    @Test
    public void testOptimizeWithDifferentFunction() {
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        MultivariateFunction newFunction = new IdentityFunction();
        optimizer.optimize(100, newFunction, GoalType.MINIMIZE, startPoint);
        Assert.assertNotNull(optimizer.getOptimum());
    }

    @Test
    public void testOptimizeWithDifferentStartPoint() {
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        double[] newStartPoint = new double[]{2.0, 2.0};
        optimizer.optimize(100, function, GoalType.MINIMIZE, newStartPoint);
        Assert.assertNotNull(optimizer.getOptimum());
    }

    @Test
    public void testConvergenceChecker() {
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-12, 1e-12));
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        Assert.assertNotNull(optimizer.getOptimum());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullConvergenceChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
    }

    @Test
    public void testMaxEval() {
        optimizer.optimize(1, function, GoalType.MINIMIZE, startPoint);
        Assert.assertNotNull(optimizer.getOptimum());
    }

    @Test
    public void testGoalType() {
        optimizer.optimize(100, function, GoalType.MAXIMIZE, startPoint);
        Assert.assertNotNull(optimizer.getOptimum());
    }

    private static class IdentityFunction implements MultivariateFunction {
        @Override
        public double value(double[] point) {
            return 0.0;
        }

        @Override
        public PointValuePair gradient(double[] point) {
            return null;
        }
    }

    private static class SineFunction implements MultivariateFunction {
        @Override
        public double value(double[] point) {
            double sum = 0.0;
            for (double value : point) {
                sum += Math.sin(value);
            }
            return sum;
        }

        @Override
        public PointValuePair gradient(double[] point) {
            double[] gradient = new double[point.length];
            for (int i = 0; i < point.length; i++) {
                gradient[i] = Math.cos(point[i]);
            }
            return new PointValuePair(gradient, 0.0);
        }
    }

    private static class RastriginFunction implements MultivariateFunction {
        @Override
        public double value(double[] point) {
            double sum = 0.0;
            for (double value : point) {
                sum += Math.pow(Math.PI * (Math.abs(value) - 2.0), 2.0)
                      + Math.pow(2.0 * Math.PI * value, 2.0) - 1.0;
            }
            sum += point.length * Math.PI;
            return sum;
        }

        @Override
        public PointValuePair gradient(double[] point) {
            throw new UnsupportedOperationException();
        }
    }

    private double[] startPoint;
}

interface AbstractSimplex {
    int getSize();

    AbstractSimplex build(double[] point);

    void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator);

    void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator);

    PointValuePair[] getPoints();

    PointValuePair getPoint(int index);
}

class EmptySimplex implements AbstractSimplex {
    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public AbstractSimplex build(double[] point) {
        return this;
    }

    @Override
    public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PointValuePair[] getPoints() {
        return new PointValuePair[0];
    }

    @Override
    public PointValuePair getPoint(int index) {
        throw new IndexOutOfBoundsException();
    }
}

class FixedPointSimplex implements AbstractSimplex {
    private double[] point;

    public FixedPointSimplex(double[] point) {
        this.point = point;
    }

    @Override
    public int getSize() {
        return point.length;
    }

    @Override
    public AbstractSimplex build(double[] point) {
        return new FixedPointSimplex(point);
    }

    @Override
    public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
        for (int i = 0; i < point.length; i++) {
            PointValuePair current = new PointValuePair(new double[]{point[i]}, function.value(point));
            comparator.compare(current, current);
        }
    }

    @Override
    public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PointValuePair[] getPoints() {
        PointValuePair[] result = new PointValuePair[getSize()];
        for (int i = 0; i < getSize(); i++) {
            result[i] = new PointValuePair(new double[]{point[i]}, Double.NaN);
        }
        return result;
    }

    @Override
    public PointValuePair getPoint(int index) {
        return new PointValuePair(new double[]{point[index]}, Double.NaN);
    }
}