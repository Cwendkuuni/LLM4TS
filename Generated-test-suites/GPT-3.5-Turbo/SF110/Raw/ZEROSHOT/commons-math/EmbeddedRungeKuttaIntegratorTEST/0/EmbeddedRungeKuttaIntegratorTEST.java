import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;

public class EmbeddedRungeKuttaIntegratorTest {
    private EmbeddedRungeKuttaIntegrator integrator;
    
    @Before
    public void setup() {
        double[] c = {0.0, 1.0, 1.0};
        double[][] a = {{0.0, 0.0, 0.0}, {0.5, 0.0, 0.0}, {-1.0, 2.0, 0.0}};
        double[] b = {1.0 / 6.0, 2.0 / 3.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        this.integrator = new EmbeddedRungeKuttaIntegratorStub("testIntegrator", true, c, a, b, prototype);
    }
    
    @Test
    public void testGetSafety() {
        double safety = 0.9;
        integrator.setSafety(safety);
        assertEquals(safety, integrator.getSafety(), 0.0);
    }
    
    @Test
    public void testGetOrder() {
        // TODO: Implement test
    }
    
    @Test
    public void testGetIntegrate() {
        // TODO: Implement test
    }
    
    @Test
    public void testGetMinReduction() {
        double minReduction = 0.2;
        integrator.setMinReduction(minReduction);
        assertEquals(minReduction, integrator.getMinReduction(), 0.0);
    }
    
    @Test
    public void testGetMaxGrowth() {
        double maxGrowth = 10.0;
        integrator.setMaxGrowth(maxGrowth);
        assertEquals(maxGrowth, integrator.getMaxGrowth(), 0.0);
    }
    
    private class EmbeddedRungeKuttaIntegratorStub extends EmbeddedRungeKuttaIntegrator {
        
        public EmbeddedRungeKuttaIntegratorStub(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype) {
            super(name, fsal, c, a, b, prototype, 0, 0, 0, 0);
        }
        
        @Override
        public int getOrder() {
            return 0;
        }
        
        @Override
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 0;
        }
    }
}