package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outStream;
    private Writer writer;

    @Before
    public void setUp() {
        final CSVFormat format = new CSVFormat();
        this.outStream = new ByteArrayOutputStream();
        this.writer = new StringWriter();
        this.printer = new CSVPrinter(this.outStream, format);
    }

    @Test
    public void testPrintNullValue() throws IOException {
        this.printer.print(null);
        final String csvRecord = this.outputAsString();
        assertNull(csvRecord);
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        this.printer.print("");
        final String csvRecord = this.outputAsString();
        assertEquals("", csvRecord);
    }

    @Test
    public void testPrintString() throws IOException {
        this.printer.print("Test");
        final String csvRecord = this.outputAsString();
        assertEquals("Test", csvRecord);
    }

    @Test
    public void testPrintObject() throws IOException {
        final String testObject = "Test Object";
        this.printer.print(testObject);
        final String csvRecord = this.outputAsString();
        assertEquals(testObject, csvRecord);
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        this.printer.print("First value");
        this.printer.print("Second value");
        this.printer.println();

        final String csvRecord = this.outputAsString();
        assertEquals("First value,Second value", csvRecord);
    }

    @Test
    public void testPrintMultipleValuesWithNewLine() throws IOException {
        this.printer.print("First value");
        this.printer.println();
        this.printer.print("Second value");
        this.printer.println();

        final String csvRecord = this.outputAsString();
        assertEquals("First value\nSecond value", csvRecord);
    }

    @Test
    public void testPrintComment() throws IOException {
        this.printer.printComment("Test comment");
        final String csvRecord = this.outputAsString();
        assertEquals("# Test comment\n", csvRecord);
    }

    @Test
    public void testPrintRecord() throws IOException {
        this.printer.printRecord(new String[]{"First value", "Second value"});
        final String csvRecord = this.outputAsString();
        assertEquals("First value,Second value\n", csvRecord);
    }

    @Test
    public void testPrintRecordWithQuotes() throws IOException {
        this.printer.setQuote("\"");
        this.printer.printRecord(new String[]{"\"Test\"", "\"First,Second\""});
        final String csvRecord = this.outputAsString();
        assertEquals("\"Test\",\"First,Second\"\n", csvRecord);
    }

    @Test
    public void testPrintRecordWithEscaping() throws IOException {
        this.printer.setEscape("\u001E");
        this.printer.printRecord(new String[]{"\"Test, Test\u001E\",\"First,Second\""});
        final String csvRecord = this.outputAsString();
        assertEquals("\"Test, Test\\u001E\",\"First,Second\"\n", csvRecord);
    }

    @Test
    public void testPrintRecords() throws IOException {
        this.printer.printRecords(new String[][]{{"First value", "Second value"}, {"Third value", "Fourth value"}});
        final String csvRecord = this.outputAsString();
        assertEquals("First value,Second value\nThird value,Fourth value\n", csvRecord);
    }

    @Test
    public void testPrintRecordsWithQuotes() throws IOException {
        this.printer.setQuote("\"");
        this.printer.printRecords(new String[][]{{"\"Test\"", "\"First,Second\""}, {"\"Third\",\"Fourth\""}});
        final String csvRecord = this.outputAsString();
        assertEquals("\"Test\",\"First,Second\"\n\"Third\",\"Fourth\"\n", csvRecord);
    }

    @Test
    public void testPrintRecordsWithEscaping() throws IOException {
        this.printer.setEscape("\u001E");
        this.printer.printRecords(new String[][]{{"\"First,\"Test, Test\u001E\"", "\"First,Second\""}, {"\"Third,\"Fourth\""}});
        final String csvRecord = this.outputAsString();
        assertEquals("\"First,\"Test, Test\\u001E\",\"First,Second\"\n\"Third,\"Fourth\"\n", csvRecord);
    }

    @Test
    public void testPrintRecordsWithComment() throws IOException {
        this.printer.printComment("Test comment");
        this.printer.printRecords(new String[][]{{"First value", "Second value"}, {"Third value", "Fourth value"}});
        final String csvRecord = this.outputAsString();
        assertEquals("# Test comment\nFirst value,Second value\nThird value,Fourth value\n", csvRecord);
    }

    @Test
    public void testFlush() throws IOException {
        this.printer.close();
        this.printer = new CSVPrinter(this.writer, new CSVFormat());
        this.printer.print("Test");
        this.printer.flush();
        final String csvRecord = this.writer.toString();
        assertEquals("Test\n", csvRecord);
    }

    @Test
    public void testClose() throws IOException {
        this.printer.close();
        final String csvRecord = this.outputAsString();
        assertTrue(csvRecord.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        this.printer = new CSVPrinter(this.outStream, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOut() throws IOException {
        this.printer = new CSVPrinter(null, new CSVFormat());
    }

    private String outputAsString() throws IOException {
        if (this.writer != null) {
            return this.writer.toString();
        }
        return this.outStream.toString();
    }
}