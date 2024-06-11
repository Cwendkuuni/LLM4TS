import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test for EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrate() {
        // Create a sample ODE and integrator
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getState(double time) {
                return new double[]{sin(time)};
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                y[0] = cos(t);
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta78Integrator(
                "Embedded Runge-Kutta 7(8) method", false,
                new double[]{0.3, 0.47360, 0.04983, 0.01296, 0.01132, 0.02389, 0.27846},
                new double[][]{
                        {0.0, 0.0},
                        {0.235988, 0.0},
                        {0.679251, 0.235988},
                        {0.865063, 0.346545},
                        {1.0, 0.422788},
                        {1.0, 0.501463},
                        {0.555556, 0.579241},
                        {0.0, 0.703912}
                },
                new double[]{0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3},
                new RungeKuttaStepInterpolator() {}, 0.001, 0.01,
                0.0001, 0.001);

        // Set up a step handler to check the results
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(double time, double[] y) {
                assertEquals(Math.sin(time), y[0], 1e-6);
            }
        };
        integrator.setStepHandler(stepHandler);

        // Integrate from 0 to 2*PI
        integrator.integrate(equations, 0);

        // Integrate from 2*PI to 4*PI
        integrator.integrate(equations, 2 * Math.PI);

        // Integrate from 4*PI to 6*PI
        integrator.integrate(equations, 4 * Math.PI);

        // Integrate from 6*PI to 8*PI
        integrator.integrate(equations, 6 * Math.PI);

        // Integrate from 8*PI to 10*PI
        integrator.integrate(equations, 8 * Math.PI);

        // Test exception when initial time is greater than final time
        try {
            integrator.integrate(equations, -2 * Math.PI);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        reset(equations);
        reset(integrator);

        // Test exception when initial time is less than minimal step
        try {
            integrator.integrate(equations, 0.0 - 0.001);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        reset(equations);
        reset(integrator);

        // Test exception when final time is less than minimal step
        try {
            integrator.integrate(equations, 0.001 - 2 * Math.PI);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        reset(equations);
        reset(integrator);

        // Test exception when relative tolerance is negative
        try {
            integrator.integrate(equations, 0, 2 * Math.PI, null, null, -0.1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        reset(equations);
        reset(integrator);

        // Test exception when absolute tolerance is negative
        try {
            integrator.integrate(equations, 0, 2 * Math.PI, new double[]{-0.1}, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        reset(equations);
        reset(integrator);

    }

    /**
     * Helper method to reset the state of the given objects.
     *
     * @param objects the objects to reset
     */
    private void reset(Object... objects) {
        for (Object obj : objects) {
            if (obj instanceof ExpandableStatefulODE) {
                ((ExpandableStatefulODE) obj).reset();
            }
            if (obj instanceof EmbeddedRungeKuttaIntegrator) {
                ((EmbeddedRungeKuttaIntegrator) obj).resetInternalState();
            }
        }
    }

}