import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[0];
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        assertNotNull(result);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        AbstractSimplex simplex = new AbstractSimplex() {
            public void build(double[] startPoint) {}

            public void evaluate(MultivariateFunction evaluationFunction,
                    Comparator<PointValuePair> comparator) {}

            public PointValuePair[] getPoints() {
                return null;
            }

            public int getSize() {
                return 0;
            }
            public double[] getPoint(int i) {
                return null;
            }
            public void iterate(MultivariateFunction evaluationFunction,
                    Comparator<PointValuePair> comparator) {}
        };
        optimizer.setSimplex(simplex);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setStartPoint(new double[] {0, 0});

        AbstractSimplex simplex = new AbstractSimplex() {
            public void build(double[] startPoint) {}

            public void evaluate(MultivariateFunction evaluationFunction,
                    Comparator<PointValuePair> comparator) {}

            public PointValuePair[] getPoints() {
                return null;
            }

            public int getSize() {
                return 0;
            }
            public double[] getPoint(int i) {
                return null;
            }
            public void iterate(MultivariateFunction evaluationFunction,
                    Comparator<PointValuePair> comparator) {}
        };
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
}