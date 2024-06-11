import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.MathException;
import org.apache.commons.math.util.MathUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;
    private double[][] testData;

    @Before
    public void setUp() {
        testData = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        pearsonsCorrelation = new PearsonsCorrelation(testData);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] expectedCorrelationMatrix = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        double[][] actualCorrelationMatrix = pearsonsCorrelation.getCorrelationMatrix().getData();
        for (int i = 0; i < expectedCorrelationMatrix.length; i++) {
            for (int j = 0; j < expectedCorrelationMatrix[i].length; j++) {
                assertEquals(expectedCorrelationMatrix[i][j], actualCorrelationMatrix[i][j], MathUtils.EPSILON);
            }
        }
    }

    @Test
    public void testComputeCorrelationMatrixWithSingleVariable() {
        double[][] singleVariableData = {{1}, {2}, {3}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(singleVariableData);
        double[][] expectedCorrelationMatrix = {{1}};
        double[][] actualCorrelationMatrix = correlation.getCorrelationMatrix().getData();
        for (int i = 0; i < expectedCorrelationMatrix.length; i++) {
            for (int j = 0; j < expectedCorrelationMatrix[i].length; j++) {
                assertEquals(expectedCorrelationMatrix[i][j], actualCorrelationMatrix[i][j], MathUtils.EPSILON);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeCorrelationMatrixWithInsufficientData() {
        double[][] insufficientData = {{1}, {2}};
        new PearsonsCorrelation(insufficientData);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] expectedStandardErrors = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        double[][] actualStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors().getData();
        for (int i = 0; i < expectedStandardErrors.length; i++) {
            for (int j = 0; j < expectedStandardErrors[i].length; j++) {
                assertEquals(expectedStandardErrors[i][j], actualStandardErrors[i][j], MathUtils.EPSILON);
            }
        }
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] expectedPValues = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        double[][] actualPValues = pearsonsCorrelation.getCorrelationPValues().getData();
        for (int i = 0; i < expectedPValues.length; i++) {
            for (int j = 0; j < expectedPValues[i].length; j++) {
                assertEquals(expectedPValues[i][j], actualPValues[i][j], MathUtils.EPSILON);
            }
        }
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expectedCorrelation = 1.0;
        double actualCorrelation = pearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(expectedCorrelation, actualCorrelation, MathUtils.EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithMismatchedArrayLengths() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {2};
        pearsonsCorrelation.correlation(xArray, yArray);
    }
}