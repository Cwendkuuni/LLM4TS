import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.general.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.general.IdentityPreconditioner;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.general.Preconditioner;
import org.apache.commons.math3.optimization.general.UnivariateSolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    }

    @Test
    public void testOptimize() {
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                return x * x; // A simple quadratic function
            }
        };

        PointValuePair result = optimizer.optimize(objectiveFunction, null, new double[]{0}, new double[]{1});
        Assert.assertEquals(0.0, result.getPoint()[0], 0.0001);
        Assert.assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeWithPreconditioner() {
        Preconditioner preconditioner = new IdentityPreconditioner();
        optimizer.setPreconditioner(preconditioner);

        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 1; // A quadratic function with -1 as a minimum point
            }
        };

        PointValuePair result = optimizer.optimize(objectiveFunction, null, new double[]{0}, new double[]{1});
        Assert.assertEquals(1.0, result.getPoint()[0], 0.0001);
        Assert.assertEquals(-1.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeWithLineSearchSolver() {
        UnivariateSolver lineSearchSolver = new BrentSolver();
        optimizer.setLineSearchSolver(lineSearchSolver);

        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x); // A sinusoidal function with multiple local minima and maxima
            }
        };

        PointValuePair result = optimizer.optimize(objectiveFunction, null, new double[]{0}, new double[]{1});
        Assert.assertEquals(Math.PI / 2, result.getPoint()[0], 0.0001);
        Assert.assertEquals(1.0, result.getValue(), 0.0001);
    }

}