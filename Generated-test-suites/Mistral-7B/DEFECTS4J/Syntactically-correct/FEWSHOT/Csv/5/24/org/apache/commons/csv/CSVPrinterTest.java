package org.apache.commons.csv;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws IOException, SQLException {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat csvFormat = new CSVFormat();
        final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outStream), csvFormat);
        assertNotNull(csvPrinter);

        csvPrinter.close();
        csvPrinter.flush();
    }

    @Test
    public void testPrint() throws IOException {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat csvFormat = new CSVFormat();
        final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outStream), csvFormat);

        csvPrinter.print("test");
        csvPrinter.flush();

        final String expectedOutput = "test";
        final String actualOutput = new String(outStream.toByteArray());
        assertNotNull(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        csvPrinter.close();
    }

    @Test
    public void testPrintQuote() throws IOException {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat csvFormat = new CSVFormat('"', Quote.MINIMAL);
        final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outStream), csvFormat);

        csvPrinter.print("test \"test\"");
        csvPrinter.flush();

        final String expectedOutput = "\"test\" \"test\"";
        final String actualOutput = new String(outStream.toByteArray());
        assertNotNull(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        csvPrinter.close();
    }

    @Test
    public void testPrintEscape() throws IOException {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat csvFormat = new CSVFormat('\\', true);
        final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outStream), csvFormat);

        csvPrinter.print("test,\"test\"");
        csvPrinter.flush();

        final String expectedOutput = "test,\"test\"";
        final String actualOutput = new String(outStream.toByteArray());
        assertNotNull(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        csvPrinter.close();
    }

    @Test
    public void testPrintComment() throws IOException {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat csvFormat = new CSVFormat();
        final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outStream), csvFormat);

        csvPrinter.printComment("test comment");
        csvPrinter.flush();

        final String expectedOutput = "# test comment";
        final String actualOutput = new String(outStream.toByteArray());
        assertNotNull(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        csvPrinter.close();
    }

    @Test
    public void testPrintRecord() throws IOException {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat csvFormat = new CSVFormat();
        final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outStream), csvFormat);

        csvPrinter.printRecord(Arrays.asList("test1", "test2"));
        csvPrinter.flush();

        final String expectedOutput = "test1,test2\n";
        final String actualOutput = new String(outStream.toByteArray());
        assertNotNull(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        csvPrinter.close();
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat csvFormat = new CSVFormat();
        final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outStream), csvFormat);

        final List<Object> record1 = Arrays.asList("test11", "test12");
        final List<Object> record2 = Arrays.asList("test21", "test22");
        final List<Object> records = Arrays.asList(record1, record2);

        csvPrinter.printRecords(records);
        csvPrinter.flush();

        final String expectedOutput = "test11,test12\n test21,test22\n";
        final String actualOutput = new String(outStream.toByteArray());
        assertNotNull(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        csvPrinter.close();
    }

    @Test
    public void testPrintRecordsArray() throws IOException, SQLException {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat csvFormat = new CSVFormat();
        final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outStream), csvFormat);

        final Object[] record1 = new Object[]{"test11", "test12"};
        final Object[] record2 = new Object[]{"test21", "test22"};
        final Object[] records = new Object[][]{record1, record2};

        csvPrinter.printRecords(records);
        csvPrinter.flush();

        final String expectedOutput = "test11,test12\n test21,test22\n";
        final String actualOutput = new String(outStream.toByteArray());
        assertNotNull(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        csvPrinter.close();
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat csvFormat = new CSVFormat();
        final CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outStream), csvFormat);

        final ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn("test11");
        when(resultSet.getString(2)).thenReturn("test12");
        when(resultSet.getString(3)).thenReturn("test21");
        when(resultSet.getString(4)).thenReturn("test22");

        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();

        final String expectedOutput = "test11,test12\ntest21,test22\n";
        final String actualOutput = new String(outStream.toByteArray());
        assertNotNull(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        csvPrinter.close();
    }
}