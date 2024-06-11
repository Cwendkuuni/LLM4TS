import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;
    private final double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};

    @Before
    public void setUp() {
        pearsonsCorrelation = new PearsonsCorrelation(data);
    }

    @Test
    public void testGetCorrelationMatrix() {
        RealMatrix expected = new BlockRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(0, 1, 1.0);
        expected.setEntry(0, 2, 1.0);
        expected.setEntry(1, 0, 1.0);
        expected.setEntry(1, 1, 1.0);
        expected.setEntry(1, 2, 1.0);
        expected.setEntry(2, 0, 1.0);
        expected.setEntry(2, 1, 1.0);
        expected.setEntry(2, 2, 1.0);

        assertEquals(expected, pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        RealMatrix expected = new BlockRealMatrix(3, 3);
        expected.setEntry(0, 0, 0.0);
        expected.setEntry(0, 1, 0.0);
        expected.setEntry(0, 2, 0.0);
        expected.setEntry(1, 0, 0.0);
        expected.setEntry(1, 1, 0.0);
        expected.setEntry(1, 2, 0.0);
        expected.setEntry(2, 0, 0.0);
        expected.setEntry(2, 1, 0.0);
        expected.setEntry(2, 2, 0.0);

        assertEquals(expected, pearsonsCorrelation.getCorrelationStandardErrors());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        RealMatrix expected = new BlockRealMatrix(3, 3);
        expected.setEntry(0, 0, 0.0);
        expected.setEntry(0, 1, 0.0);
        expected.setEntry(0, 2, 0.0);
        expected.setEntry(1, 0, 0.0);
        expected.setEntry(1, 1, 0.0);
        expected.setEntry(1, 2, 0.0);
        expected.setEntry(2, 0, 0.0);
        expected.setEntry(2, 1, 0.0);
        expected.setEntry(2, 2, 0.0);

        assertEquals(expected, pearsonsCorrelation.getCorrelationPValues());
    }

    @Test
    public void testCorrelation() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        try {
            double expected = 1.0;
            double result = pearsonsCorrelation.correlation(x, y);
            assertEquals(expected, result, 0);
        } catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidArrayLength() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};

        pearsonsCorrelation.correlation(x, y);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] x = {1.0};
        double[] y = {4.0};

        pearsonsCorrelation.correlation(x, y);
    }
}