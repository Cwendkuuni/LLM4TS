import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.EventHandler;
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
                // implementation of the integrate method goes here
            }
        };

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // implementation of the StepHandler goes here
            }
        });

        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handleState(double t, double[] y) {
                // implementation of the EventHandler goes here
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // implementation of the init method goes here
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                // implementation of the stepAccepted method goes here
            }

            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                // implementation of the evaluateStep method goes here
                return false;
            }

            @Override
            public double getEventTime() {
                // implementation of the getEventTime method goes here
                return 0.0;
            }
        }, 1.0, 1.0e-6, 10);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // implementation of the computeDerivatives method goes here
            }
        };

        double[] initialState = new double[]{1.0};
        double targetTime = 2.0;

        try {
            integrator.initIntegration(0.0, initialState, targetTime);
            integrator.setEquations(equations);
            double result = integrator.integrate(equations, targetTime);
            assertArrayEquals(new double[]{2.0}, initialState);
            assertEquals(targetTime, result, 1e-6);
        } catch (DimensionMismatchException | NumberIsTooSmallException |
                MaxCountExceededException | NoBracketingException e) {
            // expected exceptions go here
        }
    }

}