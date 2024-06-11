import static org.junit.Assert.*;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.NonstiffIntegrator;
import org.apache.commons.math3.ode.initialvalues.InitialValueProblem;
import org.apache.commons.math3.ode.noises.RandomValueGeneratorFunction;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ACCURACY = 1e-12;
    private static final double RELATIVE_TOLERANCE = 1e-6;
    private static final double ABSOLUTE_TOLERANCE = 1e-14;
    private static final double INITIAL_TIME = 0.0;
    private static final double FINAL_TIME = 2 * Math.PI;

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;

    @Before
    public void setup() {
        integrator = new RungeKuttaFehlberg54Method();
        equations = new InitialValueProblem(new BesselODEFunction(), new double[]{1.0}, INITIAL_TIME);
        equations.setStepHandler(new DummyStepHandler());
    }

    @Test
    public void testIntegratorFunctionality() {
        integrator.integrate(equations, FINAL_TIME);
        assertEquals(IntegratorType.RKF54.name(), integrator.getName());
        assertTrue(integrator.isFSAL());
        assertArrayEquals(equations.getCompleteState(), integrator.getLastState(), ACCURACY);
        assertArrayEquals(equations.getState(), integrator.getState(), ACCURACY);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidMinStepSize() {
        double minStep = -1.0;
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, minStep, FINAL_TIME, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidMaxStepSize() {
        double maxStep = -1.0;
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, INITIAL_TIME, maxStep, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidAbsoluteTolerance() {
        double[] vecAbsoluteTolerance = new double[]{-1.0};
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, INITIAL_TIME, FINAL_TIME,
                vecAbsoluteTolerance, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidRelativeTolerance() {
        double[] vecRelativeTolerance = new double[]{-1.0};
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, INITIAL_TIME, FINAL_TIME,
                null, vecRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithNullEquations() {
        integrator.integrate(null, FINAL_TIME);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithNullTime() {
        integrator.integrate(equations, null);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidTime() {
        integrator.integrate(equations, -1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidStepSize() {
        double[] vecStepSizes = new double[]{-1.0};
        integrator.integrate(equations, FINAL_TIME, vecStepSizes);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithInvalidScaledError() {
        double error = -1.0;
        integrator.acceptStep(null, null, null, FINAL_TIME, error);
    }

    @Test
    public void testIntegrateWithValidStepSize() {
        double[] vecStepSizes = new double[]{0.1, 0.2};
        integrator.integrate(equations, FINAL_TIME, vecStepSizes);
    }

    @Test
    public void testIntegrateWithLargeError() {
        double error = 100.0;
        integrator.acceptStep(null, null, null, FINAL_TIME, error);
        assertEquals(IntegratorState.REJECTED, integrator.getState(), IntegratorState.REJECTED);
    }

    @Test
    public void testIntegrateWithSmallError() {
        double error = 0.001;
        integrator.acceptStep(null, null, null, FINAL_TIME, error);
        assertEquals(IntegratorState.ACCEPTED, integrator.getState(), IntegratorState.ACCEPTED);
    }

    @Test
    public void testIntegrateWithValidMinStepSize() {
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, 0.1, FINAL_TIME, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
    }

    @Test
    public void testIntegrateWithValidMaxStepSize() {
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, INITIAL_TIME, 1.0, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
    }

    @Test
    public void testIntegrateWithValidAbsoluteTolerance() {
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, INITIAL_TIME, FINAL_TIME,
                new double[]{ABSOLUTE_TOLERANCE}, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
    }

    @Test
    public void testIntegrateWithValidRelativeTolerance() {
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, INITIAL_TIME, FINAL_TIME,
                null, new double[]{RELATIVE_TOLERANCE}) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
        };
    }

    @Test
    public void testIntegrateWithValidSafety() {
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, INITIAL_TIME, FINAL_TIME, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
            @Override
            public double getSafety() {
                return 0.5;
            }
        };
    }

    @Test
    public void testIntegrateWithValidMinReduction() {
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, INITIAL_TIME, FINAL_TIME, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
            @Override
            public double getMinReduction() {
                return 0.3;
            }
        };
    }

    @Test
    public void testIntegrateWithValidMaxGrowth() {
        NonstiffIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "TestIntegrator", false, null, null, null, null, INITIAL_TIME, FINAL_TIME, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }
            @Override
            public double getMaxGrowth() {
                return 2.0;
            }
        };
    }

}

class BesselODEFunction implements ExpandableStatefulODE {

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void initialize(double[] state, double time) {
        state[0] = 1.0;
    }

    @Override
    public void computeDerivatives(double time, double[] state, double[] derivatives) {
        derivatives[0] = -BesselFunctions.secondKind(0, time) * state[0];
    }

}

class DummyStepHandler implements StepHandler {

    @Override
    public void handleStep(StepInterpolator interpolator, StepInfo info) {
        // empty implementation
    }

}