package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sample.RungeKuttaStepInterpolatorFactory;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKutta23;
import org.apache.commons.math3.special.E;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test the Embedded Runge-Kutta method 2-3.
     */
    @Test
    public void testEmbeddedRungeKutta23() {
        final String methodName = EmbeddedRungeKutta23.NAME;
        final double[] c = EmbeddedRungeKutta23.TIME_STEPS;
        final double[][] a = EmbeddedRungeKutta23.WEIGHTS;
        final double[] b = EmbeddedRungeKutta23.PROPAGATION_WEIGHTS;
        final double minStep = 1.0e-10;
        final double maxStep = 1.0;
        final double scalAbsoluteTolerance = 1.0e-10;
        final double scalRelativeTolerance = 1.0e-8;

        final RungeKuttaStepInterpolatorFactory interpolatorFactory =
            new RungeKuttaStepInterpolatorFactory(EmbeddedRungeKuttaMethod.valueOf(methodName), minStep, maxStep);
        final RungeKuttaStepInterpolator prototype = interpolatorFactory.createStepInterpolator();

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
            methodName, EmbeddedRungeKutta23.IS_FSAL, c, a, b, prototype, minStep, maxStep,
            scalAbsoluteTolerance, scalRelativeTolerance);

        final ExpandableStatefulODE equations = createEquations();

        // test integration
        double[] y = new double[]{1.0, 1.0};
        integrator.integrate(equations, 0.0);
        assertEquals(E.E_1_E, equations.getCompleteState()[0], 1.0e-8);
        assertEquals(E.E_1_E, equations.getCompleteState()[1], 1.0e-8);

        // test integration with step size control
        integrator.setSafety(0.8);
        integrator.setMinReduction(0.4);
        integrator.setMaxGrowth(2.0);
        integrator.integrate(equations, 2.0);
        assertEquals(E.E_2_E, equations.getCompleteState()[0], 1.0e-8);
        assertEquals(E.E_2_E, equations.getCompleteState()[1], 1.0e-8);

        // test integration with absolute tolerance
        integrator.setSafety(0.8);
        integrator.setMinReduction(0.4);
        integrator.setMaxGrowth(2.0);
        integrator.setVecAbsoluteTolerance(new double[]{1.0e-6, 1.0e-6});
        integrator.integrate(equations, 3.0);
        assertEquals(E.E_3_E, equations.getCompleteState()[0], 1.0e-6);
        assertEquals(E.E_3_E, equations.getCompleteState()[1], 1.0e-6);

        // test integration with relative tolerance
        integrator.setSafety(0.8);
        integrator.setMinReduction(0.4);
        integrator.setMaxGrowth(2.0);
        integrator.setVecRelativeTolerance(new double[]{1.0e-5, 1.0e-5});
        integrator.integrate(equations, 4.0);
        assertEquals(E.E_4_E, equations.getCompleteState()[0], 1.0e-5);
        assertEquals(E.E_4_E, equations.getCompleteState()[1], 1.0e-5);

        // test integration with a function that requires a large step size
        final double[] function = new double[]{1.0, -1.0};
        final double[] derivatives = new double[]{0.1, -0.1};
        equations.setTime(0.0);
        equations.setPrimaryMapper(new ExpandableStatefulODE.Mapper() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void map(double[] state, double[] y) {
                System.arraycopy(y, 0, state, 0, 2);
            }

            @Override
            public void reverseMap(double[] state, double[] y) {
                System.arraycopy(state, 0, y, 0, 2);
            }

            @Override
            public double[] derivatives(double[] state, double t) {
                return derivatives;
            }
        });
        equations.setSecondaryMappers(new ExpandableStatefulODE.Mapper[0]);
        integrator.integrate(equations, 10.0);
        assertEquals(10.0, equations.getTime(), 1.0e-8);
        assertEquals(new double[]{10.0, 0.0}, equations.getCompleteState(), 1.0e-8);

        // test integration with a function that requires a small step size
        equations.setTime(0.0);
        equations.setPrimaryMapper(new ExpandableStatefulODE.Mapper() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void map(double[] state, double[] y) {
                System.arraycopy(y, 0, state, 0, 2);
            }

            @Override
            public void reverseMap(double[] state, double[] y) {
                System.arraycopy(state, 0, y, 0, 2);
            }

            @Override
            public double[] derivatives(double[] state, double t) {
                return new double[]{Math.exp(-1.0 * t) * state[1],
                                    Math.exp(-1.0 * t) * state[1] * state[1]};
            }
        });
        integrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getTime(), 1.0e-8);
        assertEquals(new double[]{1.543080634769253E-7, 1.543080634769253E-7}, equations.getCompleteState(), 1.0e-8);

        resetInternalState(integrator);

    }

    private static ExpandableStatefulODE createEquations() {
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void map(double[] state, double[] y) {
                System.arraycopy(y, 0, state, 0, 2);
            }

            @Override
            public void reverseMap(double[] state, double[] y) {
                System.arraycopy(state, 0, y, 0, 2);
            }

            @Override
            public double[] derivatives(double[] state, double t) {
                return new double[]{state[1], 0.0};
            }
        };
        return equations;
    }

    private static void resetInternalState(EmbeddedRungeKuttaIntegrator integrator) {
        integrator.resetInternalState();
        integrator.setEquations(null);
    }

}