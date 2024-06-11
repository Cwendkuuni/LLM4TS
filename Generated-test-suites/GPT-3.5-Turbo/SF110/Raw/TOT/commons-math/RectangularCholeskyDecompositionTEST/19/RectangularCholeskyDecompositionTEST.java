import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.junit.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor_1_positiveDefiniteMatrix() {
        double[][] data = {{4, -1}, {-1, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(4, root.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(0, 1), 0.0001);
        Assert.assertEquals(-1, root.getEntry(1, 0), 0.0001);
        Assert.assertEquals(3, root.getEntry(1, 1), 0.0001);
        Assert.assertEquals(2, decomposition.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_1_nonPositiveDefiniteMatrix() {
        double[][] data = {{4, -2}, {-2, 8}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testConstructor_1_zeroRank() {
        double[][] data = {{0, 0}, {0, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(0, root.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(0, 1), 0.0001);
        Assert.assertEquals(0, root.getEntry(1, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(1, 1), 0.0001);
        Assert.assertEquals(0, decomposition.getRank());
    }
    
    @Test
    public void testConstructor_1_smallValue() {
        double[][] data = {{1, 0}, {0, 6.25e-8}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(1, root.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(0, 1), 0.0001);
        Assert.assertEquals(0, root.getEntry(1, 0), 0.0001);
        Assert.assertEquals(0.0001, root.getEntry(1, 1), 0.0001);
        Assert.assertEquals(2, decomposition.getRank());
    }
    
    @Test
    public void testConstructor_1_largeValue() {
        double[][] data = {{1, 0}, {0, 1e10}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(1, root.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(0, 1), 0.0001);
        Assert.assertEquals(0, root.getEntry(1, 0), 0.0001);
        Assert.assertEquals(1e5, root.getEntry(1, 1), 0.0001);
        Assert.assertEquals(2, decomposition.getRank());
    }
    
    @Test
    public void testConstructor_1_smallSmallValue() {
        double[][] data = {{1, 0}, {0, 1.25e-12}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(1, root.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(0, 1), 0.0001);
        Assert.assertEquals(0, root.getEntry(1, 0), 0.0001);
        Assert.assertEquals(0.0001, root.getEntry(1, 1), 0.0001);
        Assert.assertEquals(2, decomposition.getRank());
    }
    
    @Test
    public void testConstructor_2_positiveDefiniteMatrix() {
        double[][] data = {{4, -1}, {-1, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(4, root.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(0, 1), 0.0001);
        Assert.assertEquals(-1, root.getEntry(1, 0), 0.0001);
        Assert.assertEquals(3, root.getEntry(1, 1), 0.0001);
        Assert.assertEquals(2, decomposition.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_2_nonPositiveDefiniteMatrix() {
        double[][] data = {{4, -2}, {-2, 8}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        new RectangularCholeskyDecomposition(matrix, 1e-8);
    }
    
    @Test
    public void testConstructor_2_zeroRank() {
        double[][] data = {{0, 0}, {0, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(0, root.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(0, 1), 0.0001);
        Assert.assertEquals(0, root.getEntry(1, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(1, 1), 0.0001);
        Assert.assertEquals(0, decomposition.getRank());
    }
    
    @Test
    public void testConstructor_2_smallValue() {
        double[][] data = {{1, 0}, {0, 6.25e-8}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(1, root.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(0, 1), 0.0001);
        Assert.assertEquals(0, root.getEntry(1, 0), 0.0001);
        Assert.assertEquals(0.00001, root.getEntry(1, 1), 0.0001);
        Assert.assertEquals(2, decomposition.getRank());
    }
    
    @Test
    public void testConstructor_2_largeValue() {
        double[][] data = {{1, 0}, {0, 1e10}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(1, root.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(0, 1), 0.0001);
        Assert.assertEquals(0, root.getEntry(1, 0), 0.0001);
        Assert.assertEquals(100, root.getEntry(1, 1), 0.0001);
        Assert.assertEquals(2, decomposition.getRank());
    }
    
    @Test
    public void testConstructor_2_smallSmallValue() {
        double[][] data = {{1, 0}, {0, 1.25e-12}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(1, root.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0, root.getEntry(0, 1), 0.0001);
        Assert.assertEquals(0, root.getEntry(1, 0), 0.0001);
        Assert.assertEquals(0.00001, root.getEntry(1, 1), 0.0001);
        Assert.assertEquals(2, decomposition.getRank());
    }
}