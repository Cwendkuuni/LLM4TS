import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimize() {
        double min = -10.0;
        double max = 10.0;

        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(1.0);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);

        boolean isMinim = true;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertNotNull(result);
        assertEquals(a.getPoint(), result.getPoint(), 1e-6);
        assertEquals(a.getValue(), result.getValue(), 1e-6);

        isMinim = false;
        result = optimizer.best(a, b, isMinim);

        assertNotNull(result);
        assertEquals(b.getPoint(), result.getPoint(), 1e-6);
        assertEquals(b.getValue(), result.getValue(), 1e-6);
    }

}