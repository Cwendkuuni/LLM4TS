import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        RandomGenerator generator = new RandomGenerator() {
            public double nextDouble() {
                return 0.5;
            }
        };

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(new UnivariateRealOptimizer(), 5, generator);

        try {
            optimizer.setConvergenceChecker(null);
            optimizer.setMaxEvaluations(100);
            optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (MathIllegalStateException e) {
            fail("MathIllegalStateException should not be thrown");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Additional test cases for edge cases and exception handling scenarios can be added here
    }
}