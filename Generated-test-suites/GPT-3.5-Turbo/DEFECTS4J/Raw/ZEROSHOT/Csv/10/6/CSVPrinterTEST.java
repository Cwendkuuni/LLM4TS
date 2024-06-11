import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringBuilder stringBuilder;
    private CSVFormat csvFormat;

    @Before
    public void setUp() throws Exception {
        stringBuilder = new StringBuilder();
        csvFormat = CSVFormat.DEFAULT;
        csvPrinter = new CSVPrinter(stringBuilder, csvFormat);
    }

    @Test
    public void testClose() throws IOException {
        csvPrinter.close();
    }

    @Test
    public void testFlush() throws IOException {
        csvPrinter.flush();
    }

    @Test
    public void testPrint_WithNullValue() throws IOException {
        csvPrinter.print(null);
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testPrint_WithStringValue() throws IOException {
        csvPrinter.print("value");
        assertEquals("value", stringBuilder.toString());
    }

    @Test
    public void testPrint_WithEscapeCharacter() throws IOException {
        csvPrinter.print(",");
        assertEquals("\",\"", stringBuilder.toString());
    }

    @Test
    public void testPrint_WithDelimiterCharacter() throws IOException {
        csvPrinter.print("value,value");
        assertEquals("value,value", stringBuilder.toString());
    }

    @Test
    public void testPrint_WithQuotingEnabled() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withQuoteChar('"').withQuoting(true);
        csvPrinter = new CSVPrinter(stringBuilder, csvFormat);
        csvPrinter.print("value");
        assertEquals("\"value\"", stringBuilder.toString());
    }

    @Test
    public void testPrint_WithQuotingAndEscapeCharacters() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withQuoteChar('"').withQuoting(true).withEscapeChar('\\');
        csvPrinter = new CSVPrinter(stringBuilder, csvFormat);
        csvPrinter.print("\"value\"");
        assertEquals("\"\\\"value\\\"\"", stringBuilder.toString());
    }

    @Test
    public void testPrintComment_WithDisabledCommenting() throws IOException {
        csvPrinter.printComment("comment");
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testPrintComment_WithEnabledCommenting() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withCommentStart('#').withCommentingEnabled(true);
        csvPrinter = new CSVPrinter(stringBuilder, csvFormat);
        csvPrinter.printComment("comment");
        assertEquals("# comment\n", stringBuilder.toString());
    }

    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        assertEquals("\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecord_WithIterable() throws IOException {
        csvPrinter.printRecord(csvFormat);
        assertEquals("org.apache.commons.csv.CSVFormat@hashcode\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecord_WithArray() throws IOException {
        Object[] values = new Object[]{"value1", "value2"};
        csvPrinter.printRecord(values);
        assertEquals("value1,value2\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecords_WithIterable() throws IOException {
        csvPrinter.printRecords(csvFormat);
        assertEquals("org.apache.commons.csv.CSVFormat@hashcode\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecords_WithArray() throws IOException {
        Object[] values = new Object[]{"value1", "value2"};
        csvPrinter.printRecords(values);
        assertEquals("value1,value2\n", stringBuilder.toString());
    }

    @Test(expected = SQLException.class)
    public void testPrintRecords_WithResultSetAndSQLException() throws IOException, SQLException {
        ResultSet resultSet = null;
        csvPrinter.printRecords(resultSet);
    }

    @Test
    public void testGetOut() {
        Appendable appendable = csvPrinter.getOut();
        assertNotNull(appendable);
        assertTrue(appendable instanceof StringBuilder);
    }
}