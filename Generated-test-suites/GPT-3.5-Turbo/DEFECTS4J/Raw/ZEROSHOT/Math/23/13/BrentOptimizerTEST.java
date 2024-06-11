import org.junit.Test;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new SimpleValueChecker());

        UnivariatePointValuePair result = optimizer.optimize(1000, (x) -> x * x - 4, 0.0, 5.0);

        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
}