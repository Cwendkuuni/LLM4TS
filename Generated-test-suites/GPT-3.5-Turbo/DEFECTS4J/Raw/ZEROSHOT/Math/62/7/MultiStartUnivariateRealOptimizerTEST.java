import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    // Create an instance of the MultiStartUnivariateRealOptimizer class
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizerImpl(), 3, new RandomGeneratorImpl());

    // FunctionEvaluationException test
    @Test(expected=FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() throws FunctionEvaluationException {

        // Create a function that throws a FunctionEvaluationException
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        // Call the optimize method and expect a FunctionEvaluationException
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    // ConvergenceException test
    @Test(expected=ConvergenceException.class)
    public void testOptimizeConvergenceException() throws FunctionEvaluationException {

        // Create a function that throws a ConvergenceException
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new ConvergenceException();
            }
        };

        // Call the optimize method and expect a ConvergenceException
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    // getMaxEvaluations test
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    // getEvaluations test
    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    // setMaxEvaluations test
    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    // getOptima test
    @Test(expected=MathIllegalStateException.class)
    public void testGetOptima() {
        optimizer.getOptima();
    }

    // optimize test
    @Test
    public void testOptimize() throws FunctionEvaluationException {

        // Create a function that returns increasing values
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };

        // Call the optimize method
        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);

        // Check that the result is correct
        assertEquals(0.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);

        // Check that the optima array is correct
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        assertNotNull(optima[0]);
        assertNull(optima[1]);
        assertNull(optima[2]);
        assertEquals(0.0, optima[0].getPoint(), 0.0);
        assertEquals(0.0, optima[0].getValue(), 0.0);
    }

    // sortPairs test (MINIMIZE)
    @Test
    public void testSortPairsMinimize() {

        // Create an array of UnivariateRealPointValuePair
        UnivariateRealPointValuePair[] pairs = new UnivariateRealPointValuePair[3];
        pairs[0] = new UnivariateRealPointValuePair(0.0, 0.0);
        pairs[1] = new UnivariateRealPointValuePair(1.0, 1.0);
        pairs[2] = new UnivariateRealPointValuePair(2.0, 2.0);

        // Sort the array with MINIMIZE goal type
        optimizer.sortPairs(GoalType.MINIMIZE);

        // Check that the array is sorted in ascending order
        assertEquals(0.0, pairs[0].getPoint(), 0.0);
        assertEquals(0.0, pairs[0].getValue(), 0.0);
        assertEquals(1.0, pairs[1].getPoint(), 0.0);
        assertEquals(1.0, pairs[1].getValue(), 0.0);
        assertEquals(2.0, pairs[2].getPoint(), 0.0);
        assertEquals(2.0, pairs[2].getValue(), 0.0);
    }

    // sortPairs test (MAXIMIZE)
    @Test
    public void testSortPairsMaximize() {

        // Create an array of UnivariateRealPointValuePair
        UnivariateRealPointValuePair[] pairs = new UnivariateRealPointValuePair[3];
        pairs[0] = new UnivariateRealPointValuePair(0.0, 0.0);
        pairs[1] = new UnivariateRealPointValuePair(1.0, 1.0);
        pairs[2] = new UnivariateRealPointValuePair(2.0, 2.0);

        // Sort the array with MAXIMIZE goal type
        optimizer.sortPairs(GoalType.MAXIMIZE);

        // Check that the array is sorted in descending order
        assertEquals(2.0, pairs[0].getPoint(), 0.0);
        assertEquals(2.0, pairs[0].getValue(), 0.0);
        assertEquals(1.0, pairs[1].getPoint(), 0.0);
        assertEquals(1.0, pairs[1].getValue(), 0.0);
        assertEquals(0.0, pairs[2].getPoint(), 0.0);
        assertEquals(0.0, pairs[2].getValue(), 0.0);
    }
}