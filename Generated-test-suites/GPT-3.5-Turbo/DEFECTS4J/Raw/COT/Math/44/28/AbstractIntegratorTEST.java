import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) {
                // do nothing
            }
        };
        
        // Test default value
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
        
        // Set custom value
        integrator.setMaxEvaluations(1000);
        assertEquals(1000, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) {
                // do nothing
            }
        };
        
        // Test default value
        assertEquals(0, integrator.getEvaluations());
        
        // Test incrementation
        integrator.computeDerivatives(0.0, new double[]{0.0}, new double[]{1.0});
        integrator.computeDerivatives(0.0, new double[]{0.0}, new double[]{1.0});
        integrator.computeDerivatives(0.0, new double[]{0.0}, new double[]{1.0});
        assertEquals(3, integrator.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) {
                // do nothing
            }
        };
        
        // Test default value
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
        
        // Set custom value
        integrator.setMaxEvaluations(1000);
        assertEquals(1000, integrator.getMaxEvaluations());
        
        // Set negative value
        integrator.setMaxEvaluations(-1);
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }
}