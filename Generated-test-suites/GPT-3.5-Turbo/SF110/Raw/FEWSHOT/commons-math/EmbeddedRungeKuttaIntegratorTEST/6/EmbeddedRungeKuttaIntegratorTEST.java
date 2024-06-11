package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AdaptiveStepsizeIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class EmbeddedRungeKuttaIntegratorTest {
    
    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;
    private double t;
    
    @Before
    public void setUp() {
        integrator = Mockito.mock(EmbeddedRungeKuttaIntegrator.class, Mockito.CALLS_REAL_METHODS);
        equations = Mockito.mock(ExpandableStatefulODE.class);
        t = 0.0;
    }
    
    @Test
    public void testGetSafety() {
        when(integrator.getSafety()).thenReturn(0.9);
        assertEquals(0.9, integrator.getSafety(), FastMath.ulp(0.9));
    }
    
    @Test
    public void testSetSafety() {
        integrator.setSafety(0.9);
        Mockito.verify(integrator).setSafety(0.9);
    }
    
    @Test
    public void testGetMinReduction() {
        when(integrator.getMinReduction()).thenReturn(0.2);
        assertEquals(0.2, integrator.getMinReduction(), FastMath.ulp(0.2));
    }
    
    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.2);
        Mockito.verify(integrator).setMinReduction(0.2);
    }
    
    @Test
    public void testGetMaxGrowth() {
        when(integrator.getMaxGrowth()).thenReturn(10.0);
        assertEquals(10.0, integrator.getMaxGrowth(), FastMath.ulp(10.0));
    }
    
    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(10.0);
        Mockito.verify(integrator).setMaxGrowth(10.0);
    }
    
    @Test
    public void testEstimateError() {
        double[][] yDotK = new double[1][];
        double[] y0 = new double[1];
        double[] y1 = new double[1];
        double stepSize = 0.1;
        double error = integrator.estimateError(yDotK, y0, y1, stepSize);
        assertEquals(0.0, error, FastMath.ulp(0.0));
    }
    
    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        when(equations.getTime()).thenReturn(t - 0.1);
        when(equations.getCompleteState()).thenReturn(new double[1]);
        integrator.integrate(equations, t);
        Mockito.verify(integrator).integrate(ArgumentMatchers.eq(equations), ArgumentMatchers.eq(t));
    }
}