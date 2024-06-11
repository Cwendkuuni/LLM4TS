package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SchurTransformerTest {
  @Test
  public void testConstructor() {
    double[][] inputMatrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);

    SchurTransformer transformer = new SchurTransformer(matrix);

    assertNotNull(transformer.getP());
    assertNotNull(transformer.getPT());
    assertNotNull(transformer.getT());
  }

  @Test(expected = NonSquareMatrixException.class)
  public void testConstructorNonSquareMatrix() {
    double[][] inputMatrix = {{1, 2, 3}, {4, 5, 6}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);

    SchurTransformer transformer = new SchurTransformer(matrix);
  }

  @Test
  public void testGetP() {
    double[][] inputMatrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);

    SchurTransformer transformer = new SchurTransformer(matrix);
    RealMatrix p = transformer.getP();

    assertEquals(1, p.getRowDimension());
    assertEquals(1, p.getColumnDimension());
  }

  @Test
  public void testGetPT() {
    double[][] inputMatrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);

    SchurTransformer transformer = new SchurTransformer(matrix);
    RealMatrix pt = transformer.getPT();

    assertEquals(1, pt.getRowDimension());
    assertEquals(1, pt.getColumnDimension());
  }

  @Test
  public void testGetT() {
    double[][] inputMatrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);

    SchurTransformer transformer = new SchurTransformer(matrix);
    RealMatrix t = transformer.getT();

    assertEquals(1, t.getRowDimension());
    assertEquals(1, t.getColumnDimension());
  }

  @Test
  public void testTransform() {
    double[][] inputMatrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);

    SchurTransformer transformer = new SchurTransformer(matrix);
    transformer.transform();

    // Add assertion here to test the transformation result.
  }
}