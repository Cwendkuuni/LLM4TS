import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate_givenValidArguments_returnsCorrectResult() {
        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] y0 = {0.0};
        double t0 = 0.0;
        double[] y = Arrays.copyOf(y0, y0.length);
        double t = 1.0;
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);

        // When
        double result = integrator.integrate(equations, t0, y0, t, y);

        // Then
        Assert.assertEquals(1.0, result, 0.01);
        Assert.assertArrayEquals(new double[]{1.0}, y, 0.01);
    }

    @Test(expected = DeriveException.class)
    public void testIntegrate_givenInvalidArguments_throwsDeriveException() {
        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] y0 = {0.0};
        double t0 = 0.0;
        double[] y = Arrays.copyOf(y0, y0.length);
        double t = 1.0;
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        when(equations.derive(y0)).thenThrow(new DeriveException("Test error"));

        // When & Then
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrate_givenEndTimeEvent_throwsIntegratorException() {
        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] y0 = {0.0};
        double t0 = 0.0;
        double[] y = Arrays.copyOf(y0, y0.length);
        double t = 1.0;
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        Event event = mock(Event.class);
        when(equations.getEvent(t)).thenReturn(event);
        when(event.isRegularEvent()).thenReturn(false);

        // When & Then
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test
    public void testIntegrate_givenMaxStepSizeReached_throwsMaxCountExceededException() {
        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] y0 = {0.0};
        double t0 = 0.0;
        double[] y = Arrays.copyOf(y0, y0.length);
        double t = 1.0;
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        when(equations.getMaxStepSize()).thenReturn(0.05);

        // When & Then
        Assert.assertThrows(MaxCountExceededException.class, () -> {
            integrator.integrate(equations, t0, y0, t, y);
        });
    }

    @Test
    public void testIntegrate_givenInvalidStepSize_throwsNumberIsTooSmallException() {
        // Given
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] y0 = {0.0};
        double t0 = 0.0;
        double[] y = Arrays.copyOf(y0, y0.length);
        double t = 1.0;
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.0001);

        // When & Then
        Assert.assertThrows(NumberIsTooSmallException.class, () -> {
            integrator.integrate(equations, t0, y0, t, y);
        });
    }

}