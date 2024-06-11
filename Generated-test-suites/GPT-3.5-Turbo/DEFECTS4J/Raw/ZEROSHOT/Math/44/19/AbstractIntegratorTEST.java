import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegratorMock();
        double t = 0.0;
        double[] y = new double[2];
        double[] yDot = new double[2];

        integrator.computeDerivatives(t, y, yDot);

        assertEquals(2, yDot.length);
        assertEquals(0.0, yDot[0], 0.0);
        assertEquals(0.0, yDot[1], 0.0);
    }

    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegratorMock();

        double t0 = 0.0;
        double[] y0 = new double[2];
        double t = 1.0;
        double[] y = new double[2];

        double result = integrator.integrate(new MockDifferentialEquations(), t0, y0, t, y);

        assertEquals(t, result, 0.0);
        assertEquals(t0, y[0], 0.0);
        assertEquals(t, y[1], 0.0);
    }

    private class AbstractIntegratorMock extends AbstractIntegrator {
        public void integrate(ExpandableStatefulODE equations, double t)
            throws MathIllegalStateException, MathIllegalArgumentException {
        }
    }

    private class MockDifferentialEquations implements FirstOrderDifferentialEquations {
        public int getDimension() {
            return 2;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot)
            throws MaxCountExceededException {
            yDot[0] = t;
            yDot[1] = t;
        }
    }

}