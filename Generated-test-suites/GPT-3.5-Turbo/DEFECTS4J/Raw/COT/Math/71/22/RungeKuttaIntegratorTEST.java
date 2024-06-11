import org.junit.Test;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;

public class RungeKuttaIntegratorTest {
    
    private final double[] c;
    private final double[][] a;
    private final double[] b;
    private final RungeKuttaStepInterpolator prototype;
    private final double step;
    
    public RungeKuttaIntegratorTest() {
        c = new double[]{0, 0.2, 0.3, 0.8, 8.0 / 9.0, 1.0, 1.0};
        a = new double[][]{{}, {0.2}, {3.0 / 40.0, 9.0 / 40.0}, {44.0 / 45.0, -56.0 / 15.0, 32.0 / 9.0}, {19372.0 / 6561.0, -25360.0 / 2187.0, 64448.0 / 6561.0, -212.0 / 729.0}, {9017.0 / 3168.0, -355.0 / 33.0, 46732.0 / 5247.0, 49.0 / 176.0, -5103.0 / 18656.0}, {35.0 / 384.0, 0, 500.0 / 1113.0, 125.0 / 192.0, -2187.0 / 6784.0, 11.0 / 84.0}};
        b = new double[]{35.0 / 384.0, 0, 500.0 / 1113.0, 125.0 / 192.0, -2187.0 / 6784.0, 11.0 / 84.0, 0};
        prototype = new RungeKuttaStepInterpolator();
        step = 0.01;
    }
    
    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        // Write your test here
    }
}