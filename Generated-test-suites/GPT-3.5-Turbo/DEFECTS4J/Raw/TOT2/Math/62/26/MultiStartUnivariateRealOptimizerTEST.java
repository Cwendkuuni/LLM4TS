import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.Sin;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.random.JDKRandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<Sin> optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 3, new JDKRandomGenerator());
    }

    @Test
    public void testOptimize() {
        Sin sin = new Sin();
        try {
            UnivariateRealPointValuePair result = optimizer.optimize(sin, GoalType.MINIMIZE, 0.0, 10.0, 5.0);
            assertNotNull(result);
            assertEquals(0.0, result.getValue(), 1e-6);
        } catch (Exception e) {
            fail("Exception thrown when it shouldn't have: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithInvalidBounds() {
        Sin sin = new Sin();
        try {
            optimizer.optimize(sin, GoalType.MINIMIZE, 10.0, 0.0, 5.0);
            fail("Expected Exception for invalid bounds was not thrown");
        } catch (MathIllegalStateException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Incorrect exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithFunctionEvaluationException() {
        Sin sin = new Sin();
        try {
            optimizer.optimize(sin, GoalType.MINIMIZE, 0.0, 10.0, 20.0);
            fail("Expected Exception for function evaluation was not thrown");
        } catch (ConvergenceException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Incorrect exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetOptima() {
        Sin sin = new Sin();
        try {
            optimizer.optimize(sin, GoalType.MINIMIZE, 0.0, 10.0, 5.0);
            UnivariateRealPointValuePair[] optima = optimizer.getOptima();
            assertNotNull(optima);
            assertTrue(optima.length >= 1);
        } catch (Exception e) {
            fail("Exception thrown when it shouldn't have: " + e.getMessage());
        }
    }

}