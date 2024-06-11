import org.junit.Assert;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Test
    public void testIntegrate() {
        integrator = new TestIntegrator();

        double t0 = 0.0;
        double[] y0 = {1.0, 0.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        double result = integrator.integrate(new TestEquations(), t0, y0, t, y);

        Assert.assertEquals(t, result, 1e-6);
        Assert.assertEquals(Math.cos(t), y[0], 1e-6);
        Assert.assertEquals(-Math.sin(t), y[1], 1e-6);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatch() {
        integrator = new TestIntegrator();

        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.integrate(new TestEquations(), t0, y0, t, y);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateIllegalState() {
        integrator = new TestIntegrator();

        double t0 = 0.0;
        double[] y0 = {1.0, 0.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.integrate(new TestEquations(), t0, y0, t, y);
        integrator.integrate(new TestEquations(), t0, y0, t, y);
    }

    private static class TestIntegrator extends AbstractIntegrator {

        @Override
        public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
            double t0 = equations.getTime();
            double[] y0 = equations.getPrimaryState();

            equations.setTime(t);
            equations.setPrimaryState(y0);
        }

    }

    private static class TestEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }

    }

}