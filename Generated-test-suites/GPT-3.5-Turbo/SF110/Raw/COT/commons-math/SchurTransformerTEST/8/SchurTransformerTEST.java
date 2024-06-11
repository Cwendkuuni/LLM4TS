
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    private SchurTransformer schurTransformer;
    private RealMatrix matrix;

    @Before
    public void setUp() {
        // Create a 3x3 example matrix
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        matrix = MatrixUtils.createRealMatrix(data);
        schurTransformer = new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix pMatrix = schurTransformer.getP();
        double[][] expectedData = {{0.267261, -0.872871, 0.408248},
                                   {0.534522, -0.218218, -0.816497},
                                   {0.801784, 0.436436, 0.408248}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(expectedMatrix.subtract(pMatrix).norm() < 1e-6);
    }

    @Test
    public void testGetPT() {
        RealMatrix ptMatrix = schurTransformer.getPT();
        double[][] expectedData = {{0.267261, 0.534522, 0.801784},
                                   {-0.872871, -0.218218, 0.436436},
                                   {0.408248, -0.816497, 0.408248}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(expectedMatrix.subtract(ptMatrix).norm() < 1e-6);
    }

    @Test
    public void testGetT() {
        RealMatrix tMatrix = schurTransformer.getT();
        double[][] expectedData = {{-13.0, -14.400000000000002, -19.687015296882016},
                                   {-0.0, 0.6639903673877348, -0.7361477164597439},
                                   {-0.0, -0.8541989452542073, -1.152814712098876}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(expectedMatrix.subtract(tMatrix).norm() < 1e-6);
    }

    // Additional test cases for potential edge cases and exceptions can be added here

}

