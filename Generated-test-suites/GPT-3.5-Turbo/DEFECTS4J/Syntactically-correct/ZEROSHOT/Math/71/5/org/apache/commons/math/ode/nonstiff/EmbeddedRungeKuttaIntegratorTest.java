//import statements
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double t0;
    private double[] y0;
    private double t;
    private double[] y;

    @Before
    public void setUp() {
        //initialize test variables
        integrator = new EmbeddedRungeKuttaIntegratorMock();
        equations = new FirstOrderDifferentialEquationsMock();
        t0 = 0.0;
        y0 = new double[]{0.0, 0.0};
        t = 1.0;
        y = new double[]{1.0, 1.0};
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        double result = integrator.integrate(equations, t0, y0, t, y);
        Assert.assertEquals(1.0, result, 1e-15);
    }
}

class EmbeddedRungeKuttaIntegratorMock extends EmbeddedRungeKuttaIntegrator {

    //constructor
    public EmbeddedRungeKuttaIntegratorMock() {
        super("Mock", false, new double[]{}, new double[][]{}, new double[]{}, null, 0, 0, 0, 0);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        return 0;
    }
}

class FirstOrderDifferentialEquationsMock implements FirstOrderDifferentialEquations {

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {

    }

    @Override
    public int getDimension() {
        return 0;
    }
}