package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.RandomInitializer;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.convergence.IterationNumberConvertergenceChecker;

/**
 * Test cases for {@link BaseOptimizer} interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expectedMaxEvaluations = 100;
        assertEquals(expectedMaxEvaluations, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer<>();
        int evaluations = optimizer.getEvaluations();
        int expectedEvaluations = 0;
        assertEquals(expectedEvaluations, evaluations);

        optimizer.optimize(new PointValuePair(0.0, 1.0));
        evaluations = optimizer.getEvaluations();
        expectedEvaluations = 1;
        assertEquals(expectedEvaluations, evaluations);

        optimizer.optimize(new PointValuePair(1.0, 0.0));
        evaluations = optimizer.getEvaluations();
        expectedEvaluations = 2;
        assertEquals(expectedEvaluations, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer<>();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);

        ConvergenceChecker<PointValuePair> expectedConvergenceChecker = new IterationNumberConvertergenceChecker(10);
        optimizer = new DummyOptimizerWithCustomConvergenceChecker<>(expectedConvergenceChecker);
        convergenceChecker = optimizer.getConvergenceChecker();
        assertEquals(expectedConvergenceChecker, convergenceChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with valid input.
     */
    @Test
    public void testOptimizeWithValidInput() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizerWithCustomConvergenceChecker<>(new IterationNumberConvertergenceChecker(10));
        PointValuePair optimizedPoint = optimizer.optimize(new PointValuePair(0.5, 1.5));
        double expectedOptimizedValue = 0.0;
        PointValuePair expectedOptimizedPoint = new PointValuePair(0.0, 0.0);
        assertEquals(expectedOptimizedPoint, optimizedPoint);
        assertEquals(expectedOptimizedValue, optimizedPoint.getValue(), 0.001);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeWithNullInput() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizerWithCustomConvergenceChecker<>(new IterationNumberConvertergenceChecker(10));
        optimizer.optimize(null);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with custom initializer.
     */
    @Test
    public void testOptimizeWithCustomInitializer() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizerWithCustomInitializerAndCustomConvergenceChecker(
                new AroundMinimumInitializer<>(0.5), new IterationNumberConvertergenceChecker(10));
        PointValuePair optimizedPoint = optimizer.optimize(new PointValuePair(1.0, 1.0));
        double expectedOptimizedValue = 0.0;
        PointValuePair expectedOptimizedPoint = new PointValuePair(0.0, 0.0);
        assertEquals(expectedOptimizedPoint, optimizedPoint);
        assertEquals(expectedOptimizedValue, optimizedPoint.getValue(), 0.001);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with random initializer.
     */
    @Test
    public void testOptimizeWithRandomInitializer() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizerWithCustomInitializerAndCustomConvergenceChecker(
                new RandomInitializer(5), new IterationNumberConvertergenceChecker(10));
        PointValuePair optimizedPoint = optimizer.optimize(new PointValuePair(1.0, 1.0));
        double expectedOptimizedValue = 0.0;
        PointValuePair expectedOptimizedPoint = null;
        // The position of the minimum depends on the random generator seed.
        // Therefore, we cannot check the exact value but only the convergence.
        assertTrue(optimizedPoint.getValue() < 1.5);
        assertFalse(optimizedPoint.getValue() > 0.5);
        assertEquals(expectedOptimizedPoint, optimizedPoint);
        assertEquals(expectedOptimizedValue, optimizedPoint.getValue(), 0.001);
    }
}

class DummyOptimizer<PAIR> implements BaseOptimizer<PAIR> {
    // Empty implementation
}

class DummyOptimizerWithCustomConvergenceChecker<PAIR> extends DummyOptimizer<PAIR> {
    private final ConvergenceChecker<PAIR> convergenceChecker;

    public DummyOptimizerWithCustomConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
        this.convergenceChecker = convergenceChecker;
    }

    @Override
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return convergenceChecker;
    }
}

class DummyOptimizerWithCustomInitializer<PAIR> extends DummyOptimizer<PAIR> {
    private final Initializer<PAIR> initializer;

    public DummyOptimizerWithCustomInitializer(Initializer<PAIR> initializer) {
        this.initializer = initializer;
    }

    @Override
    public PointValuePair optimize(PAIR initialPoint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setInitialPoint(PAIR initialPoint) {
        this.initializer.initialize(initialPoint);
    }

    @Override
    public PointValuePair getInitialPoint() {
        return this.initializer.getInitialPoint();
    }

    @Override
    public void initialize(PAIR initialPoint) {
        this.setInitialPoint(initialPoint);
    }
}