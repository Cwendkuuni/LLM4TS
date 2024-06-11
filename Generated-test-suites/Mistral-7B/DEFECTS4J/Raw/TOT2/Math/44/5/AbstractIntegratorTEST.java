import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;

    @Before
    public void setup() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // Override this method to make integration a no-op
            }
        };
        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };
        integrator.setEquations(equations);
    }

    @Test
    public void testBasicIntegration() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = new double[]{1.0};
        final double tEnd = 1.0;
        double t = integrator.integrate(equations, t0, y0, tEnd);
        assertArrayEquals(new double[]{1.1}, y0);
        assertEquals(tEnd, t, 1e-10);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidInitialValues() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = new double[]{0.0};
        final double[] y = new double[]{1.0};
        final double tEnd = 1.0;
        integrator.integrate(equations, t0, y0, tEnd, y);
        fail("Exception should have been thrown");
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidTargetValues() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = new double[]{1.0};
        final double tEnd = 1.0;
        final double[] y = new double[]{2.0};
        integrator.integrate(equations, t0, y0, tEnd, y);
        fail("Exception should have been thrown");
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFinishedIntegration() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = new double[]{1.0};
        final double tEnd = 1.0;
        integrator.integrate(equations, t0, y0, tEnd);
        integrator.integrate(equations, t0, y0, tEnd);
        fail("Integration should not have been possible");
    }

    @Test
    public void testEventHandling() throws Exception {
        integrator.addEventHandler(event -> {
            if (event.getIndex() == 0) {
                throw new MathIllegalStateException("Test event triggered");
            }
        }, 0.1, 1e-6, 10);

        final double t0 = 0.0;
        final double[] y0 = new double[]{1.0};
        final double tEnd = 1.1;
        integrator.integrate(equations, t0, y0, tEnd);
        assertTrue(integrator.isLastStep());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testEventReset() throws Exception {
        integrator.addEventHandler(event -> {
            if (event.getIndex() == 0) {
                event.reset(event.getTime(), event.getState());
            }
        }, 0.1, 1e-6, 10);

        final double t0 = 0.0;
        final double[] y0 = new double[]{1.0};
        final double tEnd = 1.1;
        integrator.integrate(equations, t0, y0, tEnd);
        assertTrue(integrator.resetOccurred());
        fail("Exception should have been thrown");
    }

    @Test
    public void testAddStepHandlers() throws Exception {
        integrator.addStepHandler(step -> {
            step.handleStep(null, true);
        });

        final double t0 = 0.0;
        final double[] y0 = new double[]{1.0};
        final double tEnd = 1.0;
        integrator.integrate(equations, t0, y0, tEnd);
        assertTrue(integrator.isLastStep());
    }

    @Test
    public void testClearStepHandlers() throws Exception {
        integrator.addStepHandler(step -> {
            step.handleStep(null, true);
        });
        integrator.clearStepHandlers();

        final double t0 = 0.0;
        final double[] y0 = new double[]{1.0};
        final double tEnd = 1.0;
        integrator.integrate(equations, t0, y0, tEnd);
        assertFalse(integrator.isLastStep());
    }

    @Test
    public void testMaxEvaluations() throws Exception {
        integrator.setMaxEvaluations(10);
        final double t0 = 0.0;
        final double[] y0 = new double[]{1.0};
        final double tEnd = 1.0;
        integrator.integrate(equations, t0, y0, tEnd);
        assertEquals(10, integrator.getEvaluations());

        try {
            integrator.integrate(equations, t0, y0, tEnd);
            fail("Exception should have been thrown");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test
    public void testResetEvaluations() throws Exception {
        integrator.integrate(equations, t0, y0, tEnd);
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

}