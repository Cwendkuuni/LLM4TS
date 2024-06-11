import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {
    
    private CSVPrinter csvPrinter;
    
    @Before
    public void setup() {
        csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
    }
    
    @Test
    public void testPrint_String() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.flush();
        assertEquals("Hello", csvPrinter.getOut().toString());
    }
    
    @Test
    public void testPrint_NullString() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals("", csvPrinter.getOut().toString());
    }
    
    @Test
    public void testPrint_Object() throws IOException {
        csvPrinter.print(new StringBuilder("Hello"));
        csvPrinter.flush();
        assertEquals("Hello", csvPrinter.getOut().toString());
    }
    
    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("# This is a comment\n", csvPrinter.getOut().toString());
    }
    
    @Test
    public void testPrintRecord_Iterable() throws IOException {
        List<String> values = new ArrayList<>();
        values.add("Value1");
        values.add("Value2");
        values.add("Value3");
        
        csvPrinter.printRecord(values);
        csvPrinter.flush();
        assertEquals("Value1,Value2,Value3\n", csvPrinter.getOut().toString());
    }
    
    @Test
    public void testPrintRecord_ObjectArray() throws IOException {
        Object[] values = new Object[] {"Value1", "Value2", "Value3"};
        
        csvPrinter.printRecord(values);
        csvPrinter.flush();
        assertEquals("Value1,Value2,Value3\n", csvPrinter.getOut().toString());
    }
    
    @Test
    public void testPrintRecords_Iterable() throws IOException {
        List<List<String>> records = new ArrayList<>();
        List<String> record1 = new ArrayList<>();
        record1.add("Value1");
        record1.add("Value2");
        record1.add("Value3");
        List<String> record2 = new ArrayList<>();
        record2.add("Value4");
        record2.add("Value5");
        record2.add("Value6");
        records.add(record1);
        records.add(record2);
        
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6\n", csvPrinter.getOut().toString());
    }
    
    @Test
    public void testPrintRecords_ObjectArray() throws IOException {
        Object[][] records = new Object[][] {{"Value1", "Value2", "Value3"}, {"Value4", "Value5", "Value6"}};
        
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6\n", csvPrinter.getOut().toString());
    }
    
    @Test
    public void testPrintRecords_ResultSet() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData().getColumnCount()).thenReturn(3);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("Value1");
        when(resultSet.getString(2)).thenReturn("Value2");
        when(resultSet.getString(3)).thenReturn("Value3");
        
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        assertEquals("Value1,Value2,Value3\n", csvPrinter.getOut().toString());
    }
}