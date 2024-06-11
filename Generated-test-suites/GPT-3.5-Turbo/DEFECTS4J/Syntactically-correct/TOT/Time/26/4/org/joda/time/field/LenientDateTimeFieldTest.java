import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.LenientDateTimeField;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    private static LenientDateTimeField lenientDateTimeField;
  
    @BeforeClass
    public static void setUp() {
        DateTimeField field = ISOChronology.getInstance().dayOfYear();
        lenientDateTimeField = (LenientDateTimeField) LenientDateTimeField.getInstance(field, ISOChronology.getInstance());
    }

    @AfterClass
    public static void tearDown() {
        lenientDateTimeField = null;
    }

    @Test
    public void testIsLenient() {
        assertEquals(true, lenientDateTimeField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = 1514764800000L; // 2018-01-01T00:00:00Z
        int value = 365;
        
        long result = lenientDateTimeField.set(instant, value);
        
        assertEquals(1514678400000L, result); // 2017-12-31T00:00:00Z
    }

    @Test
    public void testSet_OutOfBounds() {
        long instant = 1514764800000L; // 2018-01-01T00:00:00Z
        int value = 366;
        
        long result = lenientDateTimeField.set(instant, value);
        
        assertEquals(1514592000000L, result); // 2017-12-30T00:00:00Z
    }

    @Test
    public void testSet_OutOfBoundsNegative() {
        long instant = 1514764800000L; // 2018-01-01T00:00:00Z
        int value = -1;
        
        long result = lenientDateTimeField.set(instant, value);
        
        assertEquals(1514851200000L, result); // 2018-01-02T00:00:00Z
    }

    @Test
    public void testSet_Typical() {
        long instant = 1514764800000L; // 2018-01-01T00:00:00Z
        int value = 1;
        
        long result = lenientDateTimeField.set(instant, value);
        
        assertEquals(1514851200000L, result); // 2018-01-02T00:00:00Z
    }

    @Test
    public void testSet_Edge() {
        long instant = 1514764800000L; // 2018-01-01T00:00:00Z
        int value = 365_000;
        
        long result = lenientDateTimeField.set(instant, value);
        
        assertEquals(11676096000000L, result); // 3018-12-31T00:00:00Z
    }
}