package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCriterion;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypePoint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.optim.nonlinear.vector.GoalTypeVector;
import org.apache.commons.math3.optim.nonlinear.vector.point.PointVectorValuePair;
import org.apache.commons.math3.optim.nonlinear.vector.vector.VectorValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class SimplexOptimizerTest {

    private MultivariateFunction function;
    private GoalType goalType;
    private double[] startPoint;

    public SimplexOptimizerTest(MultivariateFunction function, GoalType goalType, double[] startPoint) {
        this.function = function;
        this.goalType = goalType;
        this.startPoint = startPoint;
    }

    @Parameterized.Parameters(name = "{index}: function={0}, goalType={1}, startPoint={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new RastriginFunction(), GoalType.MINIMIZE, new double[]{0.0},
                new RastriginFunction(), GoalType.MAXIMIZE, new double[]{0.0},
                new AckleyFunction(), GoalType.MINIMIZE, new double[]{0.0},
                new AckleyFunction(), GoalType.MAXIMIZE, new double[]{0.0}
        });
    }

    @Test
    public void testOptimize() throws Exception {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());

        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setInitialGuess(new AbstractSimplex(new double[function.getNumberOfVariables()]
                .clone()));
        optimizationData.setInitialGuess(startPoint);
        optimizer.setOptimizationData(optimizationData);

        PointValuePair result = optimizer.optimize(new MaxEval(50), function, goalType, optimizationData);

        PointValuePair expected = function.valueAndDerivative(startPoint);
        assertEquals(expected.getValue(), result.getValue(), 0.001);
        assertEquals(expected.getPoint(), result.getPoint(), 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.optimize(new MaxEval(1), null, goalType, startPoint);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.optimize(new MaxEval(1), function, goalType, null);
    }

    @Test(expected = UnboundedDomainException.class)
    public void testUnboundedFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.optimize(new MaxEval(1), new UnboundedFunction(), goalType, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.optimize(new MaxEval(1), function, GoalType.SADDLE, startPoint);
    }

    @Test
    public void testConvergenceChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(0.001, 0.01));

        PointValuePair[] previous = new PointValuePair[simplex.getSize()];
        double[] current = new double[function.getNumberOfVariables()];

        // Initialize search
        AbstractSimplex simplex = new SimpleSimplex(new double[function.getNumberOfVariables()]
                .clone());
        simplex.build(startPoint);
        simplex.evaluate(function, new GoalTypeVector(goalType));

        optimizer.setOptimizationData(new OptimizationData() {{
            setInitialGuess(simplex);
        }});

        // Check convergence
        PointValuePair result = optimizer.optimize(new MaxEval(100), function, goalType, optimizationData);
        PointValuePair[] points = simplex.getPoints();

        for (int i = 0; i < simplex.getSize(); i++) {
            previous[i] = points[i];
        }

        List<PointValuePair> worst = Arrays.asList(previous);
        List<PointValuePair> best = Arrays.asList(result, previous[0]);

        assertTrue(optimizer.getConvergenceChecker().converged(1, worst, best));
    }
}

class RastriginFunction implements MultivariateFunction {

    public double value(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(Math.PI * Math.sqrt(Math.abs(x[i])), 2.0) - 5.1 * Math.cos(2.0 * Math.PI * x[i]) + 1.0;
        }
        sum += x.length * 10.0;
        return sum;
    }

    public VectorValue gradient(double[] x) {
        double[] grad = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            grad[i] = 4.0 * Math.PI * Math.sqrt(Math.abs(x[i])) * Math.sin(2.0 * Math.PI * x[i]);
        }
        return new VectorValue(grad);
    }
}

class AckleyFunction implements MultivariateFunction {

    public double value(double[] x) {
        double sum = 0.0;
        double exSum = 0.0;
        double sqrtSum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(Math.sin(Math.sqrt(Math.abs(x[i]))), 2.0) - Math.cos(x[i]);
            exSum += Math.exp(Math.abs(x[i]));
            sqrtSum += Math.pow(Math.abs(x[i]), 2.0);
        }
        double term1 = 20.0 * Math.exp(-Math.sqrt(1.0 / (double) x.length) * Math.sqrt(sum))
                * Math.exp(1.0 / (double) x.length * sqrtSum);
        double term2 = Math.exp(1.0 / (double) x.length * exSum);
        return -1.0 * term1 / term2;
    }

    public VectorValue gradient(double[] x) {
        double[] grad = new double[x.length];
        double exSum = 0.0;
        double sqrtSum = 0.0;
        for (int i = 0; i < x.length; i++) {
            double term1 = Math.sin(Math.sqrt(Math.abs(x[i])) * Math.PI / 2.0) / (Math.pow(Math.abs(x[i]), 1.5));
            double term2 = -Math.exp(-Math.abs(x[i])) / (Math.pow(Math.abs(x[i]), 2.0));
            grad[i] = term1 + term2;
            sqrtSum += Math.pow(Math.abs(x[i]), 2.0);
            exSum += Math.exp(Math.abs(x[i]));
        }

        double term1 = -20.0 * Math.exp(-Math.sqrt(1.0 / (double) x.length) * Math.sqrt(FunctionUtils.sum(grad)));
        double term2 = Math.exp(1.0 / (double) x.length * exSum);

        for (int i = 0; i < x.length; i++) {
            grad[i] *= term1 / term2;
        }

        return new VectorValue(grad);
    }
}

class UnboundedFunction implements MultivariateFunction {

    public double value(double[] x) {
        throw new UnsupportedOperationException();
    }

    public VectorValue gradient(double[] x) {
        throw new UnsupportedOperationException();
    }
}

class SimpleSimplex extends AbstractSimplex {

    public SimpleSimplex(double[] p) {
        super(p.length);
        for (int i = 0; i < size; i++) {
            setPoint(i, p[i]);
        }
    }
}