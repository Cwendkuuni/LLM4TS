import org.joda.time.chrono.BasicChronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n = System.currentTimeMillis();
        assertEquals(chronology.getMonthOfYear(n), field.get(n));
    }

    @Test
    public void testAddInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n = System.currentTimeMillis();
        int value = 2;
        long expected = chronology.add(n, value);
        assertEquals(expected, field.add(n, value));
    }

    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n = System.currentTimeMillis();
        long value = 2;
        long expected = chronology.add(n, value);
        assertEquals(expected, field.add(n, value));
    }

    @Test
    public void testAddReadablePartialIntArrayInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        ReadablePartial readablePartial = null;
        int n = 0;
        int[] array = null;
        int n2 = 0;
        int[] expected = field.add(readablePartial, n, array, n2);
        assertArrayEquals(expected, field.add(readablePartial, n, array, n2));
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n = System.currentTimeMillis();
        int value = 2;
        long expected = chronology.addWrapField(n, value);
        assertEquals(expected, field.addWrapField(n, value));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n1 = System.currentTimeMillis();
        long n2 = System.currentTimeMillis();
        long expected = chronology.getDifferenceAsLong(n1, n2);
        assertEquals(expected, field.getDifferenceAsLong(n1, n2));
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n = System.currentTimeMillis();
        int value = 2;
        long expected = chronology.set(n, value);
        assertEquals(expected, field.set(n, value));
    }

    @Test
    public void testGetRangeDurationField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        assertNotNull(field.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n = System.currentTimeMillis();
        boolean expected = chronology.isLeapYear(chronology.getYear(n)) && chronology.getMonthOfYear(n) == field.getLeapMonth();
        assertEquals(expected, field.isLeap(n));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n = System.currentTimeMillis();
        assertEquals(field.isLeap(n) ? 1 : 0, field.getLeapAmount(n));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        assertNotNull(field.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        assertEquals(chronology.getMaxMonth(), field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n = System.currentTimeMillis();
        long expected = chronology.getYearMonthMillis(chronology.getYear(n), chronology.getMonthOfYear(n));
        assertEquals(expected, field.roundFloor(n));
    }

    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        long n = System.currentTimeMillis();
        long expected = n - field.roundFloor(n);
        assertEquals(expected, field.remainder(n));
    }

    @Test
    public void testReadResolve() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 6);
        assertEquals(chronology.monthOfYear(), field.readResolve());
    }
}