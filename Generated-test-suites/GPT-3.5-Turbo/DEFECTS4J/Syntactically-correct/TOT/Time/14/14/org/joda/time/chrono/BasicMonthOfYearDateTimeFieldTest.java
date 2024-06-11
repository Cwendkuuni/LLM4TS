import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setup() {
        BasicChronology chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);
    }

    @Test
    public void testGet() {
        long instant = 1577836800L; // 2020-01-01T00:00:00Z
        assertEquals(1, field.get(instant));
    }

    @Test
    public void testAdd() {
        long instant = 1577836800L; // 2020-01-01T00:00:00Z
        long result = field.add(instant, 2);
        assertEquals(3, field.get(result));
    }

    @Test
    public void testAddWrapField() {
        long instant = 1577836800L; // 2020-01-01T00:00:00Z
        long result = field.addWrapField(instant, 2);
        assertEquals(3, field.get(result));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = 1577836800L; // 2020-01-01T00:00:00Z
        long instant2 = 1580515200L; // 2020-02-01T00:00:00Z
        long difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(1, difference);
    }

    @Test
    public void testIsValidValue() {
        assertTrue(field.isValidValue(1));
        assertFalse(field.isValidValue(13));
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidValue() {
        long instant = 1577836800L; // 2020-01-01T00:00:00Z
        field.set(instant, 13);
    }

    @Test
    public void testRoundFloor() {
        long instant = 1577836800L; // 2020-01-01T00:00:00Z
        long result = field.roundFloor(instant);
        assertEquals(1577836800L, result);
    }

    @Test
    public void testRemainder() {
        long instant = 1577836800L; // 2020-01-01T00:00:00Z
        long result = field.remainder(instant);
        assertEquals(0, result);
    }

}