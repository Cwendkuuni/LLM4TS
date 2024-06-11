import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private RungeKuttaStepInterpolator interpolator;
    private double[] y0 = new double[] {0.0, 0.0};

    @Before
    public void setUp() {
        equations = mock(FirstOrderDifferentialEquations.class);
        interpolator = new RungeKuttaStepInterpolator(1, 0.0, Double.POSITIVE_INFINITY, null);
        integrator = new MyEmbeddedRungeKuttaIntegrator("testName", false, new double[] {0.5, 0.5},
                new double[][] {{0.0, 0.5}, {0.5, 0.0}}, new double[] {0.5, 0.5}, interpolator, 0.01, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.1;
            }
        };
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);
    }

    @Test
    public void testIntegration() throws Exception {
        doReturn(new double[] {1.0, 1.0}).when(equations).derive(anyDouble(), anyArray());
        double tEnd = 2.0;
        double[] yEnd = new double[] {2.0, 2.0};
        integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        verify(equations, times(integrator.getOrder() + 1)).derive(anyDouble(), anyArray());
        Assert.assertArrayEquals(yEnd, integrator.getY(), 0.0);
        Assert.assertEquals(tEnd, integrator.getStep(), 0.0);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrationRejection() throws Exception {
        doReturn(new double[] {1.0, 1.0}).when(equations).derive(anyDouble(), anyArray());
        double tEnd = 2.0;
        integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        verify(equations, times(integrator.getOrder() + 1)).derive(anyDouble(), anyArray());
    }

    @Test
    public void testIntegrationWithEvents() throws Exception {
        doReturn(new double[] {1.0, 1.0}).when(equations).derive(anyDouble(), anyArray());
        double tEnd = 2.0;
        double eventTime = 1.5;
        double[] yEnd = new double[] {2.0, 2.0};
        integrator.addEvent(new MockEvent() {
            @Override
            public double getAbsoluteTime() {
                return eventTime;
            }

            @Override
            public int getId() {
                return 1;
            }

            @Override
            public void evaluateStep(double h, double[] y, double[] yDot, double[] eventData, int id) {
                Assert.assertEquals(1.0, y[0], 0.0);
                Assert.assertEquals(1.0, y[1], 0.0);
            }
        });
        integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        verify(equations, times(integrator.getOrder() + 1)).derive(anyDouble(), anyArray());
        Assert.assertArrayEquals(yEnd, integrator.getY(), 0.0);
        Assert.assertEquals(tEnd, integrator.getStep(), 0.0);
        verify(equations).addEvent(any(Event.class), anyDouble(), anyInt());
    }

}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

}