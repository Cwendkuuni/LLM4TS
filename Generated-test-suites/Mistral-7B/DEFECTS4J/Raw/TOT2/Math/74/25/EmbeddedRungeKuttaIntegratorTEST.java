package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.LienardEquations;
import org.apache.commons.math3.ode.events.RelativisticStepHandler;
import org.apache.commons.math3.ode.events.StepEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** The equations to integrate. */
    private static final FirstOrderDifferentialEquations equations =
        new LienardEquations();

    /** The integrator to test. */
    private static EmbeddedRungeKuttaIntegrator integrator;

    /** The interpolator to use. */
    private static StepInterpolator interpolator;

    /** The step handler to use. */
    private static final StepHandler stepHandler = new RelativisticStepHandler();

    /**
     * Initialize the integrator.
     */
    @Before
    public void setUp() {
        integrator = new EmbeddedRk4Integrator(
                "Rk4", false,
                new double[] { 0.25, 0.25, 0.5, 0.5 },
                new double[][] { { 0.0, 0.0, 0.5, 0.5 },
                                { 0.25, 0.25, 0.25, 0.25 },
                                { 0.0, 0.5, 0.5, 0.0 },
                                { 0.5, 0.0, 0.0, 0.5 } },
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(StepInterpolator interpolator,
                                              EmbeddedRungeKuttaIntegrator integrator,
                                              RealVector y, double[] yDot,
                                              boolean forward) {
                        this.interpolator = interpolator;
                        this.integrator = integrator;
                    }
                },
                0.001, 0.01,
                1.0e-8, 1.0e-8);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(10.0);
        interpolator = integrator.getPrototype().copy();
    }

    /**
     * Test the integration of a simple system.
     *
     * @throws MaxCountExceededException if the integration fails
     */
    @Test
    public void testSimpleIntegration() throws MaxCountExceededException {
        final double[] y0 = { 1.0, 1.0 };
        final double tStart = 0.0;
        final double tEnd = 5.0;
        final double[] yEnd = { 2.71828, 2.71828 };
        integrator.setEquations(equations);
        final double stepSize = integrator.integrate(equations, tStart, y0, tEnd, yEnd);
        assertEquals(tEnd, integrator.getStepStart(), 1e-9);
        assertArrayEquals(yEnd, y0, 1e-9);
        assertEquals(4, integrator.getNumberOfSteps());
        assertEquals(stepSize, integrator.getStepSize(), 1e-9);
        interpolator.storeTime(tEnd);
        assertArrayEquals(yEnd, interpolator.getStepVector(), 1e-9);
        assertEquals(integrator.getMaxError(), integrator.getErrorEstimate(), 1e-9);
    }

    /**
     * Test the integration of a simple system with an event.
     *
     * @throws MaxCountExceededException if the integration fails
     * @throws NumberFormatException if an event occurs at an unexpected time
     */
    @Test
    public void testIntegrationWithEvent() throws MaxCountExceededException, NumberFormatException {
        integrator.setEquations(new LienardEquationsWithEvent());
        integrator.setEventsManager(new CombinedEventsManager());
        final double[] y0 = { 1.0, 1.0 };
        final double tStart = 0.0;
        final double tEnd = 2.0;
        integrator.addEvent(new StepEvent(1.5) {
            @Override
            public void handle(StepHandler handler) {
                handler.handleStep(this);
            }
        });
        integrator.integrate(equations, tStart, y0, tEnd, null);
        assertTrue(stepHandler.isEventHandled());
        assertFalse(integrator.isDone());
    }

    /**
     * Test the integration of a singular system and check that an exception is
     * thrown.
     *
     * @throws MaxCountExceededException if the integration fails
     * @throws SingularMatrixException if a singular matrix occurs
     */
    @Test(expected = SingularMatrixException.class)
    public void testIntegrationSingularSystem() throws MaxCountExceededException {
        integrator.setEquations(new SingularSystem());
        final double[] y0 = { 0.0, 0.0 };
        integrator.integrate(equations, 0.0, y0, 1.0, null);
    }

    /**
     * Test the integration of a system with a very small step size.
     *
     * @throws MaxCountExceededException if the integration fails
     */
    @Test
    public void testIntegrationSmallStep() throws MaxCountExceededException {
        integrator.setEquations(new LienardEquations());
        final double[] y0 = { 1.0, 1.0 };
        final double tStart = 0.0;
        final double tEnd = 5.0;
        integrator.setMinStep(1e-15);
        integrator.integrate(equations, tStart, y0, tEnd, null);
    }

}

class SingularSystem implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 1;
    }

    @Override
    public RealMatrix getJacobian(double t, RealVector y) {
        return new Array2DRowRealMatrix(new double[][] { { 0.0 } });
    }

    @Override
    public RealVector getDerivatives(double t, RealVector y) {
        return new Array2DRowRealMatrix(new double[][] { { 1.0 / (1.0 + y.getEntry(0) * y.getEntry(0)) } })
            .operate(y);
    }

}

class LienardEquationsWithEvent implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public RealMatrix getJacobian(double t, RealVector y) {
        return new Array2DRowRealMatrix(new double[][] {
            { -y.getEntry(1), y.getEntry(0) },
            { y.getEntry(0), -1.0 }
        });
    }

    @Override
    public RealVector getDerivatives(double t, RealVector y) {
        return new Array2DRowRealMatrix(new double[][] {
            { -y.getEntry(1), y.getEntry(0) },
            { y.getEntry(0), -1.0 }
        }).operate(y);
    }

}