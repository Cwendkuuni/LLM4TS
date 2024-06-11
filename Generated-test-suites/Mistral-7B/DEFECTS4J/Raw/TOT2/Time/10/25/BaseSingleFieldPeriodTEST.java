import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Tests the constructor with a valid period value.
     */
    @Test
    public void testValidPeriodValue() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Tests the getValue method with a valid period instance.
     */
    @Test
    public void testGetValue() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Tests the setValue method with a valid period instance and a new value.
     */
    @Test
    public void testSetValue() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    /**
     * Tests the size method with a valid period instance.
     */
    @Test
    public void testSize() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    /**
     * Tests the getFieldType method with a valid period instance.
     */
    @Test
    public void testGetFieldType() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(fieldType, period.getFieldType());
    }

    /**
     * Tests the getPeriodType method with a valid period instance.
     */
    @Test
    public void testGetPeriodType() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Tests the isSupported method with a valid period instance and a valid field type.
     */
    @Test
    public void testIsSupported() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.isSupported(fieldType));
    }

    /**
     * Tests the isSupported method with a valid period instance and an invalid field type.
     */
    @Test
    public void testIsSupportedInvalidFieldType() {
        DurationFieldType fieldType = DurationFieldType.HOURS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertFalse(period.isSupported(fieldType));
    }

    /**
     * Tests the toPeriod method with a valid period instance.
     */
    @Test
    public void testToPeriod() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period expectedPeriod = new Period(5, PeriodUnit.DAYS);
        Period actualPeriod = period.toPeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    /**
     * Tests the toMutablePeriod method with a valid period instance.
     */
    @Test
    public void testToMutablePeriod() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod expectedPeriod = new MutablePeriod();
        expectedPeriod.add(new Period(5, PeriodUnit.DAYS));
        MutablePeriod actualPeriod = period.toMutablePeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    /**
     * Tests the equals method with a valid period instance and a null object.
     */
    @Test(expected = NullPointerException.class)
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Object nullObject = null;
        period.equals(nullObject);
    }

    /**
     * Tests the equals method with a valid period instance and an invalid period type.
     */
    @Test
    public void testEqualsInvalidPeriodType() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Object other = new Object();
        assertFalse(period.equals(other));
    }

    /**
     * Tests the equals method with a valid period instance and a valid period with the same value.
     */
    @Test
    public void testEqualsSameValue() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    /**
     * Tests the equals method with a valid period instance and a valid period with a different value.
     */
    @Test
    public void testEqualsDifferentValue() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        assertFalse(period1.equals(period2));
    }

    /**
     * Tests the compareTo method with a valid period instance and a null object.
     */
    @Test(expected = NullPointerException.class)
    public void testCompareToNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.compareTo(null);
    }

    /**
     * Tests the compareTo method with a valid period instance and a smaller period.
     */
    @Test
    public void testCompareToSmallerPeriod() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int result = period1.compareTo(period2);
        assertLessThan(0, result);
    }

    /**
     * Tests the compareTo method with a valid period instance and a greater period.
     */
    @Test
    public void testCompareToGreaterPeriod() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        int result = period1.compareTo(period2);
        assertGreaterThan(0, result);
    }

    /**
     * Tests the between method with valid start and end instants and a valid field type.
     */
    @Test
    public void testBetweenValid() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        ReadableInstant start = ISOChronology.getInstanceUTC().getInstant();
        ReadableInstant end = start.plus(1, fieldType);
        int result = BaseSingleFieldPeriod.between(start, end, fieldType);
        assertEquals(1, result);
    }

    /**
     * Tests the between method with valid start and end instants and an invalid field type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidFieldType() {
        ReadableInstant start = ISOChronology.getInstanceUTC().getInstant();
        ReadableInstant end = start.plus(1, DurationFieldType.HOURS);
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
    }

    /**
     * Tests the between method with null start or end instants.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullInstants() {
        DurationFieldType fieldType = DurationFieldType.DAYS;
        BaseSingleFieldPeriod.between(null, null, fieldType);
    }

    /**
     * Tests the standardPeriodIn method with a valid period and a valid millisPerUnit value.
     */
    @Test
    public void testStandardPeriodInValidPeriodAndMillisPerUnit() {
        ReadablePeriod period = new Period(2, PeriodUnit.DAYS, 1, PeriodUnit.HOURS);
        long millisPerUnit = 1000 * 60 * 60 * 24;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(2, result);
    }

    /**
     * Tests the standardPeriodIn method with a valid period and an invalid millisPerUnit value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidMillisPerUnit() {
        ReadablePeriod period = new Period(2, PeriodUnit.DAYS, 1, PeriodUnit.HOURS);
        long millisPerUnit = 1000 * 60;
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    /**
     * Tests the standardPeriodIn method with a null period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullPeriod() {
        long millisPerUnit = 1000 * 60 * 60 * 24;
        BaseSingleFieldPeriod.standardPeriodIn(null, millisPerUnit);
    }
}