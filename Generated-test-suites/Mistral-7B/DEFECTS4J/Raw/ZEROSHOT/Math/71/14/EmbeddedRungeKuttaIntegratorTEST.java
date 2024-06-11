import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultCombinedEventManager;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Test constructor with absolute tolerances
        double[] c = {0.0, 0.25, 0.5, 0.875, 1.0};
        double[][] a = {{0.0, 0.0, 0.0, 0.0, 0.0},
                        {0.25, 0.69230588855, 0.25, 0.0, 0.0},
                        {0.5, 0.87876455325, 0.25, 0.69230588855, 0.25},
                        {0.875, 0.92380952376, 0.5, 0.87876455325, 0.25},
                        {1.0, 1.0, 0.875, 0.92380952376, 0.875}};
        double[] b = {0.0, 0.25, 0.5, 0.875, 1.0};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, 1e-9, 1e-6);
        assertEquals(5, integrator.getOrder());

        // Test constructor with relative tolerances
        double[] vecRelativeTolerance = {1e-5, 1e-3, 1e-4, 1e-6, 1e-7};
        integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, vecRelativeTolerance, vecRelativeTolerance);
        assertEquals(5, integrator.getOrder());

        // Test getSafety()
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 1e-6);

        // Test getMinReduction()
        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 1e-6);

        // Test getMaxGrowth()
        integrator.setMaxGrowth(2.5);
        assertEquals(2.5, integrator.getMaxGrowth(), 1e-6);

        // Test integrate() with exact derivatives
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {3.0, 4.0};
        double result = integrator.integrate(equations, t0, y0, t1, y1);
        assertEquals(t1, result, 1e-6);

        // Test integrate() with approximate derivatives
        double[] yDot0 = new double[y0.length];
        double[] yDot1 = new double[y0.length];
        for (int i = 0; i < y0.length; i++) {
            yDot0[i] = equations.getValue(t0, y0)[i];
            yDot1[i] = equations.getValue(t1, y1)[i];
        }
        double error = integrator.estimateError(yDot0, y0, y1, integrator.stepSize);
        assertTrue(error > 1.0);

        // Test integration with events
        CombinedEventsManager manager = new DefaultCombinedEventManager();
        manager.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandler event, int type) {
                if (type == 1) {
                    fail("An event of type 1 should not be reached");
                }
            }
        });
        integrator.addStepHandler(event -> {
            if (event.getEventTime() == 0.5) {
                assertEquals(0.5, event.getStepNumber(), 1);
                assertEquals(0.25, integrator.getStepSize(), 1e-6);
            }
        });
        integrator.integrate(equations, 0.0, y0, 1.0, y1);
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public double[] value(double t, double[] y) {
        double[] result = new double[y.length];
        for (int i = 0; i < y.length; i++) {
            result[i] = y[i] * (1 - y[i]);
        }
        return result;
    }

    @Override
    public int getDimension() {
        return y.length;
    }

}