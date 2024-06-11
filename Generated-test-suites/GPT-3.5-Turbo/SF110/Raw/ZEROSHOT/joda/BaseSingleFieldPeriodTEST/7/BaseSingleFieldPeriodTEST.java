import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Assert;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenWithReadableInstant() {
        ReadableInstant instant1 = new DateTime(2021, 1, 1, 0, 0, 0, 0);
        ReadableInstant instant2 = new DateTime(2021, 1, 2, 0, 0, 0, 0);
        DurationFieldType fieldType = DurationFieldType.minutes();

        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);

        Assert.assertEquals(1440, result);
    }

    @Test
    public void testBetweenWithReadablePartial() {
        ReadablePartial partial1 = new DateTime(2021, 1, 1, 0, 0, 0, 0).toLocalDate();
        ReadablePartial partial2 = new DateTime(2021, 1, 2, 0, 0, 0, 0).toLocalDate();
        ReadablePeriod period = new Period(1, 0, 0, 0, 0, 0, 0, 0);

        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);

        Assert.assertEquals(1, result);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 0, 0, 0, 0, 0, 0, 0);

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1);

        Assert.assertEquals(86400000, result);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);

        int result = period.getValue();

        Assert.assertEquals(1, result);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);

        period.setValue(2);

        int result = period.getValue();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);

        int result = period.size();

        Assert.assertEquals(1, result);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return DurationFieldType.minutes(); }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        DurationFieldType result = period.getFieldType();

        Assert.assertEquals(DurationFieldType.minutes(), result);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return null; }

            @Override
            public PeriodType getPeriodType() { return PeriodType.standard(); }
        };

        PeriodType result = period.getPeriodType();

        Assert.assertEquals(PeriodType.standard(), result);
    }

    @Test
    public void testGetFieldTypeWithIndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return null; }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        try {
            period.getFieldType(1);
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGetValueWithIndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return null; }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        try {
            period.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return DurationFieldType.minutes(); }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        int result = period.get(DurationFieldType.minutes());

        Assert.assertEquals(1, result);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return DurationFieldType.minutes(); }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        boolean result = period.isSupported(DurationFieldType.minutes());

        Assert.assertTrue(result);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return DurationFieldType.minutes(); }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        Period result = period.toPeriod();

        Assert.assertEquals(Period.ZERO.withFields(period), result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return DurationFieldType.minutes(); }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        MutablePeriod result = period.toMutablePeriod();

        Assert.assertEquals(new MutablePeriod().add(period), result);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return DurationFieldType.minutes(); }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        boolean result = period.equals(period);

        Assert.assertTrue(result);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return DurationFieldType.minutes(); }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        int result = period.hashCode();

        Assert.assertEquals(27 * (27 * 17 + 1) + DurationFieldType.minutes().hashCode(), result);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() { return DurationFieldType.minutes(); }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() { return DurationFieldType.minutes(); }

            @Override
            public PeriodType getPeriodType() { return null; }
        };

        int result1 = period1.compareTo(period2);
        Assert.assertEquals(-1, result1);

        int result2 = period2.compareTo(period1);
        Assert.assertEquals(1, result2);

        int result3 = period1.compareTo(period1);
        Assert.assertEquals(0, result3);
    }
}