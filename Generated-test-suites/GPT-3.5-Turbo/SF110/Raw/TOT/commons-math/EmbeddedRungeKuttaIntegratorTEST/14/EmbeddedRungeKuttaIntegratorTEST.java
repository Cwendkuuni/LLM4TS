import org.junit.Test;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.exception.*;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.001);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }

    @Test
    public void testIntegrate() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        });

        double[] y0 = {1.0, 0.0};
        equations.setTime(0.0);
        equations.setCompleteState(y0);

        try {
            integrator.integrate(equations, 2 * Math.PI);
        } catch (Exception e) {
            fail("Exception thrown during integration: " + e.getMessage());
        }

        double[] y = equations.getCompleteState();
        assertEquals(1.0, y[0], 0.01);
        assertEquals(0.0, y[1], 0.01);
    }

    @Test(expected = Exception.class)
    public void testIntegrateException() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                throw new RuntimeException("Exception in computeDerivatives");
            }
        });

        double[] y0 = {1.0, 0.0};
        equations.setTime(0.0);
        equations.setCompleteState(y0);

        integrator.integrate(equations, 10.0);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new CustomEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.001);
    }

    private class CustomEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public int getOrder() {
            return 4;
        }
        protected double estimateError(double[][] yDotK, double[] y1, double[] y2, double h) {
            return 0.0;
        }
        protected void computeDerivatives(double t, double[] y, double[] yDot) {
        }
    }
}