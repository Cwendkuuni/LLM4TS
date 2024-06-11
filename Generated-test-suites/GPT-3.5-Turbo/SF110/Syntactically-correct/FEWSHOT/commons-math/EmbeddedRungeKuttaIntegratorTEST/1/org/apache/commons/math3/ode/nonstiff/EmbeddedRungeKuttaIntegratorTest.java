package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testConstructor1() {
        // Test the constructor that takes scalar tolerances
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{0.1}}, new double[]{0.2}, new RungeKuttaStepInterpolator(), 1.0, 2.0, 0.0001, 0.000001);
        
        assertEquals(1.0, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMaxGrowth(), 0.0);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{0.1}}, new double[]{0.2}, new RungeKuttaStepInterpolator(), 1.0, 2.0, 0.0001, 0.000001);
        
        integrator.setSafety(0.5);
        
        assertEquals(0.5, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{0.1}}, new double[]{0.2}, new RungeKuttaStepInterpolator(), 1.0, 2.0, 0.0001, 0.000001);
        
        integrator.setMinReduction(0.1);
        
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{0.1}}, new double[]{0.2}, new RungeKuttaStepInterpolator(), 1.0, 2.0, 0.0001, 0.000001);
        
        integrator.setMaxGrowth(1.5);
        
        assertEquals(1.5, integrator.getMaxGrowth(), 0.0);
    }
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{0.1}}, new double[]{0.2}, new RungeKuttaStepInterpolator(), 1.0, 2.0, 0.0001, 0.000001);
        
        assertEquals(0, integrator.getOrder());
    }

    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        // Create a dummy ExpandableStatefulODE object for testing
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new EquationsMapper() {
            public int getDimension() {
                return 2;
            }
        });
        equations.setTime(0.0);
        equations.setInitialValue(new double[]{1.0, 2.0});
        
        // Create an instance of EmbeddedRungeKuttaIntegrator for testing
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{0.1}}, new double[]{0.2}, new RungeKuttaStepInterpolator(), 1.0, 2.0, 0.0001, 0.000001);
        
        // Integrate the equations
        integrator.integrate(equations, 1.0);
        
        // Verify the result
        double[] expectedState = new double[]{2.0, 3.0};
        assertArrayEquals(expectedState, equations.getCompleteState(), 0.0);
    }

    @Test
    public void testEstimateError() {
        // Create an instance of EmbeddedRungeKuttaIntegrator for testing
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{1.0}, new double[][]{{0.1}}, new double[]{0.2}, new RungeKuttaStepInterpolator(), 1.0, 2.0, 0.0001, 0.000001);
        
        // Define the variables for testing
        double[][] yDotK = new double[][]{{1.0, 2.0, 3.0}};
        double[] y2 = new double[]{2.0, 3.0, 4.0};
        double[] yTmp = new double[]{1.0, 2.0, 3.0};
        double stepSize = 0.1;
        
        // Call the method for testing
        double error = integrator.estimateError(yDotK, y2, yTmp, stepSize);
        
        // Verify the result
        assertEquals(0.0, error, 0.0);
    }
}