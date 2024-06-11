import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Assert;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    
    @Test
    public void testGetInstance() {
        DateTimeField field = null;
        Chronology base = null;
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        Assert.assertNull(result);
        
        field = new StrictDateTimeField(null);
        result = LenientDateTimeField.getInstance(field, base);
        Assert.assertNotNull(result);
        
        field = new StrictDateTimeField(null);
        Chronology chronology = null;
        result = LenientDateTimeField.getInstance(field, chronology);
        Assert.assertNotNull(result);
        
        field = new StrictDateTimeField(null);
        Chronology nonNullChronology = new Chronology(null, null, null);
        result = LenientDateTimeField.getInstance(field, nonNullChronology);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testIsLenient() {
        DateTimeField field = new StrictDateTimeField(null);
        Chronology base = new Chronology(null, null, null);
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(field, base);
        Assert.assertTrue(lenientDateTimeField.isLenient());
    }
    
    @Test
    public void testSet() {
        DateTimeField field = new StrictDateTimeField(null);
        Chronology base = new Chronology(null, null, null);
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(field, base);
        
        long instant = 1000L;
        int value = 100;
        long result = lenientDateTimeField.set(instant, value);
        Assert.assertEquals(0L, result);
        
        instant = 2000L;
        value = 200;
        result = lenientDateTimeField.set(instant, value);
        Assert.assertEquals(0L, result);
    }
}