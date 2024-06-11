import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CSVPrinterTest {

    private StringWriter writer;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        writer = new StringWriter();
        printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint_StringObject_ValidOutput() throws IOException {
        printer.print("John");
        printer.print("Doe");
        printer.println();
        assertEquals("John,Doe\r\n", writer.toString());
    }

    @Test
    public void testPrint_NullObject_ValidOutput() throws IOException {
        printer.print(null);
        printer.println();
        assertEquals(",\r\n", writer.toString());
    }

    @Test
    public void testPrintAndQuote_QuotingEnabled_ValidOutput() throws IOException {
        printer.printAndQuote(null, "Hello World", 0, 11);
        printer.println();
        assertEquals("\"Hello World\"\r\n", writer.toString());
    }

    @Test
    public void testPrintAndEscape_EscapingEnabled_ValidOutput() throws IOException {
        printer.printAndEscape("Hello,World!", 0, 12);
        printer.println();
        assertEquals("Hello,,World!\r\n", writer.toString());
    }

    @Test
    public void testPrintAndEscape_EscapingEnabledMultipleDelims_ValidOutput() throws IOException {
        printer.printAndEscape("Hello,World!Foo,Bar", 0, 21);
        printer.println();
        assertEquals("Hello,,World!Foo,,Bar\r\n", writer.toString());
    }
    
    @Test
    public void testPrintComment_CommentingEnabled_ValidOutput() throws IOException {
        printer.printComment("This is a comment");
        assertEquals("# This is a comment\r\n", writer.toString());
    }

    @Test
    public void testPrintComment_CommentingDisabled_NoOutput() throws IOException {
        printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withCommentingDisabled());
        printer.printComment("This is a comment");
        assertEquals("", writer.toString());
    }

    @Test
    public void testPrintRecord_Iterable_ValidOutput() throws IOException {
        List<String> record = new ArrayList<>(Arrays.asList("John", "Doe"));
        printer.printRecord(record);
        assertEquals("John,Doe\r\n", writer.toString());
    }

    @Test
    public void testPrintRecord_Array_ValidOutput() throws IOException {
        String[] record = new String[]{"John", "Doe"};
        printer.printRecord(record);
        assertEquals("John,Doe\r\n", writer.toString());
    }

    @Test
    public void testPrintRecords_Iterable_ValidOutput() throws IOException {
        List<List<String>> records = new ArrayList<>();
        records.add(Arrays.asList("John", "Doe"));
        records.add(Arrays.asList("Jane", "Smith"));
        printer.printRecords(records);
        assertEquals("John,Doe\r\nJane,Smith\r\n", writer.toString());
    }

    @Test
    public void testPrintRecords_Array_ValidOutput() throws IOException {
        String[][] records = new String[][]{{"John", "Doe"}, {"Jane", "Smith"}};
        printer.printRecords(records);
        assertEquals("John,Doe\r\nJane,Smith\r\n", writer.toString());
    }

    @Test
    public void testPrintRecords_ResultSet_ValidOutput() throws IOException {
        ResultSet resultSet = createMockResultSet();
        printer.printRecords(resultSet);
        assertEquals("John,Doe\r\nJane,Smith\r\n", writer.toString());
    }

    @Test
    public void testClose_WriterShouldBeClosed() throws IOException {
        printer.close();
        assertTrue(writer.toString().isEmpty());
        assertTrue(writer.getBuffer().length() == 0);
        assertTrue(writer.getBuffer().capacity() == 0);
    }

    private ResultSet createMockResultSet() {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        try {
            when(resultSet.next()).thenReturn(true, true, false);
            when(resultSet.getString(1)).thenReturn("John", "Jane");
            when(resultSet.getString(2)).thenReturn("Doe", "Smith");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}