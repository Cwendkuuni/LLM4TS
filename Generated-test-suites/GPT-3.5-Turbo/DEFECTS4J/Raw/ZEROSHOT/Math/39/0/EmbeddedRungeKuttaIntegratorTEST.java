import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        // Create the Butcher arrays
        double[] c = {0.0, 0.25, 0.5, 0.75, 1.0};
        double[][] a = {
                {},
                {0.25},
                {0.0, 0.5},
                {0.0, 0.0, 0.75},
                {0.0, 0.0, 0.0, 1.0}
        };
        double[] b = {1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0, 0.0};

        // Create the prototype of the step interpolator
        RungeKuttaStepInterpolator interpolator = new RungeKuttaStepInterpolator();

        // Create the integrator
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, interpolator, 0.001, 0.1, 1e-5, 1e-5);
    }

    @Test
    public void testIntegrate() {
        // Create the equations
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        });

        // Set initial state
        double[] y0 = {0.0, 1.0};
        equations.setTime(0.0);
        equations.setCompleteState(y0);

        // Integrate
        double t = 10.0;
        integrator.integrate(equations, t);
    }
}