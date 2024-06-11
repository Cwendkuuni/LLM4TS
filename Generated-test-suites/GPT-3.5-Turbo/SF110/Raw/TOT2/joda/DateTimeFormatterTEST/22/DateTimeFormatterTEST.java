import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        Locale locale = Locale.US;
        DateTimeFormatter newFormatter = formatter.withLocale(locale);
        assertEquals(locale, newFormatter.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter newFormatter = formatter.withOffsetParsed();
        assertTrue(newFormatter.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        Chronology chronology = Chronology.getDefault();
        DateTimeFormatter newFormatter = formatter.withChronology(chronology);
        assertEquals(chronology, newFormatter.getChronology());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeZone zone = DateTimeZone.forID("America/New_York");
        DateTimeFormatter newFormatter = formatter.withZone(zone);
        assertEquals(zone, newFormatter.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        int pivotYear = 2000;
        DateTimeFormatter newFormatter = formatter.withPivotYear(pivotYear);
        assertEquals(Integer.valueOf(pivotYear), newFormatter.getPivotYear());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        int defaultYear = 2000;
        DateTimeFormatter newFormatter = formatter.withDefaultYear(defaultYear);
        assertEquals(defaultYear, newFormatter.getDefaultYear());
    }
    
    // Additional test cases for other methods can be added here
    
}