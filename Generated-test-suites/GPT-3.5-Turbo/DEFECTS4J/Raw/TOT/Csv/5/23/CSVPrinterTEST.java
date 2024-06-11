import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;


public class CSVPrinterTest {

    private CSVPrinter printer;
    
    @Before
    public void setUp() {
        printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
    }
    
    @Test
    public void testPrint_NullValue() throws Exception {
        printer.print(null);
        assertEquals("\"\"", printer.getOut().toString());
    }
    
    @Test
    public void testPrint_NonNullValue() throws Exception {
        printer.print("TestValue");
        assertEquals("TestValue", printer.getOut().toString());
    }
    
    @Test
    public void testPrint_EmptyValue() throws Exception {
        printer.print("");
        assertEquals("", printer.getOut().toString());
    }
    
    @Test
    public void testPrintAndEscape() throws Exception {
        printer.print("\"TestValue\"");
        assertEquals("\"\"\"TestValue\"\"\"", printer.getOut().toString());
    }
    
    @Test
    public void testPrintAndQuote() throws Exception {
        printer.printAndQuote("TestValue");
        assertEquals("\"TestValue\"", printer.getOut().toString());
    }
    
    @Test
    public void testPrintComment() throws Exception {
        printer.printComment("TestComment");
        assertEquals("# TestComment\n", printer.getOut().toString());
    }
    
    @Test
    public void testPrintRecord_Iterable() throws Exception {
        Iterable<String> values = Arrays.asList("Value1", "Value2", "Value3");
        printer.printRecord(values);
        assertEquals("Value1,Value2,Value3\n", printer.getOut().toString());
    }
    
    @Test
    public void testPrintRecord_ObjectArray() throws Exception {
        Object[] values = {"Value1", "Value2", "Value3"};
        printer.printRecord(values);
        assertEquals("Value1,Value2,Value3\n", printer.getOut().toString());
    }
    
    @Test
    public void testPrintRecords_Iterable() throws Exception {
        Iterable<String[]> records = Arrays.asList(
            new String[]{"Value1", "Value2", "Value3"},
            new String[]{"Value4", "Value5", "Value6"}
        );
        printer.printRecords(records);
        assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6\n", printer.getOut().toString());
    }
    
    @Test
    public void testPrintRecords_ObjectArray() throws Exception {
        Object[] records = new Object[]{
            new Object[]{"Value1", "Value2", "Value3"},
            new Object[]{"Value4", "Value5", "Value6"}
        };
        printer.printRecords(records);
        assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6\n", printer.getOut().toString());
    }
    
    @Test
    public void testPrintRecords_ResultSet() throws Exception {
        ResultSet resultSet = createMockResultSet();
        printer.printRecords(resultSet);
        assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6\n", printer.getOut().toString());
    }
    
    private ResultSet createMockResultSet() {
        // create and return a mock ResultSet for testing
    }
}