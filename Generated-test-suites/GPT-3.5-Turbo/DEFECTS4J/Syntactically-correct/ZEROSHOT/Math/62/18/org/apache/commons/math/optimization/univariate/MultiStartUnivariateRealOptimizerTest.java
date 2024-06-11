import org.junit.Test;
import org.apache.commons.math.optimization.univariate.*;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.util.FastMath;

public class MultiStartUnivariateRealOptimizerTest {

	@Test
	public void testOptimize() {
		BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>() {
			public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
			public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {return null;}
			public int getMaxEvaluations() {return 0;}
			public int getEvaluations() {return 0;}
			public void setMaxEvaluations(int maxEvaluations) {}
			public UnivariateRealPointValuePair optimize(final FUNC f,
                                                 final GoalType goal,
                                                 final double min, final double max)
        throws FunctionEvaluationException {
            return null;
        }
        public UnivariateRealPointValuePair optimize(final FUNC f, final GoalType goal,
                                                 final double min, final double max,
                                                 final double startValue)
        throws FunctionEvaluationException {
            return null;
        }
		};

		MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 5, new RandomGenerator());

		UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
		org.junit.Assert.assertEquals(5, optima.length);
		for(UnivariateRealPointValuePair point : optima) {
			org.junit.Assert.assertNull(point);
		}
	}

	@Test
	public void testSortPairs() {
		BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>() {
			public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
			public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {return null;}
			public int getMaxEvaluations() {return 0;}
			public int getEvaluations() {return 0;}
			public void setMaxEvaluations(int maxEvaluations) {}
			public UnivariateRealPointValuePair optimize(final FUNC f,
                                                 final GoalType goal,
                                                 final double min, final double max)
        throws FunctionEvaluationException {
            return null;
        }
        public UnivariateRealPointValuePair optimize(final FUNC f, final GoalType goal,
                                                 final double min, final double max,
                                                 final double startValue)
        throws FunctionEvaluationException {
            return null;
        }
		};

		MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 5, new RandomGenerator());

		UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[5];
		optima[0] = new UnivariateRealPointValuePair(1.0, 1.0);
		optima[1] = new UnivariateRealPointValuePair(2.0, 2.0);
		optima[2] = null;
		optima[3] = new UnivariateRealPointValuePair(3.0, 3.0);
		optima[4] = null;

		multiStartOptimizer.sortPairs(GoalType.MINIMIZE);

		org.junit.Assert.assertNull(optima[0]);
		org.junit.Assert.assertNull(optima[1]);
		org.junit.Assert.assertNull(optima[2]);
		org.junit.Assert.assertEquals(3.0, optima[3].getValue(), 0.0);
		org.junit.Assert.assertEquals(2.0, optima[4].getValue(), 0.0);
	}

}