import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MaxCountExceededException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private double t0;
    private double[] y0;
    private double t;
    private double[] y;

    @Before
    public void setUp() {
        integrator = new MockIntegrator();
        t0 = 0.0;
        y0 = new double[]{1.0, 2.0, 3.0};
        t = 10.0;
        y = new double[y0.length];
    }

    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        double result = integrator.integrate(new MockEquations(), t0, y0, t, y);
        assertEquals(t, result, 1e-8);
        assertEquals(y0[0], y[0], 1e-8);
        assertEquals(y0[1], y[1], 1e-8);
        assertEquals(y0[2], y[2], 1e-8);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatch() throws MathIllegalStateException, MathIllegalArgumentException {
        double[] invalidY0 = new double[y0.length + 1];
        integrator.integrate(new MockEquations(), t0, invalidY0, t, y);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateResultDimensionMismatch() throws MathIllegalStateException, MathIllegalArgumentException {
        double[] invalidY = new double[y0.length + 1];
        integrator.integrate(new MockEquations(), t0, y0, t, invalidY);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateSmallInterval() throws MathIllegalStateException, MathIllegalArgumentException {
        double tSmall = t0 - 1e-10;
        integrator.integrate(new MockEquations(), t0, y0, tSmall, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxEvaluationsExceeded() throws MathIllegalStateException, MathIllegalArgumentException {
        integrator.setMaxEvaluations(1);
        integrator.integrate(new MockEquations(), t0, y0, t, y);
    }

    private static class MockIntegrator extends AbstractIntegrator {
        public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
            equations.setTime(t);
            equations.getPrimaryState()[0] = 1.0;
            equations.getPrimaryState()[1] = 2.0;
            equations.getPrimaryState()[2] = 3.0;
        }
    }

    private static class MockEquations implements FirstOrderDifferentialEquations {
        public int getDimension() {
            return 3;
        }

        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException {
            yDot[0] = 0.0;
            yDot[1] = 0.0;
            yDot[2] = 0.0;
        }
    }

}