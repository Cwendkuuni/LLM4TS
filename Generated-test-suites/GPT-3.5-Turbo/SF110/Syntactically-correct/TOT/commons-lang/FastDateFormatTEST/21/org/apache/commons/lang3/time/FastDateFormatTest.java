import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertNotNull(format);
        assertEquals(format.getPattern(), "");

        FastDateFormat formatWithPattern = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(formatWithPattern);
        assertEquals(formatWithPattern.getPattern(), "yyyy-MM-dd HH:mm:ss");

        FastDateFormat formatWithPatternAndTimeZone = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"));
        assertNotNull(formatWithPatternAndTimeZone);
        assertEquals(formatWithPatternAndTimeZone.getPattern(), "yyyy-MM-dd HH:mm:ss");
        assertEquals(formatWithPatternAndTimeZone.getTimeZone().getID(), "GMT");

        FastDateFormat formatWithPatternAndLocale = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.US);
        assertNotNull(formatWithPatternAndLocale);
        assertEquals(formatWithPatternAndLocale.getPattern(), "yyyy-MM-dd HH:mm:ss");
        assertEquals(formatWithPatternAndLocale.getLocale(), Locale.US);

        FastDateFormat formatWithPatternAndTimeZoneAndLocale = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(formatWithPatternAndTimeZoneAndLocale);
        assertEquals(formatWithPatternAndTimeZoneAndLocale.getPattern(), "yyyy-MM-dd HH:mm:ss");
        assertEquals(formatWithPatternAndTimeZoneAndLocale.getTimeZone().getID(), "GMT");
        assertEquals(formatWithPatternAndTimeZoneAndLocale.getLocale(), Locale.US);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(dateFormat);
        assertEquals(dateFormat.getPattern(), "EEEE, MMMM d, yyyy");

        FastDateFormat dateFormatWithLocale = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.US);
        assertNotNull(dateFormatWithLocale);
        assertEquals(dateFormatWithLocale.getPattern(), "MMMM d, yyyy");

        FastDateFormat dateFormatWithTimeZone = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(dateFormatWithTimeZone);
        assertEquals(dateFormatWithTimeZone.getPattern(), "MMM d, yyyy");

        FastDateFormat dateFormatWithTimeZoneAndLocale = FastDateFormat.getDateInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(dateFormatWithTimeZoneAndLocale);
        assertEquals(dateFormatWithTimeZoneAndLocale.getPattern(), "M/d/yy");

    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(timeFormat);
        assertEquals(timeFormat.getPattern(), "h:mm:ss a zzzz");

        FastDateFormat timeFormatWithLocale = FastDateFormat.getTimeInstance(FastDateFormat.LONG, Locale.US);
        assertNotNull(timeFormatWithLocale);
        assertEquals(timeFormatWithLocale.getPattern(), "h:mm:ss a z");

        FastDateFormat timeFormatWithTimeZone = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(timeFormatWithTimeZone);
        assertEquals(timeFormatWithTimeZone.getPattern(), "h:mm:ss a");

        FastDateFormat timeFormatWithTimeZoneAndLocale = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(timeFormatWithTimeZoneAndLocale);
        assertEquals(timeFormatWithTimeZoneAndLocale.getPattern(), "h:mm a");
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT);
        assertNotNull(dateTimeFormat);
        assertEquals(dateTimeFormat.getPattern(), "EEEE, MMMM d, yyyy h:mm a");

        FastDateFormat dateTimeFormatWithLocale = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, Locale.US);
        assertNotNull(dateTimeFormatWithLocale);
        assertEquals(dateTimeFormatWithLocale.getPattern(), "MMMM d, yyyy h:mm a");

        FastDateFormat dateTimeFormatWithTimeZone = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, TimeZone.getTimeZone("GMT"));
        assertNotNull(dateTimeFormatWithTimeZone);
        assertEquals(dateTimeFormatWithTimeZone.getPattern(), "MMM d, yyyy h:mm:ss a z");

        FastDateFormat dateTimeFormatWithTimeZoneAndLocale = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(dateTimeFormatWithTimeZoneAndLocale);
        assertEquals(dateTimeFormatWithTimeZoneAndLocale.getPattern(), "M/d/yy h:mm:ss a");
    }

    @Test
    public void testFormat() {
        FastDateFormat format = FastDateFormat.getInstance("YYYY-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"), Locale.US);

        String expectedFormattedDate = "2022-01-01 12:34:56";

        String formattedObjectStringBuffer = format.format(new Object(), new StringBuffer(), null).toString();
        assertEquals(formattedObjectStringBuffer, expectedFormattedDate);

        String formattedMillis = format.format(1641041696000L);
        assertEquals(formattedMillis, expectedFormattedDate);

        String formattedDate = format.format(new Date(1641041696000L));
        assertEquals(formattedDate, expectedFormattedDate);

        String formattedCalendar = format.format(Calendar.getInstance());
        assertEquals(formattedCalendar, expectedFormattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance("YYYY-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"), Locale.US);

        String dateString = "2022-01-01 12:34:56";
        Date expectedDate = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").parse(dateString);

        Date parsedDateWithString = format.parse(dateString);
        assertEquals(parsedDateWithString, expectedDate);

        Date parsedDateWithParsePosition = format.parse(dateString, new ParsePosition(0));
        assertEquals(parsedDateWithParsePosition, expectedDate);
    }
}