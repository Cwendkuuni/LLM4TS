import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException_whenGivenNullConvergenceChecker() {
        new GaussNewtonOptimizer(null);
    }

    @Test
    public void testGaussNewtonOptimizer_defaultConstructor() {
        new GaussNewtonOptimizer();
    }

    @Test
    public void testGaussNewtonOptimizer_constructorWithCustomChecker() {
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iter, PointVectorValuePair previous, PointVectorValuePair current) {
                return current.getValue()[0] < 0.001;
            }
        };
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(convergenceChecker);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGaussNewtonOptimizer_constructorWithCustomSolverAndChecker() {
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iter, PointVectorValuePair previous, PointVectorValuePair current) {
                return current.getValue()[0] < 0.001;
            }
        };
        DecompositionSolver solver = new LUDecomposition(new BlockRealMatrix(new double[5][5]))
                .getSolver();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(solver, convergenceChecker);
        assertSame(solver, optimizer.getSolver());
        assertSame(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException_whenGivenNullWeightMatrix() {
        new GaussNewtonOptimizer(null, new SimpleVectorValueChecker());
    }

    @Test
    public void testComputeObjectiveValue() {
        // Given
        double[] currentPoint = {1.0, 2.0};
        double[] targetValues = {1.5, 2.5};
        double[] currentObjective = new GaussNewtonOptimizer(new SimpleVectorValueChecker()).computeObjectiveValue(currentPoint);
        double[] expectedObjective = new double[]{3.0, 3.0};

        // When
        double[] targetWeights = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[2][2]);
        weightMatrix.setEntry(0, 0, targetWeights[0]);
        weightMatrix.setEntry(0, 1, 0);
        weightMatrix.setEntry(1, 0, 0);
        weightMatrix.setEntry(1, 1, targetWeights[1]);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker(), weightMatrix);
        double[] resultObjective = optimizer.computeObjectiveValue(currentPoint);

        // Then
        Assert.assertArrayEquals(expectedObjective, resultObjective);
    }

    @Test
    public void testComputeResiduals() {
        // Given
        double[] currentPoint = {1.0, 2.0};
        double[] targetValues = {1.5, 2.5};
        double[] currentResiduals = new GaussNewtonOptimizer(new SimpleVectorValueChecker()).computeResiduals(targetValues, currentPoint);
        double[] expectedResiduals = new double[]{0.5, 0.5};

        // When
        double[] targetWeights = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[2][2]);
        weightMatrix.setEntry(0, 0, targetWeights[0]);
        weightMatrix.setEntry(0, 1, 0);
        weightMatrix.setEntry(1, 0, 0);
        weightMatrix.setEntry(1, 1, targetWeights[1]);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker(), weightMatrix);
        double[] resultResiduals = optimizer.computeResiduals(targetValues, currentPoint);

        // Then
        Assert.assertArrayEquals(expectedResiduals, resultResiduals);
    }

    @Test
    public void testComputeWeightedJacobian() {
        // Given
        double[] currentPoint = {1.0, 2.0};
        double[] targetValues = {1.5, 2.5};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[2][2]);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(0, 1, 0);
        weightMatrix.setEntry(1, 0, 0);
        weightMatrix.setEntry(1, 1, 1.0);
        RealMatrix weightedJacobian = new GaussNewtonOptimizer(new SimpleVectorValueChecker(), weightMatrix).computeWeightedJacobian(currentPoint);
        double[][] expectedJacobian = {{1.5, 2.5}, {2.5, 3.5}};

        // When
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker(), weightMatrix);
        RealMatrix resultJacobian = optimizer.computeWeightedJacobian(currentPoint);

        // Then
        Assert.assertArrayEquals(expectedJacobian, resultJacobian.getData());
    }

    @Test
    public void testSolveNormalEquations_whenLUDecomposition() {
        // Given
        int nRows = 3;
        int nCols = 3;
        double[][] a = {{1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}};
        double[] b = {1.0, 2.0, 3.0};
        RealMatrix matrixA = new BlockRealMatrix(a);

        // When
        DecompositionSolver solver = new LUDecomposition(matrixA).getSolver();
        double[] result = solver.solve(new ArrayRealVector(b, false)).toArray();

        // Then
        Assert.assertArrayEquals(new double[]{0.52777823E-15, 1.13247181E-16, 1.42108547E-16}, result);
    }

    @Test
    public void testSolveNormalEquations_whenQRDecomposition() {
        // Given
        int nRows = 3;
        int nCols = 3;
        double[][] a = {{1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}};
        double[] b = {1.0, 2.0, 3.0};
        RealMatrix matrixA = new BlockRealMatrix(a);

        // When
        DecompositionSolver solver = new QRDecomposition(matrixA).getSolver();
        double[] result = solver.solve(new ArrayRealVector(b, false)).toArray();

        // Then
        Assert.assertArrayEquals(new double[]{-1.55740772E-15, -1.11022302E-15, -6.66133815E-17}, result);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException_whenSolvingNormalEquations_withSingularMatrix() {
        // Given
        int nRows = 3;
        int nCols = 3;
        double[][] a = {{1.0, 2.0, 3.0},
                {0.0, 0.0, 0.0},
                {4.0, 5.0, 6.0}};
        double[] b = {1.0, 2.0, 3.0};
        RealMatrix matrixA = new BlockRealMatrix(a);

        // When
        DecompositionSolver solver = new LUDecomposition(matrixA).getSolver();

        // Then (should throw an exception)
        solver.solve(new ArrayRealVector(b, false));
    }

    @Test
    public void testOptimize_whenConvergenceIsReached() {
        // Given
        double[] startPoint = new double[]{1.0, 2.0};
        double[] targetValues = new double[]{3.0, 4.0};
        double[] weightValues = new double[]{1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(weightValues);
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1.0E-10);

        // When
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, convergenceChecker);
        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);

        // Then
        Assert.assertNotNull(result);
        double[] expectedCost = new double[]{2.0734375, 2.0734375};
        Assert.assertArrayEquals(expectedCost, result.getValue());
    }

    @Test
    public void testOptimize_whenConvergenceIsNotReached() {
        // Given
        double[] startPoint = new double[]{1.0, 2.0};
        double[] targetValues = new double[]{3.0, 4.0};
        double[] weightValues = new double[]{1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(weightValues);
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1.0E-5);

        // When
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, convergenceChecker);
        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);

        // Then
        Assert.assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException_whenOptimizeIsCalledWithNullTargetValues() {
        // Given
        double[] startPoint = new double[]{1.0, 2.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[2][2]);
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1.0E-10);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, convergenceChecker);

        // When
        optimizer.optimize(null, startPoint, weightMatrix);

        // Then (should throw an exception)
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException_whenOptimizeIsCalledWithNullStartPoint() {
        // Given
        double[] targetValues = new double[]{3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[2][2]);
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1.0E-10);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, convergenceChecker);

        // When
        optimizer.optimize(targetValues, null, weightMatrix);

        // Then (should throw an exception)
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException_whenOptimizeIsCalledWithNullWeightMatrix() {
        // Given
        double[] startPoint = new double[]{1.0, 2.0};
        double[] targetValues = new double[]{3.0, 4.0};
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1.0E-10);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, convergenceChecker);

        // When
        optimizer.optimize(targetValues, startPoint, null);

        // Then (should throw an exception)
    }
}