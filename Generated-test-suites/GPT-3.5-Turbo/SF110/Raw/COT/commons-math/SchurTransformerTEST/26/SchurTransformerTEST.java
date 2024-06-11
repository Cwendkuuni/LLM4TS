import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SchurTransformerTest {

    private SchurTransformer schurTransformer;
    
    @Before
    public void setUp() {
        double[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        schurTransformer = new SchurTransformer(new RealMatrix(matrix));
    }
    
    @Test
    public void testGetP() {
        RealMatrix expectedP = new RealMatrix(new double[][]{{0.2311139827751778, 1.0, 0.5477225575051661},
                                                             {0.5253220933012345, 0.0, -0.8192011688617589},
                                                             {0.8195302038272915, 0.0, 0.16744473665122055}});
        assertTrue(schurTransformer.getP().equals(expectedP));
    }
    
    @Test
    public void testGetPT() {
        RealMatrix expectedPt = new RealMatrix(new double[][]{{0.2311139827751778, 0.5253220933012345, 0.8195302038272915},
                                                              {1.0, 0.0, 0.0},
                                                              {0.5477225575051661, -0.8192011688617589, 0.16744473665122055}});
        assertTrue(schurTransformer.getPT().equals(expectedPt));
    }
    
    @Test
    public void testGetT() {
        RealMatrix expectedT = new RealMatrix(new double[][]{{14.906759795570197, 9.58885211678849, -0.2599090958097521},
                                                             {0.0, -0.28235294117647297, -3.8908501256941934},
                                                             {0.0, 0.0, -7.62340764230218}});
        assertTrue(schurTransformer.getT().equals(expectedT));
    }
    
    // Additional test cases for edge cases and exceptions
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        SchurTransformer schurTransformer = new SchurTransformer(new RealMatrix(matrix));
    }
}