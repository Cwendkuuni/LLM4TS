import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) {
                // Do nothing
            }
        };

        double t = 0.0;
        double[] y = new double[2];
        double[] yDot = new double[2];
        try {
            integrator.computeDerivatives(t, y, yDot);
            assertEquals(1, integrator.getEvaluations());
        } catch (MaxCountExceededException e) {
            fail();
        }
    }

    @Test
    public void testAcceptStep() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) {
                // Do nothing
            }
        };

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
                // Do nothing
            }

            @Override
            public StepInterpolator copy() {
                // Do nothing
                return null;
            }
        };

        double[] y = new double[2];
        double[] yDot = new double[2];
        double tEnd = 1.0;
        try {
            double result = integrator.acceptStep(interpolator, y, yDot, tEnd);
            assertEquals(tEnd, result, 0.0);
        } catch (MathIllegalStateException e) {
            fail();
        }
    }

}