import org.junit.Test;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor1() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12}, {12, 37}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomp.getRootMatrix();
            int rank = decomp.getRank();
            Assert.assertEquals(rootMatrix.getEntry(0, 0), 2.0, 0.0001);
            Assert.assertEquals(rootMatrix.getEntry(1, 0), 6.0, 0.0001);
            Assert.assertEquals(rootMatrix.getEntry(1, 1), 1.0, 0.0001);
            Assert.assertEquals(rank, 2);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructor2() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12}, {12, 37}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.001);
            RealMatrix rootMatrix = decomp.getRootMatrix();
            int rank = decomp.getRank();
            Assert.assertEquals(rootMatrix.getEntry(0, 0), 2.0, 0.0001);
            Assert.assertEquals(rootMatrix.getEntry(1, 0), 6.0, 0.0001);
            Assert.assertEquals(rootMatrix.getEntry(1, 1), 1.0, 0.0001);
            Assert.assertEquals(rank, 2);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructor3() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-4, 12}, {12, 37}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(e.getPrincipal(), -4.0, 0.0001);
            Assert.assertEquals(e.getIndex(), 0);
            Assert.assertEquals(e.getTolerance(), 0.0, 0.0001);
        }
    }
    
    @Test
    public void testConstructor4() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12}, {12, 37}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.001);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(e.getPrincipal(), 0.0, 0.0001);
            Assert.assertEquals(e.getIndex(), 0);
            Assert.assertEquals(e.getTolerance(), 0.001, 0.0001);
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12}, {12, 37}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomp.getRootMatrix();
            Assert.assertEquals(rootMatrix.getEntry(0, 0), 2.0, 0.0001);
            Assert.assertEquals(rootMatrix.getEntry(1, 0), 6.0, 0.0001);
            Assert.assertEquals(rootMatrix.getEntry(1, 1), 1.0, 0.0001);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12}, {12, 37}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
            int rank = decomp.getRank();
            Assert.assertEquals(rank, 2);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

}