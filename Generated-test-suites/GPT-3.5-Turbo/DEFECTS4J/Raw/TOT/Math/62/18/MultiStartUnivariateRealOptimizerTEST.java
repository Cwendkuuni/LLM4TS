import org.apache.commons.math.analysis.Sin;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<Sin> optimizer;
    private Sin sinFunction;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizer(), 5, new RandomGenerator());
        sinFunction = new Sin();
    }

    @Test
    public void testOptimize_minimizeGoal_convergedOptimum() throws Exception {
        UnivariateRealPointValuePair result = optimizer.optimize(sinFunction, GoalType.MINIMIZE, 0.0, 10.0, 2.0);
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 0.001);
        assertEquals(Math.PI, result.getPoint(), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_maximizeGoal_noConvergenceException() throws Exception {
        optimizer.optimize(sinFunction, GoalType.MAXIMIZE, 0.0, 1.0, 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptima_noOptimumComputedYet_exceptionThrown() {
        optimizer.getOptima();
    }

    @Test
    public void testGetOptima_optimaFoundInMultipleStarts_optimaReturned() throws Exception {
        optimizer.optimize(sinFunction, GoalType.MINIMIZE, 0.0, 10.0, 1.0);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNotNull(optima);
        assertEquals(5, optima.length);
        assertNotNull(optima[0]);
        assertEquals(0.0, optima[0].getValue(), 0.001);
        assertNotNull(optima[1]);
        assertEquals(Math.PI, optima[1].getPoint(), 0.001);
        // Remaining optima value should be null as they didn't converge
        for (int i = 2; i < optima.length; i++) {
            assertNull(optima[i]);
        }
    }

    @Test
    public void testGetOptima_noConvergenceInMultipleStarts_allOptimaNull() throws Exception {
        optimizer.optimize(sinFunction, GoalType.MAXIMIZE, 0.0, 1.0, 0.0);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNotNull(optima);
        assertEquals(5, optima.length);
        // All optima values should be null since no convergence occurred
        for (int i = 0; i < optima.length; i++) {
            assertNull(optima[i]);
        }
    }

    @Test
    public void testGetOptima_noOptimumComputedYetWithExceptionThrown_exceptionThrown() {
        try {
            optimizer.getOptima();
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            assertEquals("No optimum has been computed yet", e.getMessage());
        }
    }

}