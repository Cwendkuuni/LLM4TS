import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op integration
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        };

        double[] y0 = {1.0};
        double tTarget = 2.0;

        integrator.initIntegration(0.0, y0, tTarget);

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalCurrentTime() {
                return 1.0;
            }

            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }
        };

        double stepSize = 0.5;

        integrator.acceptStep(interpolator, y0, new double[y0.length], tTarget);

        assertEquals(1.5, integrator.getCurrentStepStart(), 0.0);
        assertEquals(-0.5, integrator.getCurrentSignedStepsize(), 0.0);
        assertArrayEquals(new double[] {2.0}, integrator.getState(), 0.0);

        integrator.integrate(equations, tTarget);

        assertEquals(2.0, integrator.getCurrentStepStart(), 0.0);
        assertEquals(0.0, integrator.getCurrentSignedStepsize(), 0.0);
        assertArrayEquals(new double[] {2.0}, integrator.getState(), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op integration
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
                yDot[1] = y[1] * 2;
            }
        };

        double[] y0 = {1.0, 1.0};
        double tTarget = 2.0;

        integrator.initIntegration(0.0, y0, tTarget);

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalCurrentTime() {
                return 1.0;
            }

            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }
        };

        integrator.acceptStep(interpolator, y0, new double[y0.length], tTarget);

        assertEquals(1.5, integrator.getCurrentStepStart(), 0.0);
        assertEquals(-0.5, integrator.getCurrentSignedStepsize(), 0.0);
        assertArrayEquals(new double[] {2.0, 2.0}, integrator.getState(), 0.0);

        integrator.integrate(equations, tTarget);

        assertEquals(2.0, integrator.getCurrentStepStart(), 0.0);
        assertEquals(0.0, integrator.getCurrentSignedStepsize(), 0.0);
        assertArrayEquals(new double[] {2.0, 2.0}, integrator.getState(), 0.0);

        // Test with a wrong dimension
        ExpandableStatefulODE wrongEquations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
                yDot[1] = y[1] * 2;
            }
        };

        integrator.setEquations(wrongEquations);

        integrator.integrate(equations, tTarget);

    }

}