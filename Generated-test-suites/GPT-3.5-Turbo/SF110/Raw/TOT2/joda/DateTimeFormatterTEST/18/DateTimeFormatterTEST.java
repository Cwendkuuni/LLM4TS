import org.joda.time.format.*;
import org.joda.time.*;
import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser() {
            @Override
            public int parseInto(ReadWritableInstant readWritableInstant, String s, int n) {
                return 0;
            }
        });
        assertFalse(formatter.isPrinter());
    }

    // Test for isParser() method would be added here

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        Locale locale = Locale.US;
        DateTimeFormatter newFormatter = formatter.withLocale(locale);
        
        assertEquals(locale, newFormatter.getLocale());
    }

    // More test methods would be added for other public methods

    @Test(expected = UnsupportedOperationException.class)
    public void testPrintToUnsupportedOperation() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, new DateMidnight());
    }

    // More error scenario tests would be added here

}