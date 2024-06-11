
import org.junit.Test;
import org.junit.Assert;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetInstance_String() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetInstance_String_TimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", timeZone);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetInstance_String_Locale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", locale);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetInstance_String_TimeZone_Locale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", timeZone, locale);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetDateInstance_Locale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetDateInstance_TimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetDateInstance_TimeZone_Locale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetTimeInstance_Locale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, locale);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetTimeInstance_TimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetTimeInstance_TimeZone_Locale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone, locale);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstance_Locale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, locale);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstance_TimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstance_TimeZone_Locale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone, locale);
        Assert.assertNotNull(format);
    }

    @Test
    public void testFormat_Object_StringBuffer_FieldPosition() {
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        Date date = new Date();
        format.format(date, buf, null);
        SimpleDateFormat sdf = new SimpleDateFormat();
        Assert.assertEquals(buf.toString(), sdf.format(date));
    }

    @Test
    public void testFormat_long() {
        FastDateFormat format = FastDateFormat.getInstance();
        long millis = System.currentTimeMillis();
        String expected = format.format(new Date(millis));
        String actual = format.format(millis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormat_Date() {
        FastDateFormat format = FastDateFormat.getInstance();
        Date date = new Date();
        String expected = format.format(date.getTime());
        String actual = format.format(date);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormat_Calendar() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        String expected = format.format(calendar.getTime());
        String actual = format.format(calendar);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormat_long_StringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        long millis = System.currentTimeMillis();
        format.format(millis, buf);
        SimpleDateFormat sdf = new SimpleDateFormat();
        Assert.assertEquals(buf.toString(), sdf.format(new Date(millis)));
    }

    @Test
    public void testFormat_Date_StringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        Date date = new Date();
        format.format(date, buf);
        SimpleDateFormat sdf = new SimpleDateFormat();
        Assert.assertEquals(buf.toString(), sdf.format(date));
    }

    @Test
    public void testFormat_Calendar_StringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buf = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        format.format(calendar, buf);
        SimpleDateFormat sdf = new SimpleDateFormat();
        Assert.assertEquals(buf.toString(), sdf.format(calendar.getTime()));
    }

    @Test
    public void testParse_String() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance();
        String source = "2022-01-01";
        Date expected = format.parse(source);
        SimpleDateFormat sdf = new SimpleDateFormat();
        Date actual = sdf.parse(source);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse_String_ParsePosition() {
        FastDateFormat format = FastDateFormat.getInstance();
        String source = "2022-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date expected = format.parseObject(source, pos);
        SimpleDateFormat sdf = new SimpleDateFormat();
        Date actual = sdf.parseObject(source, pos);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat();
        String expected = sdf.toPattern();
        String actual = format.getPattern();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat();
        TimeZone expected = sdf.getTimeZone();
        TimeZone actual = format.getTimeZone();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat();
        Locale expected = sdf.getLocale();
        Locale actual = format.getLocale();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat();
        int expected = sdf.getCalendar().getMaximum(Calendar.YEAR);
        int actual = format.getMaxLengthEstimate();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();
        Assert.assertEquals(format1, format2);
    }

    @Test
    public void testHashCode() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();
        Assert.assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat();
        String expected = "FastDateFormat[" + sdf.toPattern() + "," + sdf.getLocale() + "," + sdf.getTimeZone().getID() + "]";
        String actual = format.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testApplyRules() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        format.applyRules(calendar, buf);
        SimpleDateFormat sdf = new SimpleDateFormat();
        String expected = sdf.format(calendar.getTime());
        String actual = buf.toString();
        Assert.assertEquals(expected, actual);
    }
}
