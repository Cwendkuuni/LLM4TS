import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    int starts;
    RandomGenerator generator;

    @Before
    public void setUp() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> baseOptimizer = new BaseUnivariateRealOptimizer<>();
        starts = 3;
        generator = new RandomGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer<>(baseOptimizer, starts, generator);
    }

    @Test
    public void testSetConvergenceChecker() {
        // Test if the ConvergenceChecker is set correctly
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = new ConvergenceChecker<>();

        optimizer.setConvergenceChecker(convergenceChecker);

        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        // Test if the maxEvaluations is set correctly
        int maxEvaluations = 100;

        optimizer.setMaxEvaluations(maxEvaluations);

        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptimaWithoutOptimize() {
        // Test if exception is thrown when getOptima() is called without calling optimize() first
        try {
            optimizer.getOptima();
            fail("Expected MathIllegalStateException to be thrown");
        } catch (MathIllegalStateException e) {
            assertEquals("No optimum computed yet", e.getMessage());
        }
    }

    @Test
    public void testOptimize() {
        // Test the optimize() method by mocking the underlying optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> baseOptimizer = mock(BaseUnivariateRealOptimizer.class);
        optimizer = new MultiStartUnivariateRealOptimizer<>(baseOptimizer, starts, generator);

        double min = 0.0;
        double max = 10.0;

        goal = GoalType.MINIMIZE;
        startValue = 5.0;
        UnivariateRealPointValuePair result = new UnivariateRealPointValuePair(3.0, 1.0);
        
        when(baseOptimizer.optimize(f, goal, min, max, startValue)).thenReturn(result);

        UnivariateRealPointValuePair actualResult = optimizer.optimize(f, goal, min, max, startValue);

        assertEquals(result, actualResult);
    }

    @Test
    public void testSortPairsForMinimize() {
        // Test the sortPairs() method for GoalType.MINIMIZE
        UnivariateRealPointValuePair pair1 = new UnivariateRealPointValuePair(3.0, 1.0);
        UnivariateRealPointValuePair pair2 = new UnivariateRealPointValuePair(5.0, 2.0);
        UnivariateRealPointValuePair pair3 = new UnivariateRealPointValuePair(1.0, 3.0);
        UnivariateRealPointValuePair pair4 = null;
        UnivariateRealPointValuePair pair5 = null;

        optimizer.sortPairs(GoalType.MINIMIZE);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        assertEquals(pair3, optima[0]);
        assertEquals(pair1, optima[1]);
        assertEquals(pair2, optima[2]);
        assertEquals(pair4, optima[3]);
        assertEquals(pair5, optima[4]);
    }

    @Test
    public void testSortPairsForMaximize() {
        // Test the sortPairs() method for GoalType.MAXIMIZE
        UnivariateRealPointValuePair pair1 = new UnivariateRealPointValuePair(3.0, 1.0);
        UnivariateRealPointValuePair pair2 = new UnivariateRealPointValuePair(5.0, 2.0);
        UnivariateRealPointValuePair pair3 = new UnivariateRealPointValuePair(1.0, 3.0);
        UnivariateRealPointValuePair pair4 = null;
        UnivariateRealPointValuePair pair5 = null;

        optimizer.sortPairs(GoalType.MAXIMIZE);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        assertEquals(pair2, optima[0]);
        assertEquals(pair1, optima[1]);
        assertEquals(pair3, optima[2]);
        assertEquals(pair4, optima[3]);
        assertEquals(pair5, optima[4]);
    }

    @Test
    public void testGetOptimaWithConvergenceException() {
        // Test if ConvergenceException is thrown when no optimum is found
        BaseUnivariateRealOptimizer<UnivariateRealFunction> baseOptimizer = mock(BaseUnivariateRealOptimizer.class);
        optimizer = new MultiStartUnivariateRealOptimizer<>(baseOptimizer, starts, generator);

        double min = 0.0;
        double max = 10.0;

        goal = GoalType.MINIMIZE;
        startValue = 5.0;

        when(baseOptimizer.optimize(f, goal, min, max, startValue)).thenThrow(new ConvergenceException());

        try {
            UnivariateRealPointValuePair actualResult = optimizer.optimize(f, goal, min, max, startValue);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException e) {
            assertEquals("No convergence with any start point", e.getMessage());
        }
    }

}