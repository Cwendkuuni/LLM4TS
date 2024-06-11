import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Constants;

import java.io.StringWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    @Before
    public void setUp() throws IOException {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.flush();
        csvPrinter.close();
    }

    @Test
    public void testPrintStringInCSVFormat() throws IOException {
        csvPrinter.print("John,Doe,30");
        csvPrinter.print("Jane,Smith,25");
        assertEquals("John,Doe,30Jane,Smith,25" + Constants.DEFAULT_LINE_SEPARATOR, stringWriter.toString());
    }

    @Test
    public void testPrintNullValue() throws IOException {
        csvPrinter.print(null);
        assertEquals(Constants.EMPTY + Constants.DEFAULT_LINE_SEPARATOR, stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment" + Constants.DEFAULT_LINE_SEPARATOR, stringWriter.toString());
    }

    @Test
    public void testPrintRecordWithArray() throws IOException {
        csvPrinter.printRecord(new String[]{"Alice", "Johnson", "40"});
        assertEquals("Alice,Johnson,40" + Constants.DEFAULT_LINE_SEPARATOR, stringWriter.toString());
    }

    @Test
    public void testPrintRecordWithIterable() throws IOException {
        csvPrinter.printRecord(new String[]{"Tom", "Brown", "35"});
        assertEquals("Tom,Brown,35" + Constants.DEFAULT_LINE_SEPARATOR, stringWriter.toString());
    }
    
    @Test
    public void testPrintRecordsWithJDBCResultSet() throws IOException, SQLException {
        // Mock the ResultSet for testing purposes
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.next()).thenReturn(true, true, false);
        when(mockedResultSet.getMetaData().getColumnCount()).thenReturn(3);
        when(mockedResultSet.getString(1)).thenReturn("Maria");
        when(mockedResultSet.getString(2)).thenReturn("Garcia");
        when(mockedResultSet.getString(3)).thenReturn("45");

        csvPrinter.printRecords(mockedResultSet);
        assertEquals("Maria,Garcia,45" + Constants.DEFAULT_LINE_SEPARATOR, stringWriter.toString());
    }

}