import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenWithReadableInstant() {
        ReadableInstant instant1 = new DateTime(2021, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2022, 1, 1, 0, 0);
        DurationFieldType fieldType = DurationFieldType.weeks();

        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);

        assertEquals(52, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadableInstant() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new DateTime(2022, 1, 1, 0, 0);
        DurationFieldType fieldType = DurationFieldType.weeks();

        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }

    @Test
    public void testBetweenWithReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDate(2022, 1, 1);
        ReadablePeriod period = Period.days(1);

        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);

        assertEquals(365, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadablePartial() {
        ReadablePartial partial1 = null;
        ReadablePartial partial2 = new LocalDate(2022, 1, 1);
        ReadablePeriod period = Period.days(1);

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithDifferentSizeReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDateTime(2022, 1, 1, 0, 0);
        ReadablePeriod period = Period.days(1);

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNonContiguousReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDate(2022, 1, 1).withMonthOfYear(3);
        ReadablePeriod period = Period.days(1);

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.years(5);
        long n = 365 * 24 * 60 * 60 * 1000L; // year in milliseconds

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);

        assertEquals(5, result);
    }

    @Test
    public void testStandardPeriodInWithNullPeriod() {
        ReadablePeriod period = null;
        long n = 365 * 24 * 60 * 60 * 1000L; // year in milliseconds

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);

        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithNonPreciseFieldType() {
        ReadablePeriod period = new Period(1, 2, 3, 4);
        long n = 1; // dummy value for n

        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        int result = period.getValue();

        assertEquals(5, result);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        period.setValue(10);

        assertEquals(10, period.getValue());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        int result = period.size();

        assertEquals(1, result);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        DurationFieldType result = period.getFieldType();

        assertEquals(DurationFieldType.weeks(), result);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        PeriodType result = period.getPeriodType();

        assertEquals(PeriodType.standard(), result);
    }

    @Test
    public void testGetFieldTypeAtIndex0() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        DurationFieldType result = period.getFieldType(0);

        assertEquals(DurationFieldType.weeks(), result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeAtNonZeroIndex() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        period.getFieldType(1);
    }

    @Test
    public void testGetValueAtIndex0() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        int result = period.getValue(0);

        assertEquals(5, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtNonZeroIndex() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        period.getValue(1);
    }

    @Test
    public void testGet() {
        DurationFieldType fieldType = DurationFieldType.weeks();
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        int result = period.get(fieldType);

        assertEquals(5, result);
    }

    @Test
    public void testGetWithNonMatchingFieldType() {
        DurationFieldType fieldType = DurationFieldType.months();
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        int result = period.get(fieldType);

        assertEquals(0, result);
    }

    @Test
    public void testIsSupported() {
        DurationFieldType fieldType = DurationFieldType.weeks();
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        boolean result = period.isSupported(fieldType);

        assertTrue(result);
    }

    @Test
    public void testIsSupportedWithNonMatchingFieldType() {
        DurationFieldType fieldType = DurationFieldType.months();
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        boolean result = period.isSupported(fieldType);

        assertFalse(result);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        Period result = period.toPeriod();

        assertEquals(Period.weeks(5), result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        MutablePeriod result = period.toMutablePeriod();

        assertEquals(Period.weeks(5), result);
    }

    @Test
    public void testEqualsWithSameObject() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        boolean result = period.equals(period);

        assertTrue(result);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);
        Object obj = new Object();

        boolean result = period.equals(obj);

        assertFalse(result);
    }

    @Test
    public void testEqualsWithReadablePeriod() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);
        ReadablePeriod other = Period.weeks(5);

        boolean result = period.equals(other);

        assertTrue(result);
    }

    @Test
    public void testEqualsWithDifferentValue() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);
        ReadablePeriod other = Period.weeks(10);

        boolean result = period.equals(other);

        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new FakeSingleFieldPeriod(5);

        int result = period.hashCode();

        int expected = 27 * (27 * 17 + 5) + DurationFieldType.weeks().hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToWithSameClass() {
        BaseSingleFieldPeriod period1 = new FakeSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new FakeSingleFieldPeriod(10);

        int result = period1.compareTo(period2);

        assertTrue(result < 0);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        BaseSingleFieldPeriod period1 = new FakeSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new FakeSingleFieldPeriod2(10);

        period1.compareTo(period2);
    }
}

class FakeSingleFieldPeriod extends BaseSingleFieldPeriod {
    public FakeSingleFieldPeriod(int iPeriod) {
        super(iPeriod);
    }

    public DurationFieldType getFieldType() {
        return DurationFieldType.weeks();
    }

    public PeriodType getPeriodType() {
        return PeriodType.standard();
    }
}

class FakeSingleFieldPeriod2 extends BaseSingleFieldPeriod {
    public FakeSingleFieldPeriod2(int iPeriod) {
        super(iPeriod);
    }

    public DurationFieldType getFieldType() {
        return DurationFieldType.days();
    }

    public PeriodType getPeriodType() {
        return PeriodType.standard();
    }
}