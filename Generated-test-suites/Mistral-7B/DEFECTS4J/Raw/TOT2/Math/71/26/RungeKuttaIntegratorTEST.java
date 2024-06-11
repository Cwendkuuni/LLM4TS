import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test the basic functionality of the integrator.
     */
    @Test
    public void testBasicFunctionality() {
        // Create a simple differential equations system
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]);
            }
        };

        // Create and initialize the integrator
        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Runge-Kutta",
                new double[] {0.5, 0.5, 0.5},
                new double[][] {
                        {0, 0.5, 0.5},
                        {0.25, 0, 0.25},
                        {0.25, 0.25, 0.25}
                },
                new double[] {0.5, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.1) {
            @Override
            protected double getStepSize() {
                return 0.1;
            }
        };

        // Initialize the step handler to capture the integration results
        final double[] y = new double[1];
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                y[0] = context.getY();
            }
        };

        // Integrate the system from t = 0 to t = 1 with a step size of 0.1
        final double initialValue = 1.0;
        integrator.integrate(equations, 0.0, new double[] {initialValue}, 1.0, y);

        // Check the final value of the variable
        Assert.assertEquals(Math.E, y[0], 1e-6);
    }

    /**
     * Test the handling of a singular point.
     */
    @Test(expected = IntegratorException.class)
    public void testSingularPoint() {
        // Create a simple differential equations system with a singular point
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0 / (y[0] * y[0]);
            }
        };

        // Create and initialize the integrator
        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Runge-Kutta",
                new double[] {0.5, 0.5, 0.5},
                new double[][] {
                        {0, 0.5, 0.5},
                        {0.25, 0, 0.25},
                        {0.25, 0.25, 0.25}
                },
                new double[] {0.5, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.1) {
            @Override
            protected double getStepSize() {
                return 0.1;
            }
        };

        // Integrate the system from t = 0 to t = 1 with a step size of 0.1
        final double initialValue = 1.0;
        integrator.integrate(equations, 0.0, new double[] {initialValue}, 1.0, null);

        // This integration should fail due to the singular point
    }

    /**
     * Test the handling of an event.
     */
    @Test
    public void testEventHandling() {
        // Create a simple differential equations system with an event
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]);
            }
        };

        // Define an event that stops the integration when y = 10
        final Event stopEvent = new When(10.0);

        // Create and initialize the integrator
        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Runge-Kutta",
                new double[] {0.5, 0.5, 0.5},
                new double[][] {
                        {0, 0.5, 0.5},
                        {0.25, 0, 0.25},
                        {0.25, 0.25, 0.25}
                },
                new double[] {0.5, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.1) {
            @Override
            protected double getStepSize() {
                return 0.1;
            }

            @Override
            public void addEventHandlers(CombinedEventsManager manager) {
                manager.addHandler(stopEvent);
            }
        };

        // Integrate the system from t = 0 to t = 10 with a step size of 0.1
        final double initialValue = 0.0;
        integrator.integrate(equations, 0.0, new double[] {initialValue}, 10.0, null);

        // Check that the integration has stopped at the event
        Assert.assertEquals(10.0, integrator.getStepStart(), 1e-6);
    }

}