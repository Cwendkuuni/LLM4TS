import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVPrinter;
import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {
    private CSVPrinter printer;
    private StringWriter writer;
    
    @Before
    public void setUp() throws IOException {
        writer = new StringWriter();
        printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }
    
    @After
    public void tearDown() throws IOException {
        writer.close();
    }
    
    @Test
    public void testPrint_NullValue() throws IOException {
        String expected = "";
        printer.print(null);
        printer.flush();
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testPrint_NonNullValue() throws IOException {
        String expected = "Hello";
        printer.print("Hello");
        printer.flush();
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testPrint_WithDelimiter() throws IOException {
        String expected = "Hello,World";
        printer.print("Hello");
        printer.print("World");
        printer.flush();
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testPrintComment_NoCommentingEnabled() throws IOException {
        String expected = "";
        printer.printComment("This is a comment");
        printer.flush();
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testPrintComment_WithCommentingEnabled() throws IOException {
        String expected = "# This is a comment" + System.lineSeparator();
        printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withCommentingEnabled());
        printer.printComment("This is a comment");
        printer.flush();
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testPrintRecord_Iterable() throws IOException {
        String expected = "Hello,World" + System.lineSeparator();
        printer.printRecord(new String[]{"Hello", "World"});
        printer.flush();
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testPrintRecord_Array() throws IOException {
        String expected = "Hello,World" + System.lineSeparator();
        printer.printRecord("Hello", "World");
        printer.flush();
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testPrintRecords_Iterable() throws IOException {
        String expected = "Hello,World" + System.lineSeparator() +
                          "Foo,Bar" + System.lineSeparator();
        printer.printRecords(new String[][]{{"Hello", "World"}, {"Foo", "Bar"}});
        printer.flush();
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testPrintRecords_Array() throws IOException {
        String expected = "Hello,World" + System.lineSeparator() +
                          "Foo,Bar" + System.lineSeparator();
        printer.printRecords(new String[]{"Hello", "World"}, new String[]{"Foo", "Bar"});
        printer.flush();
        assertEquals(expected, writer.toString());
    }
    
}