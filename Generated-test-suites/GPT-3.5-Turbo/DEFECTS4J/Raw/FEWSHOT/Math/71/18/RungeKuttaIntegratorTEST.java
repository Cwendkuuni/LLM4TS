
package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

	private RungeKuttaIntegrator integrator;
	private FirstOrderDifferentialEquations equations;
	private CombinedEventsManager eventsHandlersManager;
	private AbstractStepInterpolator prototype;
	private StepHandler stepHandler;
	
	private double t0;
	private double[] y0;
	private double t;
	private double[] y;
	
	@Before
	public void setUp() {
		double[] c = {};
		double[][] a = {};
		double[] b = {};
		prototype = mock(AbstractStepInterpolator.class);
		stepHandler = mock(StepHandler.class);
		integrator = new RungeKuttaIntegrator("name", c, a, b, prototype, 1.0);
		equations = mock(FirstOrderDifferentialEquations.class);
		eventsHandlersManager = mock(CombinedEventsManager.class);
		
		t0 = 0.0;
		y0 = new double[] {1.0, 2.0};
		t = 1.0;
		y = new double[y0.length];
	}
	
	@Test
	public void testIntegrate() throws DerivativeException, IntegratorException {
		when(equations.getDimension()).thenReturn(y0.length);
		injectMocks();
		double result = integrator.integrate(equations, t0, y0, t, y);
		assertEquals(1.0, result, 1e-10);
		verify(interpolator, times(1)).reinitialize(any(), any(), any(), anyBoolean());
		verify(eventsHandlersManager, times(1)).isEmpty();
		verify(interpolator, times(1)).storeTime(t0);
		verify(equations, times(1)).getDimension();
		verify(stepHandler, times(1)).reset();
		verify(manager, times(1)).evaluateStep(interpolator);
		verify(interpolator, times(1)).shift();
		verify(computer, times(1)).computeDerivatives(stepStart, y, yDotK[0]);
		verify(computer, times(1)).computeDerivatives(stepStart + c[k-1] * stepSize, yTmp, yDotK[k]);
		verify(manager, times(2)).getEventTime();
		verify(interpolator, times(1)).storeTime(stepStart + stepSize);
		verify(manager, times(1)).stop();
		verify(computer, times(1)).computeDerivatives(stepStart, y, yDotK[0]);
		verify(interpolator, times(1)).storeTime(nextStep);
		verify(stepHandler, times(1)).handleStep(interpolator, lastStep);
		verify(manager, times(1)).reset(stepStart, y);
		verify(computer, times(1)).computeDerivatives(stepStart, y, yDotK[0]);
	}
	
	private void injectMocks() {
		integrator.stepHandlers.add(stepHandler);
		integrator.eventsHandlersManager = eventsHandlersManager;
		integrator.resetEvaluations();
		when(prototype.copy()).thenReturn(prototype);
		when(prototype.copy()).thenReturn(prototype);
		when(prototype.getPrimaryMapper()).thenReturn(null);
		when(prototype.getSecondaryMapper()).thenReturn(null);
		when(prototype.getGlobalPreviousState()).thenReturn(null);
		when(prototype.getGlobalCurrentState()).thenReturn(null);
		when(prototype.getGlobalCurrentTime()).thenReturn(0.0);
		interpolator = spy(new RungeKuttaStepInterpolator());
		when(prototype.reinitialize(any(), any(), any(), anyBoolean())).thenReturn(interpolator);
		when(prototype.createInterpolator(true, null, null, null)).thenReturn(interpolator);
		when(prototype.getInterpolatedState()).thenReturn(yTmp);
		when(interpolator.getInterpolatedDerivatives()).thenReturn(yDotK[stages - 1]);
		when(interpolator.getPreviousTime()).thenReturn(t0);
		when(interpolator.getCurrentTime()).thenReturn(t);
		interpolator.setInterpolatedTime(t0);
		when(eventsHandlersManager.getEventsStates()).thenReturn(null);
		manager = spy(integrator.addEndTimeChecker(t0, t, eventsHandlersManager));
		when(manager.evaluateStep(interpolator)).thenReturn(false);
		computer = spy((RungeKuttaIntegrator) integrator);
		computer.yDotK = new double[][] {{1, 2}, {3, 4}};
		computer.yTmp = new double[] {1, 2};
		computer.stepStart = 0.0;
		computer.stepSize = 1.0;
	}
}
