import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private double[] y0;
    private double t0;
    private double t;
    private double[] result;

    @Before
    public void setUp() {
        integrator = new ConcreteIntegrator();
        y0 = new double[] {1.0, 0.0};
        t0 = 0.0;
        t = 1.0;
        result = new double[2];
    }

    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };

        try {
            integrator.integrate(equations, t0, y0, t, result);
            Assert.assertEquals(result[0], Math.sin(t), 0.000001);
            Assert.assertEquals(result[1], -Math.cos(t), 0.000001);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetCurrentStepStart() {
        try {
            Assert.assertTrue(integrator.getCurrentStepStart().isNaN());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        try {
            Assert.assertTrue(integrator.getCurrentSignedStepsize().isNaN());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testMaxEvaluations() {
        try {
            Assert.assertEquals(integrator.getMaxEvaluations(), Integer.MAX_VALUE);
            integrator.setMaxEvaluations(100);
            Assert.assertEquals(integrator.getMaxEvaluations(), 100);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetEvaluations() {
        try {
            Assert.assertEquals(integrator.getEvaluations(), 0);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private class ConcreteIntegrator extends AbstractIntegrator {
        public void integrate(ExpandableStatefulODE equations, double t) {
            // Do nothing (mock method for testing)
        }
    }

}