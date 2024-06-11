package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleBoundingBox;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FisherFalfaline;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexSearch;

import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private ObjectiveFunction function;
    private MultiDirectional optimizer;

    @Before
    public void setup() {
        function = new FisherFalfaline();
        optimizer = new MultiDirectional(2.0, 0.5);
    }

    @Test
    public void testConstructorDefault() {
        MultiDirectional md = new MultiDirectional();
        assertEquals(2.0, md.khi, 0.001);
        assertEquals(0.5, md.gamma, 0.001);
    }

    @Test
    public void testConstructorCustom() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, md.khi, 0.001);
        assertEquals(0.7, md.gamma, 0.001);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        SimpleBoundingBox box = new SimpleBoundingBox(-5.0, 5.0, 5);
        optimizer.setInitialGuess(box.getMinPoint());
        optimizer.setMaxIterations(1000);
        optimizer.optimize(function, GoalType.MINIMIZE);

        PointValuePair best = optimizer.getBestSolution();
        double[] x = best.getPoint();
        double fx = best.getValue();

        System.out.println("Best solution: " + x[0] + ", " + x[1] + ", " + fx);

        assertNotNull(best);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType(), 0.0);
        assertTrue(optimizer.isOptimized(), "The optimization did not converge.");
        assertLessThan(1e-12, fx, 0.0);

        SimplexSearch simplexSearch = new SimplexSearch(new FisherFalfaline(), new SimpleBoundingBox(-5.0, 5.0, 5), 1000);
        PointValuePair bestSimplexSearch = simplexSearch.optimize();
        assertEquals(best.getPoint()[0], bestSimplexSearch.getPoint()[0], 1e-12);
        assertEquals(best.getPoint()[1], bestSimplexSearch.getPoint()[1], 1e-12);
        assertEquals(fx, bestSimplexSearch.getValue(), 1e-12);
    }

}