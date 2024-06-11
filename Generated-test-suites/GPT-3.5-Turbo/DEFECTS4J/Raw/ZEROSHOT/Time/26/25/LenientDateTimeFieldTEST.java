import org.joda.time.field.LenientDateTimeField;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        LenientDateTimeField field = LenientDateTimeField.getInstance(null, null);
        Assert.assertNull(field);

        // Create a normal DateTimeField
        DateTimeField normalField = new DateTimeField(){

            @Override
            public int get(long instant) {
                return 0;
            }

            @Override
            public long set(long instant, int value) {
                return 0;
            }

            @Override
            public String getAsText(long instant) {
                return null;
            }

            @Override
            public String getAsShortText(long instant) {
                return null;
            }

            @Override
            public long add(long instant, int value) {
                return 0;
            }

            @Override
            public long add(Object instant, int value) {
                return 0;
            }

            @Override
            public long addWrapField(long instant, int value) {
                return 0;
            }

            @Override
            public long addWrapField(Object instant, int value) {
                return 0;
            }

            @Override
            public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
                return new int[0];
            }

            @Override
            public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd, int[] lowerBounds, int[] upperBounds) {
                return new int[0];
            }

            @Override
            public long getDifference(long minuendInstant, long subtrahendInstant) {
                return 0;
            }

            @Override
            public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
                return 0;
            }

            @Override
            public Chronology getChronology() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public boolean isSupported() {
                return false;
            }

            @Override
            public boolean isLenient() {
                return false;
            }

        };

        // Test getInstance with normal field
        field = LenientDateTimeField.getInstance(normalField, null);
        Assert.assertNotNull(field);
        Assert.assertEquals(normalField, field.getWrappedField());

        // Create a strict DateTimeField
        DateTimeField strictField = new DateTimeField(){

            @Override
            public int get(long instant) {
                return 0;
            }

            @Override
            public long set(long instant, int value) {
                return 0;
            }

            @Override
            public String getAsText(long instant) {
                return null;
            }

            @Override
            public String getAsShortText(long instant) {
                return null;
            }

            @Override
            public long add(long instant, int value) {
                return 0;
            }

            @Override
            public long add(Object instant, int value) {
                return 0;
            }

            @Override
            public long addWrapField(long instant, int value) {
                return 0;
            }

            @Override
            public long addWrapField(Object instant, int value) {
                return 0;
            }

            @Override
            public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
                return new int[0];
            }

            @Override
            public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd, int[] lowerBounds, int[] upperBounds) {
                return new int[0];
            }

            @Override
            public long getDifference(long minuendInstant, long subtrahendInstant) {
                return 0;
            }

            @Override
            public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
                return 0;
            }

            @Override
            public Chronology getChronology() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public boolean isSupported() {
                return false;
            }

            @Override
            public boolean isLenient() {
                return false;
            }

        };

        // Test getInstance with strict field
        field = LenientDateTimeField.getInstance(strictField, null);
        Assert.assertNotNull(field);
        Assert.assertEquals(strictField, field.getWrappedField());

        // Create a lenient DateTimeField
        DateTimeField lenientField = new DateTimeField(){

            @Override
            public int get(long instant) {
                return 0;
            }

            @Override
            public long set(long instant, int value) {
                return 0;
            }

            @Override
            public String getAsText(long instant) {
                return null;
            }

            @Override
            public String getAsShortText(long instant) {
                return null;
            }

            @Override
            public long add(long instant, int value) {
                return 0;
            }

            @Override
            public long add(Object instant, int value) {
                return 0;
            }

            @Override
            public long addWrapField(long instant, int value) {
                return 0;
            }

            @Override
            public long addWrapField(Object instant, int value) {
                return 0;
            }

            @Override
            public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
                return new int[0];
            }

            @Override
            public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd, int[] lowerBounds, int[] upperBounds) {
                return new int[0];
            }

            @Override
            public long getDifference(long minuendInstant, long subtrahendInstant) {
                return 0;
            }

            @Override
            public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
                return 0;
            }

            @Override
            public Chronology getChronology() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public boolean isSupported() {
                return false;
            }

            @Override
            public boolean isLenient() {
                return true;
            }
        };

        // Test getInstance with lenient field
        field = LenientDateTimeField.getInstance(lenientField, null);
        Assert.assertNotNull(field);
        Assert.assertEquals(lenientField, field.getWrappedField());
    }

    @Test
    public void testIsLenient() {
        LenientDateTimeField field = new LenientDateTimeField(null, null);
        Assert.assertTrue(field.isLenient());
    }

    @Test
    public void testSet() {
        LenientDateTimeField field = new LenientDateTimeField(null, null);

        long instant = 1234567890L;
        int value = 10;
        long result = field.set(instant, value);

        Assert.assertEquals(1234567880L, result);
    }


}