import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RealMatrix actual = transformer.getP();
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1, 3}, {2, 4}});
        RealMatrix actual = transformer.getPT();
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {0, -2}});
        RealMatrix actual = transformer.getT();
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        // Test transformed matrix
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {0, -2}});
        RealMatrix actual = transformer.getT();
        assertArrayEquals(expected.getData(), actual.getData());
        // Test eigenvalues
        double[] expectedEigenvalues = new double[]{-1, 5};
        double[] actualEigenvalues = matrix.getEigenvalues().toArray();
        assertArrayEquals(expectedEigenvalues, actualEigenvalues, 0.0001);
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double expected = 14;
		double actual = transformer.getNorm();
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expected = 0;
		int actual = transformer.findSmallSubDiagonalElement(1, 14);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeShift() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        transformer.computeShift(0, 1, 0, shift);
        double expectedX = 4;
        double expectedY = 3;
        double expectedW = 0;
		double actualX = shift.x;
        double actualY = shift.y;
        double actualW = shift.w;
        assertEquals(expectedX, actualX, 0.0001);
        assertEquals(expectedY, actualY, 0.0001);
        assertEquals(expectedW, actualW, 0.0001);
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int expected = 0;
		int actual = transformer.initQRStep(0, 1, new SchurTransformer.ShiftInfo(), hVec);
        assertEquals(expected, actual);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        transformer.performDoubleQRStep(0, 0, 1, shift, hVec);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{4.6832, 6.5081}, {0, -1.6832}});
        RealMatrix actual = transformer.getT();
        assertArrayEquals(expected.getData(), actual.getData());
    }
}