import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        // Test with valid value
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertSame(DurationFieldType.DAYS, period.getFieldType());

        // Test with negative value
        period = new BaseSingleFieldPeriod(-5);
        assertEquals(-5, period.getValue());
        assertSame(DurationFieldType.DAYS, period.getFieldType());

        // Test with zero value
        period = new BaseSingleFieldPeriod(0);
        assertEquals(0, period.getValue());
        assertSame(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertSame(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertTrue(periodType.isStandard());
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);

        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);

        int comparison12 = period1.compareTo(period2);
        int comparison13 = period1.compareTo(period3);

        assertTrue(comparison12 > 0);
        assertEquals(0, comparison13);
    }

    /**
     * Test standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);

        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, 1000 * 60 * 60);
        assertEquals(168, hours);
    }

    /**
     * Test between method with valid instants.
     */
    @Test
    public void testBetweenWithValidInstants() {
        ReadableInstante start = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        ReadableInstante end = new DateTime(2022, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC());

        int days = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, days);
    }

    /**
     * Test between method with invalid instants.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithInvalidInstants() {
        ReadableInstant start = null;
        ReadableInstant end = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());

        int days = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
    }

    /**
     * Test between method with partial datetimes.
     */
    @Test
    public void testBetweenWithPartialDatetimes() {
        ReadablePartial start = new LocalDate(2022, 1, 1);
        ReadablePartial end = new LocalDate(2022, 1, 2);

        int days = BaseSingleFieldPeriod.between(start, end, Period.standard());
        assertEquals(1, days);
    }

    /**
     * Test between method with non-contiguous partial datetimes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNonContiguousPartialDatetimes() {
        ReadablePartial start = new LocalDate(2022, 1, 1).with(Temporals.monthOfYear().withValue(2));
        ReadablePartial end = new LocalDate(2022, 1, 2);

        int days = BaseSingleFieldPeriod.between(start, end, Period.standard());
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);

        assertEquals(5, period.get(DurationFieldType.DAYS));
        assertEquals(0, period.get(DurationFieldType.HOURS));
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);

        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    /**
     * Test toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);

        Period periodInstance = period.toPeriod();
        assertEquals(5, periodInstance.getDays());
        assertEquals(0, periodInstance.getHours());
    }

    /**
     * Test toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);

        MutablePeriod mutablePeriod = period.toMutablePeriod();
        mutablePeriod.add(DurationFieldType.DAYS, 5);
        assertEquals(10, mutablePeriod.getDays());
    }
}