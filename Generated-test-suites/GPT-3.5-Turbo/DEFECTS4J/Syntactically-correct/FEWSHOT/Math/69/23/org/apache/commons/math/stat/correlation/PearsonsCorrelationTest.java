import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertEquals(null, correlation.getCorrelationMatrix());
    }

    @Test
    public void testArrayConstructor() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(2, 2), 0.001);
    }

    @Test
    public void testArrayConstructorWithInvalidData() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6, 7}};
        try {
            PearceonsCorrelation correlation = new PearsonsCorrelation(data);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testMatrixConstructor() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(2, 2), 0.001);
    }

    // TODO: Add tests for the remaining constructors and methods

}