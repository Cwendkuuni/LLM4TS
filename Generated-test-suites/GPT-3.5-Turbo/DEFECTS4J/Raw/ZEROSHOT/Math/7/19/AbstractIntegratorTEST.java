import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class AbstractIntegratorTest {

	@Test
	public void testGetMaxEvaluations() {
		AbstractIntegrator integrator = new AbstractIntegrator();
		int expected = -1;
		int actual = integrator.getMaxEvaluations();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSetMaxEvaluations() {
		AbstractIntegrator integrator = new AbstractIntegrator();
		int expected = 100;
		integrator.setMaxEvaluations(expected);
		int actual = integrator.getMaxEvaluations();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetEvaluations() {
		AbstractIntegrator integrator = new AbstractIntegrator();
		int expected = 0;
		int actual = integrator.getEvaluations();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testComputeDerivatives() throws MaxCountExceededException, DimensionMismatchException {
		AbstractIntegrator integrator = new AbstractIntegrator();
		integrator.computeDerivatives(1.0, new double[]{1.0}, new double[]{2.0});
		assertEquals(1, integrator.getEvaluations());
	}
	
	@Test
	public void testAcceptStep() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
		AbstractIntegrator integrator = new AbstractIntegrator();
		AbstractStepInterpolator interpolator = new AbstractStepInterpolator();
		double[] y = {1.0};
		double[] yDot = {2.0};
		double expected = 1.0;
		double actual = integrator.acceptStep(interpolator, y, yDot, 1.0);
		assertEquals(expected, actual, 0.0);
	}
}