import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream output;
    private CSVFormat format;

    @Before
    public void setUp() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/testDB", "username", "password")) {
            String query = "SELECT * FROM testTable";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withHeader("Header1", "Header2", "Header3"));
            output = new ByteArrayOutputStream();
            printer = new CSVPrinter(new StringWriter(output), format);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Hello World");
        String expected = "Hello World" + LF;
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintQuoted() throws IOException {
        printer.print("\"Hello World\"");
        String expected = "\"" + "Hello World" + "\"" + LF;
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        String expected = format.getNullString() + LF;
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord("Test", 1);
        String expected = "Test,1" + LF;
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordQuoted() throws IOException {
        printer.printRecord("\"Test\"", 1);
        String expected = "\"" + "Test" + "\",1" + LF;
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordNull() throws IOException {
        printer.printRecord(null, 1);
        String expected = CSVFormat.DEFAULT.NULL_STRING + CSVFormat.DEFAULT.DELIMITER + "1" + LF;
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        printer.printRecords(format.makeParser(output.toString()).getRecords());
        String expected = "Header1,Header2,Header3" + LF
                + "Test1,1,Test3" + LF
                + "Test2,2,Test4" + LF;
        Assert.assertEquals(expected, output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOut() {
        new CSVPrinter(null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFormat() {
        new CSVPrinter(output, null);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        String expected = format.getCommentStart() + SP + "This is a comment" + LF
                + format.getCommentStart() + SP + "This is a comment" + LF;
        Assert.assertEquals(expected, output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintCommentNullFormat() {
        printer.format = null;
        printer.printComment("Comment");
    }

    @Test
    public void testPrintDelimiter() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';'));
        printer = new CSVPrinter(output, format);
        printer.print("Test");
        String expected = "Test" + format.getDelimiter();
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintDelimiterInValue() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';'));
        printer = new CSVPrinter(output, format);
        printer.print("Test;Test");
        String expected = "Test;" + format.getDelimiter() + "Test";
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintEscapedDelimiter() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';').withEscape('\u001F'));
        printer = new CSVPrinter(output, format);
        printer.print("Test;Test");
        String expected = "Test\\u001FTest";
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintEscapedChar() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withEscape('\u001F'));
        printer = new CSVPrinter(output, format);
        printer.print("Test," + CR + "Test");
        String expected = "Test,\u000dTest";
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintEscapedQuote() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withQuoteChar('"').withEscape('\u001F'));
        printer = new CSVPrinter(output, format);
        printer.print("Test,\"Test\"" + CR);
        String expected = "Test,\"Test\\u001F\"";
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordEscapedChar() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withEscape('\u001F'));
        printer = new CSVPrinter(output, format);
        printer.printRecord("Test," + CR + "Test");
        String expected = "Test,\"Test\u001F\"";
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordQuoteEscaped() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withQuoteChar('"').withEscape('\u001F'));
        printer = new CSVPrinter(output, format);
        printer.printRecord("Test,\"Test\\u001F\"");
        String expected = "Test,\"Test\u001F\"";
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordNullAndQuoted() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withQuotePolicy(Quote.ALWAYS));
        printer = new CSVPrinter(output, format);
        printer.printRecord(null, "Test");
        String expected = format.getNullString() + format.getDelimiter() + "\"" + "Test" + "\"" + LF;
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordNullAndQuotedEscaped() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withQuotePolicy(Quote.ALWAYS).withEscape('\u001F'));
        printer = new CSVPrinter(output, format);
        printer.printRecord(null, "Test\\u001F");
        String expected = format.getNullString() + format.getDelimiter() + "\"" + "Test\\u001F" + "\"" + LF;
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordQuotedInQuoted() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withQuotePolicy(Quote.MINIMAL));
        printer = new CSVPrinter(output, format);
        printer.printRecord(new String[]{"Test,\"Test\""});
        String expected = "Test,\"Test\"";
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordQuotedInQuotedEscaped() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withQuotePolicy(Quote.MINIMAL).withEscape('\u001F'));
        printer = new CSVPrinter(output, format);
        printer.printRecord(new String[]{"Test,\"Test\\u001F\""});
        String expected = "Test,\"Test\\u001F\"";
        Assert.assertEquals(expected, output.toString());
    }
}