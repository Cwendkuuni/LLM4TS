import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.constants.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVFormat csvFormat;
    private Appendable out;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        out = mock(Appendable.class);
        csvPrinter = new CSVPrinter(out, csvFormat);
    }

    @After
    public void tearDown() {
        csvPrinter = null;
        out = null;
        csvFormat = null;
    }

    @Test
    public void testClose() throws IOException {
        Closeable closeable = mock(Closeable.class);
        csvPrinter = new CSVPrinter(closeable, csvFormat);
        doNothing().when(closeable).close();
        csvPrinter.close();
        verify(closeable).close();
    }
    
    @Test
    public void testFlush() throws IOException {
        Flushable flushable = mock(Flushable.class);
        csvPrinter = new CSVPrinter(flushable, csvFormat);
        doNothing().when(flushable).flush();
        csvPrinter.flush();
        verify(flushable).flush();
    }
    
    @Test
    public void testPrint_NullValue() throws IOException {
        csvPrinter.print(null);
        
        String expected = Constants.EMPTY;
        verify(out).append(expected);
    }
    
    @Test
    public void testPrint_NonNullValue() throws IOException {
        String value = "test";
        csvPrinter.print(value);
        
        verify(out).append(value);
    }
    
    @Test
    public void testPrintComment_Disabled() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withCommentStart(Character.MIN_VALUE);
        csvPrinter = new CSVPrinter(out, csvFormat);
        csvPrinter.printComment("This is a comment");
        
        verify(out, never()).append(anyString());
    }
    
    @Test
    public void testPrintComment_Enabled() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withCommentStart('#');
        csvPrinter = new CSVPrinter(out, csvFormat);
        
        String comment = "This is a comment";
        csvPrinter.printComment(comment);
        
        verify(out).append('#');
        verify(out, times(comment.length())).append(anyChar());
        verify(out, times(comment.length() + 1)).append(anyString());
    }
    
    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        
        String recordSeparator = csvFormat.getRecordSeparator();
        verify(out).append(recordSeparator);
    }
    
    @Test
    public void testPrintRecord_Iterable() throws IOException {
        Iterable<Object> values = mock(Iterable.class);
        csvPrinter.printRecord(values);
        
        verify(csvPrinter).print(any());
        verify(csvPrinter).println();
    }
    
    @Test
    public void testPrintRecord_Array() throws IOException {
        Object[] values = new Object[3];
        csvPrinter.printRecord(values);
        
        verify(csvPrinter, times(values.length)).print(any());
        verify(csvPrinter).println();
    }
    
    @Test
    public void testPrintRecords_Iterable() throws IOException {
        Iterable<Iterable<Object>> values = mock(Iterable.class);
        csvPrinter.printRecords(values);
        
        verify(csvPrinter, times(3)).print(any());
        verify(csvPrinter, times(3)).println();
    }
    
    @Test
    public void testPrintRecords_Array() throws IOException {
        Object[][] values = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        csvPrinter.printRecords(values);
        
        verify(csvPrinter, times(values.length * values[0].length)).print(any());
        verify(csvPrinter, times(values.length)).println();
    }
    
    @Test
    public void testPrintRecords_ResultSet() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        csvPrinter.printRecords(resultSet);
        
        verify(resultSet).next();
        verify(resultSet).getString(anyInt());
        verify(csvPrinter, times(3)).print(any());
        verify(csvPrinter, times(3)).println();
    }
}