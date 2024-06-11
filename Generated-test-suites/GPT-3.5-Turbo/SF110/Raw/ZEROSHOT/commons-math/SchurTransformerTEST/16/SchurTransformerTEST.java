import static org.junit.Assert.*;
import org.junit.*;

public class SchurTransformerTest {
  
  private RealMatrix matrix;
  private SchurTransformer transformer;
  
  @Before
  public void setUp() {
    double[][] data = {
      {1.0, 2.0, 3.0},
      {4.0, 5.0, 6.0},
      {7.0, 8.0, 9.0}
    };
    matrix = MatrixUtils.createRealMatrix(data);
    transformer = new SchurTransformer(matrix);
  }
  
  @Test(expected = NonSquareMatrixException.class)
  public void constructor_nonSquareMatrix_exceptionThrown() {
    double[][] data = {
      {1.0, 2.0, 3.0},
      {4.0, 5.0, 6.0}
    };
    RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(data);
    new SchurTransformer(nonSquareMatrix);
  }
  
  @Test
  public void getP_cachedPTNotNull() {
    assertNotNull(transformer.getPT());
  }
  
  @Test
  public void getP_cachedTNotNull() {
    assertNotNull(transformer.getT());
  }
  
  @Test
  public void getP_cachedPNotNull() {
    assertNotNull(transformer.getP());
  }
  
  @Test
  public void transform_matrixTLengthEqualToMatrixLength() {
    assertEquals(matrix.getRowDimension(), transformer.getT().getRowDimension());
    assertEquals(matrix.getColumnDimension(), transformer.getT().getColumnDimension());
  }
  
  @Test
  public void getNorm_resultIsNotNegative() {
    assertTrue(transformer.getNorm() >= 0.0);
  }
  
  @Test
  public void findSmallSubDiagonalElement_resultIsValidIndex() {
    int startIdx = 2;
    double norm = transformer.getNorm();
    int result = transformer.findSmallSubDiagonalElement(startIdx, norm);
    assertTrue(result >= 0);
    assertTrue(result < startIdx);
  }
  
  @Test
  public void computeShift_shiftIsValid() {
    int l = 0;
    int idx = 2;
    int iteration = 5;
    SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
    
    transformer.computeShift(l, idx, iteration, shift);
    
    assertTrue(shift.x >= 0.0);
    assertTrue(shift.y >= 0.0);
    assertTrue(shift.w < 0.0);
  }
  
  @Test
  public void initQRStep_imIsValidIndex() {
    int il = 0;
    int iu = 2;
    SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
    double[] hVec = new double[3];
    
    int result = transformer.initQRStep(il, iu, shift, hVec);
    
    assertTrue(result >= il);
    assertTrue(result <= iu);
  }
  
  // Add more test cases to fully test all methods in the SchurTransformer class
  
}