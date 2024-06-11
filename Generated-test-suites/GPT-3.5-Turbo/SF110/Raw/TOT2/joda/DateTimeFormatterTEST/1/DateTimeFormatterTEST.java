import static org.junit.Assert.*;
import org.junit.Test;

public class DateTimeFormatterTest {

    private DateTimePrinter mockPrinter;
    private DateTimeParser mockParser;

    @Before
    public void setUp() {
        mockPrinter = new MockPrinter();
        mockParser = new MockParser();
    }

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(mockPrinter, mockParser);
        assertTrue(formatter.isPrinter());
    }

    @Test
    public void testWithZone() {
        DateTimeZone zone = DateTimeZone.forID("America/New_York");
        DateTimeFormatter formatter = new DateTimeFormatter(mockPrinter, mockParser);
        assertTrue(formatter.withZone(zone).getZone().equals(zone));
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(mockPrinter, mockParser);
        LocalDate date = formatter.parseLocalDate("2022-01-01");
        assertEquals(date.getYear(), 2022);
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(mockPrinter, mockParser);
        long millis = formatter.parseMillis("2022-01-01T00:00:00");
        assertEquals(millis, 1640995200000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntoWithNullInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(mockPrinter, mockParser);
        formatter.parseInto(null, "2022-01-01T00:00:00", 0);
    }

    @Test
    public void testPrintToWithReadableInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(mockPrinter, mockParser);
        StringBuffer sb = new StringBuffer();
        ReadableInstant instant = new Instant(1640995200000);
        formatter.printTo(sb, instant);
        assertEquals(sb.toString(), "2022-01-01T00:00:00.000Z");
    }

}