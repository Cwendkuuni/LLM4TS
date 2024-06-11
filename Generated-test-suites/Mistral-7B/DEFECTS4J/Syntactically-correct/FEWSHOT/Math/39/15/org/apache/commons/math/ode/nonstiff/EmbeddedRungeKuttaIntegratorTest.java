package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.expensive.RungeKutta78Method;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.util.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ACCURACY = 1e-9;
    private static final double TOLERANCE = 1e-8;
    private static final double STEP_SIZE = 0.1;
    private static final double INITIAL_TIME = 0.0;
    private static final double END_TIME = 1.0;

    private ExpandableStatefulODE equations;
    private EmbeddedRungeKuttaIntegrator integrator;
    private RungeKuttaStepInterpolator interpolator;
    private double[] initialState;

    @Before
    public void setup() {
        equations = new RungeKutta78Method(new SimpleDifferentialEquations());
        equations.setTime(INITIAL_TIME);
        initialState = equations.getCompleteState();

        integrator = new EmbeddedRungeKutta4(
                "EmbeddedRungeKutta4", true,
                new double[] { 0.25, 0.25, 0.5 },
                new double[][] { { 0.0, 0.25, 0.25 }, { 0.5, 0.25, 0.0 } },
                new double[] { 0.25, 0.25 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(
                            EmbeddedRungeKuttaIntegrator integrator,
                            double[] state, double[] derivatives,
                            boolean forward,
                            ODEFunctionMapper mapper,
                            ODEFunctionMapper... mappers) {
                        // no-op
                    }

                    @Override
                    public void storeTime(double time) {
                        // no-op
                    }

                    @Override
                    public void shift() {
                        // no-op
                    }
                }, 0.0001, 0.01, new double[] { ACCURACY, ACCURACY },
                new double[] { TOLERANCE, TOLERANCE });

        interpolator = new RungeKuttaStepInterpolator(integrator);
    }

    @Test
    public void testIntegration() throws MathIllegalArgumentException, MaxCountExceededException {
        equations.setTime(INITIAL_TIME);

        integrateAndCheck(equations, END_TIME);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() throws MathIllegalArgumentException, MaxCountExceededException {
        integrator.setMinStep(-STEP_SIZE);
        integrateAndCheck(equations, END_TIME);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testZeroStepSize() throws MathIllegalArgumentException, MaxCountExceededException {
        integrator.setMinStep(0);
        integrateAndCheck(equations, END_TIME);
    }

    @Test
    public void testIntegrationWithStepHandler() throws MathIllegalArgumentException, MaxCountExceededException {
        StepHandler stepHandler = new StepHandler() {
            @Override
            public int handle(double time, double[] state, double[] derivatives) {
                assertEquals(initialState, state, 0.0);
                return 1;
            }
        };

        equations.setTime(INITIAL_TIME);
        integrateAndCheckWithStepHandler(equations, END_TIME, stepHandler);
    }

    private void integrateAndCheck(ExpandableStatefulODE equations, double endTime)
            throws MathIllegalArgumentException, MaxCountExceededException {
        integrator.integrate(equations, endTime);
        double time = equations.getTime();
        assertFalse("Last step was not the last one", isLastStep(equations));
        assertEquals(endTime, time, ACCURACY);
        assertArrayEquals(initialState, equations.getCompleteState(), ACCURACY);
    }

    private void integrateAndCheckWithStepHandler(ExpandableStatefulODE equations, double endTime, StepHandler stepHandler)
            throws MathIllegalArgumentException, MaxCountExceededException {
        integrator.integrate(equations, endTime, stepHandler);
        double time = equations.getTime();
        assertFalse("Last step was not the last one", isLastStep(equations));
        assertEquals(endTime, time, ACCURACY);
        assertArrayEquals(initialState, equations.getCompleteState(), ACCURACY);
    }

    private boolean isLastStep(ExpandableStatefulODE equations) {
        return integrator.isLastStep(equations);
    }

}

class SimpleDifferentialEquations implements ExpandableStatefulODE {

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public int getPrimaryDimension() {
        return 1;
    }

    @Override
    public int getSecondaryDimension() {
        return 0;
    }

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public void setTime(double time) {
        // no-op
    }

    @Override
    public double[] getCompleteState() {
        return new double[] { 0.0 };
    }

    @Override
    public void setCompleteState(double[] state) {
        // no-op
    }

    @Override
    public void setTime(double time, double[] state) {
        // no-op
    }

    @Override
    public void setCompleteState(double time, double[] state) {
        // no-op
    }

    @Override
    public void setPrimaryMapper(ODEFunctionMapper mapper) {
        // no-op
    }

    @Override
    public void setSecondaryMapper(int index, ODEFunctionMapper mapper) {
        // no-op
    }

    @Override
    public void setPrimaryMapper(ODEFunctionMapper mapper, ODEFunctionMapper... mappers) {
        // no-op
    }

    @Override
    public void setCompleteMapper(ODEFunctionMapper mapper, ODEFunctionMapper... mappers) {
        // no-op
    }

    @Override
    public void setEventHandler(ODEEventHandler handler) {
        // no-op
    }

    @Override
    public void setStepHandler(StepHandler stepHandler) {
        // no-op
    }

    @Override
    public void addStepHandler(StepHandler stepHandler) {
        // no-op
    }

    @Override
    public void removeStepHandler(StepHandler stepHandler) {
        // no-op
    }

    @Override
    public double value(double time, double[] state) {
        return 0;
    }

    @Override
    public void derivative(double time, double[] state, double[] derivatives) {
        derivatives[0] = 1;
    }

}