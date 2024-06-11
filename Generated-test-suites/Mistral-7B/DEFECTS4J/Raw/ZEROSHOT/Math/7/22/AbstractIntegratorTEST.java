import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // no implementation required for testing
            }
        };

        // test default values
        assertNull(integrator.getName());
        assertTrue(integrator.getStepHandlers().isEmpty());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertEquals(0, integrator.getEventHandlers().size());
        assertFalse(integrator.isStateInitialized());
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());

        // test setters and getters
        integrator.setName("testIntegrator");
        assertEquals("testIntegrator", integrator.getName());
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, boolean isLastStep) {
                // no implementation required for testing
            }
        });
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
        integrator.isLastStep = true;
        assertTrue(integrator.isLastStep());
        integrator.resetOccurred = true;
        assertTrue(integrator.resetOccurred);
        integrator.addEventHandler(new AbstractStatefulEventHandler() {
            @Override
            public boolean isStateTransversinglyActive(double t, double[] y) {
                return false;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                // no implementation required for testing
            }
        }, 1.0, 0.001, 10);
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
        integrator.setEvaluations(10);
        assertEquals(10, integrator.getEvaluations());

        // test integration
        integrator.initIntegration(0.0, new double[0], 1.0);
        assertFalse(integrator.isLastStep());
        integrator.integrate(new AbstractStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        }, 1.0);
        assertTrue(integrator.isLastStep());

        // test accept step
        abstract class TestStepInterpolator extends AbstractStepInterpolator {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 1.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[0];
            }

            @Override
            public void setInterpolatedTime(double t) {
                // no implementation required for testing
            }
        }

        TestStepInterpolator interpolator = new TestStepInterpolator();
        integrator.acceptStep(interpolator, new double[0], new double[0], 1.0);
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        integrator.acceptStep(interpolator, new double[0], new double[0], 1.0);
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        integrator.acceptStep(interpolator, new double[0], new double[0], 1.0);
        assertTrue(integrator.isLastStep());
        integrator.acceptStep(interpolator, new double[0], new double[0], 1.0);
        assertTrue(integrator.isLastStep());
        integrator.acceptStep(interpolator, new double[0], new double[0], 1.0);
        assertTrue(integrator.isLastStep());
        integrator.acceptStep(interpolator, new double[0], new double[0], 1.0);
        assertTrue(integrator.isLastStep());

        // test acceptEvent
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.addEventHandler(new AbstractStatefulEventHandler() {
            @Override
            public boolean isStateTransversinglyActive(double t, double[] y) {
                return t <= 0.5;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                integrator.resetOccurred = true;
            }
        }, 0.5, 0.001, 10);
        integrator.integrate(new AbstractStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        }, 1.0);
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.resetOccurred);
        integrator.reset();
        integrator.integrate(new AbstractStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        }, 1.0);
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);

        // test sanity checks
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // no implementation required for testing
            }
        };
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.setEquations(new AbstractStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        });
        try {
            integrator.sanityChecks(integrator.expandable, 0.5);
            fail();
        } catch (DimensionMismatchException e) {
            // expected
        }
        integrator.setEquations(new AbstractStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        });
        try {
            integrator.sanityChecks(integrator.expandable, 1.0);
            fail();
        } catch (NumberIsTooSmallException e) {
            // expected
        }

    }

}