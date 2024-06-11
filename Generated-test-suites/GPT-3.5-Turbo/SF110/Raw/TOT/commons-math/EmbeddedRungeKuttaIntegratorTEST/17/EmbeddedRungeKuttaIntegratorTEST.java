import org.junit.Test;
import org.apache.commons.math3.ode.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        int order = integrator.getOrder();
        assertEquals(4, order);
    }
    
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        double safety = integrator.getSafety();
        assertEquals(0.9, safety, 0.001);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        double safety = integrator.getSafety();
        assertEquals(0.8, safety, 0.001);
    }
    
    @Test
    public void testGetIntegrate() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FakeODE());
        double t = 10.0;
        try {
            integrator.integrate(equations, t);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals(t, equations.getTime(), 0.001);
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        double minReduction = integrator.getMinReduction();
        assertEquals(0.2, minReduction, 0.001);
    }
    
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        double minReduction = integrator.getMinReduction();
        assertEquals(0.1, minReduction, 0.001);
    }
    
    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(10.0, maxGrowth, 0.001);
    }
    
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(20.0);
        double maxGrowth = integrator.getMaxGrowth();
        assertEquals(20.0, maxGrowth, 0.001);
    }
    
    private class FakeEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

        public FakeEmbeddedRungeKuttaIntegrator() {
            super("FakeEmbeddedRungeKuttaIntegrator", false, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        }

        @Override
        public int getOrder() {
            return 4;
        }

        @Override
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 0.0;
        }
    }
    
    private class FakeODE implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 0;
        }

        @Override
        public void computeDerivatives(double v, double[] doubles, double[] doubles1) {
            
        }
    }
}