import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {
  
    private BrentOptimizer brentOptimizer;
    
    @Before
    public void setUp() {
        brentOptimizer = new BrentOptimizer(0.001, 0.001);
    }
  
    @Test
    public void testDoOptimize() {
        double min = 0;
        double max = 10;
        double startValue = 5;
        double[] a = new double[]{0, 0, 0};
        double[] b = new double[]{1, 1, 1};
        brentOptimizer.setMin(min);
        brentOptimizer.setMax(max);
        brentOptimizer.setStartValue(startValue);
        UnivariatePointValuePair result = brentOptimizer.doOptimize();
        assertEquals(startValue, result.getPoint(), 0.001);
    }
  
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 5);
        UnivariatePointValuePair result = brentOptimizer.best(a, b, true);
        assertEquals(b, result);
    }
}