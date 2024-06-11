import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class SchurTransformerTest {

    @Test
    public void testConstructor_IllegalArgument() {
        try {
            RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
            SchurTransformer schurTransformer = new SchurTransformer(matrix);
        } catch (NonSquareMatrixException e) {
            return;
        }
        fail("NonSquareMatrixException expected.");
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix pMatrix = schurTransformer.getP();
        assertEquals(1.0, pMatrix.getEntry(0, 0), Precision.EPSILON);
        assertEquals(2.0, pMatrix.getEntry(0, 1), Precision.EPSILON);
        assertEquals(3.0, pMatrix.getEntry(1, 0), Precision.EPSILON);
        assertEquals(4.0, pMatrix.getEntry(1, 1), Precision.EPSILON);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix ptMatrix = schurTransformer.getPT();
        assertEquals(1.0, ptMatrix.getEntry(0, 0), Precision.EPSILON);
        assertEquals(3.0, ptMatrix.getEntry(0, 1), Precision.EPSILON);
        assertEquals(2.0, ptMatrix.getEntry(1, 0), Precision.EPSILON);
        assertEquals(4.0, ptMatrix.getEntry(1, 1), Precision.EPSILON);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix tMatrix = schurTransformer.getT();
        assertEquals(1.0, tMatrix.getEntry(0, 0), Precision.EPSILON);
        assertEquals(2.0, tMatrix.getEntry(0, 1), Precision.EPSILON);
        assertEquals(0.0, tMatrix.getEntry(1, 0), Precision.EPSILON);
        assertEquals(3.0, tMatrix.getEntry(1, 1), Precision.EPSILON);
    }
}