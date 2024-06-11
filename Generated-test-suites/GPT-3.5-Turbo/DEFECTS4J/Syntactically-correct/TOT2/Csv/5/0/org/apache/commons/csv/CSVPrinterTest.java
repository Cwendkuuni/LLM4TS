import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.StringWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVPrinterTest {
    
    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;
    
    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }
    
    @Test
    public void testPrint_NullValue() throws Exception {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals("", stringWriter.toString());
    }
    
    @Test
    public void testPrint_EmptyString() throws Exception {
        csvPrinter.print("");
        csvPrinter.flush();
        assertEquals("", stringWriter.toString());
    }
    
    @Test
    public void testPrint_NumberValue() throws Exception {
        csvPrinter.print(123);
        csvPrinter.flush();
        assertEquals("123", stringWriter.toString());
    }
    
    @Test
    public void testPrint_WithEscapeCharacter() throws Exception {
        csvPrinter.print("Hello, \"World\"");
        csvPrinter.flush();
        assertEquals("\"Hello, \"\"World\"\"\"", stringWriter.toString());
    }
    
    @Test
    public void testPrintComment() throws Exception {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("# This is a comment\n", stringWriter.toString());
    }
    
    @Test
    public void testPrintRecord() throws Exception {
        csvPrinter.printRecord("value1", "value2", "value3");
        csvPrinter.flush();
        assertEquals("value1,value2,value3\n", stringWriter.toString());
    }
    
    @Test
    public void testPrintRecords() throws Exception {
        String[] record1 = {"value1", "value2"};
        String[] record2 = {"value3", "value4"};
        csvPrinter.printRecords(record1);
        csvPrinter.printRecords(record2);
        csvPrinter.flush();
        assertEquals("value1,value2\nvalue3,value4\n", stringWriter.toString());
    }
    
    @Test
    public void testPrintRecordsWithNull() throws Exception {
        csvPrinter.printRecords(new String[] {"value1", null, "value3"});
        csvPrinter.flush();
        assertEquals("value1,,value3\n", stringWriter.toString());
    }
    
}