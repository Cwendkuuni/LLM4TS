package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.ts.StepHandler;
import org.apache.commons.math3.ode.ts.StepInterpolator;
import org.apache.commons.math3.ode.ts.StepSizeEstimator;
import org.apache.commons.math3.ode.ts.WeightedStepSizeRule;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta45Integrator("Embedded Runge-Kutta Fehlberg 4-5", true, new double[]{0.25, 0.25, 0.25, 0.5, 0.875, 0.45}, new double[][]{
                {0.25, 0}, {0}, {0.25, 0.25}, {0.25, 0.25}, {0.5, 0.25}, {0.875, 0.125}, {1, 0.0625},
                {0.45, 0.1875}, {0.45, 0.21875}, {0.45, 0.16875}, {0.45, 0.1296875}, {0.45, 0.084765625}, {0.45, 0.05551125}, {0.45, 0.03125}});

        ExpandableStatefulODE equations = new MyODE();
        double[] initState = {0.0, 0.0};
        integrator.integrate(equations, 0.0, initState);

        double expectedState[] = {1.0, 1.0};
        assertArrayEquals(expectedState, equations.getCompleteState(), 0.0001);

        // Test integration with step size control
        integrator.setMinStep(0.1);
        integrator.setMaxStep(10.0);
        integrator.setAbsoluteTolerance(1e-9);
        integrator.setRelativeTolerance(1e-9);

        integrator.integrate(equations, 0.0, initState, 1.0);

        assertEquals(1.0, equations.getTime(), 1e-9);
        assertArrayEquals(expectedState, equations.getCompleteState(), 1e-9);

        // Test integration with step size control and events
        List<StepHandler> eventHandlers = new ArrayList<>();
        eventHandlers.add(new MyStepHandler());
        integrator.setStepHandlers(eventHandlers);

        integrator.integrate(equations, 0.0, initState, 1.1);

        // Test integration with different tolerance
        integrator.setAbsoluteTolerance(1e-10);
        integrator.setRelativeTolerance(1e-10);

        integrator.integrate(equations, 0.0, initState, 1.2);

        // Test integration with different step size rule
        StepSizeEstimator stepSizeRule = new WeightedStepSizeRule(integrator, 0.1, 1.0);
        integrator.setStepSizeRule(stepSizeRule);
        integrator.integrate(equations, 0.0, initState, 1.5);

        // Test integration with different integration direction
        integrator.integrate(equations, 0.0, initState, -1.0);

        resetIntegrator(integrator);

        // Test integration with different initial state
        double initialState[] = {2.0, 2.0};
        integrator.integrate(equations, 0.0, initialState);

        expectedState = new double[]{3.0, 3.0};
        assertArrayEquals(expectedState, equations.getCompleteState(), 0.0001);

        resetIntegrator(integrator);

        // Test integration with initial state out of bounds
        initialState = new double[]{-1.0, -1.0};
        try {
            integrator.integrate(equations, 0.0, initialState);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        resetIntegrator(integrator);

        // Test integration with equations out of bounds
        try {
            integrator.integrate(null, 0.0, initState);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        resetIntegrator(integrator);

        // Test integration with wrong number of variables
        ExpandableStatefulODE wrongNumberOfVariablesEquations = new MyODEWithWrongNumberOfVariables();
        try {
            integrator.integrate(wrongNumberOfVariablesEquations, 0.0, initState);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        resetIntegrator(integrator);

        // Test integration with wrong number of derivatives
        MyODEWithWrongNumberOfDerivatives equations = new MyODEWithWrongNumberOfDerivatives();
        try {
            integrator.integrate(equations, 0.0, initState);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    private void resetIntegrator(EmbeddedRungeKuttaIntegrator integrator) {
        integrator.reset();
    }

    private static class MyODE implements ExpandableStatefulODE {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public int getPrimaryDimension() {
            return 2;
        }

        @Override
        public int getSecondaryDimension() {
            return 0;
        }

        @Override
        public void initialize(double[] y, double t) {
            y[0] = 0.0;
            y[1] = 0.0;
        }

        @Override
        public void computePrimaryDerivatives(double t, double[] y, double[] yDot) {
            double x = y[0];
            double yValue = y[1];
            yDot[0] = x;
            yDot[1] = yValue + Math.sin(x) * 0.1;
        }

        @Override
        public void computeSecondaryDerivatives(double t, double[] y, double[] yDot, int[] secondary) {
            // No secondary derivatives
        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public void setTime(double time) {
            // No-op
        }

        @Override
        public double[] getCompleteState() {
            return new double[]{0.0, 0.0};
        }

        @Override
        public void setCompleteState(double[] newState) {
            // No-op
        }

        @Override
        public void setEquations(ExpandableStatefulODE equations) {
            // No-op
        }

        @Override
        public void setStepHandlers(List<StepHandler> handlers) {
            // No-op
        }

        @Override
        public void setStepSizeRule(StepSizeEstimator rule) {
            // No-op
        }

        @Override
        public void setStepInterpolator(StepInterpolator interpolator) {
            // No-op
        }
    }

    private static class MyODEWithWrongNumberOfVariables implements ExpandableStatefulODE {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public int getPrimaryDimension() {
            return 2;
        }

        @Override
        public int getSecondaryDimension() {
            return 0;
        }

        @Override
        public void initialize(double[] y, double t) {
            // No-op
        }

        @Override
        public void computePrimaryDerivatives(double t, double[] y, double[] yDot) {
            // No-op
        }

        @Override
        public void computeSecondaryDerivatives(double t, double[] y, double[] yDot, int[] secondary) {
            // No-op
        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public void setTime(double time) {
            // No-op
        }

        @Override
        public double[] getCompleteState() {
            return new double[]{0.0};
        }

        @Override
        public void setCompleteState(double[] newState) {
            // No-op
        }

        @Override
        public void setEquations(ExpandableStatefulODE equations) {
            // No-op
        }

        @Override
        public void setStepHandlers(List<StepHandler> handlers) {
            // No-op
        }

        @Override
        public void setStepSizeRule(StepSizeEstimator rule) {
            // No-op
        }

        @Override
        public void setStepInterpolator(StepInterpolator interpolator) {
            // No-op
        }
    }

    private static class MyODEWithWrongNumberOfDerivatives implements ExpandableStatefulODE {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public int getPrimaryDimension() {
            return 2;
        }

        @Override
        public int getSecondaryDimension() {
            return 1;
        }

        @Override
        public void initialize(double[] y, double t) {
            // No-op
        }

        @Override
        public void computePrimaryDerivatives(double t, double[] y, double[] yDot) {
            // No-op
        }

        @Override
        public void computeSecondaryDerivatives(double t, double[] y, double[] yDot, int[] secondary) {
            yDot[secondary[0]] = 1.0;
        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public void setTime(double time) {
            // No-op
        }

        @Override
        public double[] getCompleteState() {
            return new double[]{0.0, 0.0};
        }

        @Override
        public void setCompleteState(double[] newState) {
            // No-op
        }

        @Override
        public void setEquations(ExpandableStatefulODE equations) {
            // No-op
        }

        @Override
        public void setStepHandlers(List<StepHandler> handlers) {
            // No-op
        }

        @Override
        public void setStepSizeRule(StepSizeEstimator rule) {
            // No-op
        }

        @Override
        public void setStepInterpolator(StepInterpolator interpolator) {
            // No-op
        }
    }

    private static class MyStepHandler implements StepHandler {

        @Override
        public int getId() {
            return 1;
        }

        @Override
        public void handleStep(int type, double[] y, double t, double[] yDot, int[] secondary, double[] event) {
            if (type == 1) {
                event[0] = Precision.round(y[0], 10);
            }
        }

        @Override
        public void handleEvent(int type, double[] y, double t, double[] yDot, int[] secondary, double[] event) {
            if (type == 1) {
                System.out.println("Event: " + event[0]);
            }
        }

        @Override
        public boolean isAcceptable(double[] y, double t, double[] yDot, int[] secondary) {
            return true;
        }

        @Override
        public void reset() {
            // No-op
        }
    }
}