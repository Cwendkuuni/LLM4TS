
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {
    
    @Test
    public void testCSVPrinter() throws IOException {
        // Arrange
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        
        // Act
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Assert
        assertNotNull(csvPrinter);
    }
    
    @Test
    public void testClose() throws IOException {
        // Arrange
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Act
        csvPrinter.close();
        
        // Assert: No exceptions should be thrown
    }
    
    @Test
    public void testFlush() throws IOException {
        // Arrange
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Act
        csvPrinter.flush();
        
        // Assert: No exceptions should be thrown
    }
    
    @Test
    public void testPrint() throws IOException {
        // Arrange
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Act
        csvPrinter.print("test");
        
        // Assert
        assertEquals("test", out.toString());
    }
    
    @Test
    public void testPrintComment() throws IOException {
        // Arrange
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Act
        csvPrinter.printComment("This is a comment");
        
        // Assert
        assertEquals("# This is a comment\n", out.toString());
    }
    
    @Test
    public void testPrintln() throws IOException {
        // Arrange
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Act
        csvPrinter.println();
        
        // Assert
        assertEquals(System.lineSeparator(), out.toString());
    }
    
    @Test
    public void testPrintRecord() throws IOException {
        // Arrange
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Act
        csvPrinter.printRecord("value1", "value2", "value3");
        
        // Assert
        assertEquals("value1,value2,value3" + System.lineSeparator(), out.toString());
    }
    
    @Test
    public void testPrintRecords() throws IOException {
        // Arrange
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        List<List<String>> records = new ArrayList<>();
        records.add(Arrays.asList("value1", "value2", "value3"));
        records.add(Arrays.asList("value4", "value5", "value6"));
        
        // Act
        csvPrinter.printRecords(records);
        
        // Assert
        assertEquals("value1,value2,value3" + System.lineSeparator()
                        + "value4,value5,value6" + System.lineSeparator(), out.toString());
    }
    
    @Test
    public void testPrintRecordsWithArray() throws IOException {
        // Arrange
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        Object[] records = new Object[] {"value1", "value2", "value3"};
        
        // Act
        csvPrinter.printRecords(records);
        
        // Assert
        assertEquals("value1,value2,value3" + System.lineSeparator(), out.toString());
    }
    
    @Test
    public void testPrintRecordsWithResultSet() throws IOException, SQLException {
        // Arrange
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");
        when(resultSet.next()).thenReturn(true, false);
        
        // Act
        csvPrinter.printRecords(resultSet);
        
        // Assert
        assertEquals("value1,value2" + System.lineSeparator(), out.toString());
    }
}
