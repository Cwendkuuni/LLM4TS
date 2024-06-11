
import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.general.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        PointValuePair result = optimizer.optimize(100, new ObjectiveFunction(), GoalType.MINIMIZE, new double[]{0.0, 0.0});

        assertEquals(2.0, result.getPoint()[0], 0.0001);
        assertEquals(4.0, result.getPoint()[1], 0.0001);
    }

    private static class ObjectiveFunction implements MultivariateDifferentiableFunction {

        @Override
        public double value(double[] point) {
            return point[0] * point[0] + 2 * point[1] * point[1];
        }

        @Override
        public DerivativeStructure value(DerivativeStructure[] point) {
            return point[0].multiply(point[0]).add(point[1].multiply(point[1]).multiply(2));
        }

        @Override
        public double[] gradient(double[] point) {
            return new double[]{2 * point[0], 4 * point[1]};
        }
    }
}
