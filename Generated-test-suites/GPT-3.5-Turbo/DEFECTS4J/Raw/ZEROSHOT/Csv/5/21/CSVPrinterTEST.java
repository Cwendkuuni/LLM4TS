import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {
    private StringWriter writer;
    private CSVFormat format;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        writer = new StringWriter();
        format = CSVFormat.DEFAULT;
        csvPrinter = new CSVPrinter(writer, format);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
        writer.close();
    }

    @Test
    public void testPrintObject() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.flush();
        Assert.assertEquals("Hello", writer.toString());
    }

    @Test
    public void testPrintObject_NullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testPrintObject_WithQuotes() throws IOException {
        format = CSVFormat.DEFAULT.withQuoteChar('\'');
        csvPrinter = new CSVPrinter(writer, format);

        csvPrinter.print("Hello");
        csvPrinter.flush();

        Assert.assertEquals("'Hello'", writer.toString());
    }

    @Test
    public void testPrintObject_WithEscapes() throws IOException {
        format = CSVFormat.DEFAULT.withEscape('\\');
        csvPrinter = new CSVPrinter(writer, format);

        csvPrinter.print("Hello, World");
        csvPrinter.flush();

        Assert.assertEquals("Hello\\, World", writer.toString());
    }

    @Test
    public void testPrintObject_WithQuotesAndEscapes() throws IOException {
        format = CSVFormat.DEFAULT.withQuoteChar('"').withEscape('\\');
        csvPrinter = new CSVPrinter(writer, format);

        csvPrinter.print("Hello, \"World\"");
        csvPrinter.flush();

        Assert.assertEquals("\"Hello, \\\"World\\\"\"", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();

        Assert.assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        List<String> values = Arrays.asList("Hello", "World");

        csvPrinter.printRecord(values);
        csvPrinter.flush();

        Assert.assertEquals("Hello,World\n", writer.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        List<List<String>> records = Arrays.asList(
            Arrays.asList("Hello", "World"),
            Arrays.asList("Java", "Programming")
        );

        csvPrinter.printRecords(records);
        csvPrinter.flush();

        Assert.assertEquals("Hello,World\nJava,Programming\n", writer.toString());
    }

    @Test
    public void testPrintRecords_NullValue() throws IOException {
        List<List<String>> records = Arrays.asList(
            Arrays.asList("Hello", null),
            Arrays.asList(null, "World")
        );

        csvPrinter.printRecords(records);
        csvPrinter.flush();

        Assert.assertEquals("Hello,\n,World\n", writer.toString());
    }

    @Test
    public void testPrintRecords_ResultSet() throws SQLException, IOException {
        ResultSet resultSetMock = // implement mocked ResultSet class for testing
        csvPrinter.printRecords(resultSetMock);
        csvPrinter.flush();

        // assert the output based on the mocked ResultSet data
        Assert.assertEquals("ResultSet output here", writer.toString());
    }
}