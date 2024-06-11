import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.001);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }

    @Test
    public void testIntegrate() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestFirstOrderDifferentialEquations());

        double t = 0.0;
        double[] y0 = new double[]{1.0};
        double[] y2 = new double[]{2.0};

        try {
            integrator.integrate(equations, t);
            double[] state = equations.getCompleteState();
            assertArrayEquals(y2, state, 0.001);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testEstimateError() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double[][] yDotK = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        double[] y2 = new double[]{1.0, 2.0, 3.0};
        double[] yTmp = new double[]{0.0, 0.0, 0.0};
        double stepSize = 0.1;
        double error = integrator.estimateError(yDotK, y2, yTmp, stepSize);
        assertEquals(0.0, error, 0.001);
    }
}

class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

    public TestEmbeddedRungeKuttaIntegrator() {
        super("Test", true, new double[]{0.0}, new double[][]{{0.1}}, new double[]{0.2}, new TestRungeKuttaStepInterpolator(), 0.01, 0.1, 0.001, 0.01);
    }

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
        return 0.0;
    }
}

class TestRungeKuttaStepInterpolator extends RungeKuttaStepInterpolator {

    @Override
    public StepInterpolator copy() {
        return new TestRungeKuttaStepInterpolator();
    }
}

class TestFirstOrderDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = 1.0;
    }
}