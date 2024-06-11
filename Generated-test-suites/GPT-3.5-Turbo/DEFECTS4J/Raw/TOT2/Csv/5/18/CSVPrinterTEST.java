import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;
import java.io.StringWriter;
import java.io.IOException;

public class CSVPrinterTest {
    
    private CSVPrinter csvPrinter;
    private StringWriter writer;
    
    @Before
    public void setUp() {
        writer = new StringWriter();
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }
    
    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("value1");
        csvPrinter.flush();
        String output = writer.toString();
        assertEquals("value1\r\n", output);
    }
    
    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        String output = writer.toString();
        assertEquals("# This is a comment\r\n", output);
    }
    
    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("value1", "value2");
        csvPrinter.flush();
        String output = writer.toString();
        assertEquals("value1,value2\r\n", output);
    }
    
    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(new String[]{"val1", "val2"}, new String[]{"val3", "val4"});
        csvPrinter.flush();
        String output = writer.toString();
        assertEquals("val1,val2\r\nval3,val4\r\n", output);
    }
    
    @Test
    public void testPrintRecordsFromResultSet() throws IOException, SQLException {
        // Prepare a mock ResultSet
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getMetaData().getColumnCount()).thenReturn(2);
        when(resultSet.getString(1)).thenReturn("val1");
        when(resultSet.getString(2)).thenReturn("val2");
        
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        String output = writer.toString();
        assertEquals("val1,val2\r\n", output);
    }
}