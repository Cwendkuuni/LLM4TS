import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private long testInstant;

    @Before
    public void setup() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.FEBRUARY);
        testInstant = 100000000L;
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        assertEquals(2, field.get(testInstant));
    }

    @Test
    public void testAdd_int() {
        long result = field.add(testInstant, 3);
        assertEquals(5, field.get(result));
    }

    @Test
    public void testAdd_long() {
        long result = field.add(testInstant, 10000000000L);
        assertEquals(2, field.get(result));
    }

    @Test
    public void testAddWrapField() {
        long result = field.addWrapField(testInstant, 10);
        assertEquals(12, field.get(result));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long result = field.getDifferenceAsLong(testInstant, testInstant + 10000000L);
        assertEquals(0, result);
    }

    @Test
    public void testSet() {
        long result = field.set(testInstant, 4);
        assertEquals(4, field.get(result));
    }

    @Test
    public void testIsLeap() {
        assertFalse(field.isLeap(testInstant));
    }

    @Test
    public void testGetLeapAmount() {
        assertEquals(0, field.getLeapAmount(testInstant));
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long result = field.roundFloor(testInstant);
        assertEquals(1, field.get(result));
    }

    @Test
    public void testRemainder() {
        long result = field.remainder(testInstant);
        assertEquals(0, result);
    }

}