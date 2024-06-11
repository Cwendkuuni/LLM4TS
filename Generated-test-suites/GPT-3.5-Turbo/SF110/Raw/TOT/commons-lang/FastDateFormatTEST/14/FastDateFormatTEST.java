import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {
    
    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals(FastDateFormat.FULL, instance.getCalendar().get(Calendar.DAY_OF_WEEK));
    }
    
    @Test
    public void testGetInstanceWithStringPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        assertEquals("14/03/2022", instance.format(new Date(1647272465000L)));
    }
    
    @Test
    public void testGetInstanceWithStringPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy", TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
        assertEquals(1647283260000L, instance.parse("14/03/2022").getTime());
    }
    
    @Test
    public void testGetInstanceWithStringPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy", Locale.FRENCH);
        assertNotNull(instance);
        assertEquals("14/03/2022", instance.format(new Date(1647272465000L)));
    }
    
    @Test
    public void testGetInstanceWithStringPatternTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy", TimeZone.getTimeZone("GMT"), Locale.FRENCH);
        assertNotNull(instance);
        assertEquals(1647283260000L, instance.parse("14/03/2022").getTime());
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        assertNotNull(instance);
        assertEquals("14/03/22", instance.format(new Date(1647272465000L)));
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.FRENCH);
        assertNotNull(instance);
        assertEquals("14/03/22", instance.format(new Date(1647272465000L)));
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
        assertEquals(1647272465000L, instance.parse("14/03/22").getTime());
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.FRENCH);
        assertNotNull(instance);
        assertEquals(1647272465000L, instance.parse("14/03/22").getTime());
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(instance);
        assertEquals("08:27 PM", instance.format(new Date(1647277676000L)));
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH);
        assertNotNull(instance);
        assertEquals("20:27", instance.format(new Date(1647277676000L)));
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
        assertEquals(1647277676000L, instance.parse("08:27 PM").getTime());
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.FRENCH);
        assertNotNull(instance);
        assertEquals(1647277676000L, instance.parse("20:27").getTime());
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertNotNull(instance);
        assertEquals("14/03/22 08:27 PM", instance.format(new Date(1647277676000L)));
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, Locale.FRENCH);
        assertNotNull(instance);
        assertEquals("14/03/22 20:27", instance.format(new Date(1647277676000L)));
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
        assertEquals(1647277676000L, instance.parse("14/03/22 08:27 PM").getTime());
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.FRENCH);
        assertNotNull(instance);
        assertEquals(1647277676000L, instance.parse("14/03/22 20:27").getTime());
    }
    
    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        instance.format(new Date(1647272465000L), buffer, position);
        assertEquals("14/03/2022", buffer.toString());
    }
    
    @Test
    public void testFormatLong() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        assertEquals("14/03/2022", instance.format(1647272465000L));
    }
    
    @Test
    public void testFormatDate() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        Date date = new Date(1647272465000L);
        assertEquals("14/03/2022", instance.format(date));
    }
    
    @Test
    public void testFormatCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 2, 14); // March 14, 2022
        assertEquals("14/03/2022", instance.format(calendar));
    }
    
    @Test
    public void testFormatLongStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        StringBuffer buffer = new StringBuffer();
        instance.format(1647272465000L, buffer);
        assertEquals("14/03/2022", buffer.toString());
    }
    
    @Test
    public void testFormatDateStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        StringBuffer buffer = new StringBuffer();
        instance.format(new Date(1647272465000L), buffer);
        assertEquals("14/03/2022", buffer.toString());
    }
    
    @Test
    public void testFormatCalendarStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        StringBuffer buffer = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 2, 14); // March 14, 2022
        instance.format(calendar, buffer);
        assertEquals("14/03/2022", buffer.toString());
    }
    
    @Test
    public void testParseString() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        Date date = instance.parse("14/03/2022");
        assertNotNull(date);
        assertEquals(1647272465000L, date.getTime());
    }
    
    @Test
    public void testParseStringParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        ParsePosition position = new ParsePosition(0);
        Date date = instance.parse("14/03/2022", position);
        assertNotNull(date);
        assertEquals(1647272465000L, date.getTime());
        assertEquals(10, position.getIndex());
    }
    
    @Test
    public void testParseObject() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        ParsePosition position = new ParsePosition(0);
        Object parsedDate = instance.parseObject("14/03/2022", position);
        assertNotNull(parsedDate);
        assertTrue(parsedDate instanceof Date);
        assertEquals(1647272465000L, ((Date) parsedDate).getTime());
        assertEquals(10, position.getIndex());
    }
    
    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        assertEquals("dd/MM/yyyy", instance.getPattern());
    }
    
    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        assertEquals(TimeZone.getDefault(), instance.getTimeZone());
    }
    
    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        assertEquals(Locale.getDefault(), instance.getLocale());
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        assertEquals(10, instance.getMaxLengthEstimate());
    }
    
    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance("dd/MM/yyyy");
        FastDateFormat instance2 = FastDateFormat.getInstance("dd/MM/yyyy");
        FastDateFormat instance3 = FastDateFormat.getInstance("MM/dd/yyyy");
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat instance1 = FastDateFormat.getInstance("dd/MM/yyyy");
        FastDateFormat instance2 = FastDateFormat.getInstance("dd/MM/yyyy");
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        String expected = "FastDateFormat[dd/MM/yyyy," + Locale.getDefault() + "," + TimeZone.getDefault().getID() + "]";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(instance);
        StringBuffer buffer = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 2, 14); // March 14, 2022
        instance.applyRules(calendar, buffer);
        assertEquals("14/03/2022", buffer.toString());
    }
}