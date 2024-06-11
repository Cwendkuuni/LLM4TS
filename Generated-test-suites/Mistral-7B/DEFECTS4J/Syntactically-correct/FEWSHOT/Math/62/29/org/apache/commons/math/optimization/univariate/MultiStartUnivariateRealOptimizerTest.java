package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateRealFunction;
import org.apache.commons.math3.optim.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.MersenneTwister;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0, -2.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister());

        try {
            UnivariateRealPointValuePair best = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertNotNull(best);
            System.out.println("Best point: " + best.getPoint() + ", value: " + best.getValue());
            UnivariateRealPointValuePair[] optima = multiOptimizer.getOptima();
            System.out.println("Found " + optima.length + " optima:");
            for (UnivariateRealPointValuePair op : optima) {
                System.out.println("Point: " + op.getPoint() + ", value: " + op.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception: " + e.getMessage());
        }
    }
}