import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.FastMath;

public class EmbeddedRungeKuttaIntegratorTest {
    
    // Helper function for comparing arrays
    private boolean compareArrays(double[] arr1, double[] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (FastMath.abs(arr1[i] - arr2[i]) > 1e-9) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testIntegrate() {
        // Initialize the integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("integrator", true, new double[]{1.0}, new double[][]{{0.5}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-3);
        
        // Initialize the equations and time
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        double t = 1.0;
        
        try {
            integrator.integrate(equations, t);
        } catch (NumberIsTooSmallException | DimensionMismatchException | MaxCountExceededException | NoBracketingException e) {
            fail(e.getMessage());
        }
        
        double[] expectedState = {0.0, 0.0};
        double[] actualState = equations.getCompleteState();
        assertTrue(compareArrays(expectedState, actualState));
    }
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("integrator", true, new double[]{1.0}, new double[][]{{0.5}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-3);
        
        int expectedOrder = 1;
        int actualOrder = integrator.getOrder();
        assertEquals(expectedOrder, actualOrder);
    }
    
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("integrator", true, new double[]{1.0}, new double[][]{{0.5}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-3);
        
        double expectedSafety = 0.9;
        double actualSafety = integrator.getSafety();
        assertEquals(expectedSafety, actualSafety, 1e-9);
    }
    
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("integrator", true, new double[]{1.0}, new double[][]{{0.5}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-3);
        
        double newSafety = 0.8;
        integrator.setSafety(newSafety);
        
        double expectedSafety = newSafety;
        double actualSafety = integrator.getSafety();
        assertEquals(expectedSafety, actualSafety, 1e-9);
    }
    
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("integrator", true, new double[]{1.0}, new double[][]{{0.5}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-3);
        
        double expectedMinReduction = 0.2;
        double actualMinReduction = integrator.getMinReduction();
        assertEquals(expectedMinReduction, actualMinReduction, 1e-9);
    }
    
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("integrator", true, new double[]{1.0}, new double[][]{{0.5}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-3);
        
        double newMinReduction = 0.1;
        integrator.setMinReduction(newMinReduction);
        
        double expectedMinReduction = newMinReduction;
        double actualMinReduction = integrator.getMinReduction();
        assertEquals(expectedMinReduction, actualMinReduction, 1e-9);
    }
    
    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("integrator", true, new double[]{1.0}, new double[][]{{0.5}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-3);
        
        double expectedMaxGrowth = 10.0;
        double actualMaxGrowth = integrator.getMaxGrowth();
        assertEquals(expectedMaxGrowth, actualMaxGrowth, 1e-9);
    }
    
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("integrator", true, new double[]{1.0}, new double[][]{{0.5}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-3);
        
        double newMaxGrowth = 20.0;
        integrator.setMaxGrowth(newMaxGrowth);
        
        double expectedMaxGrowth = newMaxGrowth;
        double actualMaxGrowth = integrator.getMaxGrowth();
        assertEquals(expectedMaxGrowth, actualMaxGrowth, 1e-9);
    }
    
    @Test
    public void testEstimateError() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("integrator", true, new double[]{1.0}, new double[][]{{0.5}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-3);
        
        double[][] yDotK = {{0.0, 0.0}};
        double[] y2 = {0.0, 0.0};
        double[] yTmp = {0.0, 0.0};
        double stepSize = 0.1;
        
        double expectedError = 0.0;
        double actualError = integrator.estimateError(yDotK, y2, yTmp, stepSize);
        assertEquals(expectedError, actualError, 1e-9);
    }

}