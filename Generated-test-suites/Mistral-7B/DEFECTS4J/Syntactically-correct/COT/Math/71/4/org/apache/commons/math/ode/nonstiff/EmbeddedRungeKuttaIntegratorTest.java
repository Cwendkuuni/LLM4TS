package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private RungeKuttaStepInterpolator stepInterpolator;
    private double[] y0 = {0.0, 0.0};

    @Before
    public void setup() {
        equations = mock(FirstOrderDifferentialEquations.class);
        stepInterpolator = new RungeKuttaStepInterpolator(1, 1.0);
        integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[] {1.0},
                new double[][] {{0.3, 0.4}, {0.5, 0.5}}, new double[] {0.5}, stepInterpolator,
                0.01, 0.02, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.1 * Math.abs(y1[0] - y0[0]);
            }
        };
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);
    }

    @Test
    public void testIntegrateBasic() throws Exception {
        doReturn(new double[] {1.0, 1.0}).when(equations).derive(anyDouble(), anyArray());

        double t = 1.0;
        double[] y = integrator.integrate(equations, 0.0, y0, t, new double[y0.length]);

        Assert.assertArrayEquals("Integrated values do not match.", y0, y);
        verify(equations).derive(anyDouble(), anyArray());
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateDerivativeException() throws Exception {
        doThrow(new DerivativeException("Test exception")).when(equations).derive(anyDouble(), anyArray());

        integrator.integrate(equations, 0.0, y0, 1.0, new double[y0.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateIntegratorException() throws Exception {
        doThrow(new IntegratorException("Test exception")).when(equations).derive(anyDouble(), anyArray());

        integrator.integrate(equations, 0.0, y0, 1.0, new double[y0.length]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxCountExceededException() throws Exception {
        doThrow(new MaxCountExceededException("Test exception")).when(equations).derive(anyDouble(), anyArray());

        integrator.integrate(equations, 0.0, y0, 1.0, new double[y0.length]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateNumberIsTooSmallException() throws Exception {
        doThrow(new NumberIsTooSmallException("Test exception")).when(equations).derive(anyDouble(), anyArray());

        integrator.integrate(equations, 0.0, y0, 0.0, new double[y0.length]);
    }

    @Test
    public void testIntegrateEvents() throws Exception {
        doReturn(new double[] {1.0, 1.0}).when(equations).derive(anyDouble(), anyArray());
        doReturn(true).when(equations).hasEvents(anyDouble(), anyArray());
        doAnswer(invocation -> ((Event) invocation.getArgument(0)).trigger(integrator)).when(equations).getEvent(anyDouble(), anyArray());

        double step = 1.0;
        integrator.integrate(equations, 0.0, y0, step, new double[y0.length]);

        verify(equations, times(2)).derive(anyDouble(), anyArray());
        verify(equations, times(1)).getEvent(anyDouble(), anyArray());
    }

    @Test
    public void testIntegrateStepSizeControl() throws Exception {
        doReturn(new double[] {1.0, 1.0}).when(equations).derive(anyDouble(), anyArray());

        double t = 5.0;
        double[] y = integrator.integrate(equations, 0.0, y0, t, new double[y0.length]);

        Assert.assertArrayEquals("Integrated values do not match.", y0, y);

        verify(stepInterpolator, times(2)).shift();
        verify(stepInterpolator, times(1)).storeTime(eq(0.0));
        verify(stepInterpolator, times(1)).storeTime(eq(t));
    }

    @Test
    public void testIntegrateStepSizeControlError() throws Exception {
        doReturn(new double[] {1.0, 1.0}).when(equations).derive(anyDouble(), anyArray());

        integrator.setMinReduction(0.0);

        double t = 5.0;
        double[] y = integrator.integrate(equations, 0.0, y0, t, new double[y0.length]);

        Assert.assertArrayEquals("Integrated values do not match.", y0, y);

        verify(stepInterpolator, times(1)).shift();
        verify(stepInterpolator, never()).storeTime(anyDouble());
    }

    @Test
    public void testIntegrateStepSizeControlMaxGrowth() throws Exception {
        doReturn(new double[] {1.0, 1.0}).when(equations).derive(anyDouble(), anyArray());

        integrator.setMaxGrowth(1.1);

        double t = 5.0;
        double[] y = integrator.integrate(equations, 0.0, y0, t, new double[y0.length]);

        Assert.assertArrayEquals("Integrated values do not match.", y0, y);

        verify(stepInterpolator, times(1)).shift();
        verify(stepInterpolator, never()).storeTime(anyDouble());
    }
}