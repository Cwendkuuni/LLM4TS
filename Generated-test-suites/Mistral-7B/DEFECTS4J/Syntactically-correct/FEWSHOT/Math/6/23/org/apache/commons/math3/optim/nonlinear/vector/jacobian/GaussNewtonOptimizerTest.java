package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointVectorValuePair;

/**
 * Comprehensive tests for {@link GaussNewtonOptimizer} class.
 *
 * @author Marius Bogoevici.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test constructor with default settings.
     * @throws ConvergenceException
     */
    @Test
    public void testConstructorDefaultSettings() throws ConvergenceException {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertTrue(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with custom settings.
     * @throws ConvergenceException
     */
    @Test
    public void testConstructorCustomSettings() throws ConvergenceException {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertFalse(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with custom settings and custom convergence checker.
     * @throws ConvergenceException
     */
    @Test
    public void testConstructorCustomSettingsWithCustomChecker() throws ConvergenceException {
        ConvergenceChecker<PointVectorValuePair> customChecker = new SimpleVectorValueChecker(1e-4, 1e-4);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, customChecker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertTrue(optimizer.isUseLU());
        assertEquals(customChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test doOptimize method with dummy inputs.
     * @throws ConvergenceException
     * @throws SingularMatrixException
     */
    @Test
    public void testDoOptimizeDummyInputs() throws ConvergenceException, SingularMatrixException {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        double[] targetValues = new double[] { 1.0, 2.0 };
        RealMatrix weightMatrix = new LUDecomposition(new IdentityRealMatrix(2)).getSolver().getInverse();
        double[] startPoint = new double[] { 0.0, 0.0 };
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1e-4, 1e-4);
        PointVectorValuePair initialGuess = new PointVectorValuePair(startPoint, new ArrayRealVector(targetValues));

        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(convergenceChecker);

        PointVectorValuePair result = optimizer.doOptimize();

        // The result should be the same as the initialGuess.
        assertArrayEquals(startPoint, result.getPoint());
        assertArrayEquals(targetValues, result.getValue());

        // The convergence checker should not have been called.
        assertArrayEquals(initialGuess.getValue(), convergenceChecker.getLastCheckedPoint().getValue());
        assertEquals(Double.POSITIVE_INFINITY, convergenceChecker.getLastIterationNumber());
    }

    /**
     * Test doOptimize method with simple example.
     * @throws ConvergenceException
     * @throws SingularMatrixException
     */
    @Test
    public void testDoOptimizeSimpleExample() throws ConvergenceException, SingularMatrixException {
        double[] targetValues = new double[] { 1.0, 2.0 };
        RealMatrix weightMatrix = new LUDecomposition(new IdentityRealMatrix(2)).getSolver().getInverse();
        double[] startPoint = new double[] { 1.0, 1.0 };
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1e-4, 1e-4);

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(convergenceChecker);

        PointVectorValuePair result = optimizer.doOptimize();

        // The result should be the minimum of the given quadratic function.
        double[] expectedResult = new double[] { 0.5, 0.5 };
        assertArrayEquals(expectedResult, result.getPoint());
        assertArrayEquals(targetValues, result.getValue());

        // The convergence checker should have been called once.
        double[] lastCheckedPoint = convergenceChecker.getLastCheckedPoint().getVector();
        assertArrayEquals(expectedResult, lastCheckedPoint);
        assertEquals(1, convergenceChecker.getLastIterationNumber());
    }

    /**
     * Test doOptimize method with simple example and custom convergence checker.
     * @throws ConvergenceException
     * @throws SingularMatrixException
     */
    @Test
    public void testDoOptimizeSimpleExampleWithCustomChecker() throws ConvergenceException, SingularMatrixException {
        double[] targetValues = new double[] { 1.0, 2.0 };
        RealMatrix weightMatrix = new LUDecomposition(new IdentityRealMatrix(2)).getSolver().getInverse();
        double[] startPoint = new double[] { 1.0, 1.0 };
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1e-5, 1e-5);

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(convergenceChecker);

        PointVectorValuePair result = optimizer.doOptimize();

        // The result should be the minimum of the given quadratic function.
        double[] expectedResult = new double[] { 0.25, 0.25 };
        assertArrayEquals(expectedResult, result.getPoint());
        assertArrayEquals(targetValues, result.getValue());

        // The convergence checker should have been called once.
        double[] lastCheckedPoint = convergenceChecker.getLastCheckedPoint().getVector();
        assertArrayEquals(expectedResult, lastCheckedPoint);
        assertEquals(1, convergenceChecker.getLastIterationNumber());
    }

    /**
     * Test doOptimize method with non-square weight matrix.
     * @throws ConvergenceException
     * @throws SingularMatrixException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeWithNonSquareWeightMatrix() throws ConvergenceException, SingularMatrixException {
        double[] targetValues = new double[] { 1.0, 2.0 };
        RealMatrix weightMatrix = new SingularValueDecomposition(new IdentityRealMatrix(3)).getSolver().getInverse();
        double[] startPoint = new double[] { 1.0, 1.0 };
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1e-4, 1e-4);

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(convergenceChecker);

        optimizer.doOptimize();
    }

    /**
     * Test doOptimize method with null input.
     * @throws ConvergenceException
     */
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullInput() throws ConvergenceException {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(null);
        optimizer.doOptimize();
    }
}