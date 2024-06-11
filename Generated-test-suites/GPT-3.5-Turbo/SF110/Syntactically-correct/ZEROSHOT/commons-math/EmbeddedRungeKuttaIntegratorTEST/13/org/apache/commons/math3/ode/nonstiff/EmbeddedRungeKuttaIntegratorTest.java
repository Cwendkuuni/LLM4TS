import org.junit.*;
import org.apache.commons.math3.ode.AdaptiveStepsizeIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }

            @Override
            protected void computeDerivatives(double v, double[] doubles, double[] doubles1) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
                // empty implementation
            }
        };

        int order = integrator.getOrder();
        Assert.assertEquals(0, order);
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        double safety = integrator.getSafety();
        Assert.assertEquals(0.9, safety, 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        integrator.setSafety(0.5);
        double safety = integrator.getSafety();
        Assert.assertEquals(0.5, safety, 0.0);
    }

    @Test
    public void testIntegrate() throws NoBracketingException, MaxCountExceededException, DimensionMismatchException, NumberIsTooSmallException {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
                return 0.0;
            }

            @Override
            protected void computeDerivatives(double v, double[] doubles, double[] doubles1) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
                // empty implementation
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        double t = 0.0;
        integrator.integrate(equations, t);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        double minReduction = integrator.getMinReduction();
        Assert.assertEquals(0.2, minReduction, 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        integrator.setMinReduction(0.3);
        double minReduction = integrator.getMinReduction();
        Assert.assertEquals(0.3, minReduction, 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        double maxGrowth = integrator.getMaxGrowth();
        Assert.assertEquals(10.0, maxGrowth, 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        integrator.setMaxGrowth(20.0);
        double maxGrowth = integrator.getMaxGrowth();
        Assert.assertEquals(20.0, maxGrowth, 0.0);
    }

    @Test
    public void testEstimateError() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            protected void computeDerivatives(double v, double[] doubles, double[] doubles1) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
                // empty implementation
            }
        };

        double[][] p0 = new double[1][1];
        double[] p1 = new double[1];
        double[] p2 = new double[1];
        double p3 = 0.0;

        double error = integrator.estimateError(p0, p1, p2, p3);
        Assert.assertEquals(0.0, error, 0.0);
    }

}