import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.stepper.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;
    private double[] y0 = new double[]{0.0, 0.0};
    private double t;
    private double[] y;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no-op
            }
        };
        integrator.setMaxEvaluations(10);
        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };
        equations.setTime(0.0);
        equations.setPrimaryState(y0);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                Assert.assertTrue(isLastStep);
            }
        });
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullEventHandler() {
        integrator.addEventHandler(null, 0.0, 0.0, 0, null);
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
                Assert.assertArrayEquals(y0, y, 0.0);
            }

            @Override
            public double getEventTime() {
                return 0.5;
            }

            @Override
            public void reinitializeBegin(AbstractStepInterpolator interpolator) {
                // no-op
            }
        }, 0.0, 0.0, 0, null);

        integrator.integrate(equations, 1.0);
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                Assert.assertArrayEquals(y0, y, 0.0);
            }

            @Override
            public double getEventTime() {
                return 0.5;
            }

            @Override
            public void reinitializeBegin(AbstractStepInterpolator interpolator) {
                // no-op
            }
        }, 0.0, 0.0, 0, null);
        integrator.clearEventHandlers();
        integrator.integrate(equations, 1.0);
    }

    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                Assert.assertArrayEquals(y0, y, 0.0);
            }

            @Override
            public double getEventTime() {
                return 0.5;
            }

            @Override
            public void reinitializeBegin(AbstractStepInterpolator interpolator) {
                // no-op
            }
        }, 0.0, 0.0, 0, null);
        List<EventHandler> handlers = new ArrayList<>(integrator.getEventHandlers());
        Assert.assertEquals(1, handlers.size());
        integrator.clearEventHandlers();
        handlers = integrator.getEventHandlers();
        Assert.assertTrue(handlers.isEmpty());
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("TestIntegrator", integrator.getName());
    }

    @Test
    public void testIntegrate() {
        integrator.integrate(equations, 1.0);
        y = equations.getPrimaryState();
        Assert.assertArrayEquals(new double[]{0.7071067811865476, -0.7071067811865476}, y, 1e-8);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateNullEquations() {
        integrator.integrate(null, 1.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithInvalidInitialState() {
        equations.setPrimaryState(new double[]{0.0, 2.0});
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithNegativeMaxEvaluations() {
        integrator.setMaxEvaluations(-1);
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidTargetTime() {
        integrator.integrate(equations, -0.1);
    }

    @Test
    public void testResetEvaluations() {
        integrator.integrate(equations, 1.0);
        Assert.assertEquals(1, integrator.getEvaluations());
        integrator.resetEvaluations();
        Assert.assertEquals(0, integrator.getEvaluations());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxEvaluationsReached() {
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 1.0);
        integrator.integrate(equations, 1.0);
    }

}