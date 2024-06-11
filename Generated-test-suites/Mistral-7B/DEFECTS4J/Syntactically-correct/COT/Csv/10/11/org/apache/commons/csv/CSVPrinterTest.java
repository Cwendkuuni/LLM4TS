package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.apache.commons.csv.test.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the CSVPrinter class
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream outStream;
    private CSVFormat format;

    @Before
    public void setUp() {
        outStream = new ByteArrayOutputStream();
        format = new CSVFormat(new CSVParser(new StringReader("")), false);
        format.setDelimiter(',');
        format.setQuoteChar('"');
        format.setQuotePolicy(Quote.MINIMAL);
    }

    @After
    public void tearDown() throws Exception {
        ((CSVPrinter) testPrinter).close();
    }

    @Test
    public void testCSVPrinterInitialization() {
        try {
            final CSVPrinter testPrinter = new CSVPrinter(new StringWriter(), format);
            assertEquals("Writer initialized", testPrinter.getOut(), format.getWriter());
        } catch (IOException e) {
            fail("Initialization failed: " + e.getMessage());
        }
    }

    @Test
    public void testClose() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.close();
        assertFalse("Close should have closed the output stream", outStream.canWrite());
    }

    @Test
    public void testFlush() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.print("test");
        testPrinter.flush();
        assertEquals("Flush should have written the test string", "test\n", outStream.toString());
    }

    @Test
    public void testPrintNullValue() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.print(null);
        assertEquals("Printing null value should print an empty string", "", outStream.toString());
    }

    @Test
    public void testPrintStringValue() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.print("testString");
        assertEquals("Printing a string value should print the string", "testString", outStream.toString());
    }

    @Test
    public void testPrintQuoteAndEscape() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.print("test,\"test,\"test\"");

        String expectedOutput = "test,\"test,\"test\"\n";
        assertEquals("Printing a string with quotes and escapes", expectedOutput, outStream.toString());
    }

    @Test
    public void testPrintQuoteOnly() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.print("test,\"test\"");

        String expectedOutput = "test,\"test\"\n";
        assertEquals("Printing a string with only quotes", expectedOutput, outStream.toString());
    }

    @Test
    public void testPrintEmptyLine() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.println();
        assertEquals("Printing an empty line should print an empty line", "", outStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.printComment("This is a comment");
        assertEquals("Printing a comment should print the comment", "This is a comment\n", outStream.toString());
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.print("value1");
        testPrinter.print("value2");
        testPrinter.println();

        String expectedOutput = "value1,value2\n";
        assertEquals("Printing multiple values should print the values separated by a delimiter",
                expectedOutput, outStream.toString());
    }

    @Test
    public void testPrintMultipleRecords() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.printRecord(new String[]{"record1", "record2"});
        testPrinter.printRecord(new String[]{"record3", "record4"});
        testPrinter.println();

        String expectedOutput = "record1,record2\nrecord3,record4\n";
        assertEquals("Printing multiple records should print the records separated by a newline",
                expectedOutput, outStream.toString());
    }

    @Test
    public void testPrintRecordsCollection() throws IOException {
        CSVPrinter testPrinter = new CSVPrinter(outStream, format);
        testPrinter.printRecords(TestUtil.stringArray("record1", "record2"));

        String expectedOutput = "record1,record2\n";
        assertEquals("Printing records from a collection should print the records separated by a newline",
                expectedOutput, outStream.toString());
    }

    @Test
    public void testPrintRecordResultSet() throws IOException, SQLException {
        String expectedOutput = "column1,column2\n";
        try (ResultSet testData = TestUtil.createTestResultSet()) {
            CSVPrinter testPrinter = new CSVPrinter(outStream, format);
            testPrinter.printRecords(testData);
        }
        assertEquals("Printing records from a JDBC ResultSet should print the records separated by a newline",
                expectedOutput, outStream.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        try {
            CSVPrinter testPrinter = new CSVPrinter(new StringWriter(), null);
        } catch (IllegalArgumentException e) {
            assertEquals("Format should not be null", "Format must not be null", e.getMessage());
            throw e;
        }
        fail("Format should have thrown IllegalArgumentException");
    }

    @Test(expected = IOException.class)
    public void testNullOutput() throws IOException {
        try {
            CSVPrinter testPrinter = new CSVPrinter(null, format);
        } catch (IOException e) {
            assertEquals("Output should not be null", "Output must not be null", e.getMessage());
            throw e;
        }
        fail("Output should have thrown IOException");
    }
}