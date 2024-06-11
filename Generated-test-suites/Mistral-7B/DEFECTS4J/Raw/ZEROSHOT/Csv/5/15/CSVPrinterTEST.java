import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() throws Exception {
        output = new ByteArrayOutputStream();
        CSVFormat csvFormat = new CSVFormat(',', Quote.MINIMAL);
        printer = new CSVPrinter(new OutputStreamWriter(output), csvFormat);
    }

    @After
    public void tearDown() throws Exception {
        printer.close();
    }

    @Test
    public void testCreateCSVPrinter() {
        assertNotNull(printer);
        assertSame(output.toString(), printer.getOut().toString());
        assertFalse(printer.isNewRecord());
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Hello, World!");
        printer.flush();
        String result = output.toString();
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        printer.print('"');
        printer.flush();
        String result = output.toString();
        assertEquals("\"", result);
    }

    @Test
    public void testPrintQuotedValueWithEscape() throws IOException {
        printer.setEscape("\u001F");
        printer.print("\"");
        printer.flush();
        String result = output.toString();
        assertEquals("\\u001F\"", result);
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        printer.flush();
        String result = output.toString();
        assertEquals("", result);
    }

    @Test
    public void testPrintQuotedNullValue() throws IOException {
        printer.print(null);
        printer.flush();
        String result = output.toString();
        assertEquals("\"", result);
    }

    @Test
    public void testPrintQuotedNullValueWithEscape() throws IOException {
        printer.setEscape("\u001F");
        printer.print(null);
        printer.flush();
        String result = output.toString();
        assertEquals("\\u001F\"", result);
    }

    @Test
    public void testPrintQuotedValueWithEscape() throws IOException {
        printer.setEscape("\u001F");
        printer.print("\"abcd\"");
        printer.flush();
        String result = output.toString();
        assertEquals("\\u001F\"abcd\"", result);
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(Arrays.asList("value1", "value2", "value3"));
        printer.flush();
        String result = output.toString();
        assertEquals("value1,value2,value3\n", result);
    }

    @Test
    public void testPrintRecordWithQuotes() throws IOException {
        printer.setQuotePolicy(Quote.ALL);
        printer.printRecord(Arrays.asList("value1", "value2", "value3"));
        printer.flush();
        String result = output.toString();
        assertEquals("\"value1\",\"value2\",\"value3\"\n", result);
    }

    @Test
    public void testPrintRecordWithNullValues() throws IOException {
        printer.printRecord(Arrays.asList("value1", null, "value3"));
        printer.flush();
        String result = output.toString();
        assertEquals("value1,\"\",value3\n", result);
    }

    @Test
    public void testPrintRecordWithEmptyValue() throws IOException {
        printer.printRecord(Collections.singletonList(""));
        printer.flush();
        String result = output.toString();
        assertEquals(",\n", result);
    }

    @Test
    public void testPrintRecordWithEmptyValues() throws IOException {
        printer.printRecord(Collections.emptyList());
        printer.flush();
        String result = output.toString();
        assertEquals("", result);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment.");
        printer.flush();
        String result = output.toString();
        assertEquals("# This is a comment.\n", result);
    }

    @Test
    public void testPrintln() throws IOException {
        printer.println();
        printer.flush();
        String result = output.toString();
        assertEquals("\n", result);
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        String sql = "SELECT COLUMN1, COLUMN2, NULL FROM TABLE1 WHERE ID = 1";
        try (Statement statement = TestHelper.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            printer.printRecords(resultSet);
            printer.flush();
            String result = output.toString();
            assertEquals("COLUMN1,COLUMN2,\n", result);
        }
    }

    @Test
    public void testPrintRecordsWithQuotedValues() throws IOException, SQLException {
        String sql = "SELECT 'Hello, World!', '12345', NULL FROM TABLE1 WHERE ID = 1";
        try (Statement statement = TestHelper.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            printer.setQuotePolicy(Quote.ALL);
            printer.printRecords(resultSet);
            printer.flush();
            String result = output.toString();
            assertEquals("\"Hello, World!\",\"12345\",\n", result);
        }
    }

    @Test
    public void testPrintRecordsWithQuotedNullValues() throws IOException, SQLException {
        String sql = "SELECT 'Hello, World!', NULL, NULL FROM TABLE1 WHERE ID = 1";
        try (Statement statement = TestHelper.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            printer.setQuotePolicy(Quote.ALL);
            printer.printRecords(resultSet);
            printer.flush();
            String result = output.toString();
            assertEquals("\"Hello, World!\",\"\",\"\"\n", result);
        }
    }
}