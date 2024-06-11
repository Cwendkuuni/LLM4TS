import org.apache.commons.csv.*;
import org.junit.*;

public class CSVPrinterTest {

    private CSVFormat format;
    private StringBuilder out;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        format = CSVFormat.DEFAULT;
        out = new StringBuilder();
        printer = new CSVPrinter(out, format);
    }

    @After
    public void tearDown() {
        out = null;
        printer = null;
    }

    @Test
    public void testPrint_NullValue() throws Exception {
        printer.print(null);
        String result = out.toString();
        Assert.assertEquals("", result);
    }

    @Test
    public void testPrint_QuotingEnabled() throws Exception {
        printer.print("Hello, World!");
        String result = out.toString();
        Assert.assertEquals("\"Hello, World!\"", result);
    }

    @Test
    public void testPrint_QuotingDisabled() throws Exception {
        format = CSVFormat.DEFAULT.withQuotePolicy(Quote.NONE);
        printer = new CSVPrinter(out, format);
        printer.print("Hello, World!");
        String result = out.toString();
        Assert.assertEquals("Hello, World!", result);
    }

    @Test
    public void testPrint_EscapingEnabled() throws Exception {
        printer.print("Hello, \"World!\"");
        String result = out.toString();
        Assert.assertEquals("Hello, \"\"World!\"\"", result);
    }

    @Test
    public void testPrint_EscapingDisabled() throws Exception {
        format = CSVFormat.DEFAULT.withEscape(null);
        printer = new CSVPrinter(out, format);
        printer.print("Hello, \"World!\"");
        String result = out.toString();
        Assert.assertEquals("Hello, \"World!\"", result);
    }

    @Test
    public void testPrint_CommentingEnabled() throws Exception {
        format = CSVFormat.DEFAULT.withCommentingEnabled(true);
        printer = new CSVPrinter(out, format);
        printer.printComment("This is a comment");
        printer.print("Hello, World!");
        String result = out.toString();
        Assert.assertEquals("# This is a comment\r\nHello, World!\r\n", result);
    }

    @Test
    public void testPrint_CommentingDisabled() throws Exception {
        printer.printComment("This is a comment");
        printer.print("Hello, World!");
        String result = out.toString();
        Assert.assertEquals("\r\nHello, World!\r\n", result);
    }

    @Test
    public void testPrintln() throws Exception {
        printer.println();
        String result = out.toString();
        Assert.assertEquals("\r\n", result);
    }

    @Test
    public void testPrintRecord() throws Exception {
        printer.printRecord("Alice", 25, "Female");
        String result = out.toString();
        Assert.assertEquals("Alice,25,Female\r\n", result);
    }

    @Test
    public void testPrintRecords_Array() throws Exception {
        Object[] records = {"Alice,25,Female", "Bob,30,Male"};
        printer.printRecords(records);
        String result = out.toString();
        Assert.assertEquals("Alice,25,Female\r\nBob,30,Male\r\n", result);
    }

    @Test
    public void testPrintRecords_Iterable() throws Exception {
        Iterable<?> records = new CSVRecord("Alice,25,Female");
        printer.printRecords(records);
        String result = out.toString();
        Assert.assertEquals("Alice,25,Female\r\n", result);
    }

    @Test(expected = SQLException.class)
    public void testPrintRecords_ResultSet() throws Exception {
        ResultSet resultSet = null;
        printer.printRecords(resultSet);
    }

    @Test
    public void testGetOut() throws Exception {
        Appendable result = printer.getOut();
        Assert.assertEquals(out, result);
    }
}