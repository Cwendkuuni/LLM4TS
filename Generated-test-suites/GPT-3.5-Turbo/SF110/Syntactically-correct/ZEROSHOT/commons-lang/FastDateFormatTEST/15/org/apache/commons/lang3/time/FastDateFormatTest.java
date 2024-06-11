import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

    // Test format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    @Test
    public void testFormatObject() {
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.format("test", buffer, null);
        Assert.assertEquals("test", buffer.toString());
    }

    // Test format(long millis)
    @Test
    public void testFormatLong() {
        FastDateFormat format = FastDateFormat.getInstance();
        String result = format.format(0);
        Assert.assertEquals("01/Jan/1970 00:00:00", result);
    }

    // Test format(Date date)
    @Test
    public void testFormatDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = sdf.parse("01/01/2022");
        FastDateFormat format = FastDateFormat.getInstance();
        String result = format.format(date);
        Assert.assertEquals("01/Jan/2022 00:00:00", result);
    }

    // Test format(Calendar calendar)
    @Test
    public void testFormatCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        FastDateFormat format = FastDateFormat.getInstance();
        String result = format.format(calendar);
        Assert.assertEquals("01/Jan/2022 00:00:00", result);
    }

    // Test format(long millis, StringBuffer buf)
    @Test
    public void testFormatLongBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.format(0, buffer);
        Assert.assertEquals("01/Jan/1970 00:00:00", buffer.toString());
    }

    // Test format(Date date, StringBuffer buf)
    @Test
    public void testFormatDateBuffer() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = sdf.parse("01/01/2022");
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.format(date, buffer);
        Assert.assertEquals("01/Jan/2022 00:00:00", buffer.toString());
    }

    // Test format(Calendar calendar, StringBuffer buf)
    @Test
    public void testFormatCalendarBuffer() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.format(calendar, buffer);
        Assert.assertEquals("01/Jan/2022 00:00:00", buffer.toString());
    }

    // Test parse(String source)
    @Test
    public void testParseString() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance();
        Date result = format.parse("01/Jan/2022 00:00:00");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date expected = sdf.parse("01/01/2022 00:00:00");
        Assert.assertEquals(expected, result);
    }

    // Test parse(String source, ParsePosition pos)
    @Test
    public void testParseStringParsePosition() {
        FastDateFormat format = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Date result = format.parse("01/Jan/2022 00:00:00", pos);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date expected = sdf.parse("01/01/2022 00:00:00");
        Assert.assertEquals(expected, result);
        Assert.assertEquals(19, pos.getIndex());
    }

    // Test parseObject(String source, ParsePosition pos)
    @Test
    public void testParseObjectParsePosition() {
        FastDateFormat format = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Object result = format.parseObject("01/Jan/2022 00:00:00", pos);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date expected = sdf.parse("01/01/2022 00:00:00");
        Assert.assertEquals(expected, result);
        Assert.assertEquals(19, pos.getIndex());
    }

    // Test getPattern()
    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getInstance();
        String pattern = format.getPattern();
        Assert.assertEquals("dd/MMM/yyyy HH:mm:ss", pattern);
    }

    // Test getTimeZone()
    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance();
        TimeZone timeZone = format.getTimeZone();
        Assert.assertEquals(TimeZone.getDefault(), timeZone);
    }

    // Test getLocale()
    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getInstance();
        Locale locale = format.getLocale();
        Assert.assertEquals(Locale.getDefault(), locale);
    }

    // Test getMaxLengthEstimate()
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getInstance();
        int maxLength = format.getMaxLengthEstimate();
        Assert.assertEquals(23, maxLength);
    }

    // Test equals(Object obj)
    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();
        Assert.assertEquals(format1, format2);
    }

    // Test hashCode()
    @Test
    public void testHashCode() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();
        Assert.assertEquals(format1.hashCode(), format2.hashCode());
    }

    // Test toString()
    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getInstance();
        String str = format.toString();
        Assert.assertEquals("FastDateFormat[dd/MMM/yyyy HH:mm:ss," + Locale.getDefault() + "," + TimeZone.getDefault().getID() + "]", str);
    }

    // Test applyRules(Calendar calendar, StringBuffer buf)
    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.applyRules(calendar, buffer);
        Assert.assertEquals("01/01/2022 00:00:00", buffer.toString());
    }
}