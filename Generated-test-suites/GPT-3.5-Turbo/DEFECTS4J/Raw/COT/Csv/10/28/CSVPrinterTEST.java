import static org.junit.Assert.*;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

public class CSVPrinterTest {
    
    @Test
    public void testPrint() throws IOException {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.print("Hello");
        printer.print("World");
        printer.println();
        assertEquals("Hello,World\n", out.toString());
    }
    
    @Test
    public void testPrintComment() throws IOException {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printComment("This is a comment");
        printer.println();
        assertEquals("# This is a comment\n", out.toString());
    }
    
    @Test
    public void testPrintRecord() throws IOException {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecord("1", "2", "3");
        assertEquals("1,2,3\n", out.toString());
    }
    
    @Test
    public void testPrintRecords() throws IOException {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecords(new Object[] {"1", "2", "3"}, new Object[] {"4", "5", "6"});
        assertEquals("1,2,3\n4,5,6\n", out.toString());
    }
    
    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecords(Arrays.asList("1", "2", "3"), Arrays.asList("4", "5", "6"));
        assertEquals("1,2,3\n4,5,6\n", out.toString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException, SQLException {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        
        ResultSet resultSet = createMockResultSet();
        printer.printRecords(resultSet);
        
        assertEquals("1,John Doe\n2,Jane Smith\n", out.toString());
    }
    
    private ResultSet createMockResultSet() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        
        when(resultSet.getInt(1)).thenReturn(1).thenReturn(2);
        when(resultSet.getString(2)).thenReturn("John Doe").thenReturn("Jane Smith");
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSetMetaData.getColumnName(1)).thenReturn("id");
        when(resultSetMetaData.getColumnName(2)).thenReturn("name");
        
        return resultSet;
    }
    
    @Test
    public void testClose() throws IOException {
        Appendable out = new StringBuilder();
        Closeable closeable = mock(Closeable.class);
        
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.close();
        
        verify(closeable, times(1)).close();
    }
    
    @Test
    public void testFlush() throws IOException {
        Flushable flushable = mock(Flushable.class);
        
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.flush();
        
        verify(flushable, times(1)).flush();
    }
}