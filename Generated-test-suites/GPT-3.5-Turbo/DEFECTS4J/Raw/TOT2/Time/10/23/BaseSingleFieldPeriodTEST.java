import org.joda.time.DateTimeUtils;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod testPeriod;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(System.currentTimeMillis());
        testPeriod = new BaseSingleFieldPeriodImpl(5);
    }

    @Test
    public void testGetValue() {
        assertEquals(5, testPeriod.getValue());
    }

    @Test
    public void testSetVaue() {
        testPeriod.setValue(10);
        assertEquals(10, testPeriod.getValue());
    }

    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.days(), testPeriod.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), testPeriod.getPeriodType());
    }

    @Test
    public void testSize() {
        assertEquals(1, testPeriod.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        assertEquals(testPeriod.getFieldType(), testPeriod.getFieldType(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeAtIndexInvalidIndex() {
        testPeriod.getFieldType(1);
    }

    @Test
    public void testGeValueAtIndex() {
        assertEquals(testPeriod.getValue(), testPeriod.getValue(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtIndexInvalidIndex() {
        testPeriod.getValue(1);
    }

    @Test
    public void testGetWithDurationFieldType() {
        assertEquals(5, testPeriod.get(DurationFieldType.days()));
    }

    @Test
    public void testIsSupported() {
        assertEquals(true, testPeriod.isSupported(DurationFieldType.days()));
    }

    @Test
    public void testToPeriod() {
        assertEquals(Period.ZERO.withFields(testPeriod), testPeriod.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(testPeriod);

        assertEquals(mutablePeriod, testPeriod.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod otherPeriod = new BaseSingleFieldPeriodImpl(5);

        assertEquals(true, testPeriod.equals(otherPeriod));
    }

    @Test
    public void testHashCode() {
        int total = 17;
        total = 27 * total + testPeriod.getValue();
        total = 27 * total + testPeriod.getFieldType().hashCode();

        assertEquals(total, testPeriod.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod otherPeriod = new BaseSingleFieldPeriodImpl(10);

        assertEquals(-1, testPeriod.compareTo(otherPeriod));
    }

}