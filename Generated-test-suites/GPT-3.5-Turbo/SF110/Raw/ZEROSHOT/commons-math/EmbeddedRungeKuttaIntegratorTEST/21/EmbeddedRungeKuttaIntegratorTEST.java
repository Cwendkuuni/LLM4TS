import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        double[] c = {0.0, 0.0, 0.0};
        double[][] a = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
        double[] b = {0.0, 0.0, 0.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double minStep = 0.0;
        double maxStep = 0.0;
        double scalAbsoluteTolerance = 0.0;
        double scalRelativeTolerance = 0.0;

        integrator = new EmbeddedRungeKuttaIntegratorImpl("testIntegrator", true, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Test
    public void testGetOrder() {
        int expected = 0;
        int actual = integrator.getOrder();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSafety() {
        double expected = 0.9;
        double actual = integrator.getSafety();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testSetSafety() {
        double safety = 0.8;
        integrator.setSafety(safety);
        double expected = safety;
        double actual = integrator.getSafety();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testIntegrate() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        double t = 0.0;
        try {
            integrator.integrate(equations, t);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetMinReduction() {
        double expected = 0.2;
        double actual = integrator.getMinReduction();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testSetMinReduction() {
        double minReduction = 0.1;
        integrator.setMinReduction(minReduction);
        double expected = minReduction;
        double actual = integrator.getMinReduction();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        double expected = 10.0;
        double actual = integrator.getMaxGrowth();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        double maxGrowth = 20.0;
        integrator.setMaxGrowth(maxGrowth);
        double expected = maxGrowth;
        double actual = integrator.getMaxGrowth();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testEstimateError() {
        double[][] p0 = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] p1 = {1.0, 2.0, 3.0};
        double[] p2 = {1.0, 2.0, 3.0};
        double p3 = 1.0;
        double expected = 0.0;
        double actual = integrator.estimateError(p0, p1, p2, p3);
        assertEquals(expected, actual, 0.0);
    }
}