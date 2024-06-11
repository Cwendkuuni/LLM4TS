import org.apache.commons.math.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            assertNotNull(decomposition.getL());
            assertNotNull(decomposition.getLT());
            assertEquals(decomposition.getLT().transpose(), decomposition.getL());
            assertEquals(7684, decomposition.getDeterminant(), 1e-15);
            assertTrue(decomposition.getSolver().isNonSingular());
            RealVector b = new ArrayRealVector(new double[]{2, 5, -7});
            RealVector x = decomposition.getSolver().solve(b);
            RealVector expectedX = new ArrayRealVector(new double[]{-0.234247, 0.150684, -0.246575});
            assertEquals(expectedX, x);
            RealMatrix bMatrix = MatrixUtils.createColumnRealMatrix(new double[]{2, 5, -7});
            RealMatrix xMatrix = decomposition.getSolver().solve(bMatrix);
            RealMatrix expectedXMatrix = MatrixUtils.createColumnRealMatrix(new double[]{-0.234247, 0.150684, -0.246575});
            assertEquals(expectedXMatrix, xMatrix);
            RealMatrix inverse = decomposition.getSolver().getInverse();
            RealMatrix expectedInverse = MatrixUtils.createRealMatrix(new double[][]{{0.438356, -0.150685, 0.0410959}, {-0.150685, 0.0821918, 0.0273973}, {0.0410959, 0.0273973, 0.0136986}});
            assertEquals(expectedInverse, inverse);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}