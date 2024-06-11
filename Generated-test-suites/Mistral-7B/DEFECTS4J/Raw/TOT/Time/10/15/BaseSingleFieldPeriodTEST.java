import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriodTest;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
        assertEquals(3, period.getValue());
    }

    /**
     * Test getting and setting the value of the period.
     */
    @Test
    public void testGetSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    /**
     * Test getting the field type of the period.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(2);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test getting the period type of the period.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(4);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    /**
     * Test the size of the period.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(2);
        assertEquals(1, period.size());
    }

    /**
     * Test getting the field type at the specified index.
     */
    @Test
    public void testGetFieldTypeIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(2);
        DurationFieldType fieldType = period.getFieldType(0);
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test getting the value at the specified index.
     */
    @Test
    public void testGetValueIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        int value = period.getValue(0);
        assertEquals(3, value);
    }

    /**
     * Test getting the value of a duration field.
     */
    @Test
    public void testGetValueField() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(4);
        int value = period.get(DurationFieldType.DAYS);
        assertEquals(4, value);
    }

    /**
     * Test checking whether the duration field is supported.
     */
    @Test
    public void testIsSupportedField() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(2);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    /**
     * Test converting the period to an immutable Period object.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period immutablePeriod = period.toPeriod();
        assertEquals(5, immutablePeriod.getDays());
    }

    /**
     * Test converting the period to a mutable Period object.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(6);
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(6, mutablePeriod.getDays());
    }

    /**
     * Test comparing two BaseSingleFieldPeriod objects.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(-2, period1.compareTo(period2));
        assertEquals(0, period1.compareTo(period1));
        assertEquals(2, period2.compareTo(period1));
    }

    /**
     * Test creating a BaseSingleFieldPeriod from a Chronology.
     */
    @Test
    public void testFromChronology() {
        Chronology chronology = ISOChronology.getInstanceUTC();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(chronology, 7);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(7, period.getValue());
    }

    /**
     * Test calculating the number of whole units between two instants.
     */
    @Test
    public void testBetweenInstant() {
        Chronology chronology = ISOChronology.getInstanceUTC();
        ReadableInstant start = chronology.getInstant(2012, 1, 1, 0, 0, 0, 0, 0);
        ReadableInstant end = chronology.getInstant(2012, 1, 2, 0, 0, 0, 0, 0);
        int duration = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(duration);
        assertEquals(duration, period.getValue());
    }

    /**
     * Test calculating the number of whole units between two partials.
     */
    @Test
    public void testBetweenPartial() {
        Chronology chronology = ISOChronology.getInstanceUTC();
        ReadablePartial start = new LocalDate(2012, 1, 1);
        ReadablePartial end = new LocalDate(2012, 1, 2);
        int duration = BaseSingleFieldPeriod.between(start, end, new LocalDate());
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(duration);
        assertEquals(duration, period.getValue());
    }

    /**
     * Test converting a period to a standard duration.
     */
    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        long millisPerUnit = 86400000; // 1 day
        int standardDuration = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(3, standardDuration); // 3 days
    }

}