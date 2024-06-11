import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl_BasicFunctionality()
            throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3},
                                                                            {4, 5, 6},
                                                                            {7, 8, 9}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{-0.206831731, 0.816496581, -0.422466563},
                                                                              {0.816496581, -0.422466563, 0.408248295},
                                                                              {0.422466563, 0.408248295, 0.816496581}});
        RealMatrix expectedUt = MatrixUtils.createRealMatrix(new double[][]{{-0.206831731, 0.816496581, 0.422466563},
                                                                               {0.816496581, -0.422466563, 0.408248295},
                                                                               {-0.422466563, 0.408248295, 0.816496581}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 0.714142857, 0.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.654493138, -0.734385558, 0.433012702},
                                                                              {0.734385558, 0.654493138, 0.433012702},
                                                                              {0.433012702, 0.433012702, -0.816496581}});
        RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][]{{-0.654493138, 0.734385558, 0.433012702},
                                                                               {-0.734385558, 0.654493138, 0.433012702},
                                                                               {0.433012702, 0.433012702, -0.816496581}});

        Assert.assertEquals(expectedU, svd.getU());
        Assert.assertEquals(expectedUt, svd.getUT());
        Assert.assertEquals(expectedS, svd.getS());
        Assert.assertEquals(expectedV, svd.getV());
        Assert.assertEquals(expectedVt, svd.getVT());

        Assert.assertArrayEquals(new double[]{1.0, 0.714142857, 0.0}, svd.getSingularValues(), 0.001);
    }

    @Test
    public void testSingularValueDecompositionImpl_EmptyMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = RealMatrix.IDENTITY_1;
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        Assert.assertNull(svd.getU());
        Assert.assertNull(svd.getUT());
        Assert.assertNull(svd.getS());
        Assert.assertNull(svd.getV());
        Assert.assertNull(svd.getVT());
        Assert.assertArrayEquals(new double[]{}, svd.getSingularValues(), 0.0);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImpl_InvalidMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 2}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionImpl_NegativeRank()
            throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 2}});
        new SingularValueDecompositionImpl(matrix, -1);
    }

    @Test
    public void testSingularValueDecompositionImpl_SingularMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, 2}, {2, 2}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.707106781, 0.707106781},
                                                                              {-0.707106781, 0.707106781}});
        RealMatrix expectedUt = MatrixUtils.createRealMatrix(new double[][]{{0.707106781, -0.707106781},
                                                                               {0.707106781, 0.707106781}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.5, 0.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.707106781, 0.707106781},
                                                                              {0.707106781, 0.707106781}});
        RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][]{{-0.707106781, 0.707106781},
                                                                               {0.707106781, -0.707106781}});

        Assert.assertEquals(expectedU, svd.getU());
        Assert.assertEquals(expectedUt, svd.getUT());
        Assert.assertEquals(expectedS, svd.getS());
        Assert.assertEquals(expectedV, svd.getV());
        Assert.assertEquals(expectedVt, svd.getVT());

        Assert.assertArrayEquals(new double[]{1.5, 0.0}, svd.getSingularValues(), 0.001);
    }

    @Test
    public void testSingularValueDecompositionImpl_ZeroMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = RealMatrix.ZERO;
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 1);
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}});
        RealMatrix expectedUt = MatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}});
        RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}});

        Assert.assertEquals(expectedU, svd.getU());
        Assert.assertEquals(expectedUt, svd.getUT());
        Assert.assertEquals(expectedS, svd.getS());
        Assert.assertEquals(expectedV, svd.getV());
        Assert.assertEquals(expectedVt, svd.getVT());

        Assert.assertArrayEquals(new double[]{1.0}, svd.getSingularValues(), 0.001);
    }

    @Test
    public void testSingularValueDecompositionImpl_DiagonalMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealDiagonalMatrix(new double[]{1, 2, 3});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.53846931, 0.57455797, -0.57455797},
                                                                              {-0.57455797, 0.53846931, 0.57455797},
                                                                              {-0.57455797, -0.57455797, 0.81649658}});
        RealMatrix expectedUt = MatrixUtils.createRealMatrix(new double[][]{{0.53846931, -0.57455797, -0.57455797},
                                                                               {0.57455797, 0.53846931, -0.57455797},
                                                                               {-0.57455797, 0.57455797, 0.81649658}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{Math.sqrt(3.0), Math.sqrt(2.0), Math.sqrt(1.0)});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{0.734385558, -0.654493138, 0},
                                                                              {-0.654493138, 0.734385558, 0},
                                                                              {0, 0, 1.0}});
        RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][]{{0.734385558, -0.654493138, 0},
                                                                               {-0.654493138, 0.734385558, 0},
                                                                               {0, 0, 1.0}});

        Assert.assertEquals(expectedU, svd.getU());
        Assert.assertEquals(expectedUt, svd.getUT());
        Assert.assertEquals(expectedS, svd.getS());
        Assert.assertEquals(expectedV, svd.getV());
        Assert.assertEquals(expectedVt, svd.getVT());

        Assert.assertArrayEquals(new double[]{Math.sqrt(3.0), Math.sqrt(2.0), Math.sqrt(1.0)}, svd.getSingularValues(), 0.001);
    }

    @Test
    public void testSingularValueDecompositionImpl_TruncatedSVD()
            throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0, 2}, {0, 3, 4}, {2, 4, 5}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.348959897, 0.634385558, -0.729978142},
                                                                              {-0.634385558, 0.634385558, 0.654493138},
                                                                              {0.682816905, -0.654493138, 0.525508558}});
        RealMatrix expectedUt = MatrixUtils.createRealMatrix(new double[][]{{0.348959897, -0.634385558, 0.682816905},
                                                                               {0.634385558, 0.634385558, -0.654493138},
                                                                               {-0.729978142, 0.654493138, 0.525508558}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.414213741, 1.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.734385558, -0.654493138},
                                                                              {0.654493138, 0.734385558}});
        RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][]{{-0.734385558, 0.654493138},
                                                                               {-0.654493138, 0.734385558}});

        Assert.assertEquals(expectedU, svd.getU());
        Assert.assertEquals(expectedUt, svd.getUT());
        Assert.assertEquals(expectedS, svd.getS());
        Assert.assertEquals(expectedV, svd.getV());
        Assert.assertEquals(expectedVt, svd.getVT());

        Assert.assertArrayEquals(new double[]{1.414213741, 1.0}, svd.getSingularValues(), 0.001);
    }
}