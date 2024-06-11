import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        int expectedOrder = 5;
        int actualOrder = integrator.getOrder();
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expectedSafety = 0.9;
        double actualSafety = integrator.getSafety();
        assertEquals(expectedSafety, actualSafety, 0.0001);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double newSafety = 0.8;
        integrator.setSafety(newSafety);
        double actualSafety = integrator.getSafety();
        assertEquals(newSafety, actualSafety, 0.0001);
    }

    @Test
    public void testIntegrate() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        equations.setCompleteState(new double[]{0.0, 1.0});
        double t = 1.0;
        try {
            integrator.integrate(equations, t);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        double expectedTime = t;
        double actualTime = equations.getTime();
        assertEquals(expectedTime, actualTime, 0.0001);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expectedMinReduction = 0.2;
        double actualMinReduction = integrator.getMinReduction();
        assertEquals(expectedMinReduction, actualMinReduction, 0.0001);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double newMinReduction = 0.3;
        integrator.setMinReduction(newMinReduction);
        double actualMinReduction = integrator.getMinReduction();
        assertEquals(newMinReduction, actualMinReduction, 0.0001);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expectedMaxGrowth = 10.0;
        double actualMaxGrowth = integrator.getMaxGrowth();
        assertEquals(expectedMaxGrowth, actualMaxGrowth, 0.0001);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double newMaxGrowth = 20.0;
        integrator.setMaxGrowth(newMaxGrowth);
        double actualMaxGrowth = integrator.getMaxGrowth();
        assertEquals(newMaxGrowth, actualMaxGrowth, 0.0001);
    }

    @Test
    public void testEstimateError() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double[][] yDotK = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] y2 = {7.0, 8.0};
        double[] yTmp = {9.0, 10.0};
        double stepSize = 0.1;
        double expectedError = 42.0;
        double actualError = integrator.estimateError(yDotK, y2, yTmp, stepSize);
        assertEquals(expectedError, actualError, 0.0001);
    }

    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestEmbeddedRungeKuttaIntegrator() {
            super("testIntegrator", true, null, null, null, null, 0.1, 1.0, 1e-6, 1e-6);
        }

        @Override
        public int getOrder() {
            return 5;
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y2, double[] yTmp, double stepSize) {
            return 42.0;
        }
    }
}