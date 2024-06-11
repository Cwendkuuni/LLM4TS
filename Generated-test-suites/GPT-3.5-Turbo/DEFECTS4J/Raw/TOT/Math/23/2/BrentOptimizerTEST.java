import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        optimizer = new BrentOptimizer(1e-6, 1e-6, checker);
    }

    @Test
    public void testDoOptimize_withValidInputs() {
        optimizer.setMin(-10.0);
        optimizer.setStartValue(0.0);
        optimizer.setMax(10.0);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    @Test
    public void testDoOptimize_withMaxLessThanMin() {
        optimizer.setMin(10.0);
        optimizer.setStartValue(0.0);
        optimizer.setMax(-10.0);
        optimizer.setGoalType(GoalType.MINIMIZE);

        try {
            UnivariatePointValuePair result = optimizer.doOptimize();
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            assertTrue(e.getMessage().contains("10.0"));
            assertTrue(e.getLocalizedMessage().contains("10.0"));
        }
    }

    @Test
    public void testDoOptimize_withInvalidGoalType() {
        optimizer.setMin(-10.0);
        optimizer.setStartValue(0.0);
        optimizer.setMax(10.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    @Test
    public void testDoOptimize_withConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                double change = Math.abs(previous.getValue() - current.getValue());
                return change < 1e-4;
            }
        };

        optimizer.setMin(-10.0);
        optimizer.setStartValue(5.0);
        optimizer.setMax(10.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(checker);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

}