import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.DEFAULT_WITH_HEADER;
import static org.apache.commons.csv.CSVRecord.builder;
import static org.apache.commons.csv.CSVRecord.header;
import static org.junit.Assert.*;

/**
 * Test cases for the CSVPrinter class.
 */
public class CSVPrinterTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;
    private StringWriter stringWriter;

    @Before
    public void setUp() throws IOException {
        CSVFormat format = DEFAULT_WITH_HEADER;
        outputStream = new ByteArrayOutputStream();
        stringWriter = new StringWriter(outputStream);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        printer = new CSVPrinter(writer, format);
    }

    @After
    public void tearDown() throws IOException {
        if (printer != null) {
            printer.close();
        }
    }

    /**
     * Test that the printer can print a single value.
     *
     * @throws IOException if there's an error writing to the output stream.
     */
    @Test
    public void shouldPrintSingleValue() throws IOException {
        printer.print("Hello");
        printer.close();

        String result = outputStream.toString();
        assertEquals("Hello", result.split(LINE_SEPARATOR)[0]);
    }

    /**
     * Test that the printer can print multiple values on a single line.
     *
     * @throws IOException if there's an error writing to the output stream.
     */
    @Test
    public void shouldPrintMultipleValuesOnASingleLine() throws IOException {
        List<Object> values = Arrays.asList("Hello", "World");
        printer.printRecord(values);
        printer.close();

        String result = outputStream.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        assertEquals(2, lines.length);
        String[] expected = {"Hello,World".split(DEFAULT.getDelimiter().toString())};
        assertArrayEquals(expected, lines[0].split(DEFAULT.getDelimiter().toString()));
    }

    /**
     * Test that the printer can print multiple lines.
     *
     * @throws IOException if there's an error writing to the output stream.
     */
    @Test
    public void shouldPrintMultipleLines() throws IOException {
        List<Object> values = Arrays.asList("Line1", "Line2", "Line3");
        printer.printRecords(values);
        printer.close();

        String result = outputStream.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        assertEquals(3, lines.length);
        for (String line : lines) {
            String[] fields = line.split(DEFAULT.getDelimiter().toString());
            assertEquals(values.size(), fields.length);
            for (int i = 0; i < values.size(); i++) {
                assertEquals(values.get(i).toString(), fields[i]);
            }
        }
    }

    /**
     * Test that the printer can print a comment.
     *
     * @throws IOException if there's an error writing to the output stream.
     * @throws SQLException if there's an error retrieving the data from the result set.
     */
    @Test
    public void shouldPrintComment() throws IOException, SQLException {
        ResultSet resultSet = ResultSetTestData.getResultSetWithComment();
        printer.printRecords(resultSet);
        printer.close();

        String result = outputStream.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        // The first line should be the comment.
        String comment = lines[0].substring(1);
        assertEquals("# This is a comment", comment);

        // The following lines should be the data records.
        for (int i = 1; i < lines.length; i++) {
            String[] fields = lines[i].split(DEFAULT.getDelimiter().toString());
            assertEquals(2, fields.length);
            assertEquals("Record1", fields[0]);
            assertEquals("Record2", fields[1]);
        }
    }

    /**
     * Test that the printer correctly handles empty values.
     *
     * @throws IOException if there's an error writing to the output stream.
     */
    @Test
    public void shouldPrintEmptyValues() throws IOException {
        List<Object> values = new ArrayList<>();
        values.add(null);
        values.add("");
        printer.printRecord(values);
        printer.close();

        String result = outputStream.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        assertEquals(2, lines.length);
        String[] expected = {"", ""};
        assertArrayEquals(expected, lines[0].split(DEFAULT.getDelimiter().toString()));
    }

    /**
     * Test that the printer correctly handles escaping.
     *
     * @throws IOException if there's an error writing to the output stream.
     */
    @Test
    public void shouldPrintEscapedValues() throws IOException {
        List<Object> values = new ArrayList<>();
        values.add("Hello, World!\"");
        printer.setEscapeMode(CSVPrinter.ESCAPE_ALL);
        printer.printRecord(values);
        printer.close();

        String result = outputStream.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        assertEquals(1, lines.length);
        String[] expected = {"Hello, World!\",\"", ""};
        assertArrayEquals(expected, lines[0].split(DEFAULT.getDelimiter().toString()));
    }

    /**
     * Test that the printer correctly handles quoting.
     *
     * @throws IOException if there's an error writing to the output stream.
     */
    @Test
    public void shouldPrintQuotedValues() throws IOException {
        List<Object> values = new ArrayList<>();
        values.add("Hello, World\"");
        printer.setQuoteMode(CSVPrinter.QUOTE_MINIMAL);
        printer.printRecord(values);
        printer.close();

        String result = outputStream.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        assertEquals(1, lines.length);
        String[] expected = {"\"Hello, World\"", ""};
        assertArrayEquals(expected, lines[0].split(DEFAULT.getDelimiter().toString()));
    }

    /**
     * Test that the printer correctly handles empty lines.
     *
     * @throws IOException if there's an error writing to the output stream.
     */
    @Test
    public void shouldPrintEmptyLines() throws IOException {
        printer.println();
        printer.println();
        printer.close();

        String result = outputStream.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        assertEquals(2, lines.length);
        assertEquals(LINE_SEPARATOR, lines[0]);
        assertEquals(LINE_SEPARATOR, lines[1]);
    }

    /**
     * Test that the printer correctly handles newlines within values.
     *
     * @throws IOException if there's an error writing to the output stream.
     */
    @Test
    public void shouldPrintValuesWithNewlines() throws IOException {
        List<Object> values = Arrays.asList("Hello\nWorld");
        printer.printRecord(values);
        printer.close();

        String result = outputStream.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        assertEquals(1, lines.length);
        String[] expected = {"Hello\nWorld"};
        assertArrayEquals(expected, lines[0].split(DEFAULT.getDelimiter().toString()));
    }

    /**
     * Test that the printer correctly handles comments with newlines.
     *
     * @throws IOException if there's an error writing to the output stream.
     * @throws SQLException if there's an error retrieving the data from the result set.
     */
    @Test
    public void shouldPrintCommentsWithNewlines() throws IOException, SQLException {
        ResultSet resultSet = ResultSetTestData.getResultSetWithNewlineComment();
        printer.printRecords(resultSet);
        printer.close();

        String result = outputStream.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        // The first line should be the comment.
        String comment = lines[0].substring(1);
        assertEquals("# This is a\ncomment", comment);

        // The following lines should be the data records.
        for (int i = 1; i < lines.length; i++) {
            String[] fields = lines[i].split(DEFAULT.getDelimiter().toString());
            assertEquals(2, fields.length);
            assertEquals("Record1", fields[0]);
            assertEquals("Record2", fields[1]);
        }
    }

    /**
     * Test that the printer correctly handles empty headers.
     *
     * @throws IOException if there's an error writing to the output stream.
     */
    @Test
    public void shouldPrintEmptyHeaders() throws IOException {
        List<String> headers = Collections.emptyList();
        printer = new CSVPrinter(new OutputStreamWriter(stringWriter), DEFAULT_WITH_HEADER.withHeader(headers));
        printer.printRecords(Collections.emptyList());
        printer.close();

        String result = stringWriter.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        assertEquals(1, lines.length);
        // Empty header line
        String[] expected = {};
        assertArrayEquals(expected, lines[0].split(DEFAULT.getDelimiter().toString()));
    }

    /**
     * Test that the printer correctly handles headers.
     *
     * @throws IOException if there's an error writing to the output stream.
     */
    @Test
    public void shouldPrintHeaders() throws IOException {
        List<String> headers = Arrays.asList("Header1", "Header2");
        printer = new CSVPrinter(new OutputStreamWriter(stringWriter), DEFAULT_WITH_HEADER.withHeader(headers));
        List<Object> values = Arrays.asList("Value1", "Value2");
        printer.printRecord(values);
        printer.close();

        String result = stringWriter.toString();
        String[] lines = result.split(LINE_SEPARATOR);
        assertEquals(2, lines.length);

        // Header line
        String[] headerLine = lines[0].split(DEFAULT.getDelimiter().toString());
        assertEquals(2, headerLine.length);
        for (int i = 0; i < headers.size(); i++) {
            assertEquals(headers.get(i), headerLine[i]);
        }

        // Data line
        String[] dataLine = lines[1].split(DEFAULT.getDelimiter().toString());
        assertEquals(2, dataLine.length);
        assertEquals("Value1", dataLine[0]);
        assertEquals("Value2", dataLine[1]);
    }
}