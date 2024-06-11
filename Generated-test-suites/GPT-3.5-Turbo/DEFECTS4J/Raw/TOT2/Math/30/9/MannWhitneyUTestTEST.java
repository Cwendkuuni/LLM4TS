import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    MannWhitneyUTest testInstance;

    @Before
    public void setUp() {
        testInstance = new MannWhitneyUTest();
    }

    @Test
    public void testEnsureDataConformanceNonNull() {
        try {
            testInstance.ensureDataConformance(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        } catch (Exception e) {
            fail("No exception should be thrown for non-null input arrays.");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullInputX() {
        testInstance.ensureDataConformance(null, new double[]{1.0, 2.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullInputY() {
        testInstance.ensureDataConformance(new double[]{1.0, 2.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyInputX() {
        testInstance.ensureDataConformance(new double[]{}, new double[]{1.0, 2.0});
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyInputY() {
        testInstance.ensureDataConformance(new double[]{1.0, 2.0}, new double[]{});
    }

    // Add more test cases for other methods

}