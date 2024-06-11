
package org.apache.commons.math.optimization;
import org.apache.commons.math.optimization.MultistartTest.Function0;
import org.apache.commons.math.optimization.MultistartTest.Function1;
import org.apache.commons.math.optimization.MultistartTest.Function2;
import org.apache.commons.math.optimization.MultistartTest.Function3;
import org.apache.commons.math.optimization.MultistartTest.Function4;
import org.apache.commons.math.optimization.MultistartTest.Function5;
import org.apache.commons.math.optimization.MultistartTest.Function6;

import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
	
	private final double EPSILON = 1e-10;
	
	@Test
	public void testIntegration() throws Exception {
	
		MultiStartUnivariateRealOptimizer optimizer = 
				new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizer() {
						public double optimize(UnivariateRealFunction f, GoalType goalType,
		                           final double min, final double max)
							throws ConvergenceException,
							FunctionEvaluationException {
							// implement optimization here
							return 0;
						}
				
				}, 10,
					null);
				
		optimizer.setMaximalIterationCount(10);
		optimizer.setMaxEvaluations(10);
		
		Function0 objectiveFunction = new Function0();
		double result = optimizer.optimize(objectiveFunction, GoalType.MINIMIZE, 0, 10);
		Assert.assertEquals(0.0, result, EPSILON);
	}

	private class Function0 implements UnivariateRealFunction {
		public double value(double x) {
			return x*x - 1;
		}
	}

}
