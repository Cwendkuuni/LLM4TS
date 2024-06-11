import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelationConstructorEmpty() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNotNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testPearsonsCorrelationConstructorDoubleArray() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testPearsonsCorrelationConstructorRealMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        assertNotNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pc.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {3.0, 4.0};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        try {
            double correlation = pc.correlation(xArray, yArray);
            assertEquals(1.0, correlation, 0.0001);
        } catch (IllegalArgumentException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pc.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }

}