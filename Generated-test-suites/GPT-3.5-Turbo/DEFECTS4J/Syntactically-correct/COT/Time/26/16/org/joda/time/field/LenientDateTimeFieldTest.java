import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.field.LenientDateTimeField;
import org.junit.Assert;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstanceWithNullDateTimeFieldReturnsNull(){
        DateTimeField lenientField = LenientDateTimeField.getInstance(null, GJChronology.getInstance());
        Assert.assertNull(lenientField);
    }

    @Test
    public void testGetInstanceWithAlreadyLenientDateTimeFieldReturnsSameInstance(){
        DateTimeField lenientField = new LenientDateTimeField(GJChronology.getInstance().year(), GJChronology.getInstance());
        DateTimeField result = LenientDateTimeField.getInstance(lenientField, GJChronology.getInstance());
        Assert.assertSame(lenientField, result);
    }

    @Test
    public void testSetWithInBoundsValueReturnsNewValue(){
        LenientChronology lenientChronology = LenientChronology.getInstance(GJChronology.getInstance());
        DateTimeField lenientField = lenientChronology.dayOfMonth();
        long instant = lenientChronology.getDateTimeMillis(new LocalDate(2021, 1, 15), new LocalTime(0, 0));
        long result = lenientField.set(instant, 20);
        Assert.assertEquals(lenientChronology.dayOfMonth().get(result), 20);
    }

    @Test
    public void testSetWithOutOfBoundsValueReturnsAdjustedValue(){
        LenientChronology lenientChronology = LenientChronology.getInstance(GJChronology.getInstance());
        DateTimeField lenientField = lenientChronology.dayOfMonth();
        long instant = lenientChronology.getDateTimeMillis(new LocalDate(2021, 1, 15), new LocalTime(0, 0));
        long result = lenientField.set(instant, 40);
        Assert.assertEquals(lenientChronology.dayOfMonth().get(result), 31);
    }

    
}