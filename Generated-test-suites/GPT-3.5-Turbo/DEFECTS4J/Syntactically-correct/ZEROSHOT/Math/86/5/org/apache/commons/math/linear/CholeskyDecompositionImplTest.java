import org.junit.jupiter.api.Test;

import org.apache.commons.math.linear.*;

class CholeskyDecompositionImplTest {

  @Test
  void testCholeskyDecomposition() {
    double[][] data = {
      {4, 2, 2},
      {2, 8, 4},
      {2, 4, 6}
    };

    RealMatrix matrix = new RealMatrixImpl(data);
    CholeskyDecompositionImpl decomposition = 
        new CholeskyDecompositionImpl(matrix);

    RealMatrix l = decomposition.getL();
    RealMatrix lt = decomposition.getLT();

    assertArrayEquals(lt.getData(), matrix.getData());
    assertArrayEquals(lt.transpose().getData(), l.getData());

    double determinant = decomposition.getDeterminant();
    assertEquals(determinant, 96);

    DecompositionSolver solver = decomposition.getSolver();
    double[] b = {8, 16, 12};
    double[] x = solver.solve(b);
    double[] expectedX = {1, 2, 1};
    assertArrayEquals(x, expectedX, 1e-6);

    RealVector bv = new RealVectorImpl(b);
    RealVector xv = solver.solve(bv);
    RealVector expectedXv = new RealVectorImpl(expectedX);
    assertArrayEquals(xv.getData(), expectedXv.getData(), 1e-6);

    RealMatrix bm = new RealMatrixImpl(new double[][]{{8}, {16}, {12}});
    RealMatrix xm = solver.solve(bm);
    RealMatrix expectedXm = new RealMatrixImpl(new double[][]{{1}, {2}, {1}});
    assertArrayEquals(xm.getData(), expectedXm.getData(), 1e-6);

    RealMatrix inv = solver.getInverse();
    RealMatrix identity = MatrixUtils.createRealIdentityMatrix(3);
    RealMatrix result = matrix.multiply(inv);
    assertArrayEquals(result.getData(), identity.getData(), 1e-6);
  }

  @Test
  void testNonSquareMatrixException() {
    double[][] data = {
      {1, 2, 3},
      {4, 5, 6}
    };

    assertThrows(
        NonSquareMatrixException.class,
        () -> new CholeskyDecompositionImpl(new RealMatrixImpl(data))
    );
  }

  @Test
  void testNotSymmetricMatrixException() {
    double[][] data = {
      {1, 2, 3},
      {2, 5, 6},
      {3, 6, 9}
    };

    assertThrows(
        NotSymmetricMatrixException.class,
        () -> new CholeskyDecompositionImpl(new RealMatrixImpl(data))
    );
  }

  @Test
  void testNotPositiveDefiniteMatrixException() {
    double[][] data = {
      {1, -2, 3},
      {-2, 5, -6},
      {3, -6, 9}
    };

    assertThrows(
        NotPositiveDefiniteMatrixException.class,
        () -> new CholeskyDecompositionImpl(new RealMatrixImpl(data))
    );
  }
}