import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {

    private StringWriter writer;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        writer = new StringWriter();
        printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        printer.flush();
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        printer.print("");
        printer.flush();
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testPrintString() throws IOException {
        printer.print("Hello");
        printer.flush();
        Assert.assertEquals("Hello", writer.toString());
    }

    @Test
    public void testPrintInteger() throws IOException {
        printer.print(123);
        printer.flush();
        Assert.assertEquals("123", writer.toString());
    }

    @Test
    public void testPrintDouble() throws IOException {
        printer.print(3.14);
        printer.flush();
        Assert.assertEquals("3.14", writer.toString());
    }

    @Test
    public void testPrintBoolean() throws IOException {
        printer.print(true);
        printer.flush();
        Assert.assertEquals("true", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.flush();
        Assert.assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord("John", "Doe", 25);
        printer.flush();
        Assert.assertEquals("John,Doe,25\n", writer.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        printer.printRecords(new Object[]{"John", "Doe", 25}, new Object[]{"Jane", "Smith", 30});
        printer.flush();
        Assert.assertEquals("John,Doe,25\nJane,Smith,30\n", writer.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        printer.printRecords(Arrays.asList(new Object[]{"John", "Doe", 25}, new Object[]{"Jane", "Smith", 30}));
        printer.flush();
        Assert.assertEquals("John,Doe,25\nJane,Smith,30\n", writer.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException {
        // TODO: Create a mock ResultSet for testing
    }

    @Test
    public void testGetOut() {
        Assert.assertEquals(writer, printer.getOut());
    }
}