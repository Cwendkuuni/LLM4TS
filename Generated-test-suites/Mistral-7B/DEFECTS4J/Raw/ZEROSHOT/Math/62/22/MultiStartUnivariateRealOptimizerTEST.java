package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    private BaseUnivariateRealOptimizer<FUNC> optimizer;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    private FUNC function;
    private GoalType goal;
    private double min, max;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        optimizer = new FibonacciOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomUtils());
        function = new PolynomialFunction(new double[]{-1, 1, -1, 1, 0}, new double[]{0, 1});
        goal = GoalType.MINIMIZE;
        min = 0;
        max = 10;
        generator = new RandomUtils();
    }

    @Test
    public void testMultiStartOptimizer() throws Exception {
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.optimize(function, goal, min, max);
        assertArrayEquals(new UnivariateRealPointValuePair[] { optima[0], optima[1], optima[2], optima[3], optima[4] },
                         new UnivariateRealPointValuePair[] { new PointValuePair(1.618033988749895, 1.2367637699257437E-6),
                                                                     new PointValuePair(1.618106236328832, 1.624727725919221E-8),
                                                                     new PointValuePair(1.618050547146556, 7.31813553114702E-6),
                                                                     new PointValuePair(1.618064256527943, 2.045920656457736E-5),
                                                                     new PointValuePair(null, null) });
        assertNull(multiStartOptimizer.getOptima()[5]);
        assertTrue(Precision.isClose(multiStartOptimizer.getOptima()[0].getValue(), 1.2367637699257437E-6, 1E-6));
        assertTrue(Precision.isClose(multiStartOptimizer.getOptima()[1].getValue(), 1.624727725919221E-8, 1E-8));
        assertTrue(Precision.isClose(multiStartOptimizer.getOptima()[2].getValue(), 7.31813553114702E-6, 1E-6));
        assertTrue(Precision.isClose(multiStartOptimizer.getOptima()[3].getValue(), 2.045920656457736E-5, 1E-5));
        assertTrue(multiStartOptimizer.getEvaluations() > 25);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws Exception {
        UnivariateOptimizer<FUNC> singleStartOptimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(singleStartOptimizer,
                                                                                                            0, new RandomUtils());
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.optimize(function, goal, min, max);
        assertArrayEquals(optima, new UnivariateRealPointValuePair[] { null });
    }

    @Test(expected = MathIllegalStateException.class)
    public void testNoOptimizationCalled() throws Exception {
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomUtils());
        multiStartOptimizer.getOptima();
    }

}

class FibonacciOptimizer implements BaseUnivariateRealOptimizer<Func> {
    // Implement the BaseUnivariateRealOptimizer interface here.
}