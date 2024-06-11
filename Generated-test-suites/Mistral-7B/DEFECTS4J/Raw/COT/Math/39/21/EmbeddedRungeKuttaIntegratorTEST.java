import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE ode;

    @Before
    public void setup() {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.0, 0.0, 1.0}, {0.3, 0.9, 0.6, 1.2}, {0.6, 1.2, 0.9, 1.5}, {1.0, 1.5, 1.2, 1.0}};
        double[] b = {0.236926885056682, 0.679726231373712, 0.275862011318392, 0.136917674196925};
        EmbeddedRungeKuttaIntegrator prototype = new RungeKutta35Integrator();
        integrator = new EmbeddedRungeKuttaIntegrator("RKF45", false, c, a, b, prototype, 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        ode = new ExpandableStatefulODE("test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getStateDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }

            @Override
            public void initialize(double t0, double[] y0) {
                y0[0] = 0.0;
            }
        };
    }

    @Test
    public void testConstructor() {
        assertNotNull(integrator);
    }

    @Test
    public void testGetOrder() {
        int order = integrator.getOrder();
        assertEquals(4, order);
    }

    @Test
    public void testGetSafety() {
        double safety = integrator.getSafety();
        assertEquals(0.9, safety, 0.001);
    }

    @Test
    public void testGetMinReduction() {
        double minReduction = integrator.getMinReduction();
        assertEquals(0.2, minReduction, 0.001);
    }

    @Test
    public void testGetMaxGrowth() {
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(10.0, maxGrowth, 0.001);
    }

    @Test
    public void testIntegrateWithSimpleFunction() throws Exception {
        double[] initState = {0.0};
        double[] expectedStates = {0.0, 0.1, 0.2, 0.3, 0.4, 0.5};
        double[] expectedErrs = {0.0, 0.37197939527948166E-7, 3.4142221140898845E-11, 1.3547384463356523E-16, 0.0};

        double t0 = 0.0;
        double t1 = 1.0;

        integrator.integrate(ode, t0);
        double[] state = ode.getCompleteState();
        assertEquals(expectedStates[0], state[0], 1e-8);

        integrator.setStep(0.1);
        integrator.integrate(ode, t0 + 0.1);
        state = ode.getCompleteState();
        assertEquals(expectedStates[1], state[0], 1e-8);

        integrator.setStep(0.1);
        integrator.integrate(ode, t0 + 0.2);
        state = ode.getCompleteState();
        assertEquals(expectedStates[2], state[0], 1e-8);

        integrator.setStep(0.1);
        integrator.integrate(ode, t0 + 0.3);
        state = ode.getCompleteState();
        assertEquals(expectedStates[3], state[0], 1e-8);

        integrator.setStep(0.1);
        integrator.integrate(ode, t0 + 0.4);
        state = ode.getCompleteState();
        assertEquals(expectedStates[4], state[0], 1e-8);

        integrator.setStep(0.1);
        integrator.integrate(ode, t0 + 0.5);
        state = ode.getCompleteState();
        assertEquals(expectedStates[5], state[0], 1e-8);

        integrator.resetInternalState();

        integrator.setStep(0.1);
        integrator.integrate(ode, t1);
        state = ode.getCompleteState();
        assertEquals(expectedStates[5], state[0], 1e-8);

        // Test the error estimation
        double[] errorEstimations = new double[6];
        integrator.setErrorEstimations(errorEstimations);

        integrator.integrate(ode, t0);
        integrator.integrate(ode, t0 + 0.1);
        integrator.integrate(ode, t0 + 0.2);
        integrator.integrate(ode, t0 + 0.3);
        integrator.integrate(ode, t0 + 0.4);
        integrator.integrate(ode, t0 + 0.5);

        assertEquals(expectedErrs[0], errorEstimations[0], 1e-8);
        assertEquals(expectedErrs[1], errorEstimations[1], 1e-8);
        assertEquals(expectedErrs[2], errorEstimations[2], 1e-8);
        assertEquals(expectedErrs[3], errorEstimations[3], 1e-8);
        assertEquals(expectedErrs[4], errorEstimations[4], 1e-8);
        assertEquals(expectedErrs[5], errorEstimations[5], 1e-8);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() throws Exception {
        integrator.setStep(-0.1);
        integrator.integrate(ode, 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithSingularJacobian() throws Exception {
        ode = new ExpandableStatefulODE("test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getStateDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0 / (y[0] * y[0]);
            }

            @Override
            public void initialize(double t0, double[] y0) {
                y0[0] = 1.0;
            }
        };

        integrator.integrate(ode, 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithJacobianThatBlowsUp() throws Exception {
        ode = new ExpandableStatefulODE("test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getStateDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = BesselFunctions.gnm(1.0, y[0]);
            }

            @Override
            public void initialize(double t0, double[] y0) {
                y0[0] = 1.0;
            }
        };

        integrator.integrate(ode, 0.0);
    }

}