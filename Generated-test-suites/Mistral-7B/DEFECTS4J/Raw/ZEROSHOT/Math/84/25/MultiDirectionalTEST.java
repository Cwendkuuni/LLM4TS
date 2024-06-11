package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.RealUnivariateFunction;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private RealUnivariateFunction function;

    @Before
    public void setup() {
        optimizer = new MultiDirectional();
        function = new RealUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(BesselFunctions.sin(Math.PI * x / 2), 2) + Math.pow(Math.sin(Math.PI * x), 2);
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertEquals(2.0, md.getKhi(), 0.001);
        assertEquals(0.5, md.getGamma(), 0.001);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, md.getKhi(), 0.001);
        assertEquals(0.7, md.getGamma(), 0.001);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-5, 5);
        optimizer.optimize(function, bounds);
        RealPointValuePair best = optimizer.getBestSolution();
        double expectedX = Math.PI / 2;
        double expectedF = Math.pow(Math.sin(Math.PI * expectedX), 2) + Math.pow(Math.sin(Math.PI * expectedX), 2);
        assertEquals(expectedX, best.getPoint()[0], 0.001);
        assertEquals(expectedF, best.getValue(), 0.001);
    }

}