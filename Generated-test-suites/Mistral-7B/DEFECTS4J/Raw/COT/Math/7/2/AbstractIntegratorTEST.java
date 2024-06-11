import org.apache.commons.math3.ode.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator_Constructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        Assert.assertEquals("TestIntegrator", integrator.getName());
        Assert.assertNotNull(integrator.getStepHandlers());
        Assert.assertEquals(0, integrator.getStepHandlers().size());
        Assert.assertFalse(integrator.isLastStep());
        Assert.assertFalse(integrator.resetOccurred);
        Assert.assertNull(integrator.getEventsStates());
        Assert.assertFalse(integrator.isStateInitialized());
        Assert.assertNull(integrator.expandable);
    }

    @Test
    public void testAbstractIntegrator_AddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(FirstOrderIntegrator integrator, boolean lastStep) {
                Assert.assertEquals("TestIntegrator", integrator.getName());
                Assert.assertTrue(lastStep);
            }
        };
        integrator.addStepHandler(stepHandler);
        Assert.assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractIntegrator_NegativeMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.setMaxEvaluations(-1);
    }

    @Test
    public void testAbstractIntegrator_GetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.setMaxEvaluations(10);
        Assert.assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testAbstractIntegrator_Integrate_DimensionMismatchException() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE(1);
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        try {
            integrator.integrate(equations, 0, new double[2], 1, new double[2]);
            Assert.fail();
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testAbstractIntegrator_Integrate_MaxCountExceededException() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE(1);
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.setMaxEvaluations(0);
        try {
            integrator.integrate(equations, 0, new double[]{1}, 1, new double[]{1});
            Assert.fail();
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testAbstractIntegrator_Integrate_NoBracketingException() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODEWithEvent(1) {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                return new double[]{y[0] + 1};
            }
        };
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        try {
            integrator.integrate(equations, 0, new double[]{1}, 1, new double[]{1});
            Assert.fail();
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test
    public void testAbstractIntegrator_Integrate() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE(1);
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        double[] initialState = new double[]{1};
        double targetTime = 2;
        double[] finalState = new double[]{2};
        integrator.integrate(equations, 0, initialState, targetTime, finalState);
        Assert.assertArrayEquals(finalState, initialState);
        Assert.assertEquals(targetTime, integrator.getCurrentStepStart());
    }

    @Test
    public void testAbstractIntegrator_ComputeDerivatives() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE(1);
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.setEquations(equations);
        double t = 0;
        double[] y = new double[]{1};
        double[] yDot = new double[1];
        integrator.computeDerivatives(t, y, yDot);
        Assert.assertEquals(1, yDot[0], 1e-10);
    }

    @Test
    public void testAbstractIntegrator_AcceptStep_NoEvent() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE(1);
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.setEquations(equations);
        AbstractStepInterpolator interpolator = new MockStepInterpolator();
        double[] initialState = new double[]{1};
        double[] finalState = new double[]{2};
        integrator.integrate(equations, 0, initialState, 1, finalState);
        integrator.setStateInitialized(false);
        double tEnd = 1.5;
        integrator.acceptStep(interpolator, initialState, new double[1], tEnd);
        Assert.assertEquals(tEnd, integrator.getCurrentStepStart());
        Assert.assertFalse(integrator.isLastStep());
        Assert.assertFalse(integrator.resetOccurred);
    }

    @Test
    public void testAbstractIntegrator_AcceptStep_Event() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODEWithEvent(1) {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                return new double[]{y[0] + 1};
            }
        };
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.setEquations(equations);
        AbstractStepInterpolator interpolator = new MockStepInterpolator();
        double[] initialState = new double[]{1};
        double[] finalState = new double[]{2};
        integrator.integrate(equations, 0, initialState, 1, finalState);
        integrator.setStateInitialized(false);
        double tEnd = 1.5;
        integrator.acceptStep(interpolator, initialState, new double[1], tEnd);
        Assert.assertEquals(1, integrator.getEvaluations());
        Assert.assertTrue(integrator.isLastStep());
        Assert.assertTrue(integrator.resetOccurred);
    }
}

class MockExpandableStatefulODE implements ExpandableStatefulODE {

    private final int dimension;

    public MockExpandableStatefulODE(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public int getStateDimension() {
        return dimension;
    }

    @Override
    public int getControlDimension() {
        return 0;
    }

    @Override
    public double getTime() {
        return 0;
    }

    @Override
    public void setTime(double time) {
    }

    @Override
    public double[] getPrimaryState() {
        return new double[0];
    }

    @Override
    public void setPrimaryState(double[] state) {
    }

    @Override
    public double[] getPrimaryStateDerivative() {
        return new double[0];
    }

    @Override
    public void setPrimaryStateDerivative(double[] derivatives) {
    }

    @Override
    public void setTime(double time, double[] state, double[] derivatives) {
    }

    @Test
    public void testMockExpandableStatefulODE_ComputeDerivatives() {
        MockExpandableStatefulODE ode = new MockExpandableStatefulODE(1);
        double[] y = new double[]{1};
        double[] yDot = new double[1];
        ode.computeDerivatives(0, y, yDot);
        Assert.assertEquals(1, yDot[0], 1e-10);
    }
}

class MockExpandableStatefulODEWithEvent implements ExpandableStatefulODE {

    private final int dimension;
    private final double eventTime;

    public MockExpandableStatefulODEWithEvent(int dimension) {
        this.dimension = dimension;
        this.eventTime = 0.5;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public int getStateDimension() {
        return dimension;
    }

    @Override
    public int getControlDimension() {
        return 0;
    }

    @Override
    public double getTime() {
        return 0;
    }

    @Override
    public void setTime(double time) {
    }

    @Override
    public double[] getPrimaryState() {
        return new double[0];
    }

    @Override
    public void setPrimaryState(double[] state) {
    }

    @Override
    public double[] getPrimaryStateDerivative() {
        return new double[0];
    }

    @Override
    public void setPrimaryStateDerivative(double[] derivatives) {
    }

    @Override
    public void setTime(double time, double[] state, double[] derivatives) {
    }

    @Override
    public double[] computeDerivatives(double time, double[] state) {
        if (time == eventTime) {
            return new double[]{Double.NaN};
        }
        return new double[]{state[0] + 1};
    }
}

class MockStepInterpolator implements AbstractStepInterpolator {

    @Override
    public void setGlobalPreviousTime(double previousTime) {
    }

    @Override
    public void setGlobalCurrentTime(double currentTime) {
    }

    @Override
    public double getGlobalPreviousTime() {
        return 0;
    }

    @Override
    public double getGlobalCurrentTime() {
        return 0;
    }

    @Override
    public void setInterpolatedTime(double time) {
    }

    @Override
    public double[] getInterpolatedState() {
        return new double[0];
    }

    @Override
    public double getStepSize() {
        return 1;
    }

    @Override
    public void setStepSize(double stepSize) {
    }

    @Override
    public void setIntegrator(FirstOrderIntegrator integrator) {
    }

    @Override
    public double[] interpolate(double time, int interpolationOrder) {
        return new double[]{0};
    }
}