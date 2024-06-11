import org.junit.Test;
import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        // Create a 3x3 matrix
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Create a SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(matrix);

        // Get the P matrix
        RealMatrix result = transformer.getP();

        // Compare the result with the expected output
        double[][] expectedData = {{-0.21132486540518708, -0.8276701389553039, 0.516185036810086}, {-0.527011621640259, -0.14231483827328512, -0.8381127310139486}, {-0.8226983778753309, 0.5430404624087335, 0.1660414257031284}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);

        assertTrue(result.equals(expected));
    }

    @Test
    public void testGetPT() {
        // Create a 3x3 matrix
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Create a SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(matrix);

        // Get the PT matrix
        RealMatrix result = transformer.getPT();

        // Compare the result with the expected output
        double[][] expectedData = {{-0.21132486540518708, -0.5270116216402591, -0.8226983778753305}, {-0.8276701389553042, -0.14231483827328543, 0.5430404624087319}, {0.5161850368100877, -0.8381127310139489, 0.16604142570312868}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);

        assertTrue(result.equals(expected));
    }

    @Test
    public void testGetT() {
        // Create a 3x3 matrix
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Create a SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(matrix);

        // Get the T matrix
        RealMatrix result = transformer.getT();

        // Compare the result with the expected output
        double[][] expectedData = {{1.0, 5.87082869338697, 6.113204413202204}, {0.0, -0.7408286933869703, 0.33333333333333204}, {0.0, 0.37041434669349453, 0.7408286933869703}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);

        assertTrue(result.equals(expected));
    }
}