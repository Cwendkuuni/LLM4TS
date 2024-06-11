import org.junit.Test;
import org.junit.Assert.*;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        DurationFieldType fieldType = instance.getFieldType();
        assertEquals(DurationFieldType.days(), fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        PeriodType periodType = instance.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        int size = instance.size();
        assertEquals(1, size);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        instance.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testgetValueInvalidIndex() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        instance.getValue(1);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        int value = instance.getValue();
        assertEquals(5, value);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.hours();
        boolean supported = instance.isSupported(fieldType);
        assertFalse(supported);

        fieldType = DurationFieldType.days();
        supported = instance.isSupported(fieldType);
        assertTrue(supported);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        Period period = instance.toPeriod();
        assertEquals(5, period.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod instance = new TestSingleFieldPeriod(5);
        MutablePeriod mutablePeriod = instance.toMutablePeriod();
        mutablePeriod.setDays(10);
        assertEquals(10, mutablePeriod.getDays());
        assertEquals(5, instance.getValue());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(5);
        TestSingleFieldPeriod instance3 = new TestSingleFieldPeriod(10);
        BaseSingleFieldPeriod instance4 = new TestSingleFieldPeriod(5);
        Period period = new Period(5, 0, 0, 0, 0, 0, 0, PeriodType.standard());
        BaseSingleFieldPeriod instance5 = new TestSingleFieldPeriod(5).withFields(period);

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertTrue(instance1.equals(instance4));
        assertTrue(instance1.equals(instance5));
        assertTrue(instance1.equals(period.toPeriod()));

        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(""));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance3 = new TestSingleFieldPeriod(10);

        int hashCode1 = instance1.hashCode();
        int hashCode2 = instance1.hashCode();
        int hashCode3 = instance2.hashCode();
        int hashCode4 = instance3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testCompareToZero() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(0);
        int compareTo = instance1.compareTo(instance2);
        assertEquals(1, compareTo);
    }

    @Test
    public void testCompareToNegative() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(-5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(0);
        int compareTo = instance1.compareTo(instance2);
        assertEquals(-1, compareTo);
    }

    @Test
    public void testCompareToEqual() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(5);
        int compareTo = instance1.compareTo(instance2);
        assertEquals(0, compareTo);
    }

    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod instance2 = new TestSingleFieldPeriod(3);
        int compareTo = instance1.compareTo(instance2);
        assertEquals(2, compareTo);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentType() {
        BaseSingleFieldPeriod instance1 = new TestSingleFieldPeriod(5);
        String instance2 = "test";
        int compareTo = instance1.compareTo(instance2);
    }

    @Test
    public void testBetween() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new org.joda.time.DateTime(2015, 11, 25, 12, 0, 0, 0, chrono).toInstant();
        ReadableInstant end = new org.joda.time.DateTime(2015, 11, 26, 12, 0, 0, 0, chrono).toInstant();
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullStart() {
        ReadableInstant start = null;
        ReadableInstant end = new org.joda.time.DateTime(2015, 11, 26, 12, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullEnd() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new org.joda.time.DateTime(2015, 11, 25, 12, 0, 0, 0, chrono).toInstant();
        ReadableInstant end = null;
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidFieldType() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new org.joda.time.DateTime(2015, 11, 25, 12, 0, 0, 0, chrono).toInstant();
        ReadableInstant end = new org.joda.time.DateTime(2015, 11, 26, 12, 0, 0, 0, chrono).toInstant();
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.months());
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(5, 0, 0, 0, 0, 0, 0, PeriodType.standard());
        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(period, java.util.Calendar.MILLISECOND);
        assertEquals(17880000, standardPeriodIn);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidPeriod() {
        ReadablePeriod period = null;
        long millisPerUnit = java.util.Calendar.MILLISECOND;
        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    public static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        public TestSingleFieldPeriod(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }
    }
}