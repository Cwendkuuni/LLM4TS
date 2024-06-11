import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private double t0;
    private double[] y0;

    @Before
    public void setUp() {
        double[] c = new double[]{0.0, 0.5, 0.5, 1.0};
        double[][] a = new double[][]{{0.0}, {0.5, 0.0}, {-1.0, 1.0, 0.0}, {0.5, -1.5, 2.0, 0.0}};
        double[] b = new double[]{1.0 / 6.0, 0.0, 2.0 / 3.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double minStep = 0.0;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1e-10;
        double scalRelativeTolerance = 1e-10;
        integrator = new EmbeddedRungeKuttaIntegrator("Test Integrator", true, c, a, b, prototype, minStep, maxStep,
                                                      scalAbsoluteTolerance, scalRelativeTolerance);
        t0 = 0.0;
        y0 = new double[]{1.0, 2.0, 3.0};
    }

    @After
    public void tearDown() {
        integrator = null;
        t0 = 0.0;
        y0 = null;
    }

    @Test
    public void testIntegrate() {
        double t = 10.0;
        double[] y = new double[3];
        double result = integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3;
            }
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + y[1] + y[2];
                yDot[1] = y[0] * y[1] + y[1] * y[2] + y[2] * y[0];
                yDot[2] = y[0] * y[1] * y[2];
            }
        }, t0, y0, t, y);
        // Assert result
        // ...
    }
}