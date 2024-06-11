import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws SQLException {
        String testData = "CREATE TABLE testCSV (" +
                "id INT PRIMARY KEY, name VARCHAR(50))";

        try (Statement stmt = TestDatabase.getConnection().createStatement()) {
            stmt.executeUpdate(testData);
        }

        outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL).withCommentMarker("#"));
        printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);
    }

    @Test
    public void testPrintSingleString() throws IOException {
        printer.print("Hello World");
        printer.flush();
        String result = outputStream.toString();
        assertEquals("\"Hello World\"", result.split(System.lineSeparator()[0])[0]);
    }

    @Test
    public void testPrintSingleNull() throws IOException {
        printer.print(null);
        printer.flush();
        String result = outputStream.toString();
        assertEquals("", result.split(System.lineSeparator()[0])[0]);
    }

    @Test
    public void testPrintSingleNumber() throws IOException {
        int number = 123;
        printer.print(number);
        printer.flush();
        String result = outputStream.toString();
        assertEquals("123", result.split(System.lineSeparator()[0])[0]);
    }

    @Test
    public void testPrintMultipleStringsWithDelimiter() throws IOException {
        printer.print("John");
        printer.print("Doe");
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        assertEquals("\"John\" \"Doe\"", result);
    }

    @Test
    public void testPrintMultipleStringsWithoutDelimiter() throws IOException {
        printer.print("JohnDoe");
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        assertEquals("\"JohnDoe\"", result);
    }

    @Test
    public void testPrintMultipleStringsWithQuote() throws IOException {
        printer.print("John \"Doe\"");
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        assertEquals("\"John\" \"Doe\"", result);
    }

    @Test
    public void testPrintMultipleStringsWithQuoteAndDelimiter() throws IOException {
        printer.print("John \"Doe\"");
        printer.print("Snow \"White\"");
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        assertEquals("\"John\" \"Doe\" \"Snow\" \"White\"", result);
    }

    @Test
    public void testPrintSingleComment() throws IOException {
        printer.printComment("# This is a comment");
        printer.flush();
        String result = outputStream.toString();
        assertEquals("# This is a comment" + System.lineSeparator(), result);
    }

    @Test
    public void testPrintMultipleComments() throws IOException {
        printer.printComment("# This is a comment 1");
        printer.printComment("# This is a comment 2");
        printer.println();
        printer.flush();
        String result = outputStream.toString();
        assertEquals("# This is a comment 1" + System.lineSeparator() + "# This is a comment 2" + System.lineSeparator(), result);
    }

    @Test
    public void testPrintMultipleRecords() throws IOException, SQLException {
        String query = "SELECT id, name FROM testCSV";

        try (ResultSet rs = TestDatabase.getConnection().createStatement().executeQuery(query)) {
            printer.printRecords(rs);
            printer.flush();
            String result = outputStream.toString();

            String[] lines = result.split(System.lineSeparator());
            assertEquals(2, lines.length);
            String firstLine = lines[0].split(System.lineSeparator()[0]);
            assertEquals(2, firstLine.length);
            assertEquals("id", firstLine[0]);
            assertEquals("name", firstLine[1]);

            String secondLine = lines[1].split(System.lineSeparator()[0])[0];
            assertEquals("1, \"John\"", secondLine);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws IOException {
        ByteArrayOutputStream outputStream = null;
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL).withCommentMarker("#"));
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);
    }
}