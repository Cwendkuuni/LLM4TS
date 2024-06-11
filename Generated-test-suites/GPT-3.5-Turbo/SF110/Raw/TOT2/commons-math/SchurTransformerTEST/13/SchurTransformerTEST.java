import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    // Test cases for constructor
    @Test
    public void testConstructor() {
        // Test a typical matrix
        double[][] typicalMatrix = {{1.0, 2.0}, {3.0, 4.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(typicalMatrix));
        
        assertNotNull(transformer.getP());
        assertNotNull(transformer.getT());
        assertNotNull(transformer.getPT());
    }

    // Test cases for getNorm
    @Test
    public void testGetNorm() {
        // Test the case where all elements are positive
        double[][] positiveMatrix = {{1.0, 2.0}, {3.0, 4.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(positiveMatrix));
        
        assertEquals(10.0, transformer.getNorm(), 0.0001);
    }

    // Test cases for findSmallSubDiagonalElement
    @Test
    public void testFindSmallSubDiagonalElement() {
        // Test the case where the epsilon is smaller than the threshold
        double[][] matrix = {{0.0, 1.0}, {0.0, 0.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        
        assertEquals(1, transformer.findSmallSubDiagonalElement(1, 0.0001));
    }

    // Test cases for computeShift
    @Test
    public void testComputeShift() {
        // Test a case where iteration is 10
        double[][] matrix = {{1.0, 2.0}, {3.0, 4.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        transformer.computeShift(0, 1, 10, shift);
        
        assertEquals(1.0, shift.exShift, 0.0001);
    }

    // Test cases for initQRStep
    @Test
    public void testInitQRStep() {
        // Test a typical case
        double[][] matrix = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        
        double[] hVec = {1.0, 2.0, 3.0};
        assertEquals(1, transformer.initQRStep(0, 2, new SchurTransformer.ShiftInfo(), hVec));
    }

    // Test cases for performDoubleQRStep
    @Test
    public void testPerformDoubleQRStep() {
        // Test a case where elements are non-zero
        double[][] matrix = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        
        double[] hVec = {1.0, 2.0, 3.0};
        transformer.performDoubleQRStep(0, 1, 2, new SchurTransformer.ShiftInfo(), hVec);
        
        assertEquals(0.0, transformer.getT()[2][0], 0.0001);
        assertEquals(0.0, transformer.getT()[2][1], 0.0001);
    }
}