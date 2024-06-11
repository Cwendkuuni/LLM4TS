import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class CSVPrinterTest {

    @Test
    public void testPrintObject() throws IOException {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        csvPrinter.print("test");
        assertEquals("test", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        csvPrinter.printComment("This is a comment");
        String expectedOutput = "# This is a comment" + System.lineSeparator();
        assertEquals(expectedOutput, csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        csvPrinter.printRecord("value1", "value2");
        assertEquals("value1,value2" + System.lineSeparator(), csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        csvPrinter.printRecords(new Object[]{"value1", "value2", "value3"});
        assertEquals("value1,value2,value3" + System.lineSeparator(), csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);

        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");

        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        csvPrinter.printRecords(resultSet);

        assertEquals("value1,value2" + System.lineSeparator(), csvPrinter.getOut().toString());
    }

    // Add more test cases for edge cases and exception handling scenarios here

}