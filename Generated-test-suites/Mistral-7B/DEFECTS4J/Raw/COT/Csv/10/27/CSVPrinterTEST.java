package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * CSVPrinter tests.
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream out;
    private CSVFormat format;
    private CSVPrinter printer;

    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        format = new CSVFormat(',', Quote.MINIMAL);
        printer = new CSVPrinter(new StringWriter(out), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Hello World");
        printer.flush();
        String result = out.toString();
        String expected = "Hello World";
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPrintQuoted() throws IOException {
        printer.printQuoted("Hello World");
        printer.flush();
        String result = out.toString();
        String expected = "\"" + "Hello World" + "\"";
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPrintEscaped() throws IOException {
        printer.printEscaped("Hello World");
        printer.flush();
        String result = out.toString();
        String expected = "\\\"Hello World\\\"";
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.flush();
        String result = out.toString();
        String expected = format.getNullString();
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(CSVRecord.builder().add("Hello").add("World").build());
        printer.flush();
        String result = out.toString();
        String expected = "Hello,World\n";
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPrintRecordWithQuotes() throws IOException {
        printer.printQuoted(true);
        printer.printRecord(CSVRecord.builder().add("Hello").add("World").build());
        printer.flush();
        String result = out.toString();
        String expected = "\"" + "Hello" + "\",\"" + "World" + "\"\n";
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("Hello comment");
        printer.flush();
        String result = out.toString();
        String expected = "# Hello comment\n";
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPrintln() throws IOException {
        printer.println();
        printer.flush();
        String result = out.toString();
        String expected = "\n";
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        String testData = "Name,Age,City\nAlice,25,New York\nBob,30,Chicago";
        printer.printRecords(testData.split("\n"));
        String result = out.toString();
        String expected = testData;
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPrintRecordsWithQuotes() throws IOException {
        printer.printQuoted(true);
        String testData = "Name,\"Age\",City\n\"Alice\",\"25\",\"New York\"\n\"Bob\",\"30\",\"Chicago\"";
        printer.printRecords(testData.split("\n"));
        String result = out.toString();
        String expected = testData;
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPrintRecordsWithQuotesAndEscapes() throws IOException {
        printer.printQuoted(true);
        printer.printEscaped(true);
        String testData = "Name,\"Age\",City\n\"Alice\",\"25\",\"New York\"\n\"Bob\",\"30\",\"Chicago with \\\"escapes\\\"\"";
        printer.printRecords(testData.split("\n"));
        String result = out.toString();
        String expected = testData;
        assertTrue(expected.equals(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullOut() throws IOException {
        new CSVPrinter(null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullFormat() throws IOException {
        new CSVPrinter(out, null);
    }

    @Test(expected = IOException.class)
    public void testPrintWithNullObject() throws IOException {
        printer.print(null);
    }

    @Test(expected = IOException.class)
    public void testPrintWithNullCharSequence() throws IOException {
        printer.print(null, null, 0, 0);
    }

    @Test(expected = IOException.class)
    public void testPrintRecordWithEmptyIterable() throws IOException {
        printer.printRecords((Iterable<?>) new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordsWithNullValues() throws IOException, SQLException {
        printer.printRecords((Iterable<?>) null);
    }

    @Test(expected = SQLException.class)
    public void testPrintRecordsWithNullResultSet() throws SQLException, IOException {
        printer.printRecords((ResultSet) null);
    }
}