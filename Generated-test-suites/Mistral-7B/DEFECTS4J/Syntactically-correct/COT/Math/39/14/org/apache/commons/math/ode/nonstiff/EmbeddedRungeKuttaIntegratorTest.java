package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.Verifier;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepControl;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for EmbeddedRungeKuttaIntegrator.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ABSOLUTE_TOLERANCE = 1.0e-12;
    private static final double RELATIVE_TOLERANCE = 1.0e-12;
    private static final double MIN_STEP = 1.0e-12;
    private static final double MAX_STEP = 1.0e12;

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;
    private RungeKuttaStepControl stepControl;
    private RungeKuttaStepInterpolator interpolator;

    @Before
    public void setUp() {
        // Create an example integrator using the embedded Runge-Kutta method RKF45
        EmbeddedRungeKuttaMethod rk45 = new EmbeddedRungeKuttaMethod(
                "RKF45", true,
                new double[]{0.0, 0.23692688505677, 0.18472843488404, 0.04679722018682, 0.0},
                new double[][]{
                        {0.0, 0.23692688505677},
                        {0.23692688505677, 0.38303736612353},
                        {0.38303736612353, 0.52363222585534},
                        {0.52363222585534, 0.65854516221332},
                        {0.65854516221332, 0.78739613958265},
                        {0.78739613958265, 0.90528861522937},
                        {0.90528861522937, 0.98060596786363},
                        {0.98060596786363, 1.0},
                        {0.31968865941644, 0.34744555554714, 0.36913147615832,
                                0.38714801956473, 0.40232150845673, 0.41483476538954,
                                0.42475179987495, 0.43202323563588, 0.43681202596791}},
                new double[]{0.31968865941644, 0.34744555554714, 0.36913147615832, 0.38714801956473,
                            0.40232150845673, 0.41483476538954, 0.42475179987495, 0.43202323563588,
                            0.43681202596791},
                new RungeKuttaStepInterpolator(new RungeKuttaStepInterpolator.DefaultStepInterpolator()));

        integrator = new EmbeddedRungeKuttaIntegrator(rk45.getName(), rk45.isFSAL(), rk45.getC(),
                rk45.getA(), rk45.getB(), rk45.getPrototype(), MIN_STEP, MAX_STEP,
                RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += FastMath.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                error = FastMath.sqrt(error);
                return error;
            }
        };

        equations = ODETestUtils.createMockEquations(new ExpandableStatefulODE.MockFunction(1, 1),
                new double[]{0.0});

        stepControl = new RungeKuttaStepControl();

        interpolator = new RungeKuttaStepInterpolator(new RungeKuttaStepInterpolator.DefaultStepInterpolator());
    }

    @Test
    public void testGetOrder() {
        assertEquals(5, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), 0.001);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.15);
        assertEquals(0.15, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testIntegrateWithPreciseInitialValue() throws Exception {
        integrator.integrate(equations, 1.0);
        assertEquals(1.0, Precision.round(equations.getCompleteState()[0], 12), 0.0);
    }

    @Test
    public void testIntegrateWithInitialValueSlightlyDifferentThanExpected() throws Exception {
        double initialValue = 1.0001;
        equations.setCompleteState(new double[]{initialValue});

        integrator.integrate(equations, 1.0);
        assertEquals(1.0, Precision.round(equations.getCompleteState()[0], 12), 0.0);
    }

    @Test
    public void testIntegrateWithInitialValueDifferentThanExpected() throws Exception {
        double initialValue = 2.0;
        equations.setCompleteState(new double[]{initialValue});

        integrator.integrate(equations, 1.0);
        assertEquals(1.0, Precision.round(equations.getCompleteState()[0], 12), 0.0);
    }

    @Test
    public void testIntegrateWithStepSizeTooSmall() throws Exception {
        integrator.setMinStep(-MIN_STEP);

        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, 1.0));
    }

    @Test
    public void testIntegrateWithStepSizeTooLarge() throws Exception {
        integrator.setMaxStep(-MAX_STEP);

        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, 1.0));
    }

    @Test
    public void testIntegrateWithAboveRelativeToleranceError() throws Exception {
        integrator.setMinReduction(0.0);
        integrator.setMaxGrowth(0.1);

        equations.setCompleteState(new double[]{0.0});
        double tolerance = 1.5;
        integrator.integrate(equations, 1.0, tolerance);

        Verifier.verifyEquivalence(equations, new double[]{1.1}, tolerance);
    }

    @Test
    public void testIntegrateWithBelowRelativeToleranceError() throws Exception {
        integrator.setMinReduction(1.1);
        integrator.setMaxGrowth(0.1);

        equations.setCompleteState(new double[]{0.0});
        double tolerance = 1.1;
        integrator.integrate(equations, 1.0, tolerance);

        Verifier.verifyEquivalence(equations, new double[]{1.0}, tolerance);
    }

    @Test
    public void testIntegrateWithAboveAbsoluteToleranceError() throws Exception {
        integrator.setMinReduction(0.9);
        integrator.setMaxGrowth(0.1);

        equations.setCompleteState(new double[]{0.0});
        double tolerance = 2.0;
        integrator.integrate(equations, 1.0, tolerance);

        Verifier.verifyEquivalence(equations, new double[]{1.0}, tolerance);
    }

    @Test
    public void testIntegrateWithBelowAbsoluteToleranceError() throws Exception {
        integrator.setMinReduction(0.9);
        integrator.setMaxGrowth(0.1);

        equations.setCompleteState(new double[]{0.0});
        double tolerance = 0.5;
        integrator.integrate(equations, 1.0, tolerance);

        Verifier.verifyEquivalence(equations, new double[]{1.0}, tolerance);
    }

    @Test
    public void testIntegrateWithFSALMethod() throws Exception {
        integrator = new EmbeddedRungeKuttaIntegrator(rk45.getName(), rk45.isFSAL(), rk45.getC(),
                rk45.getA(), rk45.getB(), rk45.getPrototype(), MIN_STEP, MAX_STEP,
                RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += FastMath.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                error = FastMath.sqrt(error);
                return error;
            }

            @Override
            public void initIntegration(double startTime, double[] y0, double[] yDot0) {
                // Save the initial derivatives to be reused in the next step
                super.initIntegration(startTime, y0, yDot0);
                for (int i = 0; i < y0.length; ++i) {
                    yDotTmp[i] = yDot0[i];
                }
            }
        };

        integrator.integrate(equations, 1.0);

        integrator.integrate(equations, 1.0);
        assertEquals(1.0, Precision.round(equations.getCompleteState()[0], 12), 0.0);
    }

    @Test
    public void testIntegrateWithNonFSALMethod() throws Exception {
        integrator = new EmbeddedRungeKuttaIntegrator(rk45.getName(), false, rk45.getC(),
                rk45.getA(), rk45.getB(), rk45.getPrototype(), MIN_STEP, MAX_STEP,
                RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE) {
                    @Override
                    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                        double error = 0.0;
                        for (int i = 0; i < y0.length; ++i) {
                            error += FastMath.pow(FastMath.abs(y1[i] - y0[i]), 2);
                        }
                        error = FastMath.sqrt(error);
                        return error;
                    }
                };

        integrator.integrate(equations, 1.0);

        integrator.integrate(equations, 1.0);
        assertEquals(1.0, Precision.round(equations.getCompleteState()[0], 12), 0.0);
    }

}