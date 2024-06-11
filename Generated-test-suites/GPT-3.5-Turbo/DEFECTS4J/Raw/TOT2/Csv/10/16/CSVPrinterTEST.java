import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT);
        
        csvPrinter.print("value1");
        csvPrinter.print(123);
        csvPrinter.print(null);
        
        assertEquals("value1,123,null", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT);

        csvPrinter.printComment("This is a comment");

        assertEquals("# This is a comment", out.toString().trim());
    }

    @Test
    public void testPrintRecord() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT);
        
        csvPrinter.printRecord("value1", 123, null);
        
        assertEquals("value1,123,null", out.toString().trim());
    }

    @Test
    public void testPrintRecords() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT);
        
        Object[] values = {"value1", 123, null};
        csvPrinter.printRecords(values);
        
        assertEquals("value1,123,null", out.toString().trim());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException, SQLException {
        StringWriter out = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT);
        
        // Mock ResultSet
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(2);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getString(1)).thenReturn("value1");
        Mockito.when(resultSet.getString(2)).thenReturn("value2");
        
        csvPrinter.printRecords(resultSet);
        
        assertEquals("value1,value2", out.toString().trim());
    }

    // Add more test cases for other methods if needed

}