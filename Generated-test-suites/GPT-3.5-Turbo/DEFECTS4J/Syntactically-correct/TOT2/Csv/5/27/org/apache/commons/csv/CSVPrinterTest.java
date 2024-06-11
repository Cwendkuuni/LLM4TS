import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVPrinterTest {
    
    private StringWriter writer;
    private CSVFormat format;
    private CSVPrinter printer;
    
    @Before
    public void setUp() {
        writer = new StringWriter();
        format = CSVFormat.DEFAULT;
        printer = new CSVPrinter(writer, format);
    }
    
    @After
    public void tearDown() throws IOException {
        printer.close();
    }
    
    @Test
    public void testPrint() throws IOException {
        printer.print("test");
        assertEquals("test", writer.toString());
    }
    
    @Test
    public void testPrint_Null() throws IOException {
        printer.print(null);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        assertEquals("# This is a comment" + System.lineSeparator(), writer.toString());
    }
    
    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord("value1", "value2");
        assertEquals("value1,value2" + System.lineSeparator(), writer.toString());
    }
    
    @Test
    public void testPrintRecords() throws IOException {
        Object[] record1 = {"value1", "value2"};
        Object[] record2 = {"value3", "value4"};
        
        printer.printRecords(new Object[]{record1, record2});
        assertEquals("value1,value2" + System.lineSeparator() + "value3,value4" + System.lineSeparator(), writer.toString());
    }
    
    @Test
    public void testPrintRecords_JDBC() throws IOException {
        // Mock ResultSet for testing
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData().getColumnCount()).thenReturn(2);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("val1");
        when(resultSet.getString(2)).thenReturn("val2");
        
        printer.printRecords(resultSet);
        assertEquals("val1,val2" + System.lineSeparator(), writer.toString());
    }
}