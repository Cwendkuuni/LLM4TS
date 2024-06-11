import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        field = new BasicMonthOfYearDateTimeField(null, 0);
    }

    @Test
    public void testGet() {
        long instant = DateTimeUtils.currentTimeMillis();
        int expected = field.get(instant);
        assertEquals(expected, field.get(instant));
    }

    @Test
    public void testAdd() {
        long instant = DateTimeUtils.currentTimeMillis();
        int months = 1;
        long expected = field.add(instant, months);
        assertEquals(expected, field.add(instant, months));
    }

    @Test
    public void testAddWrapField() {
        long instant = DateTimeUtils.currentTimeMillis();
        int months = 1;
        long expected = field.addWrapField(instant, months);
        assertEquals(expected, field.addWrapField(instant, months));
    }

    @Test
    public void testSet() {
        long instant = DateTimeUtils.currentTimeMillis();
        int month = 1;
        long expected = field.set(instant, month);
        assertEquals(expected, field.set(instant, month));
    }

    @Test
    public void testGetMinimumValue() {
        int expected = DateTimeConstants.JANUARY;
        assertEquals(expected, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        int expected = field.getMaximumValue();
        assertEquals(expected, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long instant = DateTimeUtils.currentTimeMillis();
        long expected = field.roundFloor(instant);
        assertEquals(expected, field.roundFloor(instant));
    }

    @Test
    public void testRemainder() {
        long instant = DateTimeUtils.currentTimeMillis();
        long expected = field.remainder(instant);
        assertEquals(expected, field.remainder(instant));
    }
}