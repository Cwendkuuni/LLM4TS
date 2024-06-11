
import org.junit.Test;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.IntegratorException;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK45", new double[]{0, 1},
                new double[][]{{}, {1.5}, {2, 3}}, new double[]{1, 2, 3}, new RungeKuttaStepInterpolator(),
                0.01);

        // TODO: initialize equations, t0, y0, t, y
        
        double result = integrator.integrate(equations, t0, y0, t, y);
        
        // TODO: assert the result

    }
}
