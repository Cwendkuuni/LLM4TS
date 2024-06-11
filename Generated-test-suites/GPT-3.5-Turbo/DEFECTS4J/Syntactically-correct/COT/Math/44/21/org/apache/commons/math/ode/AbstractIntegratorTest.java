import static org.junit.Assert.*;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private TestDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new MockIntegrator();
        equations = new TestDifferentialEquations();
    }

    @Test
    public void testIntegrate() throws Exception {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];

        double finalT = integrator.integrate(equations, t0, y0, t, y);

        // Check final time
        assertEquals(t, finalT, 0.0);

        // Check final state
        double[] expectedY = {2.0, 4.0};
        assertArrayEquals(expectedY, y, 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatch() throws Exception {
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[2];

        integrator.integrate(equations, t0, y0, t, y);
    }

    private static class TestDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = 2 * y[0];
            yDot[1] = 2 * y[1];
        }
    }

    private static class MockIntegrator extends AbstractIntegrator {

        @Override
        public void integrate(ExpandableStatefulODE equations, double t) {
            double[] y0 = equations.getPrimaryState();
            double[] yDot = new double[y0.length];
            equations.computeDerivatives(t, y0, yDot);
            equations.setTime(t);
            equations.setPrimaryState(y0);
        }
    }
}