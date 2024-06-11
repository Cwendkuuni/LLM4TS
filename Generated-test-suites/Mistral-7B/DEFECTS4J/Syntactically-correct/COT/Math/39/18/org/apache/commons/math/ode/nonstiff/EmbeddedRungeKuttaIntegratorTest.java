package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;

    @Before
    public void setUp() {
        equations = new SimpleDifferentialEquations();
        integrator = new EmbeddedRungeKutta23Method(
                "Embedded Runge-Kutta 2-3 Method",
                false,
                new double[] {0.25, 0.4666666666666667, 0.25},
                new double[][] {{0.25, 0.0, 0.25}, {0, 0.25, 0.25}},
                new double[] {0.5, 0.5, 0.5},
                new RungeKuttaStepInterpolator(new RK4StepInterpolator()));
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(integrator);
        Assert.assertEquals("Embedded Runge-Kutta 2-3 Method", integrator.getName());
    }

    @Test
    public void testIntegrateBasic() throws MathIllegalStateException, MathIllegalArgumentException {
        double y0 = 1.0;
        double[] expectedY = {1.0, 1.5, 1.75, 1.9375};
        double expectedT = 3.0;

        integrator.integrate(equations, 0.0);
        double[] y = equations.getCompleteState();
        Assert.assertArrayEquals(y, new double[] {y0, y0 + equations.getDt()});

        integrator.integrate(equations, 1.0);
        y = equations.getCompleteState();
        Assert.assertArrayEquals(y, expectedY);
        Assert.assertEquals(expectedT, equations.getTime(), 1e-10);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWrongNumberOfVariables() throws MathIllegalStateException, MathIllegalArgumentException {
        equations.setDimension(2);
        integrator = new EmbeddedRungeKutta23Method(
                "Embedded Runge-Kutta 2-3 Method",
                false,
                new double[] {0.25, 0.4666666666666667, 0.25},
                new double[][] {{0.25, 0.0, 0.25}, {0, 0.25, 0.25}},
                new double[] {0.5, 0.5, 0.5},
                new RungeKuttaStepInterpolator(new RK4StepInterpolator()));
        integrator.integrate(equations, 0.0);
    }

    @Test
    public void testIntegrateStepSizeControl() throws MathIllegalStateException, MathIllegalArgumentException {
        integrator = new EmbeddedRungeKuttaFehlberg78Method(
                "Embedded Runge-Kutta 7-8 Method",
                false,
                new double[] {0.3, 0.3829787234, 0.5555555555555556, 0.8333333333333333, 0.0},
                new double[][] {
                        {0.0, 0.0, 0.0, 0.0, 0.0},
                        {0.3, 0.0, 0.0, 0.0, 0.0},
                        {0.3, 0.3, 0.0, 0.0, 0.0},
                        {0.6809016393442511, 0.061803364815646, 0.0, 0.0, 0.0},
                        {0.810496742556748, 0.356874836826266, 0.356874836826266, 0.0, 0.0},
                        {0.963203225806462, 0.6746169635529796, 0.6746169635529796, 0.33203225806462, 0.0},
                        {1.0, 0.9, 0.9, 0.9, 0.9}
                },
                new double[] {0.4, 0.0, 0.02, 0.02, 0.0},
                new RungeKuttaStepInterpolator(new RK78StepInterpolator()));

        double t = 1.0;
        double[] y0 = {1.0, 1.0};

        integrator.integrate(equations, t);
        double[] y1 = equations.getCompleteState();
        Assert.assertArrayEquals(y1, y0);

        double stepSize = 0.2;
        integrator.setMinStep(0.1);
        integrator.setMaxStep(1.0);
        integrator.setStepSize(stepSize);

        double tolerance = 1e-6;
        double error = integrator.estimateError(y1, y0, y1, stepSize);
        Assert.assertFalse(Precision.abs(error) < tolerance);

        stepSize *= 0.1;
        integrator.integrate(equations, t + stepSize);
        y1 = equations.getCompleteState();
        error = integrator.estimateError(y1, y0, y1, stepSize);
        Assert.assertTrue(Precision.abs(error) < tolerance);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWrongTime() throws MathIllegalStateException, MathIllegalArgumentException {
        integrator.integrate(equations, -1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWrongInitialConditions() throws MathIllegalStateException, MathIllegalArgumentException {
        double[] y0 = {1.0, 1.0};
        integrator.integrate(equations, 1.0, y0);
        y0[1] += 1.0;
        integrator.integrate(equations, 1.0, y0);
    }

}

class SimpleDifferentialEquations implements ExpandableStatefulODE {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public int getPrimaryDimension() {
        return getDimension();
    }

    @Override
    public int getSecondaryDimension() {
        return 0;
    }

    @Override
    public void setTime(double time) {
    }

    @Override
    public double getTime() {
        return 0.0;
    }

    @Override
    public void setCompleteState(double[] state) {
    }

    @Override
    public double[] getCompleteState() {
        return new double[2];
    }

    @Override
    public void setDt(double dt) {
    }

    @Override
    public double getDt() {
        return 0.1;
    }

    @Override
    public void setPrimaryMapper(FunctionMapper mapper) {
    }

    @Override
    public void setSecondaryMappers(FunctionMapper[] mappers) {
    }

    @Override
    public void computeDerivatives(double time, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
    }

    @Override
    public void reset() {
    }

}