
import org.apache.commons.math.linear.CholeskyDecompositionImpl;
import org.apache.commons.math.linear.NonSquareMatrixException;
import org.apache.commons.math.linear.NotSymmetricMatrixException;
import org.apache.commons.math.linear.NotPositiveDefiniteMatrixException;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        
        try {
            RealMatrix matrix = new RealMatrixImpl(data);
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix L = decomposition.getL();
            RealMatrix LT = decomposition.getLT();
            
            double[][] expectedLData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
            double[][] expectedLTData = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
            
            assertMatrixEquals(expectedLData, L.getData());
            assertMatrixEquals(expectedLTData, LT.getData());
            
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetDeterminant() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        
        try {
            RealMatrix matrix = new RealMatrixImpl(data);
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            double determinant = decomposition.getDeterminant();
    
            double expectedDeterminant = 324.0;
            
            assertEquals(expectedDeterminant, determinant, 1e-10);
            
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetSolver() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        
        try {
            RealMatrix matrix = new RealMatrixImpl(data);
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            
            RealMatrix b = new RealMatrixImpl(new double[][]{{1}, {2}, {3}});
            RealMatrix x = decomposition.getSolver().solve(b);
            
            double[][] expectedXData = {{-1.0/3}, {-2.0/3}, {1.0/3}};
            
            assertMatrixEquals(expectedXData, x.getData());
            
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSolve() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        
        try {
            RealMatrix matrix = new RealMatrixImpl(data);
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            
            double[] b = {1, 2, 3};
            double[] x = decomposition.getSolver().solve(b);
            
            double[] expectedX = {-1.0/3, -2.0/3, 1.0/3};
            
            assertArrayEquals(expectedX, x, 1e-10);
            
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    private void assertMatrixEquals(double[][] expected, double[][] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], 1e-10);
        }
    }
    
    private void assertArrayEquals(double[] expected, double[] actual, double delta) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], delta);
        }
    }
    
}

