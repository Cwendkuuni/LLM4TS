package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.ContinuousIntegratorEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;

public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    private static final double RELATIVE_TOLERANCE = 1e-6;
    private static final double ABSOLUTE_TOLERANCE = 1e-8;

    private static final double[] iy = { 1.0, 1.0 };
    private static final double[] oy = { 1.0, 2.0 };
    private static final double[][] iyv = { { 1.0, 1.0 }, { 1.0, 2.0 } };
    private static final double[] oyv = { 2.0, 3.0 };
    private static final double[] parameters = { 0.01, 0.01, 0.9, 0.2, 10.0 };

    public void testSimpleIntegration() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new RK23Integrator("RK23", true, new double[] { 0.5, 0.5 },
                new double[][] { { 0.25, 0.25 }, { 0.25, 0.25 } }, new double[] { 0.5, 0.5 },
                new RungeKuttaStepInterpolator(0.1), 0.0, 10.0, null, null);

        FirstOrderDifferentialEquations equations = new DifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void calculateDerivative(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 0.1;
            }
        };

        double[] result = integrator.integrate(equations, 0.0, iy, 2.0, oy);
        double[] expected = new double[] { 1.3664631286923591, 1.3664631286923591 };
        assertEquals("Unexpected result", expected, result);
    }

    public void testIntegrationWithEvents() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new RK45Integrator("RK45", true, new double[] { 0.3, 0.3 },
                new double[][] { { 0.25, 0.25, 0.25, 0.25 }, { 0.25, 0.25, 0.25, 0.25 } },
                new double[] { 0.5, 0.5 }, new RungeKuttaStepInterpolator(0.1), 0.0, 10.0,
                new double[] { 1.0e-8, 1.0e-8 }, null);

        FirstOrderDifferentialEquations equations = new DifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void calculateDerivative(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 0.1;
            }
        };

        EventManager eventManager = new EventManager();
        eventManager.addEvent(new When(1.5).addAction(new StepHandler() {
            @Override
            public void handleStep(double time, double[] y, double[] yDot) {
                y[0] = 1.0 / Math.exp(y[0]);
            }
        }));

        double[] result = integrator.integrate(equations, 0.0, iy, 2.0, oy, eventManager);
        double[] expected = new double[] { 0.7358052145613426, 0.7358052145613426 };
        assertEquals("Unexpected result", expected, result);

        Event events = eventManager.getLastEvent();
        ContinuousIntegratorEvent event = (ContinuousIntegratorEvent) events;
        assertEquals("Unexpected event time", 1.5, event.getTime());
    }

    public void testIntegrationWithDenseOutput() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[] { 0.3, 0.3 },
                new double[][] { { 0.25, 0.25, 0.25, 0.25 }, { 0.25, 0.25, 0.25, 0.25 } },
                new double[] { 0.5, 0.5 }, new RungeKuttaStepInterpolator(0.1), 0.0, 10.0,
                new double[] { 1.0e-8, 1.0e-8 }, new double[] { 1.0e-8, 1.0e-8 }, new DenseOutputHandler());

        FirstOrderDifferentialEquations equations = new DifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void calculateDerivative(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 0.1;
                derivatives[1] = state[1] * 0.1;
            }
        };

        double[] result = integrator.integrate(equations, 0.0, iy, 2.0, oy, null);
        double[] expected = new double[] { 1.3658855792380615, 1.3658855792380615 };
        assertEquals("Unexpected result", expected, result);

        double[][] yDots = integrator.getYdots();
        double[][] expectedYdots = new double[][] { { 1.3658855792380615, 1.3658855792380615 },
                { 1.402415985637151, 1.402415985637151 },
                { 1.4386393895687175, 1.4386393895687175 },
                { 1.474571431723466, 1.474571431723466 },
                { 1.5103176254079465, 1.5103176254079465 } };
        assertArrayEquals("Unexpected ydots", expectedYdots, yDots);
    }

    public void testIntegrationWithCustomStepInterpolator() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[] { 0.3, 0.3 },
                new double[][] { { 0.25, 0.25, 0.25, 0.25 }, { 0.25, 0.25, 0.25, 0.25 } },
                new double[] { 0.5, 0.5 }, new MyStepInterpolator(), 0.0, 10.0,
                new double[] { 1.0e-8, 1.0e-8 }, new double[] { 1.0e-8, 1.0e-8 });

        FirstOrderDifferentialEquations equations = new DifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void calculateDerivative(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 0.1;
            }
        };

        double[] result = integrator.integrate(equations, 0.0, iy, 2.0, oy, null);
        double[] expected = new double[] { 1.3658855792380615, 1.3658855792380615 };
        assertEquals("Unexpected result", expected, result);
    }

    public void testIntegrationWithException() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new RKF45Integrator("RKF45", true, new double[] { 0.3, 0.3 },
                new double[][] { { 0.25, 0.25, 0.25, 0.25 }, { 0.25, 0.25, 0.25, 0.25 } },
                new double[] { 0.5, 0.5 }, new RungeKuttaStepInterpolator(0.1), 0.0, 10.0,
                new double[] { 1.0e-8, 1.0e-8 }, null);

        FirstOrderDifferentialEquations equations = new DifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void calculateDerivative(double time, double[] state, double[] derivatives) {
                throw new RuntimeException("Test exception");
            }
        };

        try {
            integrator.integrate(equations, 0.0, iy, 2.0, oy, null);
            fail("An exception should have been thrown");
        } catch (IntegratorException e) {
            // expected
        }
    }

    public void testIntegrationWithDerivativeException() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new RKF45Integrator("RKF45", true, new double[] { 0.3, 0.3 },
                new double[][] { { 0.25, 0.25, 0.25, 0.25 }, { 0.25, 0.25, 0.25, 0.25 } },
                new double[] { 0.5, 0.5 }, new RungeKuttaStepInterpolator(0.1), 0.0, 10.0,
                new double[] { 1.0e-8, 1.0e-8 }, null);

        FirstOrderDifferentialEquations equations = new DifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void calculateDerivative(double time, double[] state, double[] derivatives) throws DerivativeException {
                throw new DerivativeException("Test exception");
            }
        };

        try {
            integrator.integrate(equations, 0.0, iy, 2.0, oy, null);
            fail("An exception should have been thrown");
        } catch (DerivativeException e) {
            // expected
        }
    }

    private static class DenseOutputHandler implements StepHandler {
        @Override
        public void handleStep(double time, double[] y, double[] yDot) {
            System.out.println(String.format("time = %f, y = %s, yDot = %s", time, java.util.Arrays.toString(y),
                    java.util.Arrays.toString(yDot)));
        }
    }

    private static class MyStepInterpolator extends RungeKuttaStepInterpolator {
        public MyStepInterpolator() {
            super(1.0, 0.1);
        }

        @Override
        public void storeTime(double time) {
            // custom implementation of the storeTime method
        }
    }

}