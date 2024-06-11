import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for EmbeddedRungeKuttaIntegrator.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for EmbeddedRungeKuttaIntegrator.integrate().
     * @throws MathIllegalArgumentException if the initial time is not valid
     */
    @Test
    public void testIntegrate() throws MathIllegalArgumentException {
        // create a simple ODE that we will use to test the integrator
        final SimpleODE ode = new SimpleODE();

        // create an instance of the Runge-Kutta integrator
        final EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKutta45Integrator();

        // integrate the ODE from t=0 to t=2 using a step size of 0.1
        integrator.integrate(ode, 0.0, 2.0, 0.1);

        // check that the result is correct
        final double[] expected = {0.0, 1.0};
        final double[] result = ode.getCompleteState();
        assertArrayEquals(expected, result, 0.001);
    }

    /**
     * Test method for EmbeddedRungeKuttaIntegrator.integrate() with an exception.
     * @throws MathIllegalArgumentException if the initial time is not valid
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateException() throws MathIllegalArgumentException {
        // create a simple ODE that we will use to test the integrator
        final SimpleODE ode = new SimpleODE();

        // create an instance of the Runge-Kutta integrator
        final EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKutta45Integrator();

        // try to integrate the ODE from a negative initial time
        integrator.integrate(ode, -1.0, 0.0, 0.1);

    }

    /**
     * Test method for EmbeddedRungeKuttaIntegrator.integrate() with step size control.
     * @throws MathIllegalArgumentException if the initial time is not valid
     */
    @Test
    public void testIntegrateWithStepSizeControl() throws MathIllegalArgumentException {
        // create a simple ODE that we will use to test the integrator
        final SimpleODE ode = new SimpleODE();

        // create an instance of the Runge-Kutta integrator
        final EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKutta45Integrator();

        // set the minimum and maximum step sizes
        integrator.setMinStep(0.01);
        integrator.setMaxStep(0.5);

        // integrate the ODE from t=0 to t=2 using adaptive step sizes
        integrator.integrate(ode, 0.0, 2.0, Double.NaN);

        // check that the result is correct
        final double[] expected = {0.0, 1.0};
        final double[] result = ode.getCompleteState();
        assertArrayEquals(expected, result, 0.001);

        // check that the step sizes were adapted correctly
        final double[] stepSizes = integrator.getStepSizes();
        assertArrayEquals(new double[]{0.01, 0.02, 0.04, 0.08, 0.16, 0.32, 0.64, 1.28, 2.56}, stepSizes, 0.0);

    }

    /**
     * Test method for EmbeddedRungeKuttaIntegrator.integrate() with custom tolerances.
     * @throws MathIllegalArgumentException if the initial time is not valid
     */
    @Test
    public void testIntegrateWithCustomTolerances() throws MathIllegalArgumentException {
        // create a simple ODE that we will use to test the integrator
        final SimpleODE ode = new SimpleODE();

        // create an instance of the Runge-Kutta integrator
        final EmbeddedRungeKutta45Integrator integrator =
                new EmbeddedRungeKutta45Integrator();

        // set custom tolerances
        final double[] vecAbsoluteTolerance = {1e-6, 1e-6};
        final double[] vecRelativeTolerance = {1e-8, 1e-8};
        integrator.setTolerances(vecAbsoluteTolerance, vecRelativeTolerance);

        // integrate the ODE from t=0 to t=2 using the custom tolerances
        integrator.integrate(ode, 0.0, 2.0, 0.1);

        // check that the result is correct
        final double[] expected = {0.0, 1.0};
        final double[] result = ode.getCompleteState();
        assertArrayEquals(expected, result, 1e-6);

    }

    /**
     * Test method for EmbeddedRungeKuttaIntegrator.integrate() with a custom step handler.
     * @throws MathIllegalArgumentException if the initial time is not valid
     */
    @Test
    public void testIntegrateWithCustomStepHandler() throws MathIllegalArgumentException {
        // create a simple ODE that we will use to test the integrator
        final SimpleODE ode = new SimpleODE();

        // create an instance of the Runge-Kutta integrator
        final EmbeddedRungeKutta45Integrator integrator =
                new EmbeddedRungeKutta45Integrator();

        // create a custom step handler
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(double[] state, double time, double[] derivatives) {
                // do something with the state and time here
            }
        };

        // integrate the ODE from t=0 to t=2 using the custom step handler
        integrator.integrate(ode, 0.0, 2.0, 0.1, stepHandler);

        // check that the result is correct
        final double[] expected = {0.0, 1.0};
        final double[] result = ode.getCompleteState();
        assertArrayEquals(expected, result, 0.001);

    }

    /**
     * Test method for EmbeddedRungeKuttaIntegrator.integrate() with an error.
     * @throws MathIllegalArgumentException if the initial time is not valid
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithError() throws MathIllegalArgumentException {
        // create a simple ODE that we will use to test the integrator
        final SimpleODE ode = new SimpleODE();

        // create an instance of the Runge-Kutta integrator
        final EmbeddedRungeKutta45Integrator integrator =
                new EmbeddedRungeKutta45Integrator();

        // try to integrate the ODE with an error in the derivative computation
        integrator.integrate(ode, 0.0, 2.0, 0.1, new MathIllegalStateException("Error"));

    }

}

/**
 * Simple ODE for testing purposes.
 */
class SimpleODE extends ExpandableStatefulODE {

    private final static double TIME_STEP = 0.1;
    private final static double[] INITIAL_STATE = {0.0, 0.0};
    private final static double[][] JACOBIAN = {{0.0, 1.0}, {1.0, 0.0}};

    /**
     * Constructor.
     */
    public SimpleODE() {
        super(2, INITIAL_STATE, JACOBIAN);
    }

    /**
     * Get the complete state of the ODE.
     * @return the complete state of the ODE
     */
    public double[] getCompleteState() {
        return getState();
    }

    /**
     * Set the complete state of the ODE.
     * @param state the complete state of the ODE
     */
    public void setCompleteState(double[] state) {
        setState(state);
    }

}