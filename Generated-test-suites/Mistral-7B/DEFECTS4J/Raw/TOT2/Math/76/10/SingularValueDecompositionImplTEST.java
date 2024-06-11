package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 3.0}
        });
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            RealMatrix u = singularValueDecomposition.getU();
            RealMatrix s = singularValueDecomposition.getS();
            RealMatrix v = singularValueDecomposition.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{
                    {0.53846014, -0.85738565},
                    {-0.85738565, 0.53846014}
            });
            Assert.assertArrayEquals(expectedU.getData(), u.getData(), 0.001);
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[]{2.23606798, 0.0});
            Assert.assertArrayEquals(expectedS.getData(), s.getData(), 0.001);
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{
                    {-0.45238363, 0.86530108},
                    {0.86530108, -0.45238363}
            });
            Assert.assertArrayEquals(expectedV.getData(), v.getData(), 0.001);

            RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{
                    {0.53846014, -0.85738565},
                    {-0.85738565, 0.53846014}
            });
            Assert.assertArrayEquals(expectedUT.getData(), u.transpose().getData(), 0.001);

            RealMatrix expectedVT = new Array2DRowRealMatrix(new double[][]{
                    {-0.45238363, 0.86530108},
                    {0.86530108, -0.45238363}
            });
            Assert.assertArrayEquals(expectedVT.getData(), v.transpose().getData(), 0.001);

            RealMatrix expectedSquareRootSingularValues = new Array2DRowRealMatrix(new double[]{2.50655547, 0.0});
            Assert.assertArrayEquals(expectedSquareRootSingularValues.getData(), singularValueDecomposition.getSingularValues(), 0.001);

            RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{
                    {0.89442719, 0.4472136},
                    {0.4472136, 0.2236068}
            });
            Assert.assertArrayEquals(expectedCovariance.getData(), singularValueDecomposition.getCovariance(0.0).getData(), 0.001);

            Assert.assertEquals(2.50655547, singularValueDecomposition.getNorm(), 0.001);
            Assert.assertEquals(5.013111, singularValueDecomposition.getConditionNumber(), 0.001);
            Assert.assertEquals(2, singularValueDecomposition.getRank());

            DecompositionSolver solver = singularValueDecomposition.getSolver();
            RealVector b = new ArrayRealVector(new double[]{3.0, 4.0});
            RealVector x = solver.solve(b);
            Assert.assertArrayEquals(new double[]{1.07122828, 0.68657553}, x.toArray(), 0.001);

            RealMatrix B = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 2.0},
                    {3.0, 4.0}
            });
            RealMatrix X = solver.solve(B);
            Assert.assertArrayEquals(new double[][]{
                    {-0.4472136, 0.86530108},
                    {0.86530108, -0.4472136}
            }, X.getData(), 0.001);

        } catch (InvalidMatrixException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 2.0}
        });
        new SingularValueDecompositionImpl(matrix);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConvergenceException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 2.0},
                {3.0, 3.0}
        });
        try {
            new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            // expected, matrix is not square
        }

        try {
            new SingularValueDecompositionImpl(matrix, 3);
        } catch (InvalidMatrixException e) {
            // expected, matrix is not square
        }

        try {
            new SingularValueDecompositionImpl(matrix, 7);
        } catch (InvalidMatrixException e) {
            RealMatrix matrixWithNoise = new Array2DRowRealMatrix(new double[][]{
                    {1.00000001, 2.0},
                    {2.0, 2.00000001},
                    {3.0, 3.0}
            });
            new SingularValueDecompositionImpl(matrixWithNoise, 7);
        }
    }

}