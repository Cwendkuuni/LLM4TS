import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.test.TestEquations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class AbstractIntegratorTest extends AbstractODETest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                    MaxCountExceededException, NoBracketingException {
                // Override integration method to avoid actual integration
            }
        };
        equations = new TestEquations();
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(integrator);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new MockStepHandler());
        Assert.assertFalse(integrator.getStepHandlers().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullStepHandler() {
        integrator.addStepHandler(null);
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new MockStepHandler());
        integrator.clearStepHandlers();
        Assert.assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new MockEventHandler(), 1.0, 1.0, 10);
        Assert.assertFalse(integrator.getEventHandlers().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullEventHandler() {
        integrator.addEventHandler(null, 1.0, 1.0, 10);
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(new MockEventHandler(), 1.0, 1.0, 10);
        integrator.clearEventHandlers();
        Assert.assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        integrator.setCurrentSignedStepsize(1.0);
        Assert.assertEquals(1.0, integrator.getCurrentSignedStepsize(), 0.0);
    }

    @Test
    public void testInitIntegration() {
        integrator.initIntegration(0.0, new double[0], 1.0);
    }

    @Test
    public void testSetEquations() {
        integrator.setEquations(equations);
        Assert.assertEquals(equations, integrator.getEquations());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullEquations() {
        integrator.setEquations(null);
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        Assert.assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        Assert.assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        integrator.setMaxEvaluations(10);
        integrator.getIntegrator().evaluate(equations, 0.0, new double[0], 1.0, new double[0]);
        Assert.assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testIntegrateWithNoEventHandlers() {
        integrator.integrate(equations, 0.0, new double[0], 1.0);
        // No exception is thrown, so the test is passed
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIntegrateWithStepHandlers() {
        integrator.addStepHandler(new MockStepHandler());
        integrator.integrate(equations, 0.0, new double[0], 1.0);
    }

    @Test
    public void testAcceptStep() {
        integrator.addStepHandler(new MockStepHandler());
        integrator.addEventHandler(new MockEventHandler(), 0.5, 0.1, 10);
        double[] y = new double[equations.getDimension()];
        double[] yDot = new double[equations.getDimension()];

        AbstractStepInterpolator interpolator = new MockStepInterpolator();
        integrator.acceptStep(interpolator, y, yDot, 0.5);
        Assert.assertEquals(0.5, interpolator.getGlobalCurrentTime(), 0.0);
        Assert.assertArrayEquals(y, interpolator.getInterpolatedState(), 0.0);
    }

    @Test
    public void testAcceptStepWithEvent() {
        integrator.addStepHandler(new MockStepHandler());
        integrator.addEventHandler(new MockEventHandler(), 0.5, 0.1, 10);
        integrator.addEventHandler(new MockEventHandler(), 0.6, 0.1, 10);
        double[] y = new double[equations.getDimension()];
        double[] yDot = new double[equations.getDimension()];

        AbstractStepInterpolator interpolator = new MockStepInterpolator();
        integrator.acceptStep(interpolator, y, yDot, 0.5);
        Assert.assertEquals(0.5, interpolator.getGlobalCurrentTime(), 0.0);
        Assert.assertArrayEquals(y, interpolator.getInterpolatedState(), 0.0);

        // The second event should occur during the step
        integrator.acceptStep(interpolator, y, yDot, 0.1);
        Assert.assertEquals(0.6, interpolator.getGlobalCurrentTime(), 0.0);
        Assert.assertArrayEquals(y, interpolator.getInterpolatedState(), 0.0);
    }

    @Test
    public void testAcceptStepWithEventReset() {
        integrator.addStepHandler(new MockStepHandler());
        integrator.addEventHandler(new MockEventHandler(), 0.5, 0.1, 10);
        integrator.addEventHandler(new MockEventHandler() {
            @Override
            public void stepAccepted(double t, double[] y) {
                super.stepAccepted(t, y);
                y[0] = 2.0;
            }
        }, 0.6, 0.1, 10);
        double[] y = new double[equations.getDimension()];
        double[] yDot = new double[equations.getDimension()];

        AbstractStepInterpolator interpolator = new MockStepInterpolator();
        integrator.acceptStep(interpolator, y, yDot, 0.5);
        Assert.assertEquals(0.5, interpolator.getGlobalCurrentTime(), 0.0);
        Assert.assertArrayEquals(new double[]{0.0}, interpolator.getInterpolatedState(), 0.0);

        integrator.acceptStep(interpolator, y, yDot, 0.1);
        Assert.assertEquals(0.6, interpolator.getGlobalCurrentTime(), 0.0);
        Assert.assertArrayEquals(new double[]{2.0}, interpolator.getInterpolatedState(), 0.0);
    }
}