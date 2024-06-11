import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        double t = 0.0;
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        
        try {
            integrator.integrate(equations, t);
        } catch (Exception e) {
            fail("Exception should not be thrown.");
        }
    }
    
    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithIllegalStateException() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        double t = 0.0;
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        
        equations.setTime(t);
        
        integrator.integrate(equations, t);
    }
    
    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestEmbeddedRungeKuttaIntegrator() {
            super("Test Embedded Integrator", false,
                  new double[] {0.0},
                  new double[][] {{}},
                  new double[] {1.0},
                  null,
                  0.0, 1.0,
                  1.0, 1.0);
        }

        public int getOrder() {
            return 1;
        }

        protected double estimateError(double[][] yDotK, double[] y0,
                                       double[] y1, double h) {
            return 0.0;
        }

        protected void computeDerivatives(double t, double[] y,
                                           double[] yDot) {
            // Do nothing
        }
    }
}