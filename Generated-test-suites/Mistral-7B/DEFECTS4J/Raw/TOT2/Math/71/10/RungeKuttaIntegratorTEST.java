import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.LagrangesInterpolator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setup() {
        integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        equations = new SimpleDifferentialEquations();
    }

    /**
     * Test the integration of a simple differential equation with no events.
     */
    @Test
    public void testIntegrationWithNoEvents() {
        double tStart = 0.0;
        double[] yStart = {0.0};
        double tEnd = 2.0;

        double result = integrator.integrate(equations, tStart, yStart, tEnd, new double[yStart.length]);
        Assert.assertEquals("The result of integration is incorrect.", tEnd, result, 1e-10);
    }

    /**
     * Test the integration of a simple differential equation with an event.
     */
    @Test
    public void testIntegrationWithEvent() {
        double tStart = 0.0;
        double[] yStart = {0.0};
        double tEnd = 2.0;

        EventManager eventManager = new EventManager();
        eventManager.addEvent(new DefaultValueEvent(1.0, 1.5, new LagrangesInterpolator(3), null));

        integrator.setEventsManager(eventManager);

        double[] y = new double[yStart.length];
        double result = integrator.integrate(equations, tStart, yStart, tEnd, y);
        Assert.assertEquals("The result of integration is incorrect.", tEnd, result, 1e-10);
        Assert.assertArrayEquals("The final state is incorrect.", new double[]{1.5}, y, 1e-10);

        // check that the event was triggered
        Assert.assertEquals("The number of events triggered is incorrect.", 1, eventManager.size());
    }

    /**
     * Test the integration of a simple differential equation with an event that cannot be reached.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testIntegrationWithUnreachableEvent() {
        double tStart = 0.0;
        double[] yStart = {0.0};
        double tEnd = 2.0;

        EventManager eventManager = new EventManager();
        eventManager.addEvent(new DefaultValueEvent(1.5, 1.5, new LagrangesInterpolator(3), null));

        integrator.setEventsManager(eventManager);

        double[] y = new double[yStart.length];
        integrator.integrate(equations, tStart, yStart, tEnd, y);
    }

}