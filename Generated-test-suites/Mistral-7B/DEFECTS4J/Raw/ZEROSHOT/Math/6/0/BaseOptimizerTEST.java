import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link BaseOptimizer} interface.
 */
public class BaseOptimizerTest {

    private BaseOptimizer<Object> optimizer;
    private ConvergenceChecker<Object> convergenceChecker;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new DummyBaseOptimizer();
        convergenceChecker = new DummyConvergenceChecker();
        optimizer.setConvergenceChecker(convergenceChecker);
    }

    /**
     * Tests getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertNotNull(maxEvaluations);
        Assert.assertEquals(MaxEval.DEFAULT, maxEvaluations);

        optimizer = new DummyBaseOptimizer(5);
        maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(5, maxEvaluations);
    }

    /**
     * Tests getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        optimizer.optimize(new PointValuePair(new Point(), 1.0));
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(1, evaluations);

        optimizer.optimize(new PointValuePair(new Point(), 2.0));
        evaluations = optimizer.getEvaluations();
        Assert.assertEquals(2, evaluations);
    }

    /**
     * Tests getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        Assert.assertNotNull(optimizer.getConvergenceChecker());
        Assert.assertSame(convergenceChecker, optimizer.getConvergenceChecker());

        optimizer.setConvergenceChecker(new DummyConvergenceChecker2());
        ConvergenceChecker<Object> newChecker = optimizer.getConvergenceChecker();
        Assert.assertNotSame(convergenceChecker, newChecker);
        Assert.assertSame(new DummyConvergenceChecker2(), newChecker);
    }

}

/**
 * Dummy implementation of the {@link BaseOptimizer} interface.
 */
class DummyBaseOptimizer extends org.apache.commons.math3.optim.BaseOptimizer<Object> {

    private int maxEvaluations;

    public DummyBaseOptimizer() {
        this(MaxEval.DEFAULT);
    }

    public DummyBaseOptimizer(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }
}

/**
 * Dummy implementation of the {@link ConvergenceChecker} interface.
 */
class DummyConvergenceChecker implements ConvergenceChecker<Object> {

}

/**
 * Dummy implementation of the {@link ConvergenceChecker} interface.
 */
class DummyConvergenceChecker2 implements ConvergenceChecker<Object> {

}

class PointValuePair<T extends Point, V extends Number> {

    private final T point;
    private final V value;

    public PointValuePair(T point, V value) {
        this.point = point;
        this.value = value;
    }

    public T getPoint() {
        return point;
    }

    public V getValue() {
        return value;
    }
}

class Point {

}