import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;

import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10);
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iterations, PointValuePair previous, PointValuePair current) {
                return Math.abs(previous.getValue() - current.getValue()) < 1e-15;
            }
        });
        
        // create a new class to test doOptimize() method
        class PowellOptimizerTestClass extends PowellOptimizer {

            PowellOptimizerTestClass(double rel, double abs, double lineRel, double lineAbs, ConvergenceChecker<PointValuePair> checker) {
                super(rel, abs, lineRel, lineAbs, checker);
            }

            @Override
            public double computeObjectiveValue(double[] parameters) {
                // return dummy value for testing purposes
                return 1.0;
            }

            @Override
            public PointValuePair optimizeInternal(int maxEval, MultivariateFunction f, GoalType goalType, double[] startPoint) {
                // return dummy value for testing purposes
                return null;
            }
        }

        PowellOptimizerTestClass powellOptimizer = new PowellOptimizerTestClass(1e-10, 1e-10, 1e-10, 1e-10, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iterations, PointValuePair previous, PointValuePair current) {
                return Math.abs(previous.getValue() - current.getValue()) < 1e-15;
            }
        });

        PointValuePair result = powellOptimizer.doOptimize();

        // assert statements
        assertNotNull(result);
        assertEquals(1.0, result.getValue(), 1e-15);
    }
}