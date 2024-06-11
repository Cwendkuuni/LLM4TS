import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.Linear;
import org.apache.commons.math.analysis.Quadratic;
import org.apache.commons.math.optimization.ObjectiveFunction;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.SimpleUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealOptimizer;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<Linear> optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(new SimpleUnivariateRealOptimizer(), 5, new RandomGenerator());
    }

    @Test
    public void testOptimize() {
        Linear f = new Linear(2.0, 3.0);
        double min = 0.0;
        double max = 10.0;
        double value = 5.0;
        MultiStartUnivariateRealOptimizer<Linear> optimizer = new MultiStartUnivariateRealOptimizer<>(new SimpleUnivariateRealOptimizer(), 5, new RandomGenerator());
        UnivariateRealOptimizer.Optimum optimum = optimizer.optimize(new ObjectiveFunction(f), value, min, max);
        assertNotNull(optimum);
    }

    @Test
    public void testGetOptima() {
        Linear f = new Linear(2.0, 3.0);
        double min = 0.0;
        double max = 10.0;
        double value = 5.0;
        MultiStartUnivariateRealOptimizer<Linear> optimizer = new MultiStartUnivariateRealOptimizer<>(new SimpleUnivariateRealOptimizer(), 5, new RandomGenerator());
        optimizer.optimize(new ObjectiveFunction(f), value, min, max);
        UnivariateRealOptimizer.Optimum[] optima = optimizer.getOptima();
        assertNotNull(optima);
    }

}