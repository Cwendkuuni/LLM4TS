package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.SamplePointsGenerator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the {@link EmbeddedRungeKuttaIntegrator} abstract class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double EPSILON = 1.0e-12;
    private static final double START_TIME = 0.0;
    private static final double END_TIME = 1.0;
    private static final double[] INITIAL_STATE = { 1.0, 2.0 };
    private ExpandableStatefulODE equations;
    private EmbeddedRungeKuttaIntegrator integrator;
    private RungeKuttaMethod method;
    private RungeKuttaStepInterpolator stepInterpolator;

    @Before
    public void setUp() {
        method = new RungeKuttaFehlberg78Method();
        stepInterpolator = new RungeKuttaStepInterpolator(method);
        equations = new ExpandableStatefulODE() {

            @Override
            public int getNumberOfEquations() {
                return INITIAL_STATE.length;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                for (int i = 0; i < state.length; ++i) {
                    derivatives[i] = state[i] * 2.0;
                }
            }

            @Override
            public double getTime() {
                return equations.getState()[0];
            }

            @Override
            public void setTime(double time) {
                equations.setState(new double[] { time });
            }

            @Override
            public double[] getCompleteState() {
                return equations.getState();
            }

            @Override
            public void setCompleteState(double[] state) {
                equations.setState(state);
            }

            @Override
            public SamplePointsGenerator getSamplePointsGenerator() {
                return null;
            }

        };
    }

    /**
     * Tests the constructor that takes the minimal and maximal step as arguments.
     */
    @Test
    public void testConstructorWithMinMaxStep() {
        integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta Fehlberg 78 method", false,
                                                    method.getC(), method.getA(),
                                                    method.getB(), stepInterpolator,
                                                    0.001, 0.1, null, null) {

            @Override
            public int getOrder() {
                return method.getOrder();
            }

            @Override
            public double estimateError(double[][] yDotK,
                                         double[] y0, double[] y1,
                                         double h) {
                // A simple error estimation based on the maximum difference
                // between the state at the start and the end of the step.
                double maxDiff = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    maxDiff = Math.max(maxDiff, FastMath.abs(y1[i] - y0[i]));
                }
                return maxDiff;
            }

            @Override
            public void initIntegration(double startTime, double[] initialState, double endTime) {
                // Empty implementation
            }

            @Override
            public void resetInternalState() {
                // Empty implementation
            }

        };
        assertEquals("Runge-Kutta Fehlberg 78 method", integrator.getName());
        assertEquals(false, integrator.isFSAL());
        assertArrayEquals(method.getC(), integrator.getC(), EPSILON);
        assertArrayEquals(method.getA(), integrator.getA(), EPSILON);
        assertArrayEquals(method.getB(), integrator.getB(), EPSILON);
        assertNotNull(integrator.getPrototype());
        assertEquals(-1.0 / method.getOrder(), integrator.getExp(), EPSILON);
        assertEquals(0.9, integrator.getSafety(), EPSILON);
        assertEquals(0.2, integrator.getMinReduction(), EPSILON);
        assertEquals(10.0, integrator.getMaxGrowth(), EPSILON);
    }

    /**
     * Tests the constructor that takes the minimal and maximal step as arrays.
     */
    @Test
    public void testConstructorWithMinMaxStepArray() {
        integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta Fehlberg 78 method", false,
                                                    method.getC(), method.getA(),
                                                    method.getB(), stepInterpolator,
                                                    0.001, new double[] { 0.1 },
                                                    null, null) {

            @Override
            public int getOrder() {
                return method.getOrder();
            }

            @Override
            public double estimateError(double[][] yDotK,
                                         double[] y0, double[] y1,
                                         double h) {
                // A simple error estimation based on the maximum difference
                // between the state at the start and the end of the step.
                double maxDiff = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    maxDiff = Math.max(maxDiff, FastMath.abs(y1[i] - y0[i]));
                }
                return maxDiff;
            }

            @Override
            public void initIntegration(double startTime, double[] initialState, double endTime) {
                // Empty implementation
            }

            @Override
            public void resetInternalState() {
                // Empty implementation
            }

        };
        assertEquals("Runge-Kutta Fehlberg 78 method", integrator.getName());
        assertEquals(false, integrator.isFSAL());
        assertArrayEquals(method.getC(), integrator.getC(), EPSILON);
        assertArrayEquals(method.getA(), integrator.getA(), EPSILON);
        assertArrayEquals(method.getB(), integrator.getB(), EPSILON);
        assertNotNull(integrator.getPrototype());
        assertEquals(-1.0 / method.getOrder(), integrator.getExp(), EPSILON);
        assertEquals(0.9, integrator.getSafety(), EPSILON);
        assertEquals(0.2, integrator.getMinReduction(), EPSILON);
        assertEquals(10.0, integrator.getMaxGrowth(), EPSILON);
    }

    /**
     * Tests the constructor that takes the minimal and maximal step,
     * the absolute and relative tolerances, and the error estimation method.
     */
    @Test
    public void testConstructorWithMinMaxStepTolerancesAndEstimationMethod() {
        integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta Fehlberg 78 method", false,
                                                    method.getC(), method.getA(),
                                                    method.getB(), stepInterpolator,
                                                    0.001, 0.1, new double[] { 1.0e-6, 1.0e-6 },
                                                    this) {

            @Override
            public int getOrder() {
                return method.getOrder();
            }

            @Override
            public double estimateError(double[][] yDotK,
                                         double[] y0, double[] y1,
                                         double h) {
                // A simple error estimation based on the maximum difference
                // between the state at the start and the end of the step.
                double maxDiff = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    maxDiff = Math.max(maxDiff, FastMath.abs(y1[i] - y0[i]));
                }
                return maxDiff;
            }

            @Override
            public void initIntegration(double startTime, double[] initialState, double endTime) {
                // Empty implementation
            }

            @Override
            public void resetInternalState() {
                // Empty implementation
            }

        };
        assertEquals("Runge-Kutta Fehlberg 78 method", integrator.getName());
        assertEquals(false, integrator.isFSAL());
        assertArrayEquals(method.getC(), integrator.getC(), EPSILON);
        assertArrayEquals(method.getA(), integrator.getA(), EPSILON);
        assertArrayEquals(method.getB(), integrator.getB(), EPSILON);
        assertNotNull(integrator.getPrototype());
        assertEquals(-1.0 / method.getOrder(), integrator.getExp(), EPSILON);
        assertEquals(0.9, integrator.getSafety(), EPSILON);
        assertEquals(0.2, integrator.getMinReduction(), EPSILON);
        assertEquals(10.0, integrator.getMaxGrowth(), EPSILON);
        assertEquals(1.0e-6, integrator.getScalAbsoluteTolerance(), EPSILON);
        assertEquals(1.0e-6, integrator.getScalRelativeTolerance(), EPSILON);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}
     * method.
     */
    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        integrator.initIntegration(START_TIME, INITIAL_STATE, END_TIME);
        integrator.integrate(equations, END_TIME);
        double[] expectedState = new double[] { 1.0 + 2.0 * (END_TIME - START_TIME), 2.0 + 4.0 * (END_TIME - START_TIME) };
        assertArrayEquals(expectedState, equations.getCompleteState(), EPSILON);
        assertTrue(equations.isStateSet());
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}
     * method with a {@link MathIllegalArgumentException}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidEndTime() throws MathIllegalStateException, MathIllegalArgumentException {
        integrator.initIntegration(START_TIME, INITIAL_STATE, END_TIME);
        integrator.integrate(equations, -END_TIME);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}
     * method with a {@link MathIllegalStateException}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithInvalidInitialState() throws MathIllegalStateException, MathIllegalArgumentException {
        integrator.initIntegration(START_TIME, null, END_TIME);
        integrator.integrate(equations, END_TIME);
    }

}