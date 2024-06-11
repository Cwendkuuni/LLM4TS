
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {
    @Test
    public void testConstructor() {
        double[][] matrixData = {
            {2, 1, 2},
            {1, 2, 1},
            {2, 1, 2}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        // Test getP()
        RealMatrix expectedP = MatrixUtils.createRealMatrix(transformer.matrixP);
        RealMatrix actualP = transformer.getP();
        assertArrayEquals(expectedP.getData(), actualP.getData());
        
        // Test getT()
        RealMatrix expectedT = MatrixUtils.createRealMatrix(transformer.matrixT);
        RealMatrix actualT = transformer.getT();
        assertArrayEquals(expectedT.getData(), actualT.getData());
        
        // Test getPT()
        RealMatrix expectedPT = expectedP.transpose();
        RealMatrix actualPT = transformer.getPT();
        assertArrayEquals(expectedPT.getData(), actualPT.getData());
    }

    // There are no additional public methods in the SchurTransformer class
}
