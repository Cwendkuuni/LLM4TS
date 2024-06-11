import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.CR;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private Appendable output;
    private CSVPrinter printer;
    private final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';'));

    @Before
    public void setUp() throws IOException {
        output = new ByteArrayOutputStream();
        printer = new CSVPrinter(output, format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrintSingleValue() throws IOException {
        printer.print("Hello");
        printer.println();
        String result = output.toString();
        String expected = "Hello" + LF;
        assertEquals(expected, result);
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        printer.printQuoted("Hello, \"World\"");
        printer.println();
        String result = output.toString();
        String expected = "\"Hello,\" \"World\"\""; // '\"' is escaped by double-quoting
        assertEquals(expected, result);
    }

    @Test
    public void testPrintEmptyValue() throws IOException {
        printer.print("");
        printer.println();
        String result = output.toString();
        // CSVFormat.DEFAULT uses MINIMAL quoting policy, so no encapsulation is required for empty values
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.printNull();
        printer.println();
        String result = output.toString();
        String expected = format.getNullString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(Arrays.asList("A", "B", "C"));
        String result = output.toString();
        String expected = "A,B,C" + LF;
        assertEquals(expected, result);
    }

    @Test
    public void testPrintRecordWithQuotedValues() throws IOException {
        printer.printRecord(Arrays.asList("A, \"B,C\"", "D"));
        String result = output.toString();
        String expected = "A,\"B,C\" ,D" + LF;
        assertEquals(expected, result);
    }

    @Test
    public void testPrintRecordWithNullValue() throws IOException {
        printer.printRecord(Collections.singletonList(null));
        String result = output.toString();
        String expected = format.getNullString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintRecordWithMixedValues() throws IOException {
        printer.printRecord(Arrays.asList("A", null, "C", "D"));
        String result = output.toString();
        String expected = "A,\"\" ,\"C\",D" + LF;
        assertEquals(expected, result);
    }

    @Test
    public void testPrintRecordWithEscapedValues() throws IOException {
        printer.printRecord(Arrays.asList("A", "B,C", "D\\\"E"));
        String result = output.toString();
        String expected = "A,B,C ,D\\\"E" + LF;
        assertEquals(expected, result);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("this is a comment");
        String result = output.toString();
        String expected = format.getCommentStart() + SP + "this is a comment" + CR + LF;
        assertEquals(expected, result);
    }

    @Test
    public void testPrintRecordWithComment() throws IOException, SQLException {
        String sql = "SELECT name, age FROM Users WHERE id = 1";
        ResultSet resultSet = null; // initialize it outside the test method for better code coverage
        printer.printRecord(resultSet);
        printer.printComment("this is a comment");
        printer.println();
        String result = output.toString();
        String expected = format.getCommentStart() + SP + "this is a comment" + CR + LF;
        assertEquals(expected, result.substring(0, result.lastIndexOf(CR) + LF.length()));
        // test the result set printing logic separately
        resultSet = TestUtil.createResultSet(Arrays.asList("Alice", "25"));
        printer.printRecords(resultSet);
        expected = "Alice,25" + LF;
        String actual = output.toString().substring(expected.length());
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecordWithEmptyRecordSeparator() throws IOException {
        CSVFormat emptyRecordSeparatorFormat = new CSVFormat(CSVFormat.DEFAULT.withRecordSeparator(""));
        CSVPrinter emptyRecordSeparatorPrinter = new CSVPrinter(new StringWriter(), emptyRecordSeparatorFormat);
        emptyRecordSeparatorPrinter.printRecord(Arrays.asList("A", "B"));
        String result = emptyRecordSeparatorPrinter.getOut().toString();
        String expected = "A,B" + CR;
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterNullFormat() throws IOException {
        new CSVPrinter(output, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterNullOut() throws IOException {
        new CSVPrinter(null, format);
    }

    @Test(expected = IOException.class)
    public void testCSVPrinterNullHeader() throws IOException {
        CSVPrinter printer = new CSVPrinter(output, format);
        printer.println(); // print an empty line to check that the header is not printed by default
        printer.close();
        printer = new CSVPrinter(output, format);
        printer.printHeader("header1,header2");
        printer.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterInvalidQuoteChar() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteChar('['));
        new CSVPrinter(output, format);
    }

    @Test(expected = IOException.class)
    public void testCSVPrinterNoDelimiter() throws IOException {
        new CSVPrinter(output, format.newBuilder().build());
    }

}