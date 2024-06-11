package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.samples.RK4SampleFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testRK2Integrator() {
        EmbeddedRungeKuttaIntegrator rk2 = new RK2Integrator();
        ExpandableStatefulODE equations = new RK2SampleFunction();

        // test simple integration
        double[] initialState = { 1.0, 1.0 };
        rk2.integrate(equations, initialState, 0.0, 1.0);
        double[] expectedState = { 2.0, 1.5 };
        assertArrayEquals(expectedState, equations.getCompleteState());

        // test integration with relative tolerance
        EmbeddedRungeKuttaIntegrator rk2Tolerance = new RK2Integrator(1e-6, 1e-8);
        rk2Tolerance.integrate(equations, initialState, 0.0, 1.0);
        assertEquals(1.0e-8, equations.getRelativeError(), 1e-12);

        // test integration with absolute tolerance
        EmbeddedRungeKuttaIntegrator rk2AbsTolerance = new RK2Integrator(1e-2, new double[] {1e-8, 1e-8});
        rk2AbsTolerance.integrate(equations, initialState, 0.0, 1.0);
        assertEquals(1.0e-2, equations.getAbsoluteError(), 1e-3);

        // test integration with non-zero initial state
        double[] initialState2 = { 2.0, 2.0 };
        rk2AbsTolerance.integrate(equations, initialState2, 0.0, 2.0);
        double[] expectedState2 = { 3.6, 2.8 };
        assertArrayEquals(expectedState2, equations.getCompleteState());

        // test integration with negative step size
        try {
            rk2AbsTolerance.integrate(equations, initialState2, 0.0, -1.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        // test integration with too small step size
        try {
            rk2AbsTolerance.integrate(equations, initialState2, 0.0, 0.1);
            fail();
        } catch (MathIllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testRK4Integrator() {
        EmbeddedRungeKuttaIntegrator rk4 = new RK4Integrator();
        ExpandableStatefulODE equations = new RK4SampleFunction();

        // test simple integration
        double[] initialState = { 1.0, 1.0 };
        rk4.integrate(equations, initialState, 0.0, 1.0);
        double[] expectedState = { 2.0, 1.5 };
        assertArrayEquals(expectedState, equations.getCompleteState());

        // test integration with relative tolerance
        EmbeddedRungeKuttaIntegrator rk4Tolerance = new RK4Integrator(1e-6, 1e-8);
        rk4Tolerance.integrate(equations, initialState, 0.0, 1.0);
        assertEquals(1.0e-8, equations.getRelativeError(), 1e-12);

        // test integration with absolute tolerance
        EmbeddedRungeKuttaIntegrator rk4AbsTolerance = new RK4Integrator(1e-2, new double[] {1e-8, 1e-8});
        rk4AbsTolerance.integrate(equations, initialState, 0.0, 1.0);
        assertEquals(1.0e-2, equations.getAbsoluteError(), 1e-3);

        // test integration with non-zero initial state
        double[] initialState2 = { 2.0, 2.0 };
        rk4AbsTolerance.integrate(equations, initialState2, 0.0, 2.0);
        double[] expectedState2 = { 3.6, 2.8 };
        assertArrayEquals(expectedState2, equations.getCompleteState());

        // test integration with negative step size
        try {
            rk4AbsTolerance.integrate(equations, initialState2, 0.0, -1.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        // test integration with too small step size
        try {
            rk4AbsTolerance.integrate(equations, initialState2, 0.0, 0.1);
            fail();
        } catch (MathIllegalStateException e) {
            // expected
        }

        // test fsal behavior
        EmbeddedRungeKuttaIntegrator rk4Fsal = new RK4Integrator(false, "RK4(fsal)", new double[] {0.3, 0.3},
                new double[][] { {0.25, 0.25}, {0.5, 0 }, {0.5, 0.5}, {1.0, 0.5} },
                new double[] { 0.25, 0.25, 0.5, 0.5 }, new RK4StepInterpolator());
        double[] initialState3 = { 3.0, 3.0 };

        rk4Fsal.integrate(equations, initialState3, 0.0, 3.0);
        double[] expectedState3 = { 6.6, 4.8 };
        assertArrayEquals(expectedState3, equations.getCompleteState());
    }

}

class RK2Integrator extends EmbeddedRungeKuttaIntegrator {

    public RK2Integrator() {
        this("RK2", true, new double[] {0.5}, new double[][] { {0.5}, {0.5} }, new double[] { 0.5, 0.5 },
                new RK2StepInterpolator(), 0.001, 0.05);
    }

    public RK2Integrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        this("RK2", true, new double[] {0.5}, new double[][] { {0.5}, {0.5} }, new double[] { 0.5, 0.5 },
                new RK2StepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

}

class RK2IntegratorWithTolerance extends EmbeddedRungeKuttaIntegrator {

    public RK2Integrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        this("RK2", true, new double[] {0.5}, new double[][] { {0.5}, {0.5} }, new double[] { 0.5, 0.5 },
                new RK2StepInterpolator(), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

}

class RK4Integrator extends EmbeddedRungeKuttaIntegrator {

    public RK4Integrator() {
        this("RK4", false, new double[] {0.3, 0.3}, new double[][] { {0.25, 0.25}, {0.5, 0}, {0.5, 0.5}, {1.0, 0.5} },
                new double[] { 0.25, 0.25, 0.5, 0.5 }, new RK4StepInterpolator(), 0.001, 0.05);
    }

    public RK4Integrator(boolean fsal, String name, double[] c, double[][] a, double[] b,
            RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public RK4Integrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        this("RK4", false, new double[] {0.3, 0.3}, new double[][] { {0.25, 0.25}, {0.5, 0}, {0.5, 0.5}, {1.0, 0.5} },
                new double[] { 0.25, 0.25, 0.5, 0.5 }, new RK4StepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public RK4Integrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        this("RK4", false, new double[] {0.3, 0.3}, new double[][] { {0.25, 0.25}, {0.5, 0}, {0.5, 0.5}, {1.0, 0.5} },
                new double[] { 0.25, 0.25, 0.5, 0.5 }, new RK4StepInterpolator(), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

}

class RK2IntegratorTest extends EmbeddedRungeKuttaIntegratorTest {

    static class RK2StepInterpolator extends RungeKuttaStepInterpolator {
        @Override
        public double interpolate(double h, double[] yDotK) {
            return yDotK[0][0];
        }
    }

}

class RK4IntegratorTest extends EmbeddedRungeKuttaIntegratorTest {

    static class RK4StepInterpolator extends RungeKuttaStepInterpolator {
        @Override
        public double interpolate(double h, double[] yDotK) {
            if (h < 0) {
                throw new IllegalArgumentException("h must be positive");
            }
            // the RK4 integrator uses the same evaluation for the first and last step,
            // so we can return the result of the second stage
            return yDotK[1][0];
        }
    }

}