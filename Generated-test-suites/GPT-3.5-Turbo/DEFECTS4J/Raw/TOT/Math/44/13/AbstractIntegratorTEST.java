import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private double[] y;
    private double[] yDot;

    @Before
    public void setUp() {
        integrator = new DummyIntegrator();
        y = new double[3];
        yDot = new double[3];
    }

    @Test
    public void testComputeDerivatives() {
        double t = 0.0;

        try {
            integrator.computeDerivatives(t, y, yDot);
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }

        // Check that the derivatives were correctly computed
        assertArrayEquals(new double[] {0.0, 0.0, 0.0}, yDot, 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testComputeDerivativesMaxCountExceeded() {
        double t = 0.0;
        ((DummyIntegrator) integrator).setEvaluations(integrator.getMaxEvaluations());

        integrator.computeDerivatives(t, y, yDot);
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;

        integrator.setMaxEvaluations(maxEvaluations);

        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;

        integrator.setMaxEvaluations(maxEvaluations);

        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        int maxEvaluations = 100;

        integrator.setMaxEvaluations(maxEvaluations);

        assertEquals(0, integrator.getEvaluations());

        try {
            integrator.computeDerivatives(0.0, y, yDot);
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }

        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testResetEvaluations() {
        int maxEvaluations = 100;

        integrator.setMaxEvaluations(maxEvaluations);
        try {
            integrator.computeDerivatives(0.0, y, yDot);
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException should not be thrown");
        }

        integrator.resetEvaluations();

        assertEquals(0, integrator.getEvaluations());
    }

    private static class DummyIntegrator extends AbstractIntegrator {

        private int evaluations;

        private DummyIntegrator() {
            super("DummyIntegrator");
            evaluations = 0;
        }

        public void setEvaluations(int evaluations) {
            this.evaluations = evaluations;
        }

        @Override
        public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {

        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException {
            if (evaluations >= getMaxEvaluations()) {
                throw new MaxCountExceededException(getMaxEvaluations());
            }
            for (int i = 0; i < yDot.length; i++) {
                yDot[i] = 0.0;
            }
            evaluations++;
        }

    }

}