import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                {4.0, 5.0, 6.0},
                                                                {7.0, 8.0, 9.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.632120559, 0.748866027, 0.454014138},
                                                                         {0.735478481, 0.454014138, 0.632120559},
                                                                         {-0.454014138, 0.632120559, 0.735478481}});
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{15.81139575},
                                                                          {9.51851476},
                                                                          {2.44948974}});
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.14845295, 0.92358723, -0.32145352},
                                                                         {-0.82243321, -0.454014138, 0.42255988},
                                                                         {0.454014138, 0.42255988, 0.82243321}});
            assertEquals("U is not equal to expected U", expectedU, u);
            assertEquals("S is not equal to expected S", expectedS, s);
            assertEquals("V is not equal to expected V", expectedV, v);
        } catch (InvalidMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionRankDeficient() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                {2.0, 2.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{-0.756802497, 0.433012702},
                                                                           {0.433012702, 0.756802497}});
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{4.47213595},
                                                                          {4.47213595}});
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.756802497, 0.433012702},
                                                                         {0.433012702, 0.756802497}});
            assertEquals("U is not equal to expected U", expectedU, u);
            assertEquals("S is not equal to expected S", expectedS, s);
            assertEquals("V is not equal to expected V", expectedV, v);
        } catch (SingularMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionIdentity() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                {0.0, 1.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.707106781, 0.707106781},
                                                                           {-0.707106781, 0.707106781}});
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{1.4142137},
                                                                          {1.4142137}});
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.707106781, -0.707106781},
                                                                         {0.707106781, 0.707106781}});
            assertEquals("U is not equal to expected U", expectedU, u);
            assertEquals("S is not equal to expected S", expectedS, s);
            assertEquals("V is not equal to expected V", expectedV, v);
        } catch (SingularMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionIdentitySingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                {0.0, 0.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 1);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.707106781},
                                                                           {0.707106781}});
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{1.4142137}});
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.707106781},
                                                                         {0.707106781}});
            assertEquals("U is not equal to expected U", expectedU, u);
            assertEquals("S is not equal to expected S", expectedS, s);
            assertEquals("V is not equal to expected V", expectedV, v);
        } catch (SingularMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionZero() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0, 0.0},
                                                                {0.0, 0.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 0);
            assertEquals("Singular Value Decomposition should not be created with zero dimension", 0,
                         svd.getRank());
        } catch (SingularMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                {4.0, 5.0, 6.0},
                                                                {7.0, 8.0, 9.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
            double expected = Math.sqrt(15.81139575 * 15.81139575);
            assertEquals("Singular Value Decomposition norm is not correct", expected, svd.getNorm());
        } catch (InvalidMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testGetConditionNumber() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                {4.0, 5.0, 6.0},
                                                                {7.0, 8.0, 9.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
            double expected = 15.81139575 / 2.44948974;
            assertEquals("Singular Value Decomposition condition number is not correct", expected,
                         svd.getConditionNumber());
        } catch (InvalidMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                {4.0, 5.0, 6.0},
                                                                {7.0, 8.0, 9.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
            int expected = 3;
            assertEquals("Singular Value Decomposition rank is not correct", expected, svd.getRank());
        } catch (InvalidMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                {4.0, 5.0, 6.0},
                                                                {7.0, 8.0, 9.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
            double[] b = {1.0, 2.0, 3.0};
            DecompositionSolver solver = svd.getSolver();
            double[] x = solver.solve(b);
            double expected[] = {-0.027966628, 0.138333281, 0.227466466};
            assertArrayEquals("Solver solution not equal to expected solution", expected, x);
        } catch (InvalidMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testSolverSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                {2.0, 2.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 1);
            double[] b = {1.0, 2.0};
            DecompositionSolver solver = svd.getSolver();
            double[] x = solver.solve(b);
            double expected[] = {-0.316227766, 0.632455532};
            assertArrayEquals("Solver solution not equal to expected solution", expected, x);
        } catch (SingularMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testSolverIdentity() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                {0.0, 1.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 1);
            double[] b = {1.0, 0.0};
            DecompositionSolver solver = svd.getSolver();
            double[] x = solver.solve(b);
            double expected[] = {1.0, 0.0};
            assertArrayEquals("Solver solution not equal to expected solution", expected, x);
        } catch (SingularMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testSolverIdentitySingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                {0.0, 0.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 0);
            double[] b = {1.0, 1.0};
            DecompositionSolver solver = svd.getSolver();
            double[] x = solver.solve(b);
            double expected[] = {1.0, 1.0};
            assertArrayEquals("Solver solution not equal to expected solution", expected, x);
        } catch (SingularMatrixException e) {
            fail("Error during Singular Value Decomposition: " + e.getMessage());
        }
    }

}