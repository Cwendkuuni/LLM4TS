import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize_minimum() {
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer();

        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> x * x);

        UnivariatePointValuePair result = optimizer.optimize(function, new double[] {1.0, 2.0, 3.0}, org.apache.commons.math3.optimization.GoalType.MINIMIZE);

        Assert.assertEquals(0.0, result.getPoint(), 1e-5);
    }

    @Test
    public void testOptimize_maximum() {
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer();

        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> -x * x);

        UnivariatePointValuePair result = optimizer.optimize(function, new double[] {1.0, 2.0, 3.0}, org.apache.commons.math3.optimization.GoalType.MAXIMIZE);

        Assert.assertEquals(0.0, result.getPoint(), 1e-5);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimize_noBracket() {
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer();

        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> x * x * x);

        optimizer.optimize(function, new double[] {1.0, 2.0, 3.0}, org.apache.commons.math3.optimization.GoalType.MINIMIZE);
    }

    @Test
    public void testOptimize_negative() {
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer();

        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> -x);

        UnivariatePointValuePair result = optimizer.optimize(function, new double[] {1.0, 2.0, 3.0}, org.apache.commons.math3.optimization.GoalType.MINIMIZE);

        Assert.assertEquals(-Double.MAX_VALUE, result.getPoint(), 1e-5);
    }

    @Test
    public void testOptimize_zero() {
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer();

        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> 0.0);

        UnivariatePointValuePair result = optimizer.optimize(function, new double[] {1.0, 2.0, 3.0}, org.apache.commons.math3.optimization.GoalType.MINIMIZE);

        Assert.assertEquals(0.0, result.getPoint(), 1e-5);
    }

    @Test
    public void testOptimize_oneVariable() {
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer();

        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> x);

        UnivariatePointValuePair result = optimizer.optimize(function, new double[] {1.0}, org.apache.commons.math3.optimization.GoalType.MINIMIZE);

        Assert.assertEquals(0.0, result.getPoint(), 1e-5);
    }

}