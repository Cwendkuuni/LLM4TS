import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.QuinticFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize_minimumGoal() {
        UnivariateFunction function = new QuinticFunction();
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMaxEvaluations(1000);
        double[] startPoint = {-2, -1, 0, 1, 2};
        optimizer.setStartPoint(startPoint);
        PointValuePair result = optimizer.optimize(function);
        
        double[] expectedPoint = {1, 1, 1, 1, 1};
        double expectedValue = -1.7290243722344975;
        Assertions.assertArrayEquals(expectedPoint, result.getPoint(), 1e-4);
        Assertions.assertEquals(expectedValue, result.getValue(), 1e-4);
    }
    
    @Test
    public void testDoOptimize_maximumGoal() {
        UnivariateFunction function = new QuinticFunction();
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMaxEvaluations(1000);
        double[] startPoint = {-2, -1, 0, 1, 2};
        optimizer.setStartPoint(startPoint);
        PointValuePair result = optimizer.optimize(function);
        
        double[] expectedPoint = {-2, -2, -2, -2, -2};
        double expectedValue = 64.0;
        Assertions.assertArrayEquals(expectedPoint, result.getPoint(), 1e-4);
        Assertions.assertEquals(expectedValue, result.getValue(), 1e-4);
    }
    
    @Test
    public void testNewPointAndDirection() {
        double[] p = {1, 2, 3};
        double[] d = {0.5, 0.5, 0.5};
        double alpha = 2;
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        double[][] result = optimizer.newPointAndDirection(p, d, alpha);
        
        double[] expectedPoint = {2, 3, 4};
        double[] expectedDirection = {1, 1, 1};
        Assertions.assertArrayEquals(expectedPoint, result[0], 1e-4);
        Assertions.assertArrayEquals(expectedDirection, result[1], 1e-4);
    }
    
    @Test
    public void testLineSearch_search() {
        UnivariateFunction function = new QuinticFunction();
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-4, 1e-4);
        powellOptimizer.setGoalType(GoalType.MINIMIZE);
        powellOptimizer.setMaxEvaluations(1000);
        double[] startPoint = {-2, -1, 0, 1, 2};
        powellOptimizer.setStartPoint(startPoint);
        PowellOptimizer.LineSearch lineSearch = powellOptimizer.new LineSearch(1e-4, 1e-4);
        BracketFinder bracketFinder = new BracketFinder();
        bracketFinder.search(function, GoalType.MINIMIZE, 0, 1);
        double alphaMin = lineSearch.search(startPoint, new double[]{-1, -1, -1}).getPoint();
        
        double expectedAlphaMin = 0;
        Assertions.assertEquals(expectedAlphaMin, alphaMin, 1e-4);
    }
    
}