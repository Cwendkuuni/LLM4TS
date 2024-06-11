import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {
    
    private StringWriter writer;
    private CSVPrinter csvPrinter;
    
    @Before
    public void setUp() throws IOException {
        writer = new StringWriter();
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }
    
    @After
    public void tearDown() throws IOException {
        writer.close();
    }
    
    @Test
    public void testPrint_NullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals(Constants.EMPTY, writer.toString());
    }
    
    @Test
    public void testPrint_NonNullValue() throws IOException {
        csvPrinter.print("testValue");
        csvPrinter.flush();
        assertEquals("testValue", writer.toString());
    }
    
    @Test
    public void testPrintComment_CommentEnabled() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("### This is a comment\n", writer.toString());
    }
    
    @Test
    public void testPrintComment_CommentDisabled() throws IOException {
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withCommentEnabled(false));
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals(Constants.EMPTY, writer.toString());
    }
    
    @Test
    public void testPrintRecord_SingleValue() throws IOException {
        csvPrinter.printRecord("value1");
        csvPrinter.flush();
        assertEquals("value1\n", writer.toString());
    }
    
    @Test
    public void testPrintRecord_MultipleValues() throws IOException {
        csvPrinter.printRecord("value1", "value2", "value3");
        csvPrinter.flush();
        assertEquals("value1,value2,value3\n", writer.toString());
    }
    
    @Test
    public void testPrintRecords_ArrayOfArrays() throws IOException {
        Object[] record1 = {"value1", "value2"};
        Object[] record2 = {"value3", "value4"};
        csvPrinter.printRecords(new Object[] {record1, record2});
        csvPrinter.flush();
        assertEquals("value1,value2\nvalue3,value4\n", writer.toString());
    }
    
    @Test
    public void testPrintRecords_CollectionOfArrays() throws IOException {
        Object[] record1 = {"value1", "value2"};
        Object[] record2 = {"value3", "value4"};
        csvPrinter.printRecords(Arrays.asList(record1, record2));
        csvPrinter.flush();
        assertEquals("value1,value2\nvalue3,value4\n", writer.toString());
    }
    
    @Test
    public void testPrintRecords_CollectionOfIterables() throws IOException {
        Iterable<String> record1 = Arrays.asList("value1", "value2");
        Iterable<String> record2 = Arrays.asList("value3", "value4");
        csvPrinter.printRecords(Arrays.asList(record1, record2));
        csvPrinter.flush();
        assertEquals("value1,value2\nvalue3,value4\n", writer.toString());
    }
    
    @Test
    public void testPrintRecords_JDBCResultSet() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData().getColumnCount()).thenReturn(2);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");
        
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        assertEquals("value1,value2\nvalue1,value2\n", writer.toString());
    }
}