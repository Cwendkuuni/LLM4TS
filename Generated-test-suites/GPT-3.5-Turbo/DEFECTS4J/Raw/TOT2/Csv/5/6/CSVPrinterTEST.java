import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.print("Alice");
        csvPrinter.println();
        
        assertEquals("Alice\n", writer.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.printRecord("Alice", "Bob", "Carol");
        
        assertEquals("Alice,Bob,Carol\n", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.printComment("This is a comment");
        
        assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        String[] record1 = {"Alice", "Bob", "Carol"};
        String[] record2 = {"Dave", "Eve", "Frank"};
        
        csvPrinter.printRecords(record1, record2);
        
        assertEquals("Alice,Bob,Carol\nDave,Eve,Frank\n", writer.toString());
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException {
        // Mock ResultSet for testing
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getMetaData()).thenReturn(Mockito.mock(ResultSetMetaData.class));
        Mockito.when(resultSet.getString(1)).thenReturn("Alice");
        Mockito.when(resultSet.getString(2)).thenReturn("Bob");
        
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.printRecords(resultSet);
        
        assertEquals("Alice,Bob\n", writer.toString());
    }
}