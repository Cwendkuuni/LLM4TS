import static org.junit.Assert.*;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private StringBuilder output;
    private CSVFormat format;
    
    @Before
    public void setUp() throws IOException {
        output = new StringBuilder();
        format = CSVFormat.DEFAULT;
        printer = new CSVPrinter(output, format);
    }
    
    @After
    public void tearDown() throws IOException {
        printer.close();
    }
    
    @Test
    public void testPrint_Object() throws IOException {
        printer.print("hello");
        printer.flush();
        assertEquals("hello", output.toString());
    }
    
    @Test
    public void testPrint_Object_Null() throws IOException {
        printer.print(null);
        printer.flush();
        assertEquals("", output.toString());
    }
    
    @Test
    public void testPrint_Comment() throws IOException {
        printer.printComment("This is a comment");
        printer.flush();
        assertEquals("\n# This is a comment\n", output.toString());
    }
    
    @Test
    public void testPrintLn() throws IOException {
        printer.println();
        printer.flush();
        assertEquals("\n", output.toString());
    }
    
    @Test
    public void testPrintRecord_Iterable() throws IOException {
        printer.printRecord(new Object[] {"1", "2", "3"});
        printer.flush();
        assertEquals("1,2,3\n", output.toString());
    }
    
    @Test
    public void testPrintRecord_ObjectArray() throws IOException {
        printer.printRecord(new Object[] {"1", "2", "3"});
        printer.flush();
        assertEquals("1,2,3\n", output.toString());
    }
    
    @Test
    public void testPrintRecords_Iterable() throws IOException {
        printer.printRecords(new Object[] {new Object[] {"1", "2", "3"}, new Object[] {"4", "5", "6"}});
        printer.flush();
        assertEquals("1,2,3\n4,5,6\n", output.toString());
    }
    
    @Test
    public void testPrintRecords_ObjectArray() throws IOException {
        printer.printRecords(new Object[] {new Object[] {"1", "2", "3"}, new Object[] {"4", "5", "6"}});
        printer.flush();
        assertEquals("1,2,3\n4,5,6\n", output.toString());
    }
    
    @Test
    public void testPrintRecords_ResultSet() throws IOException, SQLException {
        ResultSet resultSet = createTestResultSet();
        printer.printRecords(resultSet);
        printer.flush();
        assertEquals("1,2,3\n4,5,6\n", output.toString());
    }
    
    private ResultSet createTestResultSet() {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next())
               .thenReturn(true)
               .thenReturn(true)
               .thenReturn(false);
        Mockito.when(resultSet.getString(Mockito.anyInt()))
               .thenReturn("1")
               .thenReturn("2")
               .thenReturn("3")
               .thenReturn("4")
               .thenReturn("5")
               .thenReturn("6");
        return resultSet;
    }
}