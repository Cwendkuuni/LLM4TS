import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {

    private CSVFormat format;
    private StringWriter writer;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        format = CSVFormat.DEFAULT;
        writer = new StringWriter();
        printer = new CSVPrinter(writer, format);
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("test");
        printer.flush();
        Assert.assertEquals("\"test\"", writer.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.flush();
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        printer.printAndQuote("test");
        printer.flush();
        Assert.assertEquals("\"test\"", writer.toString());
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        printer.printAndEscape("test");
        printer.flush();
        Assert.assertEquals("test", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.flush();
        Assert.assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord("value1", "value2", "value3");
        printer.flush();
        Assert.assertEquals("value1,value2,value3\n", writer.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        Iterable<String> records = new String[]{"value1", "value2", "value3"};
        printer.printRecords(records);
        printer.flush();
        Assert.assertEquals("value1,value2,value3\n", writer.toString());
    }

    @Test
    public void testGetOut() {
        Assert.assertEquals(writer, printer.getOut());
    }

}