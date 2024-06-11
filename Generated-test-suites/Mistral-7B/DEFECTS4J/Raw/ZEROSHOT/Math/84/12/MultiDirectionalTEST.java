import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleValueFunction;
import org.apache.commons.math3.optim.initialization.ArrayRealPointValues;
import org.apache.commons.math3.optim.point.RealPointValue;
import org.apache.commons.math3.optim.search.AbstractVectorOptimizer;
import org.apache.commons.math3.optim.search.MultidimensionalSearchBase;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.PointValuePair.pair;
import static org.apache.commons.math3.optim.InitialGuess.RANDOM_SEED;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectional() {
        // Test MultiDirectional optimization with a simple function
        double[] functionValues = {1.5, 2.0, 3.5};
        RealFunction function = new SimpleValueFunction(functionValues);
        MultiDirectional optimizer = new MultiDirectional();

        // Test with default coefficients
        double[] initialGuess = {1.0, 1.0};
        PointValuePair solution = optimizer.optimize(function, new ArrayRealPointValues(initialGuess));
        Assert.assertEquals(pair(new double[]{1.5, 1.5}, 1.5), solution);

        // Test with custom coefficients
        optimizer = new MultiDirectional(3.0, 0.2);
        solution = optimizer.optimize(function, new ArrayRealPointValues(initialGuess));
        Assert.assertEquals(pair(new double[]{0.4479841746390351, 0.4479841746390351}, 0.6650158253609649), solution);

        // Test with a function that does not converge
        function = new FunctionWithNonConvergingPoint(1.0);
        optimizer = new MultiDirectional();
        optimizer.setMaxEvaluations(1000);
        optimizer.optimize(function, new ArrayRealPointValues(initialGuess));
        Assert.assertFalse(optimizer.isFound());

        // Test convergence checker
        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker(1.0e-8);
        optimizer = new MultiDirectional();
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.optimize(function, new ArrayRealPointValues(initialGuess));
        Assert.assertTrue(optimizer.isFound());
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, optimizer.getBestPoint().getPointRef(), 0.01);
    }

    // A non-converging function for testing purposes
    public static class FunctionWithNonConvergingPoint implements RealFunction {

        private double nonConvergingPoint = 1.0;

        public FunctionWithNonConvergingPoint(double nonConvergingPoint) {
            this.nonConvergingPoint = nonConvergingPoint;
        }

        @Override
        public double value(double[] point) {
            if (Arrays.equals(point, new double[]{nonConvergingPoint, nonConvergingPoint})) {
                throw new FunctionEvaluationException("Function does not converge at this point");
            }
            return Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 1.0, 2.0);
        }

    }

}