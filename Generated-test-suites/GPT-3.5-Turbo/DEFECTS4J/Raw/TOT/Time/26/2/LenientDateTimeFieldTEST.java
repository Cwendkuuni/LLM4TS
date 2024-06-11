import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.LenientDateTimeField;
import org.junit.Assert;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private static final DateTimeField ORIGINAL_FIELD = ISOChronology.getInstance().dayOfMonth();
    private static final LenientDateTimeField LENIENT_FIELD = new LenientDateTimeField(ORIGINAL_FIELD, ISOChronology.getInstance());

    @Test
    public void testGet() {
        DateTime dateTime = new DateTime(2022, 1, 10, 0, 0);
        Assert.assertEquals(10, LENIENT_FIELD.get(dateTime.getMillis()));
    }

    @Test
    public void testSet_Typical() {
        DateTime dateTime = new DateTime(2022, 1, 10, 0, 0);
        long newMillis = LENIENT_FIELD.set(dateTime.getMillis(), 15);
        Assert.assertEquals(15, LENIENT_FIELD.get(newMillis));
    }

    @Test
    public void testSet_EdgeCase() {
        DateTime dateTime = new DateTime(2022, 1, 31, 0, 0);
        long newMillis = LENIENT_FIELD.set(dateTime.getMillis(), 32);
        Assert.assertEquals(1, LENIENT_FIELD.get(newMillis));
    }

    @Test
    public void testSet_Error() {
        DateTime dateTime = new DateTime(2022, 1, 10, 0, 0);
        long newMillis = LENIENT_FIELD.set(dateTime.getMillis(), -5);
        Assert.assertEquals(28, LENIENT_FIELD.get(newMillis));
    }

    @Test
    public void testIsLenient() {
        Assert.assertTrue(LENIENT_FIELD.isLenient());
    }

}