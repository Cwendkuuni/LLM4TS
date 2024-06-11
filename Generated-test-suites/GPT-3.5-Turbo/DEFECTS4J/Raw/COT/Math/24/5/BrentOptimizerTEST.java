
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Sin;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        UnivariateFunction function = new Sin();

        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 2*Math.PI);

        // Add asserts to check the returned result
    }
}
