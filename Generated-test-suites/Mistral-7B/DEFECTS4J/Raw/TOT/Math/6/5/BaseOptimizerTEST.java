import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.optim.initializer.AroundMinimum;
import org.apache.commons.math3.optim.initializer.RandomMinSammon;
import org.apache.commons.math3.optim.initializer.ValueBound;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.statistics.DescriptiveStatistics;

import java.util.List;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        }, new SimpleBounds(0, 10), GoalType.MINIMIZE);

        int maxEvaluations = optimizer.getMaxEvaluations();
        optimizer.setMaxEvaluations(100); // Set it to a new value
        int newMaxEvaluations = optimizer.getMaxEvaluations();

        assertEquals(10, maxEvaluations); // Tests the initial value
        assertEquals(100, newMaxEvaluations); // Tests the new value
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        }, new SimpleBounds(0, 10), GoalType.MINIMIZE);

        optimizer.optimize(new AroundMinimum(0.5), new MaxEval(1));
        int evaluations = optimizer.getEvaluations();

        assertEquals(1, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        }, new SimpleBounds(0, 10), GoalType.MINIMIZE);

        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();

        assertNotNull(convergenceChecker);
    }

    @Test
    public void testOptimizationWithDefaultParameters() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        }, new SimpleBounds(0, 10), GoalType.MINIMIZE);

        PointValuePair solution = optimizer.optimize(new ValueBound(0, 10), new MaxEval(1000));

        double expectedSolution = 1.0;
        double tolerance = 0.001;
        assertEquals("The solution should be close to the expected solution.", expectedSolution, solution.getPoint(), tolerance);

        List<PointValuePair> results = optimizer.getIterates();
        DescriptiveStatistics statistics = new DescriptiveStatistics(results.stream().mapToDouble(PointValuePair::getValue).toArray());

        double expectedMinimum = Math.pow(expectedSolution, 2);
        assertEquals("The minimum found by the optimizer should be close to the expected minimum.", expectedMinimum, statistics.getMin(), tolerance);
    }

    @Test
    public void testOptimizationWithRandomInitialization() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        }, new SimpleBounds(0, 10), GoalType.MINIMIZE);

        PointValuePair solution = optimizer.optimize(new RandomMinSammon(2), new MaxEval(1000));

        double expectedSolution = 1.0;
        double tolerance = 0.01;
        assertEquals("The solution should be close to the expected solution.", expectedSolution, solution.getPoint(), tolerance);

        List<PointValuePair> results = optimizer.getIterates();
        DescriptiveStatistics statistics = new DescriptiveStatistics(results.stream().mapToDouble(PointValuePair::getValue).toArray());

        double expectedMinimum = Math.pow(expectedSolution, 2);
        assertEquals("The minimum found by the optimizer should be close to the expected minimum.", expectedMinimum, statistics.getMin(), tolerance);
    }
}