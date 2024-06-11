import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testIntegrate() {
        double c[] = {1.0, 0.5};
        double a[][] = {{}, {0.5}, {1.0}};
        double b[] = {1.0, 0.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double minStep = 0.01;
        double maxStep = 0.1;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test", true, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new Equations());
        double t = 0;
        try {
            integrator.integrate(equations, t);
        } catch (MathIllegalStateException e) {
            fail();
        } catch (MathIllegalArgumentException e) {
            fail();
        }
    }
    
    private class Equations implements OrdinaryDifferentialEquation {
        public int getDimension() {
            return 2;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }
}