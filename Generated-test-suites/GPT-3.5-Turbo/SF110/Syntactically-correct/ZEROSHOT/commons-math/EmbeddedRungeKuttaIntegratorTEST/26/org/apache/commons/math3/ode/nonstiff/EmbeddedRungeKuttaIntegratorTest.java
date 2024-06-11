package org.apache.commons.math3.ode.nonstiff;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

	private EmbeddedRungeKuttaIntegrator integrator;

	@Before
	public void setup() {
		// create an instance of the embedded Runge-Kutta integrator
		// with necessary parameters
		double[] c = {1.0};
		double[][] a = {{1.0}};
		double[] b = {1.0};
		RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
		double minStep = 0.001;
		double maxStep = 0.1;
		double scalAbsoluteTolerance = 1e-6;
		double scalRelativeTolerance = 1e-6;
		integrator = new EmbeddedRungeKuttaIntegratorImpl("Test", true, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
	}

	// test getOrder() method
	@Test
	public void testGetOrder() {
		assertEquals(1, integrator.getOrder());
	}

	// test getSafety() method
	@Test
	public void testGetSafety() {
		assertEquals(0.9, integrator.getSafety(), 0.0);
	}

	// test setSafety() method
	@Test
	public void testSetSafety() {
		integrator.setSafety(0.8);
		assertEquals(0.8, integrator.getSafety(), 0.0);
	}

	// test getMinReduction() method
	@Test
	public void testGetMinReduction() {
		assertEquals(0.2, integrator.getMinReduction(), 0.0);
	}

	// test setMinReduction() method
	@Test
	public void testSetMinReduction() {
		integrator.setMinReduction(0.1);
		assertEquals(0.1, integrator.getMinReduction(), 0.0);
	}

	// test getMaxGrowth() method
	@Test
	public void testGetMaxGrowth() {
		assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
	}

	// test setMaxGrowth() method
	@Test
	public void testSetMaxGrowth() {
		integrator.setMaxGrowth(15.0);
		assertEquals(15.0, integrator.getMaxGrowth(), 0.0);
	}

	// test integrate() method
	@Test
	public void testIntegrate() {
		// TODO: add integrate method test
	}

	// test estimateError() method
	@Test
	public void testEstimateError() {
		// TODO: add estimateError method test
	}
}