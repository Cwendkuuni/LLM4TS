import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the RungeKuttaIntegrator class.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test case for the integrate method with a simple differential equation.
     */
    @Test
    public void testSimpleIntegration() {
        double[] y0 = {1.0};
        double t0 = 0.0;
        double tEnd = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y0.length; i++) {
                    yDot[i] = y[i] * 0.1;
                }
            }
        };

        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new RungeKuttaStepInterpolator(new EulerInterpolator(0.1)));
        double result = integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);

        Assert.assertEquals(tEnd, result, 1e-6);
    }

    /**
     * Test case for the integrate method with a differential equation with a singularity.
     */
    @Test(expected = DerivativeException.class)
    public void testSingularity() {
        double[] y0 = {1.0 / 0.0};
        double t0 = 0.0;
        double tEnd = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y0.length; i++) {
                    yDot[i] = 1.0 / y[i];
                }
            }
        };

        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new RungeKuttaStepInterpolator(new EulerInterpolator(0.1)));
        integrator.integrate(equations, t0, y0, 1.0, new double[y0.length]);
    }

    /**
     * Test case for the integrate method with a differential equation with an event.
     */
    @Test
    public void testEvent() {
        double[] y0 = {1.0};
        double t0 = 0.0;
        double tEnd = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y0.length; i++) {
                    yDot[i] = y[i] * 0.1;
                }
            }
        };

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (stepNumber > 10) {
                    throw new RuntimeException("Test exception");
                }
            }
        };

        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new RungeKuttaStepInterpolator(new EulerInterpolator(0.1)));
        integrator.setStepHandlers(stepHandler);

        CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(new When(1.0) {
            @Override
            public Event nextEvent(double t, double[] y) {
                return new Event() {
                    @Override
                    public void handle(double t, double[] y) {
                        throw new RuntimeException("Event exception");
                    }
                };
            }
        });

        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length], eventsManager);

        // The test should not reach this point
    }

    /**
     * Test case for the integrate method with a differential equation that throws an exception.
     */
    @Test(expected = IntegratorException.class)
    public void testException() {
        double[] y0 = {1.0};
        double t0 = 0.0;
        double tEnd = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                throw new RuntimeException("Integrator exception");
            }
        };

        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new RungeKuttaStepInterpolator(new EulerInterpolator(0.1)));
        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);
    }

}