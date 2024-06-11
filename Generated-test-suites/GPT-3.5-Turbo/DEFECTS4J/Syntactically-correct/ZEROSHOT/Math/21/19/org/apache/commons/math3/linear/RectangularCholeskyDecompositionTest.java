import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private final double epsilon = 1e-10;

    @Test
    public void testDecompose() {

    	// Test 1: A positive definite matrix
    	double[][] matrix1 = {{4, 0, 0},
    			              {0, 9, 0},
    			              {0, 0, 16}};
    	RealMatrix m1 = MatrixUtils.createRealMatrix(matrix1);
    	RectangularCholeskyDecomposition rcd1 = new RectangularCholeskyDecomposition(m1, epsilon);
    	RealMatrix root1 = rcd1.getRootMatrix();
    	int rank1 = rcd1.getRank();

    	double[][] expectedRoot1 = {{2, 0, 0},
    			                    {0, 3, 0},
    			                    {0, 0, 4}};
    	RealMatrix expectedResult1 = MatrixUtils.createRealMatrix(expectedRoot1);
    	int expectedRank1 = 3;

    	assertTrue(root1.subtract(expectedResult1).getFrobeniusNorm() < epsilon);
    	assertEquals(rank1, expectedRank1);

        // Test 2: A positive semidefinite matrix
        double[][] matrix2 = {{4, 1, 1},
	                          {1, 9, 1},
	                          {1, 1, 16}};
	    RealMatrix m2 = MatrixUtils.createRealMatrix(matrix2);
	    RectangularCholeskyDecomposition rcd2 = new RectangularCholeskyDecomposition(m2, epsilon);
	    RealMatrix root2 = rcd2.getRootMatrix();
	    int rank2 = rcd2.getRank();

	    double[][] expectedRoot2 = {{2.04939, 0.244948, 0.244948},
	                                {0, 2.98998, 0.138675},
	                                {0, 0, 3.96863}};
	    RealMatrix expectedResult2 = MatrixUtils.createRealMatrix(expectedRoot2);
	    int expectedRank2 = 3;

	    assertTrue(root2.subtract(expectedResult2).getFrobeniusNorm() < epsilon);
	    assertEquals(rank2, expectedRank2);

        // Test 3: A non-positive semidefinite matrix
	    double[][] matrix3 = {{4, 1, 1},
	    		              {1, 9, 1},
	    		              {1, 1, 25}};
	    RealMatrix m3 = MatrixUtils.createRealMatrix(matrix3);

	    try {
	    	RectangularCholeskyDecomposition rcd3 = new RectangularCholeskyDecomposition(m3, epsilon);
	    	fail("NonPositiveDefiniteMatrixException not thrown.");
	    } catch (NonPositiveDefiniteMatrixException e) {
	    	// Exception expected
	    }

    }
}