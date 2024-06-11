package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.842732055409387, -0.545848836614134},
                    {-0.545848836614134, 0.842732055409387}});
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.545848836614134, 0.842732055409387},
                    {-0.842732055409387, 0.545848836614134}});
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{
                    {8.485281355044134, 0.0}, {0.0, 0.0}});
            assertEquals(expectedU, u);
            assertEquals(expectedS, s);
            assertEquals(expectedV, v.transpose());
        } catch (InvalidMatrixException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testSingularValueDecompositionImplSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{-0.756163523877252, 0.458257813631873},
                    {0.458257813631873, -0.756163523877252}});
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{
                    {5.196152802366105, 0.0}, {0.0, 0.0}});
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.756163523877252, 0.458257813631873},
                    {0.458257813631873, -0.756163523877252}});
            assertEquals(expectedU, u);
            assertEquals(expectedS, s);
            assertEquals(expectedV, v.transpose());
        } catch (InvalidMatrixException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testGetSingularValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double[] singularValues = svd.getSingularValues();
            double[] expectedSingularValues = {8.485281355044134, 0.0};
            assertArrayEquals(expectedSingularValues, singularValues);
        } catch (InvalidMatrixException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testGetCovariance() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
            RealMatrix covariance = svd.getCovariance(0.01);
            RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.419666032207293, 0.352365197526671},
                    {0.352365197526671, 0.419666032207293}});
            assertEquals(expectedCovariance, covariance);
        } catch (InvalidMatrixException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double norm = svd.getNorm();
            double expectedNorm = 8.485281355044134;
            assertEquals(expectedNorm, norm, 0.001);
        } catch (InvalidMatrixException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testGetConditionNumber() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double conditionNumber = svd.getConditionNumber();
            double expectedConditionNumber = 8.485281355044134 / 0.0;
            assertEquals(expectedConditionNumber, conditionNumber, 0.001);
        } catch (InvalidMatrixException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            int rank = svd.getRank();
            int expectedRank = 2;
            assertEquals(expectedRank, rank);
        } catch (InvalidMatrixException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            DecompositionSolver solver = svd.getSolver();
            Solver expectedSolver = new Solver(svd.getSingularValues().clone(), svd.getUT(), svd.getV(), svd.getRank() == matrix.getRowDimension());
            assertEquals(expectedSolver.getClass(), solver.getClass());
        } catch (InvalidMatrixException e) {
            fail("Should not throw exception");
        }
    }
}