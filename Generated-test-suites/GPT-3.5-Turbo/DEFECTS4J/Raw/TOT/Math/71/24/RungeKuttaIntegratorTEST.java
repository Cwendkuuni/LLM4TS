import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RungeKuttaIntegratorTest {

    private static final double TEST_STEP_SIZE = 0.1;
    private static final double TEST_TOLERANCE = 1e-10;

    private RungeKuttaIntegrator integrator;
    private TestEquations equations;

    private static class TestEquations implements FirstOrderDifferentialEquations {

        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = 2 * y[0];
        }

        public int getDimension() {
            return 1;
        }
    }

    @Before
    public void setUp() {
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {
                {0.0, 0.0, 0.0, 0.0},
                {0.5, 0.0, 0.0, 0.0},
                {0.0, 0.5, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0}
        };
        double[] b = {1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0};

        this.integrator = new RungeKuttaIntegrator("TestIntegrator", c, a, b, null, TEST_STEP_SIZE);
        this.equations = new TestEquations();
    }

    @Test
    public void testIntegration() throws DerivativeException {
        double[] y0 = {1.0};
        double[] y = new double[1];

        double t0 = 0.0;
        double t = 1.0;

        double stopTime = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(t, stopTime, TEST_TOLERANCE);
        assertEquals(Math.exp(2 * t), y[0], TEST_TOLERANCE);
    }

    @Test(expected = DerivativeException.class)
    public void testComputeDerivativesException() throws DerivativeException {
        double[] y0 = {1.0};
        double[] y = new double[1];

        double t0 = 0.0;
        double t = 1.0;

        integrator.integrate(new FirstOrderDifferentialEquations() {
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                throw new DerivativeException("Error computing derivatives");
            }

            public int getDimension() {
                return 1;
            }
        }, t0, y0, t, y);
    }

    @Test
    public void testStepHandler() throws DerivativeException {
        double[] y0 = {1.0};
        double[] y = new double[1];

        double t0 = 0.0;
        double t = 1.0;

        StepHandler stepHandler = new StepHandler() {
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                double t = interpolator.getCurrentTime();
                double[] y = interpolator.getInterpolatedState();
                assertEquals(t, interpolator.getPreviousTime() + TEST_STEP_SIZE, TEST_TOLERANCE);
                assertEquals(Math.exp(2 * t), y[0], TEST_TOLERANCE);
            }

            public void init(double t0, double[] y0, double t) {}

            public void reset() {}
        };

        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test
    public void testZeroStepSize() throws DerivativeException {
        double[] y0 = {1.0};
        double[] y = new double[1];

        double t0 = 0.0;
        double t = 1.0;

        integrator.setStepSize(0.0);
        double stopTime = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(t, stopTime, TEST_TOLERANCE);
        assertEquals(1.0, y[0], TEST_TOLERANCE);
    }

}