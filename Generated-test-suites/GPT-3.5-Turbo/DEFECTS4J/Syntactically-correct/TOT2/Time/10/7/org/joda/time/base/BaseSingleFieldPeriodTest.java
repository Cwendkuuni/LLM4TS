import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_Instants() {
        ReadableInstant start = new MockInstant(1000L);
        ReadableInstant end = new MockInstant(2000L);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.seconds());
        assertEquals(1, result);
    }

    @Test
    public void testBetween_Partial() {
        ReadablePartial start = new MockPartial(DurationFieldType.minutes(), 30);
        ReadablePartial end = new MockPartial(DurationFieldType.minutes(), 45);
        ReadablePeriod zeroInstance = new Period();
        int result = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        assertEquals(15, result);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 2, 3, 0);
        long millisPerUnit = 3600000L;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(6, result);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        period.setValue(15);
        assertEquals(15, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        assertEquals(DurationFieldType.seconds(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        assertEquals(PeriodType.time(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        assertEquals(DurationFieldType.seconds(), period.getFieldType(0));
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        assertEquals(5, period.get(DurationFieldType.seconds()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.seconds()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(Period.ZERO.withFields(period), result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        MutablePeriod expected = new MutablePeriod();
        expected.add(period);
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MockSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        int expected = 27 * 17 + 5;
        assertEquals(expected, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new MockSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new MockSingleFieldPeriod(5);
        assertEquals(1, period1.compareTo(period2));
    }
}

class MockInstant implements ReadableInstant {
    private long millis;

    public MockInstant(long millis) {
        this.millis = millis;
    }

    @Override
    public long getMillis() {
        return millis;
    }
}

class MockPartial implements ReadablePartial {
    private DurationFieldType type;
    private int value;

    public MockPartial(DurationFieldType type, int value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public DurationFieldType getFieldType(int index) {
        return type;
    }

    @Override
    public int getValue(int index) {
        return value;
    }
}

class MockSingleFieldPeriod extends BaseSingleFieldPeriod {
    public MockSingleFieldPeriod(int period) {
        super(period);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.seconds();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.time();
    }
}