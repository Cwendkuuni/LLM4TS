import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;

    @Before
    public void setUp() {
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        pearsonsCorrelation = new PearsonsCorrelation(data);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertThat(correlationMatrix.getColumnDimension(), equalTo(3));
        assertThat(correlationMatrix.getRowDimension(), equalTo(3));
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
        assertThat(correlationStandardErrors.getColumnDimension(), equalTo(3));
        assertThat(correlationStandardErrors.getRowDimension(), equalTo(3));
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        assertNotNull(correlationPValues);
        assertThat(correlationPValues.getColumnDimension(), equalTo(3));
        assertThat(correlationPValues.getRowDimension(), equalTo(3));
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 4, 7};
        double[] yArray = {2, 5, 8};
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        assertThat(correlation, closeTo(1, 0.001));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidLength() {
        double[] xArray = {1, 4, 7};
        double[] yArray = {2, 5};
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {2};
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {
            {1, 2},
            {2, 4}
        };
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
        assertThat(correlationMatrix.getColumnDimension(), equalTo(2));
        assertThat(correlationMatrix.getRowDimension(), equalTo(2));
        assertThat(correlationMatrix.getEntry(0, 0), closeTo(1, 0.001));
        assertThat(correlationMatrix.getEntry(0, 1), closeTo(1, 0.001));
        assertThat(correlationMatrix.getEntry(1, 0), closeTo(1, 0.001));
        assertThat(correlationMatrix.getEntry(1, 1), closeTo(1, 0.001));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataInsufficientRows() {
        double[][] data = {{1, 2}};
        pearsonsCorrelation.checkSufficientData(new BlockRealMatrix(data));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataInsufficientColumns() {
        double[][] data = {{1}, {2}};
        pearsonsCorrelation.checkSufficientData(new BlockRealMatrix(data));
    }

}