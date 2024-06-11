import static org.junit.Assert.*;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.field.LenientDateTimeField;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;

    @Before
    public void setUp() {
        DateTimeField baseField = new BaseDateTimeFieldStub();
        lenientDateTimeField = new LenientDateTimeField(baseField, null);
    }

    @Test
    public void isLenientShouldReturnTrue() {
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void setShouldAddDifferenceToInstant() {
        long instant = 1000L;
        int value = 1;
        long expected = 2000L;

        long result = lenientDateTimeField.set(instant, value);

        assertEquals(expected, result);
    }

    private static class BaseDateTimeFieldStub extends DateTimeField {
        protected BaseDateTimeFieldStub() {
            super(null);
        }

        public int get(long instant) {
            return 1000;
        }

        public long set(long instant, int value) {
            return 2000L;
        }

        public long add(long instant, long value) {
            return instant + value;
        }

        public long addWrapField(long instant, int value) {
            return instant + value;
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return (int) (minuendInstant - subtrahendInstant);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return minuendInstant - subtrahendInstant;
        }

        public long set(long instant, String text) {
            return 0;
        }

        public int getMinimumValue() {
            return 0;
        }

        public int getMaximumValue() {
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

        public long remainder(long instant) {
            return 0;
        }
    }
}