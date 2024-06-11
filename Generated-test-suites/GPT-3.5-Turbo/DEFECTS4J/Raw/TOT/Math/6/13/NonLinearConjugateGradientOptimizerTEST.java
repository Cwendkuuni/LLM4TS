import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer.IdentityPreconditioner;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer.Preconditioner;
import org.junit.Assert;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                                                                   new SimpleValueChecker());
        optimizer.setInitialStep(0.1);
        
        double[] startPoint = {1, 2, 3};
        GoalType goal = GoalType.MINIMIZE;
        int maxEvaluations = 1000;
        
        PointValuePair result = optimizer.optimize(maxEvaluations, new UnivariateFunction() {
            
            public double value(double x) {
                double[] point = startPoint.clone();
                point[0] += x;
                return computeObjectiveValue(point);
            }
        }, goal, startPoint);
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE,
                                                                                                   new SimpleValueChecker());
        
        UnivariateFunction f = new UnivariateFunction() {
            
            public double value(double x) {
                return x * x;
            }
        };
        
        double a = 0;
        double h = 0.1;
        
        double upperBound = optimizer.findUpperBound(f, a, h);
        
        Assert.assertTrue(upperBound > a);
    }
    
    private double computeObjectiveValue(double[] point) {
        // TODO: Implement objective function computation
        return 0;
    }

    private double[] computeObjectiveGradient(double[] point) {
        // TODO: Implement gradient computation
        return new double[point.length];
    }
}