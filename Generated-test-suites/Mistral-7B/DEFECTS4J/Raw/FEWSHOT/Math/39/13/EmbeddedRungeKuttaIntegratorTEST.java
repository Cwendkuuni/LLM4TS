package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.Verifier;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ACCURACY = 1e-14;
    private static final double RELATIVE_TOLERANCE = 1e-6;
    private static final double ABSTOLERANCE = 1e-16;
    private static final double MAX_STEP = 10.0;
    private static final double MIN_STEP = 0.001;

    private EmbeddedRungeKuttaIntegrator integrator;
    private RungeKuttaStepInterpolator prototype;

    @Before
    public void setUp() throws Exception {
        prototype = new RungeKuttaStepInterpolator(RungeKuttaMethod.RK4, RK4Interpolator.RK4());
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, RK4_COEFFS, RK4_WEIGHTS, prototype, MIN_STEP, MAX_STEP, ABSTOLERANCE, RELATIVE_TOLERANCE) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y1.length; i++) {
                    error += Math.pow(y1[i] - y0[i], 2);
                }
                return Math.sqrt(error);
            }
        };
    }

    @Test
    public void testIntegrateSimple() throws Exception {
        final ExpandableStatefulODE equations = new SimpleODE();
        double[] y0 = equations.getCompleteState();
        double t = 1.0;
        integrator.integrate(equations, t);
        verifySolution(equations, t);
    }

    @Test
    public void testIntegrateComplex() throws Exception {
        final ExpandableStatefulODE equations = new ComplexODE();
        double[] y0 = equations.getCompleteState();
        double t = 2.0 * Math.PI;
        integrator.integrate(equations, t);
        verifySolution(equations, t);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateTooSmallStep() throws Exception {
        final ExpandableStatefulODE equations = new SimpleODE();
        double[] y0 = equations.getCompleteState();
        double t = 1.0;
        integrator.setMinStep(MIN_STEP * 0.5);
        integrator.integrate(equations, t);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testIntegrateTooLargeStep() throws Exception {
        final ExpandableStatefulODE equations = new SimpleODE();
        double[] y0 = equations.getCompleteState();
        double t = 1.0;
        integrator.setMaxStep(MAX_STEP * 2.0);
        integrator.integrate(equations, t);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateNullEquations() throws Exception {
        double[] y0 = new double[1];
        integrator.integrate(null, 1.0);
    }

    private void verifySolution(ExpandableStatefulODE equations, double t) {
        double[] y = equations.getCompleteState();
        double[] solution = new double[] {E.EulerNumber.E.doubleValue(), E.EulerNumber.E.doubleValue()};
        assertArrayEquals(solution, y, ACCURACY);
    }

}

//SimpleODE.java:
public class SimpleODE implements ExpandableStatefulODE {

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
    public int getOrder() {
        return 2;
    }

    @Override
    public double[] getCompleteState() {
        double[] y = new double[2];
        y[0] = 1.0;
        y[1] = 1.0;
        return y;
    }

    @Override
    public void setTime(double time) {
    }

    @Override
    public void setCompleteState(double[] state) {
    }

    @Override
    public void setTime(double time, double[] state) {
    }

    @Override
    public void setParameter(String name, double value) {
    }

    @Override
    public void setParameters(String[] names, double[] values) {
    }

    @Override
    public void setPrimaryMapper(StateMapper mapper) {
    }

    @Override
    public void setSecondaryMappers(StateMapper[] mappers) {
    }

    @Override
    public void setEquations(ExpandableStatefulODE equations) {
    }

    @Override
    public double[] derive(double t, double[] state, double[] stateDerivatives) {
        stateDerivatives[0] = state[1];
        stateDerivatives[1] = state[0];
        return stateDerivatives;
    }

}

//ComplexODE.java:
public class ComplexODE
        extends SimpleODE {

    @Override
    public double[] derive(double t, double[] state, double[] stateDerivatives) {
        stateDerivatives[0] = -state[1];
        stateDerivatives[1] = state[0];
        return stateDerivatives;
    }

}