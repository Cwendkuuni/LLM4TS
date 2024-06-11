package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpartial;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.E;

import java.util.Random;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        });

        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }

        matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0},
                {0.0, 0.0, 0.0}
        });

        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // expected
        }

        matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, -1.0, 0.0},
                {-1.0, 1.0, 0.0},
                {0.0, 0.0, 0.0}
        });

        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }

        matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.1, 0.1},
                {0.1, 1.0, 0.1},
                {0.1, 0.1, 1.0}
        });

        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);

        RealMatrix l = decomp.getL();
        RealMatrix lt = decomp.getLT();
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(3);

        assertEquals("L != LT * L", l, lt.multiply(l));
        assertEquals("L * L.T != I", l.multiply(lt), identity);
        assertEquals("Determinant != 1", decomp.getDeterminant(), E.EULER, 1e-6);

        RealVector b = new Array2DRowRealVector(new double[]{1.0, 1.0, 1.0});
        RealVector x = decomp.getSolver().solve(b);

        assertArrayEquals("L * X != B", l.multiply(x.toArray()).toArray(), b.toArray(), 1e-6);

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            RealMatrix randomMatrix = new Array2DRowRealMatrix(randomMatrixSize(random));
            CholeskyDecompositionImpl decomp2 = new CholeskyDecompositionImpl(randomMatrix);

            RealMatrix l2 = decomp2.getL();
            RealMatrix lt2 = decomp2.getLT();
            RealVector b2 = randomVector(random, lt2.getRowDimension());

            RealVector x2 = decomp2.getSolver().solve(b2);

            assertArrayEquals("L * X != B", l2.multiply(x2.toArray()).toArray(), b2.toArray(), 1e-6);
        }
    }

    private static int randomMatrixSize(Random random) {
        return random.nextInt(5) + 1;
    }

    private static RealVector randomVector(Random random, int size) {
        double[] data = new double[size];
        for (int i = 0; i < size; i++) {
            data[i] = random.nextGaussian();
        }
        return new RealVectorImpartial(data, false);
    }
}