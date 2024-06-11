import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        double rel = 1e-10;
        double abs = 1e-10;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        optimizer.setMin(0);
        optimizer.setMax(10);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(0, result.getPoint(), 0);
        assertEquals(0, result.getValue(), 0);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(0, result.getPoint(), 0);
        assertEquals(0, result.getValue(), 0);
    }

    @Test
    public void testBestWithEqualValues() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(0, result.getPoint(), 0);
        assertEquals(0, result.getValue(), 0);
    }
}