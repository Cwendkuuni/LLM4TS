import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVPrinterTest {

    @Mock
    StringWriter mockOut;
    
    @Test
    public void testConstructor() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(mockOut, format);
        assertNotNull(csvPrinter);
    }
    
    @Test
    public void testClose() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(mockOut, format);
        csvPrinter.close();
        Mockito.verify(mockOut).close();
    }
    
    @Test
    public void testFlush() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(mockOut, format);
        csvPrinter.flush();
        Mockito.verify(mockOut).flush();
    }
    
    @Test
    public void testPrint() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(mockOut, format);
        csvPrinter.print("value1");
        Mockito.verify(mockOut).append("value1");
    }
    
    @Test
    public void testPrintComment() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart('#');
        CSVPrinter csvPrinter = new CSVPrinter(mockOut, format);
        csvPrinter.printComment("This is a comment");
        Mockito.verify(mockOut).append("# This is a comment");
    }
    
    @Test
    public void testPrintRecord() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(mockOut, format);
        csvPrinter.printRecord("value1", "value2", "value3");
        Mockito.verify(mockOut).append("value1,");
        Mockito.verify(mockOut).append("value2,");
        Mockito.verify(mockOut).append("value3,");
        Mockito.verify(mockOut).append("\n");
    }
    
    @Test
    public void testPrintRecords() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(mockOut, format);
        Object[] values = {"value1", "value2", "value3"};
        csvPrinter.printRecords(values);
        Mockito.verify(mockOut, Mockito.times(3)).append("valueX,");
        Mockito.verify(mockOut).append("\n");
    }
    
    @Test
    public void testPrintRecordsWithJDBC() throws IOException {
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockResultSet.next()).thenReturn(true, true, false);
        Mockito.when(mockResultSet.getString(1)).thenReturn("value1", "value2", "value3");
        
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(mockOut, format);
        csvPrinter.printRecords(mockResultSet);
        
        Mockito.verify(mockOut, Mockito.times(3)).append("valueX,");
        Mockito.verify(mockOut, Mockito.times(3)).append("\n");
    }
}