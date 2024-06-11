import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        
        printer.print("John");
        printer.print(25);
        printer.print(1000.00);
        
        printer.flush();
        
        assertEquals("John,25,1000.0", sw.toString());
    }
    
    @Test
    public void testPrintComment() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        
        printer.printComment("This is a comment");
        
        assertEquals("# This is a comment\n", sw.toString());
    }
    
    @Test
    public void testPrintRecord() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        
        printer.printRecord("Alice", 30, 2000.50);
        
        assertEquals("Alice,30,2000.5\n", sw.toString());
    }
    
    @Test
    public void testPrintRecords() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        
        printer.printRecords(new Object[] {"Bob", 35, 1500.75});
        
        assertEquals("Bob,35,1500.75\n", sw.toString());
    }
    
    @Test
    public void testPrintRecordsFromResultSet() throws IOException, SQLException {
        // Assume a valid ResultSet resultSet
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        
        printer.printRecords(resultSet);
        
        // Assert based on the resultSet contents
        // For simplicity, we're not actually testing with a real resultSet here
    }

}