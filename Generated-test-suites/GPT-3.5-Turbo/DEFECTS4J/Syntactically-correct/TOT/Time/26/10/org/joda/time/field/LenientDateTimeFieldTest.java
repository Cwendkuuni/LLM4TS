import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.junit.Assert;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance_NullField_ReturnNull() {
        DateTimeField field = null;
        Chronology base = ISOChronology.getInstance(DateTimeZone.UTC);

        DateTimeField result = LenientDateTimeField.getInstance(field, base);

        Assert.assertNull(result);
    }

    @Test
    public void testGetInstance_LenientField_ReturnSameField() {
        DateTimeField field = new LenientDateTimeField(new StubDateTimeField(), ISOChronology.getInstance(DateTimeZone.UTC));
        Chronology base = ISOChronology.getInstance(DateTimeZone.UTC);

        DateTimeField result = LenientDateTimeField.getInstance(field, base);

        Assert.assertEquals(field, result);
    }

    @Test
    public void testGetInstance_StrictField_ReturnWrappedField() {
        DateTimeField field = new StrictDateTimeField(new StubDateTimeField());
        Chronology base = ISOChronology.getInstance(DateTimeZone.UTC);

        DateTimeField result = LenientDateTimeField.getInstance(field, base);

        Assert.assertEquals(field.getWrappedField(), result);
    }

    @Test
    public void testGetInstance_NormalField_ReturnLenientField() {
        DateTimeField field = new StubDateTimeField();
        Chronology base = ISOChronology.getInstance(DateTimeZone.UTC);

        DateTimeField result = LenientDateTimeField.getInstance(field, base);

        Assert.assertTrue(result instanceof LenientDateTimeField);
        Assert.assertEquals(field, ((LenientDateTimeField) result).getWrappedField());
        Assert.assertEquals(base, ((LenientDateTimeField) result).getChronology());
    }

    @Test
    public void testIsLenient_ReturnTrue() {
        DateTimeField field = new LenientDateTimeField(new StubDateTimeField(), ISOChronology.getInstance(DateTimeZone.UTC));

        boolean result = field.isLenient();

        Assert.assertTrue(result);
    }

    @Test
    public void testSet_ValueInRange_ReturnUpdatedInstant() {
        DateTimeField field = new LenientDateTimeField(new StubDateTimeField(), ISOChronology.getInstance(DateTimeZone.UTC));
        long instant = 1582114800000L; // 2020-02-19T10:00:00Z
        int value = 5;

        long result = field.set(instant, value);

        Assert.assertEquals(1582305600000L, result); // 2020-02-22T10:00:00Z
    }

    @Test
    public void testSet_ValueLessThanMinValue_ReturnUpdatedInstant() {
        DateTimeField field = new LenientDateTimeField(new StubDateTimeField(), ISOChronology.getInstance(DateTimeZone.UTC));
        long instant = 1582114800000L; // 2020-02-19T10:00:00Z
        int value = -5;

        long result = field.set(instant, value);

        Assert.assertEquals(1581873600000L, result); // 2020-02-17T10:00:00Z
    }

    @Test
    public void testSet_ValueGreaterThanMaxValue_ReturnUpdatedInstant() {
        DateTimeField field = new LenientDateTimeField(new StubDateTimeField(), ISOChronology.getInstance(DateTimeZone.UTC));
        long instant = 1582114800000L; // 2020-02-19T10:00:00Z
        int value = 30;

        long result = field.set(instant, value);

        Assert.assertEquals(1582512000000L, result); // 2020-02-24T10:00:00Z
    }

    @Test
    public void testSet_ValueWithinRange_ReturnUpdatedInstant() {
        DateTimeField field = new LenientDateTimeField(new StubDateTimeField(), ISOChronology.getInstance(DateTimeZone.UTC));
        long instant = 1582114800000L; // 2020-02-19T10:00:00Z
        int value = 22;

        long result = field.set(instant, value);

        Assert.assertEquals(1582358400000L, result); // 2020-02-22T10:00:00Z
    }

    @Test
    public void testSet_ValueWithinRange_WithDifferentTimeZone_ReturnUpdatedInstant() {
        DateTimeField field = new LenientDateTimeField(new StubDateTimeField(), ISOChronology.getInstance(DateTimeZone.forID("America/New_York")));
        long instant = 1582114800000L; // 2020-02-19T10:00:00Z
        int value = 22;

        long result = field.set(instant, value);

        Assert.assertEquals(1582347600000L, result); // 2020-02-22T05:00:00Z
    }

    @Test
    public void testSet_InvalidInstant_ReturnUpdatedInstant() {
        DateTimeField field = new LenientDateTimeField(new StubDateTimeField(), ISOChronology.getInstance(DateTimeZone.UTC));
        long instant = 1582114800000L; // 2020-02-19T10:00:00Z
        int value = 1000000;

        long result = field.set(instant, value);

        Assert.assertEquals(253402300799999L, result); // Maximum possible instant
    }

    private static class StubDateTimeField extends BaseDateTimeField {

        public StubDateTimeField() {
            super("StubDateTimeField");
        }

        @Override
        public long addWrapField(long instant, int amount) {
            return 0;
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }

        @Override
        public int getLeapAmount(long instant) {
            return 0;
        }

        @Override
        public int getMaximumShortTextLength() {
            return 0;
        }

        @Override
        public int getMaximumTextLength() {
            return 0;
        }

        @Override
        public int getMaximumValue() {
            return 24;
        }

        @Override
        public int getMaximumValue(long instant) {
            return 0;
        }

        @Override
        public int getMinimumValue() {
            return 0;
        }

        @Override
        public int getMinimumValue(long instant) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            return null;
        }

        @Override
        public boolean isLeap(long instant) {
            return false;
        }

        @Override
        public long roundFloor(long instant) {
            return 0;
        }

        @Override
        public long set(long instant, int value) {
            return 0;
        }
    }
}