package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for RungeKuttaIntegrator.
 */
public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialConditions;

    @Before
    public void setUp() {
        equations = new TestDifferentialEquations();
        initialConditions = new double[] { 1.0, 0.0 };
        integrator = new EulerIntegrator(0.1, 10, new EulerStepInterpolator(), 0.1);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws Exception if test fails
     */
    @Test
    public void testIntegrate() throws Exception {
        double[] result = integrator.integrate(equations, 0.0, initialConditions, 2.0, new double[initialConditions.length]);
        assertArrayEqual(initialConditions, result);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * With step size smaller than the minimum allowed.
     * @throws Exception if test fails
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateSmallStep() throws Exception {
        integrator = new RungeKuttaIntegrator(null, new double[] { 0.3, 0.4, 0.3, 0.4 },
                new double[][] { { 0.0, 0.0, 0.5, 0.5 }, { 0.25, 0.25, 0.0, 0.5 },
                        { 0.0, 0.5, 0.5, 0.0 }, { 0.5, 0.0, 0.5, 0.0 } },
                new double[] { 0.5, 0.5 }, new EulerStepInterpolator(), 0.00001) {
            @Override
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                // do nothing
                return 0.0;
            }
        };
        integrator.integrate(equations, 0.0, initialConditions, 2.0, new double[initialConditions.length]);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * With invalid step size.
     * @throws Exception if test fails
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateInvalidStep() throws Exception {
        integrator = new EulerIntegrator(-0.1, 10, new EulerStepInterpolator(), 0.1);
        integrator.integrate(equations, 0.0, initialConditions, 2.0, new double[initialConditions.length]);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * With events.
     * @throws Exception if test fails
     */
    @Test
    public void testIntegrateWithEvents() throws Exception {
        integrator = new RungeKuttaIntegrator(null, new double[] { 0.3, 0.4, 0.3, 0.4 },
                new double[][] { { 0.0, 0.0, 0.5, 0.5 }, { 0.25, 0.25, 0.0, 0.5 },
                        { 0.0, 0.5, 0.5, 0.0 }, { 0.5, 0.0, 0.5, 0.0 } },
                new double[] { 0.5, 0.5 }, new EulerStepInterpolator(), 0.1) {
            @Override
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                // do nothing
                return 0.0;
            }

            @Override
            public void addStepHandler(StepHandler handler) {
                super.addStepHandler(handler);
                handler.handleStep(this, false);
            }

            @Override
            public void handleStep(StepHandler handler, boolean lastStep) {
                super.handleStep(handler, lastStep);
                handler.handleStep(this, true);
            }
        };
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(RungeKuttaIntegrator integrator, boolean lastStep) {
                if (lastStep) {
                    assertEquals(BesselFunctions.BesselK(0, 2.0), integrator.getY()[0], 1e-6);
                    assertEquals(BesselFunctions.BesselI(0, 2.0), integrator.getY()[1], 1e-6);
                }
            }
        });

        Event event = new When(1.5).setHandler(new StepHandler() {
            @Override
            public void handleStep(RungeKuttaIntegrator integrator, boolean lastStep) {
                throw new AssertionError("Should not reach this step");
            }
        });
        integrator.setEventsManager(new CombinedEventsManager(event));
        integrator.integrate(equations, 0.0, initialConditions, 2.0, new double[initialConditions.length]);
    }

}

class TestDifferentialEquations implements FirstOrderDifferentialEquations {

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
    }

}

class TestRungeKuttaStepInterpolator extends EulerStepInterpolator {

}