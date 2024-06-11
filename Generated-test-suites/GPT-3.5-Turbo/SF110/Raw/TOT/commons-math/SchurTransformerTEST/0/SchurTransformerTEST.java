package org.apache.commons.math3.linear;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SchurTransformerTest {
  
  private static final double[][] MATRIX = {
      {1.0, 2.0, 3.0},
      {4.0, 5.0, 6.0},
      {7.0, 8.0, 9.0}
  };
  
  private static final double[][] H_MATRIX = {
      {1.0, 4.0, 7.0},
      {0.0, 5.0, 8.0},
      {0.0, 0.0, 9.0}
  };
  
  private static final double[][] P_MATRIX = {
      {1.0, 0.0, 0.0},
      {0.0, 1.0, 0.0},
      {0.0, 0.0, 1.0}
  };
  
  private static final double[][] TEST_MATRIX = {
      {1.0, 2.0, 3.0},
      {4.0, 5.0, 6.0},
      {7.0, 8.0, 9.0}
  };
  
  private SchurTransformer schurTransformer;
  
  @Before
  public void setup() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(MATRIX);
    schurTransformer = new SchurTransformer(matrix);
  }
  
  @Test
  public void testGetP() {
    RealMatrix expectedP = MatrixUtils.createRealMatrix(P_MATRIX);
    RealMatrix actualP = schurTransformer.getP();
    Assert.assertEquals(expectedP, actualP);
  }
  
  @Test
  public void testGetPT() {
    RealMatrix expectedPT = MatrixUtils.createRealMatrix(P_MATRIX).transpose();
    RealMatrix actualPT = schurTransformer.getPT();
    Assert.assertEquals(expectedPT, actualPT);
  }
  
  @Test
  public void testGetT() {
    RealMatrix expectedT = MatrixUtils.createRealMatrix(H_MATRIX);
    RealMatrix actualT = schurTransformer.getT();
    Assert.assertEquals(expectedT, actualT);
  }
  
  @Test
  public void testTransform() {
    RealMatrix expectedT = MatrixUtils.createRealMatrix(H_MATRIX);
    schurTransformer.transform();
    RealMatrix actualT = schurTransformer.getT();
    Assert.assertEquals(expectedT, actualT);
  }
  
  @Test
  public void testGetNorm() {
    double expectedNorm = 45.0;
    double actualNorm = schurTransformer.getNorm();
    Assert.assertEquals(expectedNorm, actualNorm, 0.0001);
  }
  
  @Test
  public void testFindSmallSubDiagonalElement() {
    int expectedIndex = 1;
    double norm = schurTransformer.getNorm();
    int actualIndex = schurTransformer.findSmallSubDiagonalElement(2, norm);
    Assert.assertEquals(expectedIndex, actualIndex);
  }
  
  @Test
  public void testComputeShift() {
    SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
    schurTransformer.computeShift(0, 2, 1, shift);
    double expectedX = 9.0;
    double expectedY = 0.0;
    double expectedW = 0.0;
    double expectedExShift = 9.0;
    Assert.assertEquals(expectedX, shift.x, 0.0001);
    Assert.assertEquals(expectedY, shift.y, 0.0001);
    Assert.assertEquals(expectedW, shift.w, 0.0001);
    Assert.assertEquals(expectedExShift, shift.exShift, 0.0001);
  }
  
  @Test
  public void testInitQRStep() {
    double[] hVec = new double[3];
    int expectedIm = 0;
    int actualIm = schurTransformer.initQRStep(0, 2, new SchurTransformer.ShiftInfo(), hVec);
    Assert.assertEquals(expectedIm, actualIm);
  }
  
  @Test
  public void testPerformDoubleQRStep() {
    schurTransformer.performDoubleQRStep(0, 0, 2, new SchurTransformer.ShiftInfo(), new double[3]);
    RealMatrix expectedMatrixT = MatrixUtils.createRealMatrix(TEST_MATRIX);
    RealMatrix actualMatrixT = schurTransformer.getT();
    Assert.assertEquals(expectedMatrixT, actualMatrixT);
  }

}