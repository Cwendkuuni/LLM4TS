import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegrator("Test Integration");
        assertEquals("Test Integration", abstractIntegrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegrator();
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {

            }
        };
        abstractIntegrator.addStepHandler(stepHandler);
        assertTrue(abstractIntegrator.getStepHandlers().contains(stepHandler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegrator();
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {

            }
        };
        abstractIntegrator.addStepHandler(stepHandler);

        abstractIntegrator.clearStepHandlers();
        assertFalse(abstractIntegrator.getStepHandlers().contains(stepHandler));
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegrator();
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }

            @Override
            public double g(double t, double[] y) {
                return 0;
            }

            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return null;
            }

            @Override
            public void resetState(double t, double[] y) {

            }
        };
        abstractIntegrator.addEventHandler(
                eventHandler,
                0.1,
                0.01,
                100
        );

        assertTrue(abstractIntegrator.getEventHandlers().contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegrator();
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {

            }

            @Override
            public double g(double t, double[] y) {
                return 0;
            }

            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return null;
            }

            @Override
            public void resetState(double t, double[] y) {

            }
        };
        abstractIntegrator.addEventHandler(
                eventHandler,
                0.1,
                0.01,
                100
        );

        abstractIntegrator.clearEventHandlers();
        assertFalse(abstractIntegrator.getEventHandlers().contains(eventHandler));
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegrator();
        abstractIntegrator.setMaxEvaluations(100);
        assertEquals(100, abstractIntegrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator abstractIntegrator = new AbstractIntegrator();
        assertEquals(0, abstractIntegrator.getEvaluations());

        double[] y = new double[10];
        double[] yDot = new double[10];
        abstractIntegrator.computeDerivatives(0, y, yDot);
        assertEquals(1, abstractIntegrator.getEvaluations());
    }
}