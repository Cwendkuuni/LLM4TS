package org.apache.commons.math3.ode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class AbstractIntegratorTest {
	@Test
	public void testGetName() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		assertEquals("test", integrator.getName());
	}

	@Test //Edge case, empty list of step handlers
	public void testGetStepHandlers_emptyList() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		assertTrue(integrator.getStepHandlers().isEmpty());
	}

	@Test
	public void testAddStepHandler() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		StepHandler handler = new StepHandlerImpl();
		integrator.addStepHandler(handler);
		assertEquals(1, integrator.getStepHandlers().size());
		assertTrue(integrator.getStepHandlers().contains(handler));
	}

	@Test //Edge case, empty list of event handlers
	public void testGetEventHandlers_emptyList() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		assertTrue(integrator.getEventHandlers().isEmpty());
	}

	@Test
	public void testAddEventHandler() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		EventHandler handler = new EventHandlerImpl();
		integrator.addEventHandler(handler, 0.1, 0.01, 10);
		assertEquals(1, integrator.getEventHandlers().size());
		assertTrue(integrator.getEventHandlers().contains(handler));
	}

	@Test
	public void testClearEventHandlers() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		EventHandler handler1 = new EventHandlerImpl();
		EventHandler handler2 = new EventHandlerImpl();
		integrator.addEventHandler(handler1, 0.1, 0.01, 10);
		integrator.addEventHandler(handler2, 0.2, 0.02, 20);
		assertEquals(2, integrator.getEventHandlers().size());

		integrator.clearEventHandlers();

		assertTrue(integrator.getEventHandlers().isEmpty());
	}

	@Test
	public void testGetMaxEvaluations_defaultValue() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		assertEquals(-1, integrator.getMaxEvaluations());
	}

	@Test
	public void testSetMaxEvaluations() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		integrator.setMaxEvaluations(100);
		assertEquals(100, integrator.getMaxEvaluations());
	}

	@Test
	public void testGetEvaluations() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		assertEquals(0, integrator.getEvaluations());
	}

	@Test
	public void testComputeDerivatives() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquationsImpl());
		double t = 0.0;
		double[] y = new double[] { 1.0 };
		double[] yDot = new double[] { 0.0 };
		try {
			integrator.computeDerivatives(t, y, yDot);
			assertEquals(1, integrator.getEvaluations());
		} catch (Exception e) {
			fail("Should not throw any exception");
		}
	}

	@Test
	public void testSanityChecks() {
		AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
		ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquationsImpl());
		equations.setTime(0.0);
		try {
			integrator.sanityChecks(equations, 1.0);
		} catch (Exception e) {
			fail("Should not throw any exception");
		}
	}

	private class AbstractIntegratorImpl extends AbstractIntegrator {
		public AbstractIntegratorImpl(String name) {
			super(name);
		}

		public void integrate(ExpandableStatefulODE p0, double p1) {
			// Not implemented
		}
	}

	private class StepHandlerImpl implements StepHandler {
		public void init(double t0, double[] y0, double t) {
			// Not implemented
		}

		public void handleStep(StepInterpolator interpolator, boolean isLast) {
			// Not implemented
		}
	}

	private class EventHandlerImpl implements EventHandler {
		public void init(double t0, double[] y0, double t) {
			// Not implemented
		}

		public int eventOccurred(double t, double[] y, boolean increasing) {
			return 0;
		}

		public double g(double t, double[] y) {
			return 0.0;
		}

		public Action getAction() {
			return Action.CONTINUE;
		}
	}

	private class FirstOrderDifferentialEquationsImpl implements FirstOrderDifferentialEquations {
		public int getDimension() {
			return 1;
		}

		public void computeDerivatives(double t, double[] y, double[] yDot) {
			// Not implemented
		}
	}
}