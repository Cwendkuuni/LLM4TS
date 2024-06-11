import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.MaxCountExceededException;
import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.AbstractStepInterpolator;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private static final double TOLERANCE = 1e-10;

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegratorStub();
    }

    @Test
    public void testGetCurrentStepStart() {
        double stepStart = integrator.getCurrentStepStart();
        assertEquals(Double.NaN, stepStart, TOLERANCE);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        double stepSize = integrator.getCurrentSignedStepsize();
        assertEquals(Double.NaN, stepSize, TOLERANCE);
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 500;
        integrator.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_DimensionMismatchException() {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double t = 5.0;
        double[] y = {1.0};
        integrator.integrate(new FirstOrderDifferentialEquationsStub(), t0, y0, t, y);
    }

    @Test
    public void testIntegrate() {
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 5.0;
        double[] y = new double[y0.length];
        integrator.integrate(new FirstOrderDifferentialEquationsStub(), t0, y0, t, y);
        assertEquals(30.0, y[0], TOLERANCE);
    }

    // Stub classes

    private static class AbstractIntegratorStub extends AbstractIntegrator {
        public String getName() {
            return null;
        }
        public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
        }
    }

    private static class FirstOrderDifferentialEquationsStub implements FirstOrderDifferentialEquations {
        public int getDimension() {
            return 1;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {

        }
    }

}