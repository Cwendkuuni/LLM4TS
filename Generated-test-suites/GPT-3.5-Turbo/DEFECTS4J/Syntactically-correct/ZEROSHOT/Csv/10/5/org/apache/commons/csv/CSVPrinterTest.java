import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    @Test
    public void testPrint() {
        // Test null value
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.print(null);
            assertEquals("", printer.getOut().toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Test non-null value
        printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.print("test");
            assertEquals("test", printer.getOut().toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testPrintComment() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printComment("This is a comment");
            assertEquals("# This is a comment\n", printer.getOut().toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testPrintln() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.println();
            assertEquals("\n", printer.getOut().toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testPrintRecordIterable() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printRecord("value1", "value2", "value3");
            assertEquals("value1,value2,value3\n", printer.getOut().toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testPrintRecordArray() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printRecord(new String[]{"value1", "value2", "value3"});
            assertEquals("value1,value2,value3\n", printer.getOut().toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testPrintRecordsIterable() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printRecords(new String[]{"value1", "value2", "value3"});
            assertEquals("value1,value2,value3\n", printer.getOut().toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testPrintRecordsArray() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printRecords(new String[][]{{"value1", "value2", "value3"}, {"value4", "value5", "value6"}});
            assertEquals("value1,value2,value3\nvalue4,value5,value6\n", printer.getOut().toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testPrintRecordsResultSet() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            // Mocking ResultSet
            ResultSet resultSet = mock(ResultSet.class);
            ResultSetMetaData metaData = mock(ResultSetMetaData.class);
            when(resultSet.getMetaData()).thenReturn(metaData);
            when(metaData.getColumnCount()).thenReturn(2);
            when(resultSet.next()).thenReturn(true, true, false);
            when(resultSet.getString(1)).thenReturn("value1", "value3");
            when(resultSet.getString(2)).thenReturn("value2", "value4");

            printer.printRecords(resultSet);
            assertEquals("value1,value2\nvalue3,value4\n", printer.getOut().toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}