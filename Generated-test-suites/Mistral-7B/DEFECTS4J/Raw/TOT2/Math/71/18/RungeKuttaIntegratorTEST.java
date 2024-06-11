import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for RungeKuttaIntegrator.
 */
public class RungeKuttaIntegratorTest {

    /** Integrator under test. */
    private RungeKuttaIntegrator integrator;

    /** Differential equations to integrate. */
    private FirstOrderDifferentialEquations equations;

    /** Step handler to log integration results. */
    private StepHandler stepHandler;

    /**
     * Set up the test environment.
     */
    @Before
    public void setUp() {
        // Create a simple differential equation to integrate: dy/dt = y
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };

        // Create a step handler to log integration results
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(double t, double[] y, double[] yDot) {
                System.out.printf("t = %.6f, y = %.6f, y' = %.6f%n", t, y[0], yDot[0]);
            }
        };

        // Create a Runge-Kutta integrator with a small step size
        integrator = new RungeKuttaIntegrator("RK4", new double[] { 0.5, 0.5, 0.5, 0.5 },
                new double[][] { { 0, 0, 1/6, 1/2, 1/2, 1/6 }, { 0, 1/2, 1/2, 1/2, 1, 1 } },
                new double[] { 1/6, 1/2, 1/2, 1/6 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public double interpolate(double[] yDot, double t) {
                        // Simple linear interpolation
                        return (1 - t) * yDot[0] + t * yDot[1];
                    }
                }, 0.01) {
            @Override
            public void addStepHandler(StepHandler handler) {
                stepHandlers.add(handler);
            }
        };

        integrator.addStepHandler(stepHandler);
    }

    /**
     * Test integration of a simple differential equation.
     */
    @Test
    public void testIntegration() {
        // Initialize the integrator with initial conditions
        double[] initialConditions = { 1.0 };
        integrator.reset();
        integrator.initialize(equations, 0.0, initialConditions);

        // Perform the integration
        double[] result = integrator.integrate(equations, 0.0, initialConditions, 1.0, new double[initialConditions.length]);

        // Check the results
        Assert.assertArrayEquals(new double[] { 2.718281828 }, result, 0.0);

        // Check the integration log
        Assert.assertEquals("t = 0.000000, y = 1.000000, y' = 1.000000\n" +
                           "t = 0.062894, y = 1.105169, y' = 1.105169\n" +
                           "t = 0.125788, y = 1.210859, y' = 1.210859\n" +
                           "t = 0.188682, y = 1.326314, y' = 1.326314\n" +
                           "t = 0.251576, y = 1.451707, y' = 1.451707\n" +
                           "t = 0.314470, y = 1.588136, y' = 1.588136\n" +
                           "t = 0.377364, y = 1.737590, y' = 1.737590\n" +
                           "t = 0.439258, y = 1.899201, y' = 1.899201\n" +
                           "t = 0.501152, y = 2.072089, y' = 2.072089\n" +
                           "t = 0.563046, y = 2.256154, y' = 2.256154\n" +
                           "t = 0.624939, y = 2.451397, y' = 2.451397\n" +
                           "t = 0.686832, y = 2.658819, y' = 2.658819\n" +
                           "t = 0.748725, y = 2.880530, y' = 2.880530\n" +
                           "t = 0.809618, y = 3.116639, y' = 3.116639\n" +
                           "t = 0.869511, y = 3.378249, y' = 3.378249\n" +
                           "t = 0.928404, y = 3.666467, y' = 3.666467\n" +
                           "t = 1.000000, y = 3.961499, y' = 3.961499", stepHandler.getLog());
    }

    /**
     * Test integration with an event.
     */
    @Test
    public void testIntegrationWithEvent() {
        // Create a differential equation with an event
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }

            @Override
            public Event[] getEvents(double t, double[] y, double[] yDot, double[] yDotDot, double[] yDotPrime) {
                if (t >= 1.0 && y[0] >= 2.0) {
                    return new Event[] { new When(t, y[0], t) };
                }
                return new Event[0];
            }
        };

        // Create a step handler to log integration results
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(double t, double[] y, double[] yDot) {
                System.out.printf("t = %.6f, y = %.6f, y' = %.6f%n", t, y[0], yDot[0]);
            }
        };

        // Create a Runge-Kutta integrator with a small step size
        integrator = new RungeKuttaIntegrator("RK4", new double[] { 0.5, 0.5, 0.5, 0.5 },
                new double[][] { { 0, 0, 1/6, 1/2, 1/2, 1/6 }, { 0, 1/2, 1/2, 1/2, 1, 1 } },
                new double[] { 1/6, 1/2, 1/2, 1/6 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public double interpolate(double[] yDot, double t) {
                        // Simple linear interpolation
                        return (1 - t) * yDot[0] + t * yDot[1];
                    }
                }, 0.01) {
            @Override
            public void addStepHandler(StepHandler handler) {
                stepHandlers.add(handler);
            }
        };

        integrator.addStepHandler(stepHandler);

        // Initialize the integrator with initial conditions
        double[] initialConditions = { 1.0 };
        integrator.reset();
        integrator.initialize(equations, 0.0, initialConditions);

        // Perform the integration
        integrator.integrate(equations, 0.0, initialConditions, 1.2, new double[initialConditions.length]);

        // Check the integration log
        Assert.assertEquals("t = 0.000000, y = 1.000000, y' = 1.000000\n" +
                           "t = 0.062894, y = 1.105169, y' = 1.105169\n" +
                           "t = 0.125788, y = 1.210859, y' = 1.210859\n" +
                           "t = 0.188682, y = 1.326314, y' = 1.326314\n" +
                           "t = 0.251576, y = 1.451707, y' = 1.451707\n" +
                           "t = 0.314470, y = 1.588136, y' = 1.588136\n" +
                           "t = 0.377364, y = 1.737590, y' = 1.737590\n" +
                           "t = 0.439258, y = 1.899201, y' = 1.899201\n" +
                           "t = 0.501152, y = 2.072089, y' = 2.072089\n" +
                           "t = 0.563046, y = 2.256154, y' = 2.256154\n" +
                           "t = 0.624939, y = 2.451397, y' = 2.451397\n" +
                           "t = 0.686832, y = 2.658819, y' = 2.658819\n" +
                           "t = 0.748725, y = 2.880530, y' = 2.880530\n" +
                           "t = 0.809618, y = 3.116639, y' = 3.116639\n" +
                           "t = 0.869511, y = 3.378249, y' = 3.378249\n" +
                           "t = 0.928404, y = 3.666467, y' = 3.666467\n" +
                           "t = 1.000000, y = 3.961499, y' = 3.961499\n" +
                           "t = 1.061235, y = 4.110613, y' = 4.110613\n", stepHandler.getLog());

        // Check the event handling
        Assert.assertEquals(1.0, integrator.getStepStart(), 0.0);
        Assert.assertEquals(1.061235, integrator.getStepEnd(), 0.0);
    }

}