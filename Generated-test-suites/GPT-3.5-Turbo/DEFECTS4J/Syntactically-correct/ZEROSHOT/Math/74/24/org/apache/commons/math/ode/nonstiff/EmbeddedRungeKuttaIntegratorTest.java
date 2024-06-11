import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private double[] dY; // derivatives

    public static class EmbeddedRungeKuttaIntegrator extends org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator {

        public EmbeddedRungeKuttaIntegrator(final String name, final boolean fsal,
                                         final double[] c, final double[][] a, final double[] b,
                                         final org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator prototype,
                                         final double minStep, final double maxStep,
                                         final double scalAbsoluteTolerance,
                                         final double scalRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        }

        public EmbeddedRungeKuttaIntegrator(final String name, final boolean fsal,
                                         final double[] c, final double[][] a, final double[] b,
                                         final org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator prototype,
                                         final double minStep, final double maxStep,
                                         final double[] vecAbsoluteTolerance,
                                         final double[] vecRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        }

        public int getOrder() {
            return 0;
        }

        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0.0;
        }

    }

    public static class FirstOrderDifferentialEquationsImpl implements FirstOrderDifferentialEquations {

        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            return;
        }

        public int getDimension() {
            return 0;
        }

    }


    @Test
    public void testIntegrate() throws Exception {

        EmbeddedRungeKuttaIntegrator intance = new EmbeddedRungeKuttaIntegrator("name", true, new double[]{}, new double[][]{}, new double[]{}, null, 0, 0, 0, 0);
        FirstOrderDifferentialEquationsImpl equations = new FirstOrderDifferentialEquationsImpl();
        intance.integrate(equations, 0, new double[]{}, 0, new double[]{});
    }

}