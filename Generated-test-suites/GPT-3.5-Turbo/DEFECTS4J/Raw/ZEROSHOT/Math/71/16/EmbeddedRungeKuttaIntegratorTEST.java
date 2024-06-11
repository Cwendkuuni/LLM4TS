import org.junit.*;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    EmbeddedRungeKuttaIntegrator integrator;
    FirstOrderDifferentialEquations equations;
    double t0;
    double[] y0;
    double t;
    double[] y;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegrator() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0;
            }

            @Override
            protected double initializeStep(FirstOrderDifferentialEquations equations, boolean forward, int order, double[] scale, double t0, double[] y0, double[] yDot0, double[] y1, double[] yDot1) throws DerivativeException {
                return 0;
            }

            @Override
            protected double filterStep(double h, boolean forward, boolean acceptSmall) throws IntegratorException {
                return 0;
            }
        };
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 0;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                
            }
        };
        t0 = 0.0;
        y0 = new double[0];
        t = 0.0;
        y = new double[0];
    }

    @Test
    public void testIntegrate() {
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (DerivativeException e) {
            fail("DerivativeException thrown");
        } catch (IntegratorException e) {
            fail("IntegratorException thrown");
        }
    }
}