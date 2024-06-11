package org.apache.commons.math.ode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.MaxCountExceededException;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

	private AbstractIntegrator integrator;
	private double t0;
	private double[] y0;
	private double t;
	private double[] y;
	
	@Before
	public void setup() {
		integrator = new AbstractIntegrator() {
			
			@Override
			public void integrate(ExpandableStatefulODE equations, double t) throws MathIllegalStateException {
				
			}
		};
		
		t0 = 0.0;
		y0 = new double[] {1.0, 2.0, 3.0};
		t = 10.0;
		y = new double[3];
	}
	
	@Test
	public void testIntegrate() throws Exception {
		try {
			integrator.integrate(new FirstOrderDifferentialEquations() {
				
				@Override
				public int getDimension() {
					return y0.length;
				}
				
				@Override
				public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
					
				}
			}, t0, y0, t, y);
		} catch(Exception e) {
			assertTrue(e instanceof DimensionMismatchException);
		}
	}
	
	@Test
	public void testSetMaxEvaluations() {
		integrator.setMaxEvaluations(10);
		assertEquals(10, integrator.getMaxEvaluations());
	}
	
	@Test
	public void testGetEvaluations() {
		assertEquals(0, integrator.getEvaluations());
	}
	
}