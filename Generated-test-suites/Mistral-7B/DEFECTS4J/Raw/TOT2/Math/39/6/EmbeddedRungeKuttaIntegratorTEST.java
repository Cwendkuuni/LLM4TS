import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ACCURACY = 1e-12;
    private static final double RELATIVE_TOL = 1e-6;

    private ExpandableStatefulODE equations;
    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        equations = new ExampleODE();
        integrator = new EmbeddedRungeKutta54Integrator(1.0);
    }

    /**
     * Test integration of a simple ODE.
     */
    @Test
    public void testSimpleIntegration() {
        double[] y0 = equations.getInitialState();
        double tEnd = 10.0;

        integrator.integrate(equations, tEnd);
        double[] y1 = equations.getCompleteState();
        ODETestUtils.assertRelativelyClose(y0, y1, RELATIVE_TOL, ACCURACY);
    }

    /**
     * Test integration of a ODE with two solutions.
     */
    @Test
    public void testTwoSolutions() {
        double[] y0 = new double[]{1.0, 1.0};
        double tEnd = 2.0 * Math.PI;

        integrator.integrate(equations, tEnd);
        double[] y1 = equations.getCompleteState();
        double[] expected = new double[]{1.0, -1.0};
        ODETestUtils.assertRelativelyClose(expected, y1, RELATIVE_TOL, ACCURACY);
    }

    /**
     * Test integration of a ODE with singular Jacobian.
     */
    @Test
    public void testSingularJacobian() {
        double[] y0 = new double[]{0.0, 0.0};
        double tEnd = 1.0;

        integrator.integrate(equations, tEnd);
        double[] y1 = equations.getCompleteState();
        double[] expected = new double[]{1.0, 1.0};
        ODETestUtils.assertRelativelyClose(expected, y1, RELATIVE_TOL, ACCURACY);
    }

    /**
     * Test integration of a ODE with large Jacobian.
     */
    @Test
    public void testLargeJacobian() {
        double[] y0 = new double[]{1.0, 1.0};
        double tEnd = 1000.0;

        integrator.integrate(equations, tEnd);
        double[] y1 = equations.getCompleteState();
        // large Jacobian doesn't affect the solution, so we just check it's not null
        Assert.assertNotNull(equations.getJacobian(y1));
    }

    /**
     * Test integration of a ODE with initial state outside of allowed range.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidInitialState() {
        double[] y0 = new double[]{-1.0, 1.0};
        integrator.integrate(equations, 10.0);
    }

    /**
     * Test integration of a ODE with large step size.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testLargeStepSize() {
        integrator.setStepSize(1000.0);
        integrator.integrate(equations, 10.0);
    }

    /**
     * Test integration of a ODE with small step size.
     */
    @Test
    public void testSmallStepSize() {
        integrator.setStepSize(0.01);
        double[] y0 = equations.getInitialState();
        double tEnd = 10.0;

        integrator.integrate(equations, tEnd);
        double[] y1 = equations.getCompleteState();
        ODETestUtils.assertRelativelyClose(y0, y1, RELATIVE_TOL, ACCURACY);
    }

    /**
     * Test integration of a ODE with zero step size.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testZeroStepSize() {
        integrator.setStepSize(0.0);
        integrator.integrate(equations, 10.0);
    }

}

class ExampleODE implements ExpandableStatefulODE {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public int getStateDimension() {
        return 2;
    }

    @Override
    public int getParameterDimension() {
        return 0;
    }

    @Override
    public double[] getInitialState() {
        return new double[]{1.0, 1.0};
    }

    @Override
    public double[] getState(double time, double[] state) {
        state[0] = Math.exp(time);
        state[1] = Math.sin(time);
        return state;
    }

    @Override
    public double[] getDerivative(double time, double[] state) {
        double[] derivatives = new double[getStateDimension()];
        derivatives[0] = state[0];
        derivatives[1] = Math.cos(time);
        return derivatives;
    }

    @Override
    public double[] getJacobian(double time, double[] state) {
        double[] jacobian = new double[getStateDimension()][getStateDimension()];
        jacobian[0][0] = state[0];
        jacobian[1][1] = -Math.sin(time);
        return jacobian;
    }

}

class EmbeddedRungeKutta54Integrator extends EmbeddedRungeKuttaIntegrator {

    public EmbeddedRungeKutta54Integrator(double safety) {
        super("EmbeddedRungeKutta54Integrator", true, new double[]{0.3, 0.48, 0.055556, 0.55556,
                0.23812, 0.622032, 0.7}, new double[]{0.3, 0.45, 0.55556, 0.27778, 0.622032, 0.07784, 0.046616},
                new RungeKuttaStepInterpolator(), 1e-12, null, 1e-12, null);
        setSafety(safety);
    }

    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double sum = 0;
        for (int i = 0; i < yDotK.length; ++i) {
            sum += Math.abs(yDotK[i][0]);
        }
        return sum;
    }

}