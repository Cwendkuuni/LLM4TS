import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private Appendable out;

    @Before
    public void setup() {
        out = mock(Appendable.class);
        csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("test");
        verify(out).append("test");
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        verify(out).append("# This is a comment");
    }

    @Test
    public void testPrintRecordArray() throws IOException {
        csvPrinter.printRecord(new Object[] { "value1", "value2" });
        verify(out).append("value1,value2");
    }

    @Test
    public void testPrintRecordIterable() throws IOException {
        csvPrinter.printRecord(Arrays.asList("value1", "value2"));
        verify(out).append("value1,value2");
    }

    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(new Object[] { new Object[] { "value1", "value2" }, new Object[] { "value3", "value4" } });
        verify(out).append("value1,value2");
        verify(out).append("value3,value4");
    }
    
    @Test
    public void testPrintRecordsWithResultSet() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getMetaData().getColumnCount()).thenReturn(2);
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");
        csvPrinter.printRecords(resultSet);
        
        verify(resultSet, times(2)).getString(1);
        verify(resultSet, times(2)).getString(2);
        verify(out, times(2)).append("value1,value2");
    }

    // Add more test cases for edge cases and exceptions as needed

}