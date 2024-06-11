
import com.google.gson.internal.bind.util.ISO8601Utils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {
    
    @Test
    public void testFormat_noMillis_noT() {
        Date date = new Date(123456789);
        String expected = "1970-01-02T10:17:36Z";
        
        String result = ISO8601Utils.format(date);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormat_noMillis_withT() {
        Date date = new Date(123456789);
        String expected = "1970-01-02T10:17:36Z";
        
        String result = ISO8601Utils.format(date);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormat_withMillis_noT() {
        Date date = new Date(123456789);
        String expected = "1970-01-02T10:17:36.789Z";
        
        String result = ISO8601Utils.format(date, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormat_withMillis_withT() {
        Date date = new Date(123456789);
        String expected = "1970-01-02T10:17:36.789Z";
        
        String result = ISO8601Utils.format(date, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormat_withMillis_noT_withOffset() {
        Date date = new Date(123456789);
        String expected = "1970-01-02T10:17:36.789+00:00";
        
        String result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormat_withMillis_withT_withOffset() {
        Date date = new Date(123456789);
        String expected = "1970-01-02T10:17:36.789+00:00";
        
        String result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse_validInput() throws ParseException {
        String date = "1970-01-02T10:17:36Z";
        Date expected = new Date(123456789);
        
        Date result = ISO8601Utils.parse(date, new ParsePosition(0));
        
        assertEquals(expected, result);
    }
    
    @Test(expected = ParseException.class)
    public void testParse_invalidInput() throws ParseException {
        String date = "1970-01-02T10:17:36";
        
        ISO8601Utils.parse(date, new ParsePosition(0));
    }
    
    @Test(expected = ParseException.class)
    public void testParse_invalidFormat() throws ParseException {
        String date = "02-01-1970T10:17:36Z";
        
        ISO8601Utils.parse(date, new ParsePosition(0));
    }
}
