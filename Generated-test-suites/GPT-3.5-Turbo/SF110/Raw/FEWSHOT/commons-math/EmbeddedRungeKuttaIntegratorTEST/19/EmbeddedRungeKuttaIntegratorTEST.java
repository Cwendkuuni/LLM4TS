import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    public static class TestIntegrator extends EmbeddedRungeKuttaIntegrator {
        
        public TestIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b,
                RungeKuttaStepInterpolator prototype, double minStep,
                double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance,
                    scalRelativeTolerance);
        }

        public TestIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b,
                RungeKuttaStepInterpolator prototype, double minStep,
                double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance,
                    vecRelativeTolerance);
        }

        @Override
        public int getOrder() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            // TODO Auto-generated method stub
            return 0;
        }
        
        @Override
        protected void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException, DimensionMismatchException {
            // TODO Auto-generated method stub
            
        }
    }
    
    @Test
    public void testGetSafety() {
        TestIntegrator integrator = new TestIntegrator("", false, null, null, null, null, 0, 0, 0, 0);
        integrator.setSafety(0.9);
        assertEquals(0.9, integrator.getSafety(), 0);
    }
    
    @Test
    public void testSetSafety() {
        TestIntegrator integrator = new TestIntegrator("", false, null, null, null, null, 0, 0, 0, 0);
        integrator.setSafety(0.9);
        assertEquals(0.9, integrator.getSafety(), 0);
    }
    
    @Test
    public void testSetMinReduction() {
        TestIntegrator integrator = new TestIntegrator("", false, null, null, null, null, 0, 0, 0, 0);
        integrator.setMinReduction(0.2);
        assertEquals(0.2, integrator.getMinReduction(), 0);
    }
    
    @Test
    public void testGetMinReduction() {
        TestIntegrator integrator = new TestIntegrator("", false, null, null, null, null, 0, 0, 0, 0);
        integrator.setMinReduction(0.2);
        assertEquals(0.2, integrator.getMinReduction(), 0);
    }
    
    @Test
    public void testSetMaxGrowth() {
        TestIntegrator integrator = new TestIntegrator("", false, null, null, null, null, 0, 0, 0, 0);
        integrator.setMaxGrowth(10.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0);
    }
    
    @Test
    public void testGetMaxGrowth() {
        TestIntegrator integrator = new TestIntegrator("", false, null, null, null, null, 0, 0, 0, 0);
        integrator.setMaxGrowth(10.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0);
    }
    
    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        TestIntegrator integrator = new TestIntegrator("", false, null, null, null, null, 0, 0, 0, 0);
        ExpandableStatefulODE equations = new ExpandableStatefulODE(integrator);
        double t = 0;
        integrator.integrate(equations, t);
    }

}