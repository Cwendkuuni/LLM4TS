import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.Array2DRowRealMatrix;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorWithoutData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNotNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testConstructorWithData() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc.getCorrelationMatrix());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataForConstructor() {
        double[][] data = {{1.0, 2.0}, {3.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double correlation = pc.correlation(xArray, yArray);
        assertEquals(1.0, correlation, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0};
        double[] yArray = {4.0, 5.0, 6.0};
        pc.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {{1.0, 0.5}, {0.5, 1.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(new Array2DRowRealMatrix(covarianceData));
        assertNotNull(pc.getCorrelationMatrix());
    }
}