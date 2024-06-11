import org.junit.*;
import java.text.*;
import java.util.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy", TimeZone.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy", Locale.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy", TimeZone.getDefault(), Locale.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault(), Locale.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getDefault(), Locale.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM);
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, Locale.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, TimeZone.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, TimeZone.getDefault(), Locale.getDefault());
        Assert.assertNotNull(instance);
    }
    
    @Test
    public void testFormatObject() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = instance.format(new Date(), buffer, pos);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testFormatMillis() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        String result = instance.format(System.currentTimeMillis());
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testFormatDate() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        String result = instance.format(new Date());
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testFormatCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        String result = instance.format(Calendar.getInstance());
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testFormatMillisWithBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.format(System.currentTimeMillis(), buffer);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testFormatDateWithBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.format(new Date(), buffer);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testFormatCalendarWithBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.format(Calendar.getInstance(), buffer);
        Assert.assertNotNull(result);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        Date result = instance.parse("01/01/2022");
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testParseWithParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        ParsePosition pos = new ParsePosition(0);
        Date result = instance.parse("01/01/2022", pos);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testParseObjectWithParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        ParsePosition pos = new ParsePosition(0);
        Object result = instance.parseObject("01/01/2022", pos);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        String result = instance.getPattern();
        Assert.assertEquals("dd/MM/yyyy", result);
    }
    
    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy", TimeZone.getDefault());
        TimeZone result = instance.getTimeZone();
        Assert.assertEquals(TimeZone.getDefault(), result);
    }
    
    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy", Locale.getDefault());
        Locale result = instance.getLocale();
        Assert.assertEquals(Locale.getDefault(), result);
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        int result = instance.getMaxLengthEstimate();
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance("dd/MM/yyyy");
        FastDateFormat instance2 = FastDateFormat.getInstance("dd/MM/yyyy");
        boolean result = instance1.equals(instance2);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        int result = instance.hashCode();
        Assert.assertTrue(result != 0);
    }
    
    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        String result = instance.toString();
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = instance.applyRules(Calendar.getInstance(), buffer);
        Assert.assertNotNull(result);
    }

}