import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-9;
    private EmbeddedRungeKuttaIntegrator integrator;
    private RungeKuttaStepInterpolator interpolator;
    private ExpandableStatefulODE equations;

    @Before
    public void setUp() {
        double[] c = {0.0, 0.2, 0.3, 0.6};
        double[][] a = {{0.0, 0.0, 0.0, 0.0},
                        {0.2, 0.4, 0.0, 0.0},
                        {0.3, 0.0, 0.54, 0.0},
                        {0.6, 0.2, 0.0, 0.6}};
        double[] b = {0.238283019, 0.236926825, 0.146513971, 0.0};

        integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, c, a, b,
                new RungeKuttaStepInterpolator() {
                    @Override
                    public double interpolate(double[] y, double[] yDot, double h) {
                        // implementation for testing purposes
                        return 0;
                    }

                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] y,
                                             double[] yDot, boolean isIntegratingForward,
                                             ExpandableStatefulODE equations,
                                             double[][] secondaryMappers) {
                        this.interpolator = (RungeKuttaStepInterpolator) integrator.prototype;
                    }
                }, 1.0e-10, 1.0e-8);
        interpolator = new RungeKuttaStepInterpolator();
        equations = new ExpandableStatefulODE("MyODE") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return integrator.getOrder();
            }

            @Override
            public double[] getState(double time) {
                return new double[]{Math.exp(time)};
            }

            @Override
            public void derive(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0];
            }
        };
    }

    @Test
    public void testGetOrder() {
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testIntegrate_success() throws Exception {
        double tStart = 0.0;
        double tEnd   = 1.0;
        integrator.integrate(equations, tStart);
        double tActual = equations.getTime();
        assertEquals(Precision.safeEqual(tActual, tStart + integrator.stepSize), true);
    }

    @Test
    public void testIntegrate_failure() throws Exception {
        integrator.setMinReduction(0.0);
        double tStart = 0.0;
        double tEnd   = 1.0;
        assertThrows(MathIllegalStateException.class, () -> integrator.integrate(equations, tStart));
    }

    @Test
    public void testIntegrate_invalidStepSize() throws Exception {
        integrator.setMinStep(-1.0);
        double tStart = 0.0;
        double tEnd   = 1.0;
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, tStart));
    }

    @Test
    public void testIntegrate_outOfBounds() throws Exception {
        double tStart = 1.0;
        double tEnd   = 0.0;
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, tStart));
    }

    @Test
    public void testIntegrate_outOfBounds_lastStep() throws Exception {
        double tStart = 1.0;
        double tEnd   = 1.0;
        integrator.integrate(equations, tStart);
        double tActual = equations.getTime();
        assertEquals(Precision.safeEqual(tActual, tEnd), true);
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), EPSILON);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), EPSILON);
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), EPSILON);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), EPSILON);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), EPSILON);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), EPSILON);
    }

}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

    public MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b,
                                         RungeKuttaStepInterpolator prototype,
                                         double minStep, double maxStep, double[] scalAbsoluteTolerance,
                                         double[] scalRelativeTolerance) {
        super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        // implementation for testing purposes
        return Math.abs(y1[0] - y0[0]);
    }

}