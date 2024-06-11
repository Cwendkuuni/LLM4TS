
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);

        csvPrinter.print("test");
        csvPrinter.print("1");
        csvPrinter.print("2");
        csvPrinter.println();

        csvPrinter.print("test");
        csvPrinter.print("3");
        csvPrinter.print("4");
        csvPrinter.println();

        csvPrinter.flush();

        assertEquals("test,1,2\n" +
                "test,3,4\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);

        csvPrinter.printRecord(Arrays.asList("test", "1", "2"));
        csvPrinter.printRecord(Arrays.asList("test", "3", "4"));

        csvPrinter.flush();

        assertEquals("test,1,2\n" +
                "test,3,4\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);

        List<Object> records = Arrays.asList(
                Arrays.asList("test", "1", "2"),
                Arrays.asList("test", "3", "4")
        );

        csvPrinter.printRecords(records);

        csvPrinter.flush();

        assertEquals("test,1,2\n" +
                "test,3,4\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsWithArray() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);

        Object[] records = {
                new Object[] {"test", "1", "2"},
                new Object[] {"test", "3", "4"}
        };

        csvPrinter.printRecords(records);

        csvPrinter.flush();

        assertEquals("test,1,2\n" +
                "test,3,4\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);

        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("test");
        when(resultSet.getString(2)).thenReturn("1");
        when(resultSet.getString(3)).thenReturn("2");

        csvPrinter.printRecords(resultSet);

        csvPrinter.flush();

        assertEquals("test,1,2\n", stringWriter.toString());
    }
}
