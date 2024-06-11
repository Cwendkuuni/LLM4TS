import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.VectorField;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     *
     * @throws MathIllegalArgumentException if the step size is negative
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorConstructor() {
        double[] c = {0.0, 0.23692688505667133, 0.6794921611268735};
        double[][] a = {{0.0, 0.23692688505667133}, {0.6794921611268735, 0.8680044473523534}, {1.0, 0.0}};
        double[] b = {0.23692688505667133, 0.6794921611268735, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 1.0, 1.0e-12, 1.0e-12);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, c, a, b, prototype, 1.0e-12, 1.0e12, null, null);
        assertEquals("RK3", integrator.getName());
        assertFalse(integrator.isFsal());
        assertArrayEquals(c, integrator.getC(), 0.0);
        assertArrayEquals(a, integrator.getA(), 0.0);
        assertArrayEquals(b, integrator.getB(), 0.0);
        assertSame(prototype, integrator.getPrototype());
        assertEquals(1.0 / 3.0, integrator.getExp(), 0.0);
        assertEquals(0.9, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double[], double[])}.
     *
     * @throws MathIllegalArgumentException if the step size is negative
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorConstructorWithTolerances() {
        double[] c = {0.0, 0.23692688505667133, 0.6794921611268735};
        double[][] a = {{0.0, 0.23692688505667133}, {0.6794921611268735, 0.8680044473523534}, {1.0, 0.0}};
        double[] b = {0.23692688505667133, 0.6794921611268735, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 1.0, 1.0e-12, 1.0e-12);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, c, a, b, prototype, 1.0e-12, 1.0e12, new double[]{1.0e-12, 1.0e-12}, new double[]{1.0e-12, 1.0e-12});
        assertEquals("RK3", integrator.getName());
        assertFalse(integrator.isFsal());
        assertArrayEquals(c, integrator.getC(), 0.0);
        assertArrayEquals(a, integrator.getA(), 0.0);
        assertArrayEquals(b, integrator.getB(), 0.0);
        assertSame(prototype, integrator.getPrototype());
        assertEquals(1.0 / 3.0, integrator.getExp(), 0.0);
        assertEquals(0.9, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
        assertArrayEquals(new double[]{1.0e-12, 1.0e-12}, integrator.getAbsoluteTolerance(), 0.0);
        assertArrayEquals(new double[]{1.0e-12, 1.0e-12}, integrator.getRelativeTolerance(), 0.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#getOrder()}.
     */
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, null, null, null, null, 1.0, 1.0, null, null);
        assertEquals(3, integrator.getOrder());

        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, null, null, null, null, 1.0, 1.0, null, null);
        assertEquals(4, integrator.getOrder());
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#getSafety()}.
     */
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, null, null, null, null, 1.0, 1.0, null, null);
        assertEquals(0.9, integrator.getSafety(), 0.0);

        integrator.setSafety(0.7);
        assertEquals(0.7, integrator.getSafety(), 0.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#setSafety(double)}.
     */
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, null, null, null, null, 1.0, 1.0, null, null);
        integrator.setSafety(0.7);
        assertEquals(0.7, integrator.getSafety(), 0.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#getMinReduction()}.
     */
    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, null, null, null, null, 1.0, 1.0, null, null);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);

        integrator.setMinReduction(0.5);
        assertEquals(0.5, integrator.getMinReduction(), 0.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#setMinReduction(double)}.
     */
    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, null, null, null, null, 1.0, 1.0, null, null);
        integrator.setMinReduction(0.5);
        assertEquals(0.5, integrator.getMinReduction(), 0.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#getMaxGrowth()}.
     */
    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, null, null, null, null, 1.0, 1.0, null, null);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);

        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#setMaxGrowth(double)}.
     */
    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, null, null, null, null, 1.0, 1.0, null, null);
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrate() throws MathIllegalArgumentException {
        int numberOfVariables = 1;
        ExpandableStatefulODE equations = new ExpandableStatefulODE(numberOfVariables, new VectorField() {
            @Override
            public int getDimension() {
                return numberOfVariables;
            }

            @Override
            public void evaluateDerivative(double t, double[] y, double[] yDot) {
                double r = Math.exp(-5.0 * t);
                yDot[0] = r * Math.sin(y[0]);
            }
        });

        double initialTime = 0.0;
        double finalTime = 2.0 * Math.PI;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, null, null, null, null, 1.0, 1.0, null, null);

        double[] initialState = new double[]{1.0};
        integrator.integrate(equations, initialTime, finalTime, initialState, null);

        double[] expectedFinalState = new double[]{Math.sin(2.0 * Math.PI)};
        assertArrayEquals(expectedFinalState, equations.getCompleteState(), 0.0);

        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, null, null, null, null, 1.0, 1.0, null, null);
        integrator.integrate(equations, initialTime, finalTime, initialState, null);
        assertArrayEquals(expectedFinalState, equations.getCompleteState(), 0.0);

        integrator = new EmbeddedRungeKuttaIntegrator("RKF45", true, null, null, null, null, 1.0, 1.0, null, null);
        integrator.integrate(equations, initialTime, finalTime, initialState, null);
        assertArrayEquals(expectedFinalState, equations.getCompleteState(), 0.0);
    }

}