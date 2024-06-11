import org.joda.time.ReadablePartial;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long instant = 1234567890L;
        int expected = 7;
        assertEquals(expected, field.get(instant));
    }

    @Test
    public void testAdd_int() {
        long instant = 1234567890L;
        int months = 5;
        long expected = 1238105490L;
        assertEquals(expected, field.add(instant, months));
    }

    @Test
    public void testAdd_int_zero() {
        long instant = 1234567890L;
        int months = 0;
        assertEquals(instant, field.add(instant, months));
    }

    @Test
    public void testAdd_long() {
        long instant = 1234567890L;
        int months = 5;
        long expected = 1238105490L;
        assertEquals(expected, field.add(instant, months));
    }

    @Test
    public void testAddWrapField_long() {
        long instant = 1234567890L;
        int months = 5;
        long expected = 1238105490L;
        assertEquals(expected, field.addWrapField(instant, months));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 987654321L;
        long expected = 24;
        assertEquals(expected, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    @Test
    public void testSet() {
        long instant = 1234567890L;
        int month = 9;
        long expected = 1238229890L;
        assertEquals(expected, field.set(instant, month));
    }

    @Test
    public void testIsLeap() {
        long instant = 1234567890L;
        assertFalse(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        long instant = 1234567890L;
        int expected = 0;
        assertEquals(expected, field.getLeapAmount(instant));
    }

    @Test
    public void testGetMinimumValue() {
        int expected = 1;
        assertEquals(expected, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        int expected = 12;
        assertEquals(expected, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long instant = 1234567890L;
        long expected = 1235832000L;
        assertEquals(expected, field.roundFloor(instant));
    }

    @Test
    public void testRemainder() {
        long instant = 1234567890L;
        long expected = 873910L;
        assertEquals(expected, field.remainder(instant));
    }

    @Test
    public void testAdd_ReadablePartial_intArray_int() {
        ReadablePartial partial = null;
        int fieldIndex = 0;
        int[] values = null;
        int valueToAdd = 0;
        int[] expected = null;
        assertArrayEquals(expected, field.add(partial, fieldIndex, values, valueToAdd));
    }

    @Test
    public void testSerialization() {
        BasicMonthOfYearDateTimeField deserializedField = null;
        assertEquals(field, deserializedField);
    }
}