package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.util.FastMath;
import org.junit.Assert;
import org.junit.Test;


public class EmbeddedRungeKuttaIntegratorTest {

	@Test
	public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
	
	EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test", false, new double[] {0, 1}, new double[][] {{0,0},{0,1}}, new double[] {1,1}, null, 1, 10, 0, 0);
	ExpandableStatefulODE equations = new ExpandableStatefulODE();
	equations.setTime(0);
	double t = 1;
	integrator.integrate(equations, t);
	
	double[] expected = {};
	double[] actual = equations.getCompleteState();
	Assert.assertArrayEquals(expected,actual,0.0001);
	
	}
}