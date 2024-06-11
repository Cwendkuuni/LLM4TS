import static org.junit.Assert.*;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.util.Incrementor;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException, MathIllegalArgumentException {
                // empty implementation
            }
        };

        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.setEquations(equations);
        integrator.setMaxEvaluations(10);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                assertEquals(interpolator.getInterpolatedTime(), integrator.getCurrentStepStart(), 0.001);
                assertEquals(lastStep, integrator.isLastStep());
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddStepHandlerNull() {
        integrator.addStepHandler(null);
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                assertEquals(t, integrator.getCurrentStepStart(), 0.001);
                assertEquals(y, integrator.getPrimaryState(), 0.001);
            }

            @Override
            public void reset(double t, double[] y) {
                integrator.resetEvaluations();
            }

            @Override
            public double getEventTime() {
                return integrator.getCurrentStepStart() + 0.1;
            }
        }, 0.1, 0.001, 1, new BracketingNthOrderBrentSolver(0.001, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerNull() {
        integrator.addEventHandler(null, 0.1, 0.001, 1, new BracketingNthOrderBrentSolver(0.001, 5));
    }

    @Test
    public void testIntegrate() throws Exception {
        integrator.integrate(equations, 0.0, new double[]{1.0}, 2.0, new double[1]);
        assertTrue(integrator.getEvaluations() > 0);
        assertEquals(2.0, integrator.getCurrentStepStart(), 0.001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateNullEquations() throws Exception {
        integrator.integrate(null, 0.0, new double[1], 2.0, new double[1]);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateInvalidT() throws Exception {
        integrator.integrate(equations, Double.NEGATIVE_INFINITY, new double[1], 2.0, new double[1]);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateInvalidStateSize() throws Exception {
        ExpandableStatefulODE equations2 = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }
        };
        integrator.setEquations(equations2);
        integrator.integrate(equations2, 0.0, new double[]{1.0, 2.0}, 2.0, new double[1]);
    }

    @Test
    public void testIntegrateMaxEvaluations() throws Exception {
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[]{1.0}, 2.0, new double[1]);
        assertEquals(1, integrator.getEvaluations());
        assertTrue(integrator.isLastStep());
    }

    @Test
    public void testSetMaxEvaluationsNegative() throws Exception {
        integrator.setMaxEvaluations(-1);
        integrator.integrate(equations, 0.0, new double[]{1.0}, 2.0, new double[1]);
        assertTrue(integrator.isLastStep());
    }

    @Test
    public void testGetMaxEvaluations() throws Exception {
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() throws Exception {
        integrator.integrate(equations, 0.0, new double[]{1.0}, 2.0, new double[1]);
        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testResetEvaluations() throws Exception {
        integrator.integrate(equations, 0.0, new double[]{1.0}, 2.0, new double[1]);
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testComputeDerivatives() throws Exception {
        double[] y = new double[]{1.0};
        double[] yDot = new double[1];

        integrator.computeDerivatives(1.0, y, yDot);
        double expected = y[0] * 0.1;
        assertEquals(expected, yDot[0], 0.001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testComputeDerivativesMaxEvaluations() throws Exception {
        integrator.setMaxEvaluations(0);
        integrator.computeDerivatives(1.0, new double[]{1.0}, new double[1]);
        integrator.computeDerivatives(1.0, new double[]{1.0}, new double[1]);
    }

    @Test
    public void testAcceptStep() throws Exception {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getInterpolatedTime() {
                return 0.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[0];
            }

            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 1.0;
            }
        };

        integrator.acceptStep(interpolator, new double[1], new double[1], 1.0);

        ArgumentCaptor<StepHandler> stepHandlerArgumentCaptor = ArgumentCaptor.forClass(StepHandler.class);
        verify(integrator).addStepHandler(stepHandlerArgumentCaptor.capture());
        StepHandler stepHandler = stepHandlerArgumentCaptor.getValue();
        stepHandler.handleStep(interpolator, integrator.isLastStep());
    }

    @Test
    public void testAcceptStepStopIntegration() throws Exception {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getInterpolatedTime() {
                return 0.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[0];
            }

            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 1.0;
            }
        };

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                assertEquals(interpolator.getInterpolatedTime(), integrator.getCurrentStepStart(), 0.001);
                assertEquals(lastStep, integrator.isLastStep());
                if (lastStep) {
                    integrator.stopIntegration();
                }
            }
        });

        integrator.acceptStep(interpolator, new double[1], new double[1], 1.0);
        assertEquals(1.0, integrator.getCurrentStepStart(), 0.001);
        assertTrue(integrator.isLastStep());
        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testAcceptStepReset() throws Exception {
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getInterpolatedTime() {
                return 0.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[]{1.1};
            }

            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 1.0;
            }
        };

        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(AbstractStepInterpolator interpolator) {
                return true;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                integrator.resetEvaluations();
            }

            @Override
            public double getEventTime() {
                return integrator.getCurrentStepStart() + 0.1;
            }
        }, 0.1, 0.001, 1, new BracketingNthOrderBrentSolver(0.001, 5));

        integrator.acceptStep(interpolator, new double[1], new double[1], 1.0);
        assertEquals(1.0, integrator.getCurrentStepStart(), 0.001);
        assertFalse(integrator.isLastStep());
        assertEquals(1, integrator.getEvaluations());

        integrator.acceptStep(interpolator, new double[1], new double[1], 1.0);
        assertEquals(1.0, integrator.getCurrentStepStart(), 0.001);
        assertTrue(integrator.isLastStep());
        assertEquals(2, integrator.getEvaluations());

        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testSanityChecks() throws Exception {
        integrator.sanityChecks(equations, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecksTooSmall() throws Exception {
        integrator.sanityChecks(equations, 0.0);
    }

}