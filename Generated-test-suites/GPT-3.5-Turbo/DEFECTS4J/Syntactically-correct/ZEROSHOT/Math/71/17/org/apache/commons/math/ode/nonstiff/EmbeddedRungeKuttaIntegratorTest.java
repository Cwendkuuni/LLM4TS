import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double t0;
    private double[] y0;
    private double t;
    private double[] y;

    @Before
    public void setUp() {
        integrator = new TestEmbeddedRungeKuttaIntegrator();
        equations = new TestFirstOrderDifferentialEquations();
        t0 = 0.0;
        y0 = new double[]{1.0, 2.0, 3.0};
        t = 1.0;
        y = new double[3];
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        double result = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(0.0, result, 0.0);
    }

}

class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

    public TestEmbeddedRungeKuttaIntegrator() {
        super("", false, new double[]{}, new double[][]{}, new double[]{}, null, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        return 0.0;
    }
}

class TestFirstOrderDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 0;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {

    }
}