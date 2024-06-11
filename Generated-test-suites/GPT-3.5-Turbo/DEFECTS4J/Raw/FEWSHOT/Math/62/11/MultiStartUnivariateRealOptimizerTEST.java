
package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.analysis.SinFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<SinFunction> multiStartOptimizer;
    private SinFunction sinFunction;

    @Before
    public void setUp() {
        BaseUnivariateRealOptimizer<SinFunction> optimizer = new BrentOptimizer(1e-10, 1e-10);
        int starts = 3;
        JDKRandomGenerator generator = new JDKRandomGenerator();
        generator.setSeed(123456789);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        sinFunction = new SinFunction();
    }

    @Test
    public void testOptimize_minimize() throws Exception {
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = Math.PI;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(sinFunction, goal, min, max);

        Assert.assertEquals(0.0, result.getPoint(), 1e-10);
        Assert.assertEquals(-1.0, result.getValue(), 1e-10);
    }

    @Test
    public void testOptimize_maximize() throws Exception {
        GoalType goal = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 2 * Math.PI;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(sinFunction, goal, min, max);

        Assert.assertEquals(Math.PI, result.getPoint(), 1e-10);
        Assert.assertEquals(1.0, result.getValue(), 1e-10);
    }

    @Test
    public void testGetOptima() throws Exception {
        GoalType goal = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 2 * Math.PI;
        multiStartOptimizer.optimize(sinFunction, goal, min, max);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertEquals(3, optima.length);

        Set<Double> expectedPoints = new HashSet<>();
        expectedPoints.add(0.0);
        expectedPoints.add(Math.PI);
        expectedPoints.add(2 * Math.PI);

        Set<Double> actualPoints = new HashSet<>();
        for (UnivariateRealPointValuePair pointValuePair : optima) {
            if (pointValuePair != null) {
                actualPoints.add(pointValuePair.getPoint());
            }
        }

        Assert.assertEquals(expectedPoints, actualPoints);
    }
}
