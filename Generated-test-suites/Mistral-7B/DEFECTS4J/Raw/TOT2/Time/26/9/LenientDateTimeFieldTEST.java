import org.junit.Assert;
import org.junit.Test;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.LocalDateTime;
import static org.joda.time.chrono.ISOChronology.instance;

public class LenientDateTimeFieldTest {

    @Test
    public void testInstance() {
        DateTimeField field = DateTimeFields.millisecondOfMinute();
        Chronology base = instance();
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        Assert.assertTrue(lenientField.isLenient());
    }

    @Test
    public void testConstructor() {
        DateTimeField field = DateTimeFields.millisecondOfMinute();
        Chronology base = instance();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        Assert.assertTrue(lenientField.isLenient());
    }

    @Test
    public void testIsLenient() {
        DateTimeField field = new LenientDateTimeField(DateTimeFields.millisecondOfMinute(), instance());
        Assert.assertTrue(field.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = DateTimeFields.millisecondOfMinute();
        Chronology base = instance();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        long instant = new LocalDateTime(2000, 1, 1, 0, 0, 0, 0).toInstant().getMillis();
        int value = 100;
        long expectedResult = instant + (DateTimeFields.MILLIS_PER_MINUTE * 10);
        long result = lenientField.set(instant, value);
        Assert.assertEquals(expectedResult, result);
    }
}