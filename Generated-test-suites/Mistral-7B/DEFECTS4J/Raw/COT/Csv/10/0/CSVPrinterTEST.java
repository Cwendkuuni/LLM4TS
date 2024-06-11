package org.apache.commons.csv; // Change the package name if necessary

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.DEFAULT_QUOTE_CHAR;
import static org.apache.commons.csv.CSVFormat.DEFAULT_QUOTE_POLICY;
import static org.apache.commons.csv.CSVFormat.EXPLICIT;
import static org.apache.commons.csv.CSVFormat.QUOTE_MINIMAL;
import static org.apache.commons.csv.CSVFormat.QUOTE_NONE;
import static org.apache.commons.csv.CSVFormat.QUOTE_ALL;
import static org.apache.commons.csv.CSVPrinterTest.createTestCSVPrinter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() throws IOException {
        // Create a test CSVPrinter
        this.output = new ByteArrayOutputStream();
        this.printer = createTestCSVPrinter(this.output, DEFAULT);
    }

    @Test
    public void testEmptyConstructor() {
        assertNull(CSVPrinter.class.newInstance());
    }

    @Test
    public void testContructorWithNullOut() {
        assertNull(new CSVPrinter(null, DEFAULT));
    }

    @Test
    public void testContructorWithNullFormat() {
        assertNull(new CSVPrinter(new ByteArrayOutputStream(), null));
    }

    @Test
    public void testPrintString() throws IOException {
        this.printer.print("Hello World");
        String result = this.printer.getOut().toString();
        assertEquals("Hello World", result);
    }

    @Test
    public void testPrintNull() throws IOException {
        this.printer.print(null);
        String result = this.printer.getOut().toString();
        assertEquals(DEFAULT.getNullString(), result);
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        this.printer.print("");
        String result = this.printer.getOut().toString();
        assertEquals(Constants.EMPTY, result);
    }

    @Test
    public void testPrintChar() throws IOException {
        this.printer.print('a');
        String result = this.printer.getOut().toString();
        assertEquals("a", result);
    }

    @Test
    public void testPrintNumber() throws IOException {
        this.printer.print(123);
        String result = this.printer.getOut().toString();
        assertEquals("123", result);
    }

    @Test
    public void testPrintByteArray() throws IOException {
        byte[] bytes = {65, 66, 67};
        this.printer.print(bytes);
        String result = this.printer.getOut().toString();
        assertEquals("ABC", result);
    }

    @Test
    public void testQuoting() throws IOException {
        CSVFormat format = DEFAULT.withQuote('"');
        CSVPrinter printer = createTestCSVPrinter(new ByteArrayOutputStream(), format);
        printer.print("hello\"world");
        String result = printer.getOut().toString();
        assertEquals("\"hello\"\"world\"", result);
    }

    @Test
    public void testEscaping() throws IOException {
        CSVFormat format = DEFAULT.withQuote('"').withEscape('\\');
        CSVPrinter printer = createTestCSVPrinter(new ByteArrayOutputStream(), format);
        printer.print("hello\"world");
        String result = printer.getOut().toString();
        assertEquals("hello\\\"world\\]", result);
    }

    @Test
    public void testPrintRecord() throws IOException {
        Object[] record = new Object[] {"Hello", 1, "World"};
        this.printer.printRecord(record);
        String result = this.printer.getOut().toString();

        CSVParser parser = CSVParser.parse(new StringReader(result));
        CSVRecord record1 = parser.getHeaderMap().size() > 0 ? parser.getRecord() : parser.getRecords().get(0);
        assertEquals(record[0], record1.get(0));
        assertEquals(record[1], record1.get(1));
        assertEquals(record[2], record1.get(2));
    }

    @Test
    public void testPrintEmptyRecord() throws IOException {
        this.printer.printRecord();
        String result = this.printer.getOut().toString();
        assertEquals(Constants.EMPTY_LINE_SEPARATOR, result);
    }

    @Test
    public void testPrintComment() throws IOException {
        this.printer.printComment("This is a comment");
        String result = this.printer.getOut().toString();
        assertEquals(Constants.COMMENT + "\n" + Constants.COMMENT + "This is a comment" + Constants.COMMENT + "\n", result);
    }

    @Test
    public void testPrintln() throws IOException {
        this.printer.println();
        String result = this.printer.getOut().toString();
        assertEquals(Constants.LINE_SEPARATOR, result);
    }

    @Test
    public void testPrintEmptyLine() throws IOException {
        this.printer.println();
        this.printer.println();
        String result = this.printer.getOut().toString();
        assertEquals(Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR, result);
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        CSVFormat format = DEFAULT;
        if (format.isQuotingEnabled()) {
            format = format.withQuote(null);
        }
        if (format.isCommentingEnabled()) {
            format = format.withCommentingEnabled(false);
        }
        ResultSet resultSet = TestUtil.createTestDataSet();

        this.printer = createTestCSVPrinter(this.output, format);
        this.printer.printRecords(resultSet);
        String result = this.printer.getOut().toString();

        CSVParser parser = CSVParser.parse(new StringReader(result));
        assertEquals(resultSet.getMetaData().getColumnCount(), parser.getHeaderMap().size());
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            CSVRecord record = parser.getRecord();
            assertEquals(resultSet.getMetaData().getColumnName(i), record.getHeaderMap().get(resultSet.getMetaData().getColumnName(i)));
            assertEquals(resultSet.getString(i), record.get(i));
        }
    }

    @Test
    public void testPrintRecordsWithQuotes() throws IOException, SQLException {
        CSVFormat format = DEFAULT.withQuote('"');
        ResultSet resultSet = TestUtil.createTestDataSet();

        this.printer = createTestCSVPrinter(this.output, format);
        this.printer.printRecords(resultSet);

        String result = this.printer.getOut().toString();
        CSVParser parser = CSVParser.parse(new StringReader(result));
        for (CSVRecord record : parser) {
            for (int i = 0; i < record.size(); i++) {
                assertEquals("'" + record.get(i) + "'", record.get(i).toString());
            }
        }
    }

    @Test
    public void testPrintRecordsWithComments() throws IOException, SQLException {
        CSVFormat format = DEFAULT.withCommentingEnabled();
        ResultSet resultSet = TestUtil.createTestDataSet();

        this.printer = createTestCSVPrinter(this.output, format);
        this.printer.printRecords(resultSet);

        String result = this.printer.getOut().toString();
        CSVParser parser = CSVParser.parse(new StringReader(result));
        for (CSVRecord record : parser) {
            for (int i = 0; i < record.size(); i++) {
                assertEquals(record.get(i).toString(), record.get(i).toString());
            }
            assertTrue(record.size() > 0);
            assertEquals(Constants.COMMENT, record.get(0).toString());
        }
    }

    @Test
    public void testPrintRecordsWithQuotesAndComments() throws IOException, SQLException {
        CSVFormat format = DEFAULT.withQuote('"').withCommentingEnabled();
        ResultSet resultSet = TestUtil.createTestDataSet();

        this.printer = createTestCSVPrinter(this.output, format);
        this.printer.printRecords(resultSet);

        String result = this.printer.getOut().toString();
        CSVParser parser = CSVParser.parse(new StringReader(result));
        for (CSVRecord record : parser) {
            for (int i = 0; i < record.size(); i++) {
                if (i == 0) {
                    assertEquals(Constants.COMMENT, record.get(i).toString());
                } else {
                    assertEquals("'" + record.get(i) + "'", record.get(i).toString());
                }
            }
        }
    }

    @Test
    public void testFlush() throws IOException {
        this.printer.close();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CSVFormat format = DEFAULT;
        CSVPrinter printer = createTestCSVPrinter(output, format);

        String expected = "Hello World";
        printer.print(expected);
        printer.flush();

        String actual = output.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testClose() throws IOException {
        this.printer.close();
        assertFalse(this.printer.isOpen());
    }

    @Test
    public void testPrintAndEscapeQuotationMarks() throws IOException {
        CSVFormat format = DEFAULT.withQuote('"').withEscape('\\');
        CSVPrinter printer = createTestCSVPrinter(new ByteArrayOutputStream(), format);
        String value = "hello\"world\"";
        printer.print(value);
        assertEquals("hello\\\"world\\\"", printer.getOut().toString());
    }

    @Test
    public void testPrintAndQuoteEscapedChars() throws IOException {
        CSVFormat format = DEFAULT.withQuote('"').withEscape('\\');
        CSVPrinter printer = createTestCSVPrinter(new ByteArrayOutputStream(), format);
        String value = "hello\\world";
        printer.print(value);
        assertEquals("hello\\\\world", printer.getOut().toString());
    }

    @Test
    public void testPrintAndQuoteDelimiters() throws IOException {
        CSVFormat format = DEFAULT.withQuote('"').withDelimiter(',').withEscape('\\');
        CSVPrinter printer = createTestCSVPrinter(new ByteArrayOutputStream(), format);
        String value = "hello,\"world\"";
        printer.print(value);
        assertEquals("hello,\"world\"", printer.getOut().toString());
    }

    @Test
    public void testPrintAndQuoteEscapeSequences() throws IOException {
        CSVFormat format = DEFAULT.withQuote('"').withEscape('\\');
        CSVPrinter printer = createTestCSVPrinter(new ByteArrayOutputStream(), format);
        String value = "hello\\\\world";
        printer.print(value);
        assertEquals("hello\\\\\\\\world", printer.getOut().toString());
    }

    private CSVPrinter createTestCSVPrinter(Appendable out, CSVFormat format) throws IOException {
        return new CSVPrinter(out, format);
    }

    @Parameters(method = "testPrintRecordsWithQuotesAndComments")
    public static Object[] testPrintRecordsWithQuotesAndCommentsData() {
        return new Object[] { TestUtil.createTestDataSet() };
    }

    private static ResultSet TestUtil_createTestDataSet() throws SQLException {
        return TestUtil.createTestDataSet("test_printer.csv");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{"Invalid"}, ',', '\'').withCommentingEnabled();
        new CSVPrinter(new ByteArrayOutputStream(), format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidQuote() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{}, ',', (char) 0xFFFF);
        new CSVPrinter(new ByteArrayOutputStream(), format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidDelimiter() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{}, (char) 0xFFFF, '\'');
        new CSVPrinter(new ByteArrayOutputStream(), format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidEscapeChar() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{}, ',', '\'', (char) 0xFFFF);
        new CSVPrinter(new ByteArrayOutputStream(), format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullQuote() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{}, ',', null);
        new CSVPrinter(new ByteArrayOutputStream(), format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullDelimiter() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{}, null, '\'');
        new CSVPrinter(new ByteArrayOutputStream(), format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullEscapeChar() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{}, ',', '\'', null);
        new CSVPrinter(new ByteArrayOutputStream(), format);
    }

}