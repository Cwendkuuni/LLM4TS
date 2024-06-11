package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenvalueDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the {@link SingularValueDecompositionImpl} class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test the SingularValueDecomposition calculation.
     */
    @Test
    public void testSingularValueDecomposition() {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        try {
            final SingularValueDecomposition decomposition =
                new SingularValueDecompositionImpl(matrix);
            final RealMatrix u = decomposition.getU();
            final RealMatrix s = decomposition.getS();
            final RealMatrix v = decomposition.getV();
            final RealMatrix ut = decomposition.getUT();
            final RealMatrix st = decomposition.getS().transpose();
            final RealMatrix vt = decomposition.getVT();
            final RealMatrix expectedUs = MatrixUtils.createRealMatrix(new double[][] {
                  {0.5467883867784915, 0.8164965809277261, 0.4330127018922193},
                  {-0.8164965809277261, 0.5467883867784915, 0.4330127018922193},
                  {-0.4330127018922193, -0.4330127018922193, 1.0}
            });
            final RealMatrix expectedSt = MatrixUtils.createRealDiagonalMatrix(new double[] {
                  1.4142137410599759E-16, 5.1822455436302694, 8.2893532850903214
            });
            final RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][] {
                  {0.4082482913259732, 0.7343855512632361, 0.6585009322725126},
                  {-0.7343855512632361, 0.4082482913259732, -0.6585009322725126},
                  {-0.6585009322725126, -0.6585009322725126, 0.4082482913259732}
            });
            final RealMatrix expectedResult = expectedUs.multiply(expectedSt).multiply(expectedVt);
            Assert.assertEquals("U should be:\n" + expectedUs, u, ut.multiply(expectedUs));
            Assert.assertEquals("S should be:\n" + expectedSt, s, st);
            Assert.assertEquals("V should be:\n" + expectedVt, v, vt);
            Assert.assertEquals("Result should be:\n" + expectedResult,
                                u.multiply(s).multiply(v), decomposition.getMatrix());
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test the SingularValueDecomposition calculation for a singular matrix.
     */
    @Test
    public void testSingularValueDecompositionSingular() {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {0.0, 2.0, 3.0},
            {4.0, 0.0, 6.0},
            {7.0, 8.0, 0.0}
        });

        try {
            final SingularValueDecomposition decomposition =
                new SingularValueDecompositionImpl(matrix);
            final RealMatrix u = decomposition.getU();
            final RealMatrix s = decomposition.getS();
            final RealMatrix v = decomposition.getV();
            final RealMatrix ut = decomposition.getUT();
            final RealMatrix st = decomposition.getS().transpose();
            final RealMatrix vt = decomposition.getVT();
            final RealMatrix expectedUs = MatrixUtils.createRealMatrix(new double[][] {
                  {0.5, 0.8, -0.8},
                  {-0.8, 0.5, 0.8},
                  {-0.8, -0.8, 0.5}
            });
            final RealMatrix expectedSt = MatrixUtils.createRealDiagonalMatrix(new double[] {
                  1.0, 0.0, 0.0
            });
            final RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][] {
                  {0.5, 0.8, -0.8},
                  {0.8, 0.5, 0.8},
                  {-0.8, 0.8, 0.5}
            });
            Assert.assertEquals("U should be:\n" + expectedUs, u, ut.multiply(expectedUs));
            Assert.assertEquals("S should be:\n" + expectedSt, s, st);
            Assert.assertEquals("V should be:\n" + expectedVt, v, vt);
            Assert.assertEquals("Result should be:\n" + expectedUs.multiply(expectedSt).multiply(expectedVt),
                                u.multiply(s).multiply(v), decomposition.getMatrix());
            Assert.assertTrue("The decomposition should be singular",
                             decomposition.getRank() < matrix.getRowDimension());
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test the pseudo-inverse calculation.
     */
    @Test
    public void testPseudoInverse() {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        try {
            final SingularValueDecomposition decomposition =
                new SingularValueDecompositionImpl(matrix);
            final RealMatrix pseudoInverse = decomposition.getSolver();
            final RealVector b = new Array2DRowRealMatrix(new double[][] {
                  {1.0, 2.0},
                  {1.0, 3.0},
                  {1.0, 4.0}
            }).transpose();
            final RealVector expectedResult = new Array2DRowRealMatrix(new double[][] {
                  {-0.014321933514859816, -0.26196954285553203},
                  {0.26196954285553203, 0.35879232594886551},
                  {0.73143548813323226, 0.35879232594886551}
            }).getColumnVector(0);
            Assert.assertEquals("Result should be:\n" + expectedResult,
                                pseudoInverse.solve(b), expectedResult);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test the pseudo-inverse calculation for a singular matrix.
     */
    @Test
    public void testPseudoInverseSingular() {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {0.0, 2.0, 3.0},
            {4.0, 0.0, 6.0},
            {7.0, 8.0, 0.0}
        });

        try {
            final SingularValueDecomposition decomposition =
                new SingularValueDecompositionImpl(matrix);
            final RealMatrix pseudoInverse = decomposition.getSolver();
            final RealVector b = new Array2DRowRealMatrix(new double[][] {
                  {1.0, 2.0},
                  {1.0, 3.0},
                  {1.0, 4.0}
            }).transpose();
            Assert.assertNull("The pseudo-inverse should be null for a singular matrix",
                             pseudoInverse.getInverse());
            Assert.assertFalse("The pseudo-inverse solver should be singular",
                              pseudoInverse.isNonSingular());
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

}