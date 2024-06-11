import org.junit.Test;

import static org.junit.Assert.*;

public class BaseOptimizerTest {

    // test case for getMaxEvaluations()
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        // assert that the returned value is greater than or equal to 0
        assertTrue(maxEvaluations >= 0);
    }

    // test case for getEvaluations()
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<>();
        int evaluations = optimizer.getEvaluations();
        // assert that the initial value is 0
        assertEquals(0, evaluations);

        // simulate an optimization process
        optimizer.optimize();

        evaluations = optimizer.getEvaluations();
        // assert that the evaluations have been incremented
        assertNotEquals(0, evaluations);
    }

    // test case for getConvergenceChecker()
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<>();

        // assert that the returned value is not null
        assertNotNull(optimizer.getConvergenceChecker());
    }

    // test case for the deprecated annotation
    @Test
    public void testDeprecatedAnnotation() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<>();

        // assert that the class is marked as deprecated
        assertTrue(optimizer.getClass().isAnnotationPresent(Deprecated.class));
    }

    // test case for the deprecated version number
    @Test
    public void testDeprecatedVersionNumber() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<>();

        // assert that the version number is correct
        assertEquals("3.0", optimizer.getClass().getAnnotation(Deprecated.class).since());
    }

    // test case for the deprecated removal version
    @Test
    public void testDeprecatedRemovalVersion() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<>();

        // assert that the removal version is correct
        assertEquals("4.0", optimizer.getClass().getAnnotation(Deprecated.class).toRemoveIn());
    }

    // test case for the deprecated message
    @Test
    public void testDeprecatedMessage() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<>();

        // assert that the message is correct
        assertEquals("As of 3.1 (to be removed in 4.0).", optimizer.getClass().getAnnotation(Deprecated.class).message());
    }

}