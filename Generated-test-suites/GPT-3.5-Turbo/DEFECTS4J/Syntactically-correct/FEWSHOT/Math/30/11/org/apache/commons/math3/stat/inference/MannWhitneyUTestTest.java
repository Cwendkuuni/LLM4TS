import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Test;

public class MannWhitneyUTestTest {
    private final double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
    private final double[] y = {1.5, 2.5, 3.5, 4.5, 5.5};

    @Test
    public void testEnsureDataConformance() {
        MannWhitneyUTest testObject = new MannWhitneyUTest();
        assertNotNull(testObject);
        try {
            testObject.ensureDataConformance(x, y);
        } catch (NullArgumentException e) {
            // exception should not be thrown
        } catch (NoDataException e) {
            // exception should not be thrown
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArguments() {
        MannWhitneyUTest testObject = new MannWhitneyUTest();
        assertNotNull(testObject);
        try {
            testObject.ensureDataConformance(null, y);
        } catch (NullArgumentException e) {
            throw e;
        } catch (NoDataException e) {
            // exception should not be thrown
        }
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyArguments() {
        MannWhitneyUTest testObject = new MannWhitneyUTest();
        assertNotNull(testObject);
        try {
            testObject.ensureDataConformance(new double[0], y);
        } catch (NullArgumentException e) {
            // exception should not be thrown
        } catch (NoDataException e) {
            throw e;
        }
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest testObject = new MannWhitneyUTest();
        assertNotNull(testObject);
        double[] concatenated = testObject.concatenateSamples(x, y);
        assertEquals(10, concatenated.length);
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 1.5, 2.5, 3.5, 4.5, 5.5};
        for (int i = 0; i < concatenated.length; i++) {
            assertEquals(expected[i], concatenated[i], 0);
        }
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest testObject = new MannWhitneyUTest();
        assertNotNull(testObject);
        try {
            double u = testObject.mannWhitneyU(x, y);
            assertEquals(25, u, 0);
        } catch (NullArgumentException | NoDataException e) {
            // exceptions should not be thrown
        }
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest testObject = new MannWhitneyUTest();
        assertNotNull(testObject);
        try {
            double pValue = testObject.calculateAsymptoticPValue(15, 5, 5);
            assertEquals(0.04449616415276067, pValue, 0);
        } catch (ConvergenceException | MaxCountExceededException e) {
            // exceptions should not be thrown
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest testObject = new MannWhitneyUTest();
        assertNotNull(testObject);
        try {
            double pValue = testObject.mannWhitneyUTest(x, y);
            assertEquals(0.04449616415276067, pValue, 0);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            // exceptions should not be thrown
        }
    }
}