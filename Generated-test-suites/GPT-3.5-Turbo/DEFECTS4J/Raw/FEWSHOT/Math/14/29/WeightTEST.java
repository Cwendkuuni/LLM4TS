package org.apache.commons.math3.optimization;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WeightTest {
    
    // Test data
    private static final double[] WEIGHT_VALUES = {1.0, 2.0, 3.0};
    private static final RealMatrix WEIGHT_MATRIX = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
    
    private Weight weight;
    
    @Before
    public void setUp() {
        weight = new Weight(WEIGHT_VALUES);
    }
    
    @Test
    public void testConstructor() {
        try {
            new Weight(new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}}));
            fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Expected NonSquareMatrixException to be thrown, but got " + e.getClass().getSimpleName());
        }
        
        RealMatrix weightMatrix = weight.getWeight();
        assertNotNull("Weight matrix should not be null", weightMatrix);
        assertEquals("Weight matrix should have same dimensions as input array", WEIGHT_VALUES.length, weightMatrix.getRowDimension());
        assertEquals("Weight matrix should have same dimensions as input array", WEIGHT_VALUES.length, weightMatrix.getColumnDimension());
        
        for (int i = 0; i < weightMatrix.getRowDimension(); i++) {
            for (int j = 0; j < weightMatrix.getColumnDimension(); j++) {
                if (i == j) assertEquals("Diagonal elements should match input values", WEIGHT_VALUES[i], weightMatrix.getEntry(i, j), 0.0);
                else assertEquals("Off-diagonal elements should be zero", 0.0, weightMatrix.getEntry(i, j), 0.0);
            }
        }
    }
    
    @Test
    public void testGetWeight() {
        RealMatrix weightMatrix = weight.getWeight();
        assertNotNull("Weight matrix should not be null", weightMatrix);
        assertEquals("Weight matrix should have same dimensions as input array", WEIGHT_VALUES.length, weightMatrix.getRowDimension());
        assertEquals("Weight matrix should have same dimensions as input array", WEIGHT_VALUES.length, weightMatrix.getColumnDimension());
        
        for (int i = 0; i < weightMatrix.getRowDimension(); i++) {
            for (int j = 0; j < weightMatrix.getColumnDimension(); j++) {
                if (i == j) assertEquals("Diagonal elements should match input values", WEIGHT_VALUES[i], weightMatrix.getEntry(i, j), 0.0);
                else assertEquals("Off-diagonal elements should be zero", 0.0, weightMatrix.getEntry(i, j), 0.0);
            }
        }
    }
}