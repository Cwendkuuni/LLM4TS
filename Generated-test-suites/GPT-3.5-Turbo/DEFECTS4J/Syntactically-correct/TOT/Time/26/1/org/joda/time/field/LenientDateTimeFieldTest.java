import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Assert;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance_LenientField() {
        DateTimeField field = new LenientDateTimeField(new StrictDateTimeField(GregorianChronology.getInstance().year()), GregorianChronology.getInstance());
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, GregorianChronology.getInstance());
        Assert.assertSame(field, lenientField);
    }

    @Test
    public void testGetInstance_StrictField() {
        DateTimeField field = new StrictDateTimeField(GregorianChronology.getInstance().year());
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, GregorianChronology.getInstance());
        Assert.assertNotSame(field, lenientField);
        Assert.assertEquals(field.getMaximumValue(), lenientField.getMaximumValue());
        Assert.assertEquals(field.getMinimumValue(), lenientField.getMinimumValue());
        Assert.assertEquals(field.isLenient(), false);
        Assert.assertEquals(lenientField.isLenient(), true);
    }

    @Test
    public void testGetInstance_NullField() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(null, GregorianChronology.getInstance());
        Assert.assertNull(lenientField);
    }

    @Test
    public void testSet_LenientField() {
        DateTimeField field = new LenientDateTimeField(new StrictDateTimeField(GregorianChronology.getInstance().year()), GregorianChronology.getInstance());
        DateTime dt = new DateTime().withYear(2000);
        long instant = dt.getMillis();
        int value = 1999;
        long newInstant = field.set(instant, value);
        DateTime newDateTime = new DateTime(newInstant);
        Assert.assertEquals(newDateTime.getYear(), value);
    }

    @Test
    public void testSet_StrictField() {
        DateTimeField field = new StrictDateTimeField(GregorianChronology.getInstance().year());
        DateTime dt = new DateTime().withYear(2000);
        long instant = dt.getMillis();
        int value = 1999;
        long newInstant = field.set(instant, value);
        DateTime newDateTime = new DateTime(newInstant);
        Assert.assertEquals(newDateTime.getYear(), value);
    }

    @Test
    public void testSet_NullField() {
        DateTimeField field = LenientDateTimeField.getInstance(null, GregorianChronology.getInstance());
        DateTime dt = new DateTime().withYear(2000);
        long instant = dt.getMillis();
        int value = 1999;
        long newInstant = field.set(instant, value);
        DateTime newDateTime = new DateTime(newInstant);
        Assert.assertEquals(newDateTime.getYear(), value);
    }

}