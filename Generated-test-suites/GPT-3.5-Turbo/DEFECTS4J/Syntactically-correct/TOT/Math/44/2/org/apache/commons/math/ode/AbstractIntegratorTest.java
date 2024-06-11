import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private double[] y0;
    private double t0;
    private double[] y;
    private double t;

    @Before
    public void setUp() {
        integrator = new TestIntegrator();
        y0 = new double[2];
        t0 = 0;
        y = new double[2];
        t = 1;
    }

    @Test
    public void testIntegrate_withValidArguments() {
        try {
            integrator.integrate(new TestDifferentialEquations(), t0, y0, t, y);
            assertEquals(t, integrator.getCurrentStepStart(), 0.0);
            assertEquals(t, integrator.getCurrentSignedStepsize(), 0.0);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_withInvalidInitialState() {
        integrator.integrate(new TestDifferentialEquations(), t0, new double[3], t, y);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_withInvalidFinalState() {
        integrator.integrate(new TestDifferentialEquations(), t0, y0, t, new double[3]);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrate_withInvalidIntegrationSpan() {
        integrator.integrate(new TestDifferentialEquations(), t0, y0, t0, y);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrate_withIllegalStateException() {
        integrator.integrate(new TestDifferentialEquations(), t0, y0, t, y);
        // Trying to integrate again without resetting the evaluations
        integrator.integrate(new TestDifferentialEquations(), t0, y0, t, y);
    }

    private class TestIntegrator extends AbstractIntegrator {

        public void integrate(ExpandableStatefulODE equations, double t) {
            equations.setTime(t);
            equations.getPrimarySetDimension();
        }

    }

    private class TestDifferentialEquations implements FirstOrderDifferentialEquations {

        public int getDimension() {
            return 2;
        }

        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0];
            yDot[1] = y[1];
        }

    }

}