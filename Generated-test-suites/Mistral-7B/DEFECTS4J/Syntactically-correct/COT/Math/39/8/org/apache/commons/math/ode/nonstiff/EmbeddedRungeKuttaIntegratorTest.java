import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testConstructor() {
        // Test the default constructor
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4));
        assertNotNull(integrator);

        // Test the constructor with custom minStep, maxStep, scalAbsoluteTolerance, and scalRelativeTolerance
        double minStep = 0.001;
        double maxStep = 0.01;
        double[] scalAbsoluteTolerance = {1e-6, 1e-5};
        double[] scalRelativeTolerance = {1e-7, 1e-6};
        EmbeddedRungeKuttaIntegrator integrator2 = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        assertNotNull(integrator2);
    }

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4));
        int order = integrator.getOrder();
        assertEquals(3, order);
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4));
        double safety = integrator.getSafety();
        assertEquals(0.9, safety, 1e-6);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4));
        integrator.setSafety(0.8);
        double safety = integrator.getSafety();
        assertEquals(0.8, safety, 1e-6);
    }

    @Test
    public void testIntegrate_ValidArguments() throws Exception {
        ExpandableStatefulODE equations = new MyEquations();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4), 0.01, 0.1, null, null);

        integrator.integrate(equations, 0.0);
        double[] expected = {1.0, 1.0};
        assertArrayEquals(expected, equations.getCompleteState(), 1e-6);

        integrator.integrate(equations, 1.0);
        expected = new double[]{2.718281828, 2.718281828};
        assertArrayEquals(expected, equations.getCompleteState(), 1e-6);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrate_InvalidArgument_MinStepNegative() throws Exception {
        ExpandableStatefulODE equations = new MyEquations();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4), -0.01, 0.1, null, null);
        integrator.integrate(equations, 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrate_InvalidArgument_NullEquations() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4), 0.01, 0.1, null, null);
        integrator.integrate(null, 0.0);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4), 0.01, 0.1, null, null);
        double minReduction = integrator.getMinReduction();
        assertEquals(0.2, minReduction, 1e-6);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4), 0.01, 0.1, null, null);
        integrator.setMinReduction(0.3);
        double minReduction = integrator.getMinReduction();
        assertEquals(0.3, minReduction, 1e-6);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4), 0.01, 0.1, null, null);
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(10.0, maxGrowth, 1e-6);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded RK method", false, new double[]{0.3, 0.4, 0.1},
                new double[][]{{0.23692688, 0.27580336}, {0.34659708, 0.32591722}, {0.43371862, 0.46791351}},
                new double[]{0.1, 0.35, 0.55}, new RungeKuttaStepInterpolator(4), 0.01, 0.1, null, null);
        integrator.setMaxGrowth(5.0);
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(5.0, maxGrowth, 1e-6);
    }

    private static class MyEquations implements ExpandableStatefulODE {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void initialize(double[] state, double[] derivatives) {
            state[0] = 1.0;
            state[1] = 1.0;
            derivatives[0] = 0.0;
            derivatives[1] = 0.0;
        }

        @Override
        public void computeDerivatives(double time, double[] state, double[] derivatives) {
            derivatives[0] = state[1];
            derivatives[1] = -state[0];
        }

        @Override
        public void stateTransition(double[] state, double[] stateTransitionValues, double[] derivatives, double[] jacobian) {
            stateTransitionValues[0] = state[0] + state[1] * 0.1;
            stateTransitionValues[1] = state[1] + state[0] * 0.1;
            jacobian[0][0] = 1.0;
            jacobian[0][1] = 1.0;
            jacobian[1][0] = 1.0;
            jacobian[1][1] = 1.0;
        }
    }
}