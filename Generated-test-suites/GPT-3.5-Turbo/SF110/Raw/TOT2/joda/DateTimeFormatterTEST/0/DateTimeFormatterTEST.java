import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.DateTimeZone;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import java.util.Locale;

public class DateTimeFormatterTest {
    private DateTimePrinter printer;
    private DateTimeParser parser;
    private DateTimeFormatter formatter;

    @Test
    public void testIsPrinter() {
        printer = new DateTimePrinter() {
            @Override
            public int estimatePrintedLength() {
                return 0;
            }
            @Override
            public void printTo(StringBuffer sb, long instant, Chronology chrono, int offset, DateTimeZone zone, Locale locale) {
            }
        };
        formatter = new DateTimeFormatter(printer, parser);
        assertTrue(formatter.isPrinter());
    }

    @Test
    public void testIsParser() {
        parser = new DateTimeParser() {
            @Override
            public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
                return 0;
            }
        };
        formatter = new DateTimeFormatter(printer, parser);
        assertTrue(formatter.isParser());
    }

    // Add more test cases here to cover all public methods and edge cases
    
    @Test
    public void testWithChronology() {
        formatter = new DateTimeFormatter(printer, parser);
        Chronology chrono = GregorianChronology.getInstance();
        formatter = formatter.withChronology(chrono);
        assertEquals(chrono, formatter.getChronology());
    }

    @Test
    public void testWithZone() {
        formatter = new DateTimeFormatter(printer, parser);
        DateTimeZone zone = DateTimeZone.forID("America/New_York");
        formatter = formatter.withZone(zone);
        assertEquals(zone, formatter.getZone());
    }

    // Merge all individual test cases into this complete JUnit 4 test file

}