import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initialization.*;
import org.apache.commons.math3.optim.nonlinear.function.*;
import org.apache.commons.math3.optim.nonlinear.simple.*;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.apache.commons.math3.optim.tests.SimpleTests.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        // Test with the default constructor (LU decomposition, SimpleVectorValueChecker)
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Test with a simple function: f(x) = x^2
        FunctionFunction f = new SimpleFunction("f", x -> Math.pow(x, 2));
        PointValuePair initialGuess = new PointValuePair(new ArrayRealVector(1.0), 1.0);
        PointValuePair solution = optimizer.optimize(f, initialGuess);
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(1.0, solution.getPoint().getEntry(0), 1e-6);

        // Test with a more complex function: f(x) = x^3 - 2x + 3
        FunctionFunction g = new Add(new ConstantFunction(3), new Function("g", x -> Math.pow(x, 3) - 2 * x));
        PointValuePair initialGuessComplex = new PointValueVector(new ArrayRealVector(1.5));
        PointValuePair solutionComplex = optimizer.optimize(g, initialGuessComplex);
        assertEquals(0.0, solutionComplex.getValue(), 1e-6);
        assertEquals(-1.0, solutionComplex.getPoint().getEntry(0), 1e-6);

        // Test exception handling with a null function
        try {
            optimizer.optimize(null, initialGuessComplex);
            fail("NullPointerException expected");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test exception handling with a null initial guess
        try {
            optimizer.optimize(f, null);
            fail("NullPointerException expected");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test exception handling with an incorrect dimension initial guess
        try {
            PointValuePair incorrectInitialGuess = new PointValuePair(new ArrayRealVector(2.0), 1.0);
            optimizer.optimize(f, incorrectInitialGuess);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test exception handling with a non-square weight matrix
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0},
                {0.0, 0.0}
        });
        SimpleLeastSquaresProblem problem = new SimpleLeastSquaresProblem(1, 2, new ArrayRealVector(3.0), f, weightMatrix);
        try {
            optimizer.optimize(problem);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test exception handling with a singular weight matrix
        RealMatrix singularWeightMatrix = new BlockRealMatrix(new double[][]{
                {0.0, 1.0},
                {1.0, 0.0}
        });
        SimpleLeastSquaresProblem problemSingular = new SimpleLeastSquaresProblem(1, 1, new ArrayRealVector(1.0), f, singularWeightMatrix);
        try {
            optimizer.optimize(problemSingular);
            fail("SingularValueDecompositionException expected");
        } catch (SingularValueDecompositionException e) {
            // Expected
        }

        // Test exception handling with a non-converged optimization
        FunctionFunction h = new SimpleFunction("h", x -> Math.sin(x));
        try {
            optimizer.optimize(h, initialGuessComplex);
            fail("ConvergenceException expected");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testGaussNewtonOptimizerWithQRDec() {
        // Test with the constructor using QR decomposition (default SimpleVectorValueChecker)
        GaussNewtonOptimizer optimizerQR = new GaussNewtonOptimizer(false);

        // Test the same cases as above with the QR decomposition optimizer
        // Test with a simple function: f(x) = x^2
        FunctionFunction f = new SimpleFunction("f", x -> Math.pow(x, 2));
        PointValuePair initialGuess = new PointValuePair(new ArrayRealVector(1.0), 1.0);
        PointValuePair solution = optimizerQR.optimize(f, initialGuess);
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(1.0, solution.getPoint().getEntry(0), 1e-6);

        // Test with a more complex function: f(x) = x^3 - 2x + 3
        FunctionFunction g = new Add(new ConstantFunction(3), new Function("g", x -> Math.pow(x, 3) - 2 * x));
        PointValuePair initialGuessComplex = new PointValueVector(new ArrayRealVector(1.5));
        PointValuePair solutionComplex = optimizerQR.optimize(g, initialGuessComplex);
        assertEquals(0.0, solutionComplex.getValue(), 1e-6);
        assertEquals(-1.0, solutionComplex.getPoint().getEntry(0), 1e-6);
    }
}