import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Assert;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private Chronology base = ISOChronology.getInstance();
    private DateTimeField field = base.dayOfWeek();
    private DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

    @Test
    public void testIsLenient() {
        Assert.assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testSet() {
        DateTime instant = new DateTime();
        int value = 2;
        long result = lenientField.set(instant.getMillis(), value);
        Assert.assertEquals(result, instant.withFieldAdded(field.getRangeUnitType(), value - field.get(instant)).getMillis());
    }

}