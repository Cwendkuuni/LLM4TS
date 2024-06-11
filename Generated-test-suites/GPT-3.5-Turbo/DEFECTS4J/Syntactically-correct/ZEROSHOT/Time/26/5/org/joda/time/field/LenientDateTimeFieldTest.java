import org.joda.time.*;
import org.junit.*;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        DateTimeField field = null;
        Chronology base = null;
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        Assert.assertNull(result);

        field = new StrictDateTimeField(field);
        result = LenientDateTimeField.getInstance(field, base);
        Assert.assertNotNull(result);
    }

    @Test
    public void testIsLenient() {
        DateTimeField baseField = new BaseDateTimeField() {
            public int get(long instant) {
                return 0;
            }
            public String getAsShortText(long instant, Locale locale) {
                return null;
            }
            public String getAsText(long instant, Locale locale) {
                return null;
            }
            public String getAsShortText(int fieldValue, Locale locale) {
                return null;
            }
            public String getAsText(int fieldValue, Locale locale) {
                return null;
            }
            public long add(long instant, int value) {
                return 0;
            }
            public long add(long instant, long value) {
                return 0;
            }
            public int[] add(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
                return null;
            }
            public int[] addWrapPartial(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
                return null;
            }
            public long addWrapField(long instant, int value) {
                return 0;
            }
            public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
                return null;
            }
            public long set(long instant, int value) {
                return 0;
            }
            public long set(long instant, String text, Locale locale) {
                return 0;
            }
            public long set(long instant, String text) {
                return 0;
            }
            public long roundFloor(long instant) {
                return 0;
            }
            public long roundCeiling(long instant) {
                return 0;
            }
            public long roundHalfFloor(long instant) {
                return 0;
            }
            public long roundHalfCeiling(long instant) {
                return 0;
            }
            public long roundHalfEven(long instant) {
                return 0;
            }
            public DurationField getDurationField() {
                return null;
            }
            public DurationField getRangeDurationField() {
                return null;
            }
            public boolean isSupported() {
                return false;
            }
            public boolean isLenient() {
                return false;
            }
            public int getMinimumValue() {
                return 0;
            }
            public int getMinimumValue(long instant) {
                return 0;
            }
            public int getMinimumValue(ReadablePartial instant) {
                return 0;
            }
            public int getMinimumValue(ReadablePartial instant, int[] values) {
                return 0;
            }
            public int getMinimumValue(ReadablePartial instant, int[] values, int startIndex) {
                return 0;
            }
            public int getMaximumValue() {
                return 0;
            }
            public int getMaximumValue(long instant) {
                return 0;
            }
            public int getMaximumValue(ReadablePartial instant) {
                return 0;
            }
            public int getMaximumValue(ReadablePartial instant, int[] values) {
                return 0;
            }
            public int getMaximumValue(ReadablePartial instant, int[] values, int startIndex) {
                return 0;
            }
            public int getMaximumTextLength(Locale locale) {
                return 0;
            }
            public int getMaximumShortTextLength(Locale locale) {
                return 0;
            }
        };

        LenientDateTimeField field = new LenientDateTimeField(baseField, null);
        boolean result = field.isLenient();
        Assert.assertTrue(result);
    }

    @Test
    public void testSet() {
        DateTimeField baseField = new BaseDateTimeField() {
            public int get(long instant) {
                return 0;
            }
            public String getAsShortText(long instant, Locale locale) {
                return null;
            }
            public String getAsText(long instant, Locale locale) {
                return null;
            }
            public String getAsShortText(int fieldValue, Locale locale) {
                return null;
            }
            public String getAsText(int fieldValue, Locale locale) {
                return null;
            }
            public long add(long instant, int value) {
                return 0;
            }
            public long add(long instant, long value) {
                return 0;
            }
            public int[] add(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
                return null;
            }
            public int[] addWrapPartial(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
                return null;
            }
            public long addWrapField(long instant, int value) {
                return 0;
            }
            public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
                return null;
            }
            public long set(long instant, int value) {
                return 0;
            }
            public long set(long instant, String text, Locale locale) {
                return 0;
            }
            public long set(long instant, String text) {
                return 0;
            }
            public long roundFloor(long instant) {
                return 0;
            }
            public long roundCeiling(long instant) {
                return 0;
            }
            public long roundHalfFloor(long instant) {
                return 0;
            }
            public long roundHalfCeiling(long instant) {
                return 0;
            }
            public long roundHalfEven(long instant) {
                return 0;
            }
            public DurationField getDurationField() {
                return null;
            }
            public DurationField getRangeDurationField() {
                return null;
            }
            public boolean isSupported() {
                return false;
            }
            public boolean isLenient() {
                return false;
            }
            public int getMinimumValue() {
                return 0;
            }
            public int getMinimumValue(long instant) {
                return 0;
            }
            public int getMinimumValue(ReadablePartial instant) {
                return 0;
            }
            public int getMinimumValue(ReadablePartial instant, int[] values) {
                return 0;
            }
            public int getMinimumValue(ReadablePartial instant, int[] values, int startIndex) {
                return 0;
            }
            public int getMaximumValue() {
                return 0;
            }
            public int getMaximumValue(long instant) {
                return 0;
            }
            public int getMaximumValue(ReadablePartial instant) {
                return 0;
            }
            public int getMaximumValue(ReadablePartial instant, int[] values) {
                return 0;
            }
            public int getMaximumValue(ReadablePartial instant, int[] values, int startIndex) {
                return 0;
            }
            public int getMaximumTextLength(Locale locale) {
                return 0;
            }
            public int getMaximumShortTextLength(Locale locale) {
                return 0;
            }
        };

        LenientDateTimeField field = new LenientDateTimeField(baseField, null);
        long instant = 0;
        int value = 0;
        long result = field.set(instant, value);
        Assert.assertEquals(0, result);
    }

}