import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;

import static org.junit.Assert.*;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        double rel = 0.1;
        double abs = 0.0001;
        double lineRel = 0.05;
        double lineAbs = 0.00001;
        ConvergenceChecker<PointValuePair> checker = null;

        PowellOptimizer powellOptimizer = new PowellOptimizer(rel, abs, lineRel, lineAbs, checker);
        PointValuePair result = powellOptimizer.doOptimize();

        assertNotNull(result);
    }
}