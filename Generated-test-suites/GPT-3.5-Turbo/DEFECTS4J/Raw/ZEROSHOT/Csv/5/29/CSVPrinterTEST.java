import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.apache.commons.csv.CSVFormat;

public class CSVPrinterTest {
    
    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;
    
    @Before
    public void setUp() {
        out = mock(Appendable.class);
        format = CSVFormat.DEFAULT;
        printer = new CSVPrinter(out, format);
    }
    
    @Test
    public void testClose() throws Exception {
        printer.close();
        verify(out).close();
    }
    
    @Test
    public void testFlush() throws Exception {
        printer.flush();
        verify(out).flush();
    }
    
    @Test
    public void testPrint() throws Exception {
        printer.print("value");
        verify(out).append("value");
    }
    
    @Test
    public void testPrint_NullValue() throws Exception {
        printer.print(null);
        verify(out).append("");
    }
    
    @Test
    public void testPrintAndEscape() throws Exception {
        printer.print("value,value");
        verify(out).append("value,value");
    }
    
    @Test
    public void testPrintAndEscape_NullValue() throws Exception {
        printer.print(null);
        verify(out).append("");
    }
    
    @Test
    public void testPrintAndQuote() throws Exception {
        printer.print("\"value\"");
        verify(out).append("\"value\"");
    }
    
    @Test
    public void testPrintAndQuote_NullValue() throws Exception {
        printer.print(null);
        verify(out).append("");
    }
    
    @Test
    public void testPrintComment() throws Exception {
        String comment = "This is a comment";
        printer.printComment(comment);
        verify(out).append("# This is a comment\n");
    }
    
    @Test
    public void testPrintComment_WithNewLine() throws Exception {
        String comment = "This is a comment with\na new line.";
        printer.printComment(comment);
        verify(out).append("# This is a comment with\n# a new line.\n");
    }
    
    @Test
    public void testPrintRecord() throws Exception {
        Object[] values = {"value1", "value2"};
        printer.printRecord(values);
        verify(out).append("value1").append(",").append("value2").append("\n");
    }
    
    @Test
    public void testPrintRecords() throws Exception {
        Object[] values1 = {"value1", "value2"};
        Object[] values2 = {"value3", "value4"};
        Object[] values3 = {"value5", "value6"};
        Object[] values4 = {"value7", "value8"};
        
        Object[] records = {values1, values2, values3, values4};
        
        printer.printRecords(records);
        
        verify(out).append("value1").append(",").append("value2").append("\n");
        verify(out).append("value3").append(",").append("value4").append("\n");
        verify(out).append("value5").append(",").append("value6").append("\n");
        verify(out).append("value7").append(",").append("value8").append("\n");
    }
    
    @Test
    public void testPrintRecords_WithIterable() throws Exception {
        Iterable<String> values1 = new ArrayList<>();
        values1.add("value1");
        values1.add("value2");
        
        Iterable<String> values2 = new ArrayList<>();
        values2.add("value3");
        values2.add("value4");
        
        Iterable<String> values3 = new ArrayList<>();
        values3.add("value5");
        values3.add("value6");
        
        Iterable<String> values4 = new ArrayList<>();
        values4.add("value7");
        values4.add("value8");
        
        Iterable<Iterable<String>> records = new ArrayList<>();
        records.add(values1);
        records.add(values2);
        records.add(values3);
        records.add(values4);
        
        printer.printRecords(records);
        
        verify(out).append("value1").append(",").append("value2").append("\n");
        verify(out).append("value3").append(",").append("value4").append("\n");
        verify(out).append("value5").append(",").append("value6").append("\n");
        verify(out).append("value7").append(",").append("value8").append("\n");
    }
    
    @Test
    public void testPrintRecords_WithResultSet() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("value1", "value3");
        when(resultSet.getString(2)).thenReturn("value2", "value4");
        
        printer.printRecords(resultSet);
        
        verify(out).append("value1").append(",").append("value2").append("\n");
        verify(out).append("value3").append(",").append("value4").append("\n");
    }
    
    @Test
    public void testGetOut() throws Exception {
        Appendable actualOut = printer.getOut();
        assertEquals(out, actualOut);
    }
}