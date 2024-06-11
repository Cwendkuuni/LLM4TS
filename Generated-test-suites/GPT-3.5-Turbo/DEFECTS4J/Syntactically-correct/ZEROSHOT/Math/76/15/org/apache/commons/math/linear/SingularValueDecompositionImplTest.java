import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    private SingularValueDecompositionImpl svd;

    @Before
    public void setUp() throws Exception {
        double[][] matrixData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        svd = new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetU() {
        RealMatrix u = svd.getU();
        Assert.assertEquals(3, u.getRowDimension());
        Assert.assertEquals(3, u.getColumnDimension());
        double[][] uData = u.getData();
        Assert.assertEquals(1.3833204664074201, uData[0][0], 0.000001);
        Assert.assertEquals(-0.32798528229956745, uData[0][1], 0.000001);
        Assert.assertEquals(-0.9278189290708745, uData[0][2], 0.000001);
        // More assertions...
    }

    // Implement tests for other methods...

}