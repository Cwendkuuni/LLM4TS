package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.ValuesStatefulEvent;
import org.apache.commons.math3.ode.interpolators.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolatorFactory;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final int MAX_ITERATIONS = 1000;
    private static final double ABSOLUTE_TOLERANCE = 1e-12;
    private static final double RELATIVE_TOLERANCE = 1e-14;
    private static final double INITIAL_STEP_SIZE = 0.01;
    private static final double MAX_STEP_SIZE = 1.0;
    private static final double MIN_STEP_SIZE = 0.0001;

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private StepInterpolator interpolator;
    private EventManager manager;

    @Before
    public void setUp() throws Exception {
        equations = new MyDifferentialEquations();
        integrateAndCheck(new double[] {0.0}, new double[] {1.0}, 0.0, 2.0);
        integrator = new EmbeddedRungeKuttaIntegratorFactory()
            .createIntegrator("RK23", false, new double[] {0.5, 0.5},
                new double[][] {{0.25, 0.25}, {0.25, 0.}, {0.5, 0.25}},
                new double[] {0.25, 0.25}, new RungeKuttaStepInterpolatorFactory()
                .createInterpolator(1));
        integrator.setMinStep(MIN_STEP_SIZE);
        integrator.setMaxStep(MAX_STEP_SIZE);
        interpolator = integrator.getInterpolator();
        manager = new EventManager();
    }

    @Test
    public void testRk23() throws Exception {
        integrateAndCheck(new double[] {0.0}, new double[] {1.0}, 0.0, 2.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNoStepHandlers() throws Exception {
        integrator.clearStepHandlers();
        integrator.integrate(equations, 0.0, new double[] {0.0}, 2.0, new double[] {});
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        integrator.setMaxIterations(0);
        integrator.integrate(equations, 0.0, new double[] {0.0}, 2.0, new double[] {});
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws Exception {
        integrator.setMinStep(MAX_STEP_SIZE);
        integrator.setMaxStep(MIN_STEP_SIZE);
        integrator.integrate(equations, 0.0, new double[] {0.0}, 2.0, new double[] {});
    }

    @Test
    public void testStepHandlers() throws Exception {
        integrator.addStepHandler((StepHandler) (context, lastStep) -> {
            assertEquals(2, context.getNumberOfEvaluations());
            assertEquals(1.0, context.getCurrentStepSize(), 1e-12);
            assertEquals(1.5, context.getLocalStep(), 1e-12);
            assertEquals(2.0, context.getY()[0], 1e-12);
            assertEquals(3.0, context.getY()[1], 1e-12);
            context.setAccept(false);
        });
        integrator.integrate(equations, 0.0, new double[] {1.0, 1.0}, 2.0, new double[] {});
    }

    @Test
    public void testStepHandlersWithEvents() throws Exception {
        integrator.addStepHandler((StepHandler) (context, lastStep) -> {
            assertEquals(2, context.getNumberOfEvaluations());
            assertEquals(1.0, context.getCurrentStepSize(), 1e-12);
            assertEquals(1.5, context.getLocalStep(), 1e-12);
            assertEquals(2.0, context.getY()[0], 1e-12);
            assertEquals(3.0, context.getY()[1], 1e-12);
            context.setAccept(false);
        });
        integrator.addEvent(new DefaultValueEvent(1.4142135625));
        integrator.addEvent(new MyEvent(1.73205080756));
        integrator.integrate(equations, 0.0, new double[] {1.0, 1.0}, 2.0, new double[] {});
    }

    @Test
    public void testErrorEstimation() throws Exception {
        integrator = new EmbeddedRungeKuttaIntegratorFactory()
            .createIntegrator("RK45", true, new double[] {0.23, 0.23, 0.23, 0.047, 0.047, 0.047, 0.047, 0.047},
                new double[][] {
                    {0.0, 0.23, 0.0, 0.23, 0.0, 0.23, 0.0, 0.23},
                    {0.23, 0.36, 0.23, 0.36, 0.23, 0.3, 0.23, 0.33},
                    {0.36, 0.49, 0.36, 0.49, 0.36, 0.45, 0.36, 0.47},
                    {0.49, 0.62, 0.49, 0.62, 0.49, 0.56, 0.49, 0.58},
                    {0.62, 0.74, 0.62, 0.74, 0.62, 0.68, 0.62, 0.71},
                    {0.74, 0.85, 0.74, 0.85, 0.74, 0.78, 0.74, 0.81},
                    {0.85, 0.96, 0.85, 0.96, 0.85, 0.9, 0.85, 0.91},
                    {0.96, 1.0, 0.96, 1.0, 0.96, 0.94, 0.96, 0.96}
                },
                new double[] {0.3466004691358024, 0.3466004691358024},
                new RungeKuttaStepInterpolatorFactory()
                    .createInterpolator(8));
        integrator.setMinStep(MIN_STEP_SIZE / 10);
        integrator.setMaxStep(MAX_STEP_SIZE);
        integrator.setMaxIterations(MAX_ITERATIONS);
        integrator.setAbsTol(ABSOLUTE_TOLERANCE);
        integrator.setRelTol(RELATIVE_TOLERANCE);

        integrator.integrate(equations, 0.0, new double[] {1.0, 1.0}, 2.0, new double[] {});

        double error = integrator.getErrorEstimate();
        assertEquals(error, 1.0, 1e-12);
    }

    private void integrateAndCheck(double[] initialConditions, double[] expected, double t0, double tFin)
        throws Exception {
        integrator = new EmbeddedRungeKuttaIntegratorFactory()
            .createIntegrator("RK23", false, new double[] {0.5, 0.5},
                new double[][] {{0.25, 0.25}, {0.25, 0.}, {0.5, 0.25}},
                new double[] {0.25, 0.25}, new RungeKuttaStepInterpolatorFactory()
                .createInterpolator(1));
        integrator.setMinStep(INITIAL_STEP_SIZE);
        integrator.setMaxStep(MAX_STEP_SIZE);
        integrator.setAbsTol(ABSOLUTE_TOLERANCE);
        integrator.setRelTol(RELATIVE_TOLERANCE);

        double[] y = new double[equations.getDimension()];
        System.arraycopy(initialConditions, 0, y, 0, initialConditions.length);
        integrator.integrate(equations, t0, y, tFin, new double[equations.getDimension()]);

        assertArrayEquals(expected, y, 1e-12);
    }

    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0];
        }

    }

    private static class MyEvent extends ValuesStatefulEvent {

        public MyEvent(double value) {
            super(value);
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public double value(double t, double[] y) {
            return value;
        }

    }

}