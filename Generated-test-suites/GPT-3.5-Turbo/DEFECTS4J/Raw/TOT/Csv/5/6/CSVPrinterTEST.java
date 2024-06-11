import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;
    private CSVFormat csvFormat;
    
    @Before
    public void setUp() throws IOException {
        stringWriter = new StringWriter();
        csvFormat = CSVFormat.DEFAULT;
        csvPrinter = new CSVPrinter(stringWriter, csvFormat);
    }
    
    @Test
    public void testPrint_Object_Null() throws IOException {
        csvPrinter.print(null);
        assertEquals("", stringWriter.toString());
    }
    
    @Test
    public void testPrint_Object_NonNull() throws IOException {
        csvPrinter.print("Hello World");
        assertEquals("Hello World", stringWriter.toString());
    }
    
    @Test
    public void testPrint_Record_OneValue() throws IOException {
        csvPrinter.printRecord("Value1");
        assertEquals("Value1" + csvFormat.getRecordSeparator(), stringWriter.toString());
    }
    
    @Test
    public void testPrint_Record_MultipleValues() throws IOException {
        csvPrinter.printRecord("Value1", "Value2", "Value3");
        assertEquals("Value1,Value2,Value3" + csvFormat.getRecordSeparator(), stringWriter.toString());
    }
    
    @Test
    public void testPrint_Records_SingleRecord() throws IOException {
        csvPrinter.printRecords(new String[] {"Value1", "Value2"});
        assertEquals("Value1,Value2" + csvFormat.getRecordSeparator(), stringWriter.toString());
    }
    
    @Test
    public void testPrint_Records_MultipleRecords() throws IOException {
        csvPrinter.printRecords(new String[] {"Value1", "Value2"}, new String[] {"Value3", "Value4"});
        assertEquals("Value1,Value2" + csvFormat.getRecordSeparator() + "Value3,Value4" +
                csvFormat.getRecordSeparator(), stringWriter.toString());
    }
    
    @Test
    public void testPrint_Comment_Enabled() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals(csvFormat.getCommentStart() + " This is a comment" + csvFormat.getRecordSeparator(),
                stringWriter.toString());
    }
    
    @Test
    public void testPrint_Comment_Disabled() throws IOException {
        csvFormat = csvFormat.withCommentMarker(null);
        csvPrinter = new CSVPrinter(stringWriter, csvFormat);
        
        csvPrinter.printComment("This is a comment");
        assertEquals("", stringWriter.toString());
    }
    
    @Test
    public void testFlush() throws IOException {
        StringWriter mockStringWriter = mock(StringWriter.class);
        csvPrinter = new CSVPrinter(mockStringWriter, csvFormat);
        csvPrinter.flush();
        verify(mockStringWriter, times(1)).flush();
    }
    
    @Test
    public void testClose() throws IOException {
        StringWriter mockStringWriter = mock(StringWriter.class);
        csvPrinter = new CSVPrinter(mockStringWriter, csvFormat);
        csvPrinter.close();
        verify(mockStringWriter, times(1)).close();
    }
}