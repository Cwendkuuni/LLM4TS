package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVPrinter.
 */
public class CSVPrinterTest {

    private static final String NEWLINE = System.lineSeparator();
    private static final String DELIMITER = ",";
    private static final String ESCAPED_DELIMITER = "\\,";
    private static final String EMPTY = "";
    private static final String NULL_STRING = "null";
    private static final String EMPTY_LINE = DELIMITER + NEWLINE;
    private static final String COMMENT = "#";
    private static final char QUOTE_CHAR = '"';

    private CSVPrinter printer;

    @Before
    public void setUp() throws IOException {
        final Appendable out = new StringBuilder();
        final CSVFormat format = new CSVFormat(null, DELIMITER, EMPTY, Quote.MINIMAL, null);
        printer = new CSVPrinter(out, format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        assertEquals(NULL_STRING, printer.getOut().toString());
    }

    @Test
    public void testPrintStringValue() throws IOException {
        printer.print("Test");
        assertEquals("Test", printer.getOut().toString());
    }

    @Test
    public void testPrintCharValue() throws IOException {
        printer.print('A');
        assertEquals("A", printer.getOut().toString());
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        printer.printQuote('"', "Test");
        assertEquals("\"Test\"", printer.getOut().toString());
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        printer.printEscape("Test" + DELIMITER + "Test" + DELIMITER + "Test");
        assertEquals("Test,\"Test\"," + "Test", printer.getOut().toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(new String[]{"Test1", "Test2"});
        assertEquals("Test1," + "Test2" + NEWLINE, printer.getOut().toString());
    }

    @Test
    public void testPrintRecordWithEmptyValue() throws IOException {
        printer.printRecord(new String[]{"Test1", EMPTY, "Test3"});
        assertEquals("Test1," + EMPTY_LINE + "Test3" + NEWLINE, printer.getOut().toString());
    }

    @Test
    public void testPrintRecordWithComment() throws IOException {
        printer.printComment("Test Comment");
        assertEquals("# Test Comment" + NEWLINE + NEWLINE, printer.getOut().toString());
    }

    @Test
    public void testPrintRecordWithQuoteAndEscape() throws IOException {
        printer.printRecord(new String[]{"Test1", "\"Test,Test\""});
        assertEquals("Test1,\"Test,\"Test\"" + NEWLINE, printer.getOut().toString());
    }

    @Test
    public void testPrintRecordWithEscapeAndQuote() throws IOException {
        printer.printRecord(new String[]{"Test1", "Test,\"Test\""});
        assertEquals("Test1,Test,\"Test\"" + NEWLINE, printer.getOut().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOut() throws IOException {
        new CSVPrinter(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFormat() throws IOException {
        new CSVPrinter(new StringBuilder(), null);
    }

    @Test(expected = IOException.class)
    public void testPrintBeforeRecordsSet() throws IOException {
        printer.print("Test");
    }

    @Test(expected = IOException.class)
    public void testPrintRecordBeforeCSVPrinterInitialized() throws IOException {
        final Appendable out = new StringBuilder();
        final CSVFormat format = new CSVFormat(null, DELIMITER, EMPTY, Quote.MINIMAL, null);
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecord(new String[]{"Test1", "Test2"});
        printer.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintCommentWithDisabledCommenting() throws IOException {
        final CSVFormat format = new CSVFormat(null, DELIMITER, EMPTY, Quote.MINIMAL, null);
        format.setCommentingEnabled(false);
        final Appendable out = new StringBuilder();
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.printComment("Test Comment");
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        printer.printRecords(new String[]{"Test1", "Test2"});
        assertEquals("Test1," + "Test2" + NEWLINE, printer.getOut().toString());

        printer.reset();
        final ResultSet resultSet = mockResultSet(new String[]{"Test1", "Test2", "Test3"});
        printer.printRecords(resultSet);
        assertEquals("Test1," + "Test2" + NEWLINE + "Test1," + "Test2" + NEWLINE + "Test1," + "Test3" + NEWLINE,
                printer.getOut().toString());
    }
}