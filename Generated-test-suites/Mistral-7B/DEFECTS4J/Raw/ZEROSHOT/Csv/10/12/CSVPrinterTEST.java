import org.apache.commons.csv.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVFormat format;
    private ByteArrayOutputStream outContent;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        format = new CSVFormat(CSVFormat.DEFAULT, CSVRecord.DEFAULT_SEPARATOR, CSVRecord.DEFAULT_LINE_END);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        printer = new CSVPrinter(outContent, format);
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Test");
        verifyPrinted("Test");
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        verifyPrinted("\"\"");
    }

    @Test
    public void testPrintRecords() throws IOException {
        printer.printRecords(Arrays.asList("Test1", "Test2"));
        verifyPrinted("Test1,Test2\n");
    }

    @Test
    public void testPrintRecordsNull() throws IOException {
        printer.printRecords(Collections.singletonList(null));
        verifyPrinted("\"\",\"\"\n");
    }

    @Test
    public void testPrintRecordsObjectArray() throws IOException {
        printer.printRecords(new Object[]{"Test1", "Test2"});
        verifyPrinted("Test1,Test2\n");
    }

    @Test
    public void testPrintRecordsObjectArrayWithNull() throws IOException {
        printer.printRecords(new Object[]{"Test1", null, "Test3"});
        verifyPrinted("Test1,\"\",Test3\n");
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("Test Comment");
        verifyPrinted("// Test Comment\n");
    }

    @Test
    public void testPrintln() throws IOException {
        printer.println();
        verifyPrinted("\n");
    }

    @Test
    public void testPrintRecord() throws IOException {
        List<Object> record = Arrays.asList("Test1", "Test2", 123);
        printer.printRecord(record);
        verifyPrinted("Test1,Test2,123\n");
    }

    @Test
    public void testPrintRecordObjectArray() throws IOException {
        List<Object> record = Arrays.asList("Test1", "Test2", new Object[]{"Test3", "Test4"});
        printer.printRecord(record.toArray(new Object[0]));
        verifyPrinted("Test1,Test2,\"Test3,Test4\"\n");
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        when(mock(Iterator.class).hasNext()).thenReturn(true).thenReturn(false);
        when(mock(Iterator.class).next()).thenReturn(1).thenReturn(2).thenReturn(null);
        Iterator<Integer> iter = mock(Iterator.class);
        List<Integer> list = Arrays.asList(1, 2, null);
        printer.printRecords(list);
        verifyPrinted("1,2,\n");
        printer.printRecords(iter);
        verifyPrinted("1,\n2,\n");
    }

    @Test
    public void testPrintRecordsResultSet() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("Test1");
        when(resultSet.getString(2)).thenReturn("Test2");
        printer.printRecords(resultSet);
        verifyPrinted("Test1,Test2\n");
    }

    private void verifyPrinted(final String expected) throws IOException {
        assertEquals(expected, outContent.toString());
        outContent.reset();
    }
}