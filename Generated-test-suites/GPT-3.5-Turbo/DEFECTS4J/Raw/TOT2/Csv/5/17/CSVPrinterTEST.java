import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class CSVPrinterTest {
    
    private StringWriter output;
    private CSVPrinter csvPrinter;
    
    @Before
    public void setUp() {
        output = new StringWriter();
        csvPrinter = new CSVPrinter(output, CSVFormat.DEFAULT);
    }
    
    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.flush();
        assertEquals("Hello\n", output.toString());
    }
    
    @Test
    public void testPrintNull() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals("\n", output.toString());
    }
    
    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("# This is a comment\n", output.toString());
    }
    
    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("John", "Doe", 30);
        csvPrinter.flush();
        assertEquals("John,Doe,30\n", output.toString());
    }
    
    // Edge case: Empty values in printRecord
    @Test
    public void testPrintRecordEmptyValues() throws IOException {
        csvPrinter.printRecord("", "", "");
        csvPrinter.flush();
        assertEquals(",,\n", output.toString());
    }
    
    // Exception handling: IOException when flushing
    @Test(expected = IOException.class)
    public void testFlushIOException() throws IOException {
        output.close();
        csvPrinter.flush();
    }
    
    // Typical use case: Print multiple records
    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(new Object[] {"Alice", "Smith", 25}, 
                                new Object[] {"Bob", "Johnson", 35});
        csvPrinter.flush();
        assertEquals("Alice,Smith,25\nBob,Johnson,35\n", output.toString());
    }
    
    @Test
    public void testGetOut() {
        assertEquals(output, csvPrinter.getOut());
    }
}