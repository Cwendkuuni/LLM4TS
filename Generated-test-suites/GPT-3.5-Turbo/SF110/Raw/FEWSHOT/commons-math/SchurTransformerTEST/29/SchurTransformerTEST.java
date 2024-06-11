package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SchurTransformerTest {
    @Test
    public void testConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][] {{0.6666666666666666, -0.6666666666666666}, 
                                                                        {0.7453559924999299, 0.7453559924999301}});
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][] {{0.6666666666666666, 0.7453559924999299}, 
                                                                         {-0.6666666666666666, 0.7453559924999301}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][] {{2.9999999999999996, 0.0}, 
                                                                        {0.0, 1.0000000000000002}});
        
        assertEquals(expectedP.getData(), transformer.getP().getData());
        assertEquals(expectedPT.getData(), transformer.getPT().getData());
        assertEquals(expectedT.getData(), transformer.getT().getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1, 2, 3}, {4, 5, 6}});
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
}