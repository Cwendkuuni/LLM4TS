import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorNoData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertEquals(pearsonsCorrelation.getCorrelationMatrix(), null);
        assertEquals(pearsonsCorrelation.getCorrelationStandardErrors(), null);
        assertEquals(pearsonsCorrelation.getCorrelationPValues(), null);
    }

    @Test
    public void testCorrelationArray() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double expectedCorrelation = 1.0;
        try {
            double calculatedCorrelation = pearsonsCorrelation.correlation(xArray, yArray);
            assertEquals(expectedCorrelation, calculatedCorrelation, 0.0001);
        } catch (IllegalArgumentException e) {
            fail("Exception thrown when calculating correlation");
        }
    }

    @Test
    public void testCorrelationArrayDifferentLength() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0};
        try {
            pearsonsCorrelation.correlation(xArray, yArray);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCorrelationArrayInsufficientData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[] xArray = {1.0};
        double[] yArray = {4.0};
        try {
            pearsonsCorrelation.correlation(xArray, yArray);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // Add more test cases for other methods

}