import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;
    
    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }
    
    // Test for print(Object value) method
    
    @Test
    public void testPrint_WithNullValue() throws IOException {
        csvPrinter.print(null);
        assertEquals("", stringWriter.toString());
    }
    
    @Test
    public void testPrint_WithNonNullValue() throws IOException {
        csvPrinter.print(10);
        assertEquals("10", stringWriter.toString());
    }
    
    // Test for printComment(String comment) method
    
    @Test
    public void testPrintComment_WithCommentEnabled() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", stringWriter.toString());
    }
    
    @Test
    public void testPrintComment_WithCommentDisabled() throws IOException {
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withCommentMarker(null));
        csvPrinter.printComment("This is a comment");
        assertEquals("", stringWriter.toString());
    }
    
    // Test for println() method
    
    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        assertEquals("\n", stringWriter.toString());
    }
    
    // Test for printRecord(Iterable<?> values) method
    
    @Test
    public void testPrintRecord_WithIterable() throws IOException {
        csvPrinter.printRecord(Arrays.asList("Value1", "Value2"));
        assertEquals("Value1,Value2\n", stringWriter.toString());
    }
    
    @Test
    public void testPrintRecord_WithEmptyIterable() throws IOException {
        csvPrinter.printRecord(Collections.emptyList());
        assertEquals("\n", stringWriter.toString());
    }
    
    // Test for printRecord(Object... values) method
    
    @Test
    public void testPrintRecord_WithObjects() throws IOException {
        csvPrinter.printRecord("Value1", "Value2", null);
        assertEquals("Value1,Value2,null\n", stringWriter.toString());
    }
    
    @Test
    public void testPrintRecord_WithEmptyObjects() throws IOException {
        csvPrinter.printRecord();
        assertEquals("\n", stringWriter.toString());
    }
    
    // Test for printRecords(Iterable<?> values) method
    
    @Test
    public void testPrintRecords_WithNestedArrays() throws IOException {
        csvPrinter.printRecords(Arrays.asList(new String[] {"Value1", "Value2"}, new String[] {"Value3", "Value4"}));
        assertEquals("Value1,Value2\nValue3,Value4\n", stringWriter.toString());
    }
    
    // Test for printRecords(Object[] values) method
    
    @Test
    public void testPrintRecords_WithObjectArray() throws IOException {
        csvPrinter.printRecords(new Object[] {"Value1", "Value2", null});
        assertEquals("Value1,Value2,null\n", stringWriter.toString());
    }
    
    // Test for printRecords(ResultSet resultSet) method
    
    @Test
    public void testPrintRecords_WithResultSet() throws IOException {
        ResultSet resultSet = createMockResultSet();
        csvPrinter.printRecords(resultSet);
        assertEquals("Value1,Value2\n", stringWriter.toString());
    }
    
    private ResultSet createMockResultSet() {
        ResultSet resultSet = EasyMock.createMock(ResultSet.class);
        try {
            EasyMock.expect(resultSet.getMetaData()).andReturn(createMockMetaData());
            EasyMock.expect(resultSet.next()).andReturn(true).andReturn(false);
            EasyMock.expect(resultSet.getString(1)).andReturn("Value1");
            EasyMock.expect(resultSet.getString(2)).andReturn("Value2");
            EasyMock.replay(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    private ResultSetMetaData createMockMetaData() {
        ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
        try {
            EasyMock.expect(metaData.getColumnCount()).andReturn(2);
            EasyMock.replay(metaData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metaData;
    }

}