import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.MaxCountExceededException;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.StepHandler;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {
    private TestIntegrator integrator;
    private boolean computeDerivativesCalled;

    @Before
    public void setUp() {
        integrator = new TestIntegrator();
        computeDerivativesCalled = false;
    }

    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        TestEquations equations = new TestEquations();
        double t = 0;
        double[] y = new double[] {1};
        double tEnd = 1;
        double[] yEnd = new double[1];

        integrator.integrate(equations, t, y, tEnd, yEnd);

        assertEquals(1.0, yEnd[0], 1e-10);
        assertTrue(computeDerivativesCalled);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatch() throws MathIllegalStateException, MathIllegalArgumentException {
        TestEquations equations = new TestEquations();
        double t = 0;
        double[] y = new double[] {1};
        double tEnd = 1;
        double[] yEnd = new double[2];

        integrator.integrate(equations, t, y, tEnd, yEnd);
    }

    private class TestEquations implements FirstOrderDifferentialEquations {

        public int getDimension() {
            return 1;
        }

        public void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException, DimensionMismatchException {
            computeDerivativesCalled = true;
            yDot[0] = 1;
        }
    }

    private class TestIntegrator extends AbstractIntegrator {

        public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
            double[] y = equations.getPrimaryState();
            y[0] = 1;
        }

    }
}