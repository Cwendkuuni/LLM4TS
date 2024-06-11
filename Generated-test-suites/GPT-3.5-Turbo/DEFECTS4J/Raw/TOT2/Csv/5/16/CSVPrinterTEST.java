import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private StringWriter stringWriter;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
        stringWriter.close();
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("1");
        assertEquals("1", stringWriter.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        csvPrinter.print(null);
        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        csvPrinter.print("escape,csv");
        assertEquals("escape,csv", stringWriter.toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        csvPrinter.print("\"quote\",csv");
        assertEquals("\"quote\",csv", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("value1", "value2");
        assertEquals("value1,value2\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(new String[] { "record1", "record2" });
        assertEquals("record1,record2\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("value1", "value2");

        csvPrinter.printRecords(resultSet);

        assertEquals("value1,value2\n", stringWriter.toString());
    }
}