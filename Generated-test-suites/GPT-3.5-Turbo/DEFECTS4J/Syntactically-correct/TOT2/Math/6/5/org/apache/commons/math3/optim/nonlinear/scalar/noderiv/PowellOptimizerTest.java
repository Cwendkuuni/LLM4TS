import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.direct.PowellOptimizer.LineSearch;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.SimpleValueChecker;

public class PowellOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        double rel = 0.001;
        double abs = 0.001;
        PowellOptimizer powell = new PowellOptimizer(rel, abs, new SimpleValueChecker(rel, abs));
        assertNotNull(powell);
    }

    @Test
    public void testConstructor() {
        double rel = 0.001;
        double abs = 0.001;
        PowellOptimizer powell = new PowellOptimizer(rel, abs);
        assertNotNull(powell);
    }

    @Test
    public void testConstructorWithInvalidRel() {
        double rel = 0;
        double abs = 0.001;
        try {
            PowellOptimizer powell = new PowellOptimizer(rel, abs);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Pass
        }
    }

    @Test
    public void testConstructorWithInvalidAbs() {
        double rel = 0.001;
        double abs = -0.001;
        try {
            PowellOptimizer powell = new PowellOptimizer(rel, abs);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        }
    }

    @Test
    public void testDoOptimize() {
        double rel = 0.001;
        double abs = 0.001;
        PowellOptimizer powell = new PowellOptimizer(rel, abs);
        double[] startPoint = {1.0, 2.0};
        powell.setStartPoint(startPoint);
        powell.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = powell.optimize();
        assertNotNull(result);
    }

    @Test
    public void testLineSearch() {
        LineSearch lineSearch = new LineSearch(0.001, 0.001);
        double[] p = {1.0, 2.0};
        double[] d = {0.1, 0.2};
        lineSearch.search(p, d);
    }

}