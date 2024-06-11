package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.RootFindFailureException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RootFunction;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** A simple test equation. */
    private static final FirstOrderDifferentialEquations equations =
        new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.exp(y[0]);
            }
        };

    /** The integrator under test. */
    private EmbeddedRungeKuttaIntegrator integrator;

    /** A simple test function. */
    private static double testFunction(double t) {
        return Math.sin(t);
    }

    /** A test root function. */
    private static class TestRootFunction implements RootFunction {
        @Override
        public double value(double t, double[] y) {
            return y[0] - testFunction(t);
        }

        @Override
        public double root(double t, double accuracy) {
            return testFunction(t);
        }
    }

    /** A step handler that counts the number of steps. */
    private static class StepCounter implements StepHandler {
        int steps = 0;

        @Override
        public void handleStep(AbstractStepInterpolator interpolator, boolean success) {
            steps++;
        }

        int getSteps() {
            return steps;
        }

        void reset() {
            steps = 0;
        }
    }

    /** Set up the integrator for testing. */
    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegrator("RK23", false,
                new double[]{1.0/4.0, 1.0/2.0, 1.0},
                new double[][]{{0.0, 1.0}, {0.0, 0.5, 0.5}, {0.0, 0.5}},
                new double[]{0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator(new DummyStepInterpolator(new double[1], false), 1, false),
                0.001, 1.0, new double[]{1.0e-6, 1.0e-6}) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double err = 0.0;
                for (int j = 0; j < y0.length; j++) {
                    err += Math.pow(Math.abs(y1[j] - y0[j]), 2);
                }
                return Math.sqrt(err);
            }

            @Override
            public int getOrder() {
                return 2;
            }
        };
    }

    /** Test the integrator with a simple equation. */
    @Test
    public void testSimpleIntegration() {
        double[] y = {0.0};
        double tStart = 0.0;
        double tStop  = 1.0;
        double result = integrator.integrate(equations, tStart, y, tStop, y);
        assertEquals(tStop, result, 1e-12);
    }

    /** Test the integrator with a simple root function. */
    @Test(expected = RootFindFailureException.class)
    public void testRootIntegration() throws RootFindFailureException {
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = -(y[0] - testFunction(t));
            }
        });
        double[] y = {0.0};
        double tStart = 2.0;
        double tStop  = 3.0;
        integrator.integrate(equations, tStart, y, tStop, y);
    }

    /** Test the integrator with a step handler. */
    @Test
    public void testStepHandler() {
        integrator.setEquations(equations);
        double[] y = {0.0};
        double tStart = 0.0;
        double tStop  = 1.0;
        StepCounter counter = new StepCounter();
        integrator.addStepHandler(counter);
        double result = integrator.integrate(equations, tStart, y, tStop, y);
        assertEquals(tStop, result, 1e-12);
        assertEquals(3, counter.getSteps());
        counter.reset();
    }

    /** Test the integrator with a simple event. */
    @Test
    public void testEventIntegration() {
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.exp(y[0]);
            }
        });
        integrator.setMinStep(1.0e-12);
        integrator.addStepHandler(new StepCounter());
        integrator.addEventsHandler(new Event(1.5, new TestRootFunction()));
        double[] y = {0.0};
        double tStart = 0.0;
        double tStop  = 2.0;
        double result = integrator.integrate(equations, tStart, y, tStop, y);
        assertEquals(1.5, integrator.getLastEventTime(), 1e-12);
        assertEquals(tStop, result, 1e-12);
    }

    /** Test the integrator with a max count exceeded exception. */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountIntegration() throws MaxCountExceededException {
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                throw new RuntimeException("Max steps exceeded.");
            }
        });
        double[] y = {0.0};
        double tStart = 0.0;
        double tStop  = 1.0;
        integrator.integrate(equations, tStart, y, tStop, y);
    }

    /** Test the integrator with a no bracket exception. */
    @Test(expected = NoBracketException.class)
    public void testNoBracketIntegration() throws NoBracketException {
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.sqrt(y[0]);
            }
        });
        double[] y = {0.0};
        double tStart = 0.0;
        double tStop  = 1.0;
        integrator.integrate(equations, tStart, y, tStop, y);
    }

}