import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSingularValueDecompositionImplConstructorWithInvalidMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {4.0, 5.0},
                {7.0, 8.0}
        });
        try {
            SingularValueDecompositionImpl singularValueDecomposition =
                    new SingularValueDecompositionImpl(matrix);
            Assert.fail("Exception should be thrown");
        } catch (InvalidMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{
                {0.0539623549754621, -0.459171254826864, 0.835719934371652},
                {-0.65807817457836, 0.414460821544792, -0.34725226084426},
                {0.74694032454031, -0.0824895581444865, 0.422389395516645}
        });

        RealMatrix actualU = singularValueDecomposition.getU();
        Assert.assertArrayEquals(expectedU.getData(), actualU.getData(), 0.00001);
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{
                {0.539623549754621, -0.459171254826864, 0.835719934371652},
                {0.459171254826864, 0.414460821544792, -0.34725226084426},
                {-0.459171254826864, 0.0824895581444865, 0.422389395516645}
        });

        RealMatrix actualUt = singularValueDecomposition.getUT();
        Assert.assertArrayEquals(expectedUt.getData(), actualUt.getData(), 0.00001);
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        double[] expectedSingularValues = {5.91643786978474, 2.23611747224813, 0.0};

        double[] actualSingularValues = singularValueDecomposition.getSingularValues();
        Assert.assertArrayEquals(expectedSingularValues, actualSingularValues, 0.00001);
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{
                {-0.459171254826864, -0.0824895581444865, 0.422389395516645},
                {-0.414460821544792, 0.414460821544792, -0.34725226084426},
                {-0.459171254826864, 0.459171254826864, 0.835719934371652}
        });

        RealMatrix actualV = singularValueDecomposition.getV();
        Assert.assertArrayEquals(expectedV.getData(), actualV.getData(), 0.00001);
    }

    @Test
    public void testGetVT() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{
                {-0.459171254826864, -0.414460821544792, -0.459171254826864},
                {-0.0824895581444865, 0.414460821544792, 0.459171254826864},
                {0.422389395516645, -0.34725226084426, 0.835719934371652}
        });

        RealMatrix actualVt = singularValueDecomposition.getVT();
        Assert.assertArrayEquals(expectedVt.getData(), actualVt.getData(), 0.00001);
    }

    @Test
    public void testGetSolver() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = singularValueDecomposition.getSolver();

        double[] b = {1.0, 2.0, 3.0};
        double[] expectedX = {-0.33436508525623, 0.395017308493216, 0.842732663718982};
        double[] actualX = solver.solve(b);
        Assert.assertArrayEquals(expectedX, actualX, 0.00001);

        RealVector realB = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector expectedRealX = new ArrayRealVector(new double[]{-0.33436508525623, 0.395017308493216, 0.842732663718982});
        RealVector actualRealX = solver.solve(realB);
        Assert.assertArrayEquals(expectedRealX.toArray(), actualRealX.toArray(), 0.00001);

        RealMatrix realBMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0}
        });
        RealMatrix expectedRealXMatrix = new Array2DRowRealMatrix(new double[][]{
                {-0.33436508525623, 0.395017308493216, 0.842732663718982}
        });
        RealMatrix actualRealXMatrix = solver.solve(realBMatrix);
        Assert.assertArrayEquals(expectedRealXMatrix.getData(), actualRealXMatrix.getData(), 0.00001);

        Assert.assertTrue(solver.isNonSingular());
        RealMatrix inverse = solver.getInverse();
        Assert.assertArrayEquals(expectedV.getData(), inverse.getData(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSolverWithInvalidMatrix() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {4.0, 5.0},
                {7.0, 8.0}
        });
        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = singularValueDecomposition.getSolver();
    }

}