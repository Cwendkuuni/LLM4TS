import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.0, 0.5, 1.0}, new double[][]{{0.0}, {0.5, 0.0}, {0.0, 1.0}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.0, 1.0, 0.1, 0.1) {
            public int getOrder() {
                return 1;
            }
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0.0;
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 1;
            }
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = t;
            }
        };

        double[] y = new double[]{0.0};

        double endTime = integrator.integrate(equations, 0.0, y, 1.0, y);
    }
}