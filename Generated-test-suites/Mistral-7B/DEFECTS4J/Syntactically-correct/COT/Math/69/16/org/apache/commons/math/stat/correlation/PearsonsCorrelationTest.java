import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        Assert.assertNull(pearsonCorrelation.getCorrelationMatrix());
        Assert.assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test
    public void testRectangularArrayConstructor() {
        double[][] data = { {1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}, {5.0, 6.0, 7.0} };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
        Assert.assertEquals(3, correlationMatrix.getRowDimension());

        Assert.assertEquals(0.86602540378444, correlationMatrix.getEntry(0, 1), 0.01);
        Assert.assertEquals(0.81649658092773, correlationMatrix.getEntry(1, 0), 0.01);
        Assert.assertEquals(0.97320508075689, correlationMatrix.getEntry(1, 1), 0.01);
        Assert.assertEquals(0.81649658092773, correlationMatrix.getEntry(2, 1), 0.01);
        Assert.assertEquals(0.86602540378444, correlationMatrix.getEntry(1, 2), 0.01);
        Assert.assertEquals(0.86602540378444, correlationMatrix.getEntry(2, 1), 0.01);
        Assert.assertEquals(0.97320508075689, correlationMatrix.getEntry(2, 2), 0.01);
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(2, correlationMatrix.getColumnDimension());
        Assert.assertEquals(2, correlationMatrix.getRowDimension());

        Assert.assertEquals(0.81649658092773, correlationMatrix.getEntry(0, 1), 0.01);
        Assert.assertEquals(0.81649658092773, correlationMatrix.getEntry(1, 0), 0.01);
        Assert.assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.01);
        Assert.assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.01);
    }

    @Test(expected = MathRuntimeException.class)
    public void testIllegalArgumentExceptionForInsufficientData() {
        double[][] data = { {1.0}, {2.0} };
        new PearsonsCorrelation(data);
    }

    @Test(expected = MathRuntimeException.class)
    public void testIllegalArgumentExceptionForNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = { {1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}, {5.0, 6.0, 7.0} };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();

        Assert.assertNotNull(correlationStandardErrors);
        Assert.assertEquals(3, correlationStandardErrors.getColumnDimension());
        Assert.assertEquals(3, correlationStandardErrors.getRowDimension());

        Assert.assertEquals(0.54772255750611, correlationStandardErrors.getEntry(0, 1), 0.01);
        Assert.assertEquals(0.54772255750611, correlationStandardErrors.getEntry(1, 0), 0.01);
        Assert.assertEquals(0.0, correlationStandardErrors.getEntry(1, 1), 0.01);
        Assert.assertEquals(0.54772255750611, correlationStandardErrors.getEntry(1, 2), 0.01);
        Assert.assertEquals(0.54772255750611, correlationStandardErrors.getEntry(2, 1), 0.01);
        Assert.assertEquals(0.54772255750611, correlationStandardErrors.getEntry(2, 2), 0.01);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = { {1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}, {5.0, 6.0, 7.0} };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();

        Assert.assertNotNull(correlationPValues);
        Assert.assertEquals(3, correlationPValues.getColumnDimension());
        Assert.assertEquals(3, correlationPValues.getRowDimension());

        Assert.assertEquals(0.35714195901234, correlationPValues.getEntry(0, 1), 0.01);
        Assert.assertEquals(0.35714195901234, correlationPValues.getEntry(1, 0), 0.01);
        Assert.assertEquals(1.0, correlationPValues.getEntry(1, 1), 0.01);
        Assert.assertEquals(0.35714195901234, correlationPValues.getEntry(1, 2), 0.01);
        Assert.assertEquals(0.35714195901234, correlationPValues.getEntry(2, 1), 0.01);
        Assert.assertEquals(0.35714195901234, correlationPValues.getEntry(2, 2), 0.01);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = { {1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}, {5.0, 6.0, 7.0} };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pearsonCorrelation.computeCorrelationMatrix(data);

        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
        Assert.assertEquals(3, correlationMatrix.getRowDimension());

        Assert.assertEquals(0.86602540378444, correlationMatrix.getEntry(0, 1), 0.01);
        Assert.assertEquals(0.81649658092773, correlationMatrix.getEntry(1, 0), 0.01);
        Assert.assertEquals(0.97320508075689, correlationMatrix.getEntry(1, 1), 0.01);
        Assert.assertEquals(0.81649658092773, correlationMatrix.getEntry(2, 1), 0.01);
        Assert.assertEquals(0.86602540378444, correlationMatrix.getEntry(1, 2), 0.01);
        Assert.assertEquals(0.86602540378444, correlationMatrix.getEntry(2, 1), 0.01);
        Assert.assertEquals(0.97320508075689, correlationMatrix.getEntry(2, 2), 0.01);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0};
        double[] yArray = {2.0, 3.0, 4.0, 5.0};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(0.81649658092773, correlation, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidArrayDimensions() {
        double[] xArray = {1.0};
        double[] yArray = {2.0, 3.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }
}