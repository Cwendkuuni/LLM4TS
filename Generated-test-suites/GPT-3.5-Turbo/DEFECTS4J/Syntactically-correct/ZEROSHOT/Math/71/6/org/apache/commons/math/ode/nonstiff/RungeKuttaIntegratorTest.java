import org.junit.Test;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.IntegratorException;

public class RungeKuttaIntegratorTest {

    private int evaluations;

    private class TestEquations implements FirstOrderDifferentialEquations {

        public int getDimension() {
            return 2;
        }

        public void computeDerivatives(double t, double[] y, double[] dot) throws DerivativeException {
            evaluations++;
            dot[0] = 2 * y[0];
            dot[1] = -3 * y[1];
        }
    }

    private class DummyStepInterpolator extends AbstractStepInterpolator {

        protected DummyStepInterpolator() {
            super(new double[]{}, new double[]{}, true);
        }

        public void computeInterpolatedState(double time, double[] state) {
            // Do nothing
        }

        protected StepInterpolator doCopy() {
            return new DummyStepInterpolator();
        }

    }

    private class TestIntegrator extends RungeKuttaIntegrator {

        public TestIntegrator() {
            super("Test", new double[]{}, new double[][]{{}}, new double[]{}, new DummyStepInterpolator(), 1.0);
        }

        public double integrate(FirstOrderDifferentialEquations equations,
                                    double t0, double[] y0,
                                    double t, double[] y) throws DerivativeException, IntegratorException {
            return 0.0;
        }

    }

    @Test
    public void testIntegrate() throws Exception {
        RungeKuttaIntegrator integrator = new TestIntegrator();
        TestEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 10.0;
        double[] y = new double[2];
        integrator.integrate(equations, t0, y0, t, y);
        assertDoubleArrayEquals(new double[]{1.0, 2.0}, y);
        assertEquals(0, evaluations);
    }

    private void assertDoubleArrayEquals(double[] expected, double[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], 0.00001);
        }
    }
}