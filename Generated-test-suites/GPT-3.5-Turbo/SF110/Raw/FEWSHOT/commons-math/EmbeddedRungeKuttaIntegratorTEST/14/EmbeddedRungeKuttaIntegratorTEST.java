
package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testConstructor() {
        String name = "Test";
        boolean fsal = true;
        double[] c = {1.0, 2.0};
        double[][] a = {{1.0, 2.0}, {3.0, 4.0}};
        double[] b = {1.0, 2.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(null, true, null, null, null);
        double minStep = 0.1;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        assertEquals(name, integrator.getName());
        assertEquals(fsal, integrator.fsal);
        assertEquals(c, integrator.c);
        assertEquals(a, integrator.a);
        assertEquals(b, integrator.b);
        assertEquals(prototype, integrator.prototype);
        assertEquals(-1.0 / integrator.getOrder(), integrator.exp, 0.0);
        assertEquals(0.9, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
        assertEquals(minStep, integrator.getMinStep(), 0.0);
        assertEquals(maxStep, integrator.getMaxStep(), 0.0);
        assertEquals(scalAbsoluteTolerance, integrator.getScalAbsoluteTolerance(), 0.0);
        assertEquals(scalRelativeTolerance, integrator.getScalRelativeTolerance(), 0.0);
        assertNull(integrator.getVecAbsoluteTolerance());
        assertNull(integrator.getVecRelativeTolerance());
    }

    @Test
    public void testConstructorWithVectorTolerance() {
        String name = "Test";
        boolean fsal = true;
        double[] c = {1.0, 2.0};
        double[][] a = {{1.0, 2.0}, {3.0, 4.0}};
        double[] b = {1.0, 2.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(null, true, null, null, null);
        double minStep = 0.1;
        double maxStep = 1.0;
        double[] vecAbsoluteTolerance = {1e-6, 1e-6};
        double[] vecRelativeTolerance = {1e-6, 1e-6};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);

        assertEquals(name, integrator.getName());
        assertEquals(fsal, integrator.fsal);
        assertEquals(c, integrator.c);
        assertEquals(a, integrator.a);
        assertEquals(b, integrator.b);
        assertEquals(prototype, integrator.prototype);
        assertEquals(-1.0 / integrator.getOrder(), integrator.exp, 0.0);
        assertEquals(0.9, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
        assertEquals(minStep, integrator.getMinStep(), 0.0);
        assertEquals(maxStep, integrator.getMaxStep(), 0.0);
        assertEquals(0.0, integrator.getScalAbsoluteTolerance(), 0.0);
        assertEquals(0.0, integrator.getScalRelativeTolerance(), 0.0);
        assertEquals(vecAbsoluteTolerance, integrator.getVecAbsoluteTolerance());
        assertEquals(vecRelativeTolerance, integrator.getVecRelativeTolerance());
    }

    @Test
    public void testGetSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl("", true, new double[0], new double[0][0], new double[0], null, 0.0, 0.0, 0.0, 0.0);

        assertEquals(0.9, integrator.getSafety(), 0.0);
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl("", true, new double[0], new double[0][0], new double[0], null, 0.0, 0.0, 0.0, 0.0);

        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl("", true, new double[0], new double[0][0], new double[0], null, 0.0, 0.0, 0.0, 0.0);

        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testConstructors() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl("", true, new double[0], new double[0][0], new double[0], null, 0.0, 0.0, 0.0, 0.0);

        assertNotNull(integrator.getName());
    }

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl("", true, new double[0], new double[0][0], new double[0], null, 0.0, 0.0, 0.0, 0.0);

        assertEquals(0, integrator.getOrder());
    }

    @Test
    public void testIntegrate() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl("", true, new double[0], new double[0][0], new double[0], null, 0.0, 0.0, 0.0, 0.0);
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DimensionMismatchException {
                // implementation...
            }

            public int getDimension() {
                return 0;
            }
        });

        try {
            double t = 0.0;
            integrator.integrate(equations, t);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
