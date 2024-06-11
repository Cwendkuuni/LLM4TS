import org.apache.commons.math.analysis.Sin;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.optimization.univariate.*;
import org.apache.commons.math.exception.*;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.*;
import org.apache.commons.math.util.FastMath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction function;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @BeforeEach
    public void setUp() {
        function = new Sin();
        optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 3, new Well512a());
        optimizer.setMaxEvaluations(100);
    }

    @Test
    public void testOptimize() throws Exception {
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2 * FastMath.PI;
        double expectedValue = Math.PI;
        double tolerance = 0.01;

        UnivariateRealPointValuePair result = optimizer.optimize(function, goal, min, max);
        double actualValue = result.getPoint();

        Assertions.assertEquals(expectedValue, actualValue, tolerance);
    }

    @Test
    public void testGetOptima() throws Exception {
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2 * FastMath.PI;
        double tolerance = 0.01;

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        for (UnivariateRealPointValuePair result : optima) {
            if (result != null) {
                double actualValue = result.getPoint();
                Assertions.assertTrue(actualValue >= min);
                Assertions.assertTrue(actualValue <= max);
                Assertions.assertEquals(goal, optimizer.getConvergenceChecker().getGoalType());
            }
        }
    }

    @Test
    public void testConvergenceException() {
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2 * FastMath.PI;
        double tolerance = 0.01;

        optimizer.setMaxEvaluations(0);

        Assertions.assertThrows(ConvergenceException.class, () -> {
            UnivariateRealPointValuePair result = optimizer.optimize(function, goal, min, max);
        });
    }

    @Test
    public void testFunctionEvaluationException() {
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2 * FastMath.PI;
        double tolerance = 0.01;

        UnivariateRealFunction invalidFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException(x);
            }
        };

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        UnivariateRealPointValuePair result = optima[0];

        Assertions.assertThrows(FunctionEvaluationException.class, () -> {
            result = optimizer.optimize(invalidFunction, goal, min, max);
        });
    }
}