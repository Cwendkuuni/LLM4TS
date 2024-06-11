import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testParse_ISO8601() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateStr = "2022-01-01T12:00:00.000Z";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(dateStr);
        
        Date actualDate = stdDateFormat.parse(dateStr);
        
        assertEquals(expectedDate.getTime(), actualDate.getTime());
    }

    @Test
    public void testParse_PlainDate() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateStr = "2022-01-01";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        
        Date actualDate = stdDateFormat.parse(dateStr);
        
        assertEquals(expectedDate.getTime(), actualDate.getTime());
    }

    @Test
    public void testParse_RFC1123() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateStr = "Sat, 01 Jan 2022 12:00:00 GMT";
        Date expectedDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).parse(dateStr);
        
        Date actualDate = stdDateFormat.parse(dateStr);
        
        assertEquals(expectedDate.getTime(), actualDate.getTime());
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidDateFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String invalidDateStr = "2022-24-01";
        
        stdDateFormat.parse(invalidDateStr);
    }

    @Test
    public void testFormat_ISO8601() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = new Date();
        String expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(date);
        
        StringBuffer actualDateFormat = new StringBuffer();
        stdDateFormat.format(date, actualDateFormat, null);
        
        assertEquals(expectedDateFormat, actualDateFormat.toString());
    }
    
    @Test
    public void testSetTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        String dateStr = "2022-01-01T12:00:00.000Z";
        Date expectedDateWithGMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(dateStr);
        
        TimeZone timeZone = TimeZone.getTimeZone("GMT+2");
        stdDateFormat.setTimeZone(timeZone);
        Date actualDateWithCustomTimeZone = stdDateFormat.parse(dateStr);
        
        assertEquals(7200000, actualDateWithCustomTimeZone.getTime() - expectedDateWithGMT.getTime());
    }

}