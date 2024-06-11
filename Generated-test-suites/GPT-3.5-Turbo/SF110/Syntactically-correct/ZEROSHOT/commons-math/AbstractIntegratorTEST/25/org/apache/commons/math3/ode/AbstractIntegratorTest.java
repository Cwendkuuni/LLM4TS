import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class AbstractIntegratorTest {
	
	AbstractIntegrator integrator;
	
	@Before
	public void setup() {
		integrator = new AbstractIntegrator() {
			@Override
			public void integrate(ExpandableStatefulODE p0, double p1)
					throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException,
					NoBracketingException {
				// dummy implementation
			}
		};
	}
	
	@Test
	public void testGetName() {
		assertEquals(null, integrator.getName());
	}
	
	@Test
	public void testAddStepHandler() {
		StepHandler handler = new DummyStepHandler();
		integrator.addStepHandler(handler);
		
		Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
		assertTrue(stepHandlers.contains(handler));
	}
	
	@Test
	public void testClearStepHandlers() {
		StepHandler handler = new DummyStepHandler();
		integrator.addStepHandler(handler);
		
		integrator.clearStepHandlers();
		Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
		assertTrue(stepHandlers.isEmpty());
	}
	
	@Test
	public void testAddEventHandler() {
		EventHandler eventHandler = new DummyEventHandler();
		double maxCheckInterval = 1.0;
		double convergence = 0.01;
		int maxIterationCount = 100;
		UnivariateSolver solver = new BrentSolver();
		
		integrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount, solver);
		
		Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
		assertTrue(eventHandlers.contains(eventHandler));
	}
	
	@Test
	public void testClearEventHandlers() {
		EventHandler eventHandler = new DummyEventHandler();
		double maxCheckInterval = 1.0;
		double convergence = 0.01;
		int maxIterationCount = 100;
		UnivariateSolver solver = new BrentSolver();
		
		integrator.addEventHandler(eventHandler, maxCheckInterval, convergence, maxIterationCount, solver);
		integrator.clearEventHandlers();
		
		Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
		assertTrue(eventHandlers.isEmpty());
	}
	
	@Test
	public void testGetMaxEvaluations() {
		assertEquals(-1, integrator.getMaxEvaluations());
	}
	
	@Test
	public void testSetMaxEvaluations() {
		int maxEvaluations = 1000;
		integrator.setMaxEvaluations(maxEvaluations);
		
		assertEquals(maxEvaluations, integrator.getMaxEvaluations());
	}
	
	@Test
	public void testGetEvaluations() {
		assertEquals(0, integrator.getEvaluations());
	}
	
	@Test
	public void testSetEquations() {
		ExpandableStatefulODE equations = new ExpandableStatefulODE();
		integrator.setEquations(equations);
		
		assertEquals(equations, integrator.getExpandable());
	}
	
	@Test
	public void testIntegrate() {
		FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
		double t0 = 0.0;
		double[] y0 = {1.0};
		double t = 1.0;
		double[] y = new double[1];
		
		try {
			integrator.integrate(equations, t0, y0, t, y);
		} catch (Exception e) {
			fail("Should not throw any exception");
		}
		
		assertEquals(t, y[0], 0.0);
	}
	
	@Test
	public void testComputeDerivatives() {
		double t = 0.0;
		double[] y = {1.0};
		double[] yDot = new double[1];
		
		try {
			integrator.computeDerivatives(t, y, yDot);
		} catch (Exception e) {
			fail("Should not throw any exception");
		}
		
		assertEquals(0.0, yDot[0], 0.0);
	}
	
	@Test
	public void testSanityChecks() {
		ExpandableStatefulODE equations = new ExpandableStatefulODE();
		double t = 0.0;
		
		try {
			integrator.sanityChecks(equations, t);
		} catch (Exception e) {
			fail("Should not throw any exception");
		}
	}

	@Test(expected = DimensionMismatchException.class)
	public void testIntegrateWithDimensionMismatchException() {
		FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
		double t0 = 0.0;
		double[] y0 = {1.0, 2.0};
		double t = 1.0;
		double[] y = new double[1];
		
		try {
			integrator.integrate(equations, t0, y0, t, y);
		} catch (DimensionMismatchException e) {
			assertEquals(y0.length, e.getDimension());
			throw e;
		}
	}
	
	@Test(expected = DimensionMismatchException.class)
	public void testComputeDerivativesWithDimensionMismatchException() {
		double t = 0.0;
		double[] y = {1.0, 2.0};
		double[] yDot = new double[1];
		
		try {
			integrator.computeDerivatives(t, y, yDot);
		} catch (DimensionMismatchException e) {
			assertEquals(y.length, e.getDimension());
			throw e;
		}
	}
	
	@Test(expected = NumberIsTooSmallException.class)
	public void testSanityChecksWithNumberIsTooSmallException() {
		ExpandableStatefulODE equations = new ExpandableStatefulODE();
		double t = 0.0;
		
		try {
			integrator.sanityChecks(equations, t);
		} catch (NumberIsTooSmallException e) {
			assertEquals(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, e.getSpecificPattern());
			throw e;
		}
	}
	
	@Test(expected = RuntimeException.class)
	public void testInvalidIntegrate() {
		FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
		double t0 = 0.0;
		double[] y0 = {1.0};
		double t = 1.0;
		double[] y = new double[1];
		
		integrator = new AbstractIntegrator() {
			@Override
			public void integrate(ExpandableStatefulODE p0, double p1)
					throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException,
					NoBracketingException {
				throw new RuntimeException();
			}
		};
		
		integrator.integrate(equations, t0, y0, t, y);
	}
	
	private class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
		@Override
		public void computeDerivatives(double t, double[] y, double[] yDot)
				throws MaxCountExceededException, DimensionMismatchException {
			yDot[0] = 0.0;
		}

		@Override
		public int getDimension() {
			return 1;
		}
	}
	
	private class DummyStepHandler implements StepHandler {
		@Override
		public void init(double t0, double[] y0, double t) {
			// dummy implementation
		}

		@Override
		public void handleStep(StepInterpolator interpolator, boolean isLast)
				throws MaxCountExceededException {
			// dummy implementation
		}
	}
	
	private class DummyEventHandler implements EventHandler {
		@Override
		public void init(double t0, double[] y0, double t) {
			// dummy implementation
		}

		@Override
		public double g(double t, double[] y) throws DimensionMismatchException {
			return 0.0;
		}

		@Override
		public Action eventOccurred(double t, double[] y, boolean increasing)
				throws DimensionMismatchException {
			return Action.CONTINUE;
		}

		@Override
		public void resetState(double t, double[] y) throws DimensionMismatchException {
			// dummy implementation
		}
	}
}