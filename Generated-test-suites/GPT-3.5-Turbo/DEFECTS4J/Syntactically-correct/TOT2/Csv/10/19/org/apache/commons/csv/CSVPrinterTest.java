import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.print("value1");
        assertEquals("value1", sb.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.printComment("Comment");
        assertEquals("# Comment\n", sb.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.printRecord("value1", "value2", "value3");
        assertEquals("value1,value2,value3\n", sb.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.printRecords(new Object[] {"value1", "value2"});
        assertEquals("value1,value2\n", sb.toString());
    }

    @Test
    public void testPrintRecordWithNullValue() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.printRecord("value1", null, "value3");
        assertEquals("value1,,value3\n", sb.toString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException {
        // Mock a ResultSet
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("value1", "value2");
        
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.printRecords(resultSet);
        
        assertEquals("value1,value2\n", sb.toString());
    }
    
    // Add more test cases for edge cases and exception handling scenarios
    
    @Test
    public void testFlush() throws IOException {
        Appendable appendable = mock(Appendable.class);
        CSVPrinter csvPrinter = new CSVPrinter(appendable, CSVFormat.DEFAULT);
        csvPrinter.flush();
        verify(appendable).flush();
    }

    @Test
    public void testClose() throws IOException {
        Closeable closeable = mock(Closeable.class);
        CSVPrinter csvPrinter = new CSVPrinter(closeable, CSVFormat.DEFAULT);
        csvPrinter.close();
        verify(closeable).close();
    }

}