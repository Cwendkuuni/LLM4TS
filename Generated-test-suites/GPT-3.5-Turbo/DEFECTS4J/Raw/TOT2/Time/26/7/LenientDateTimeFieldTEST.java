import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.ISOChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField field;

    @Before
    public void setUp() {
        field = new LenientDateTimeField(DateTimeFieldType.year(), ISOChronology.getInstance());
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void testSet() {
        long instant = 1546300800000L; // 01-01-2019
        int value = 2021; // year 2021
        long result = field.set(instant, value);
        long expected = 1609459200000L; // 01-01-2021
        assertEquals(expected, result);
    }

    // Add more test cases for other public methods and edge cases here

}