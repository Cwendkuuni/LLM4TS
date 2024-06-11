import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{1, 3}, {2, 4}});
        RealMatrix actualPT = transformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {0, -2}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {0, -2}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetNorm() {
        double[][] matrixData = {{1, 2}, {3, 4}};
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixData));

        double expectedNorm = 10;
        double actualNorm = transformer.getNorm();
        assertEquals(expectedNorm, actualNorm, 0.0);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] matrixData = {{1, 2}, {3, 4}};
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixData));

        int expectedIndex = 1;
        int actualIndex = transformer.findSmallSubDiagonalElement(1, 10);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testComputeShift() {
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        int l = 1;
        int idx = 2;
        int iteration = 0;

        double[] hVec = new double[3];
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}}));

        transformer.computeShift(l, idx, iteration, shift);

        assertEquals(4, shift.x, 0.0);
        assertEquals(4, shift.y, 0.0);
        assertEquals(3, shift.w, 0.0);
    }

    @Test
    public void testInitQRStep() {
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        int il = 0;
        int iu = 2;

        double[] hVec = new double[3];
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1, 2, 0}, {3, 4, 5}, {0, 6, 7}}));

        int expectedIm = 1;
        int actualIm = transformer.initQRStep(il, iu, shift, hVec);
        assertEquals(expectedIm, actualIm);
    }

    @Test
    public void testPerformDoubleQRStep() {
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        int il = 0;
        int im = 1;
        int iu = 2;

        double[] hVec = new double[3];
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1, 2, 0}, {3, 4, 5}, {0, 6, 7}}));

        transformer.performDoubleQRStep(il, im, iu, shift, hVec);

        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{3, 4, 5}, {0, -1.123, -0.123}, {0, 6, 7}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }
}