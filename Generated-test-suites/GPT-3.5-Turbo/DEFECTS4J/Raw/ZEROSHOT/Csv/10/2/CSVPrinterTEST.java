package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter writer;

    @Before
    public void setUp() {
        writer = new StringWriter();
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrintObject() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.print(123);
        csvPrinter.print(3.14);
        csvPrinter.print(null);
        csvPrinter.flush();

        assertEquals("Hello,123,3.14,\n", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();

        assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecordIterable() throws IOException {
        csvPrinter.printRecord("Value1", "Value2", "Value3");
        csvPrinter.flush();

        assertEquals("Value1,Value2,Value3\n", writer.toString());
    }

    @Test
    public void testPrintRecordVarargs() throws IOException {
        csvPrinter.printRecord("Value1", "Value2", "Value3");
        csvPrinter.flush();

        assertEquals("Value1,Value2,Value3\n", writer.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        Iterable<String[]> records = new Iterable<String[]>() {
            @Override
            public java.util.Iterator<String[]> iterator() {
                return new java.util.Iterator<String[]>() {

                    private int count = 0;

                    @Override
                    public boolean hasNext() {
                        return count < 2;
                    }

                    @Override
                    public String[] next() {
                        count++;
                        if (count == 1) {
                            return new String[] { "Value1", "Value2" };
                        } else {
                            return new String[] { "Value3", "Value4" };
                        }
                    }

                    @Override
                    public void remove() {
                        // Not implemented
                    }
                };
            }
        };

        csvPrinter.printRecords(records);
        csvPrinter.flush();

        assertEquals("Value1,Value2\nValue3,Value4\n", writer.toString());
    }

    @Test
    public void testPrintRecordsArray() throws IOException {
        Object[] records = new Object[] { 
            new Object[] { "Value1", "Value2" },
            new Object[] { "Value3", "Value4" }
        };

        csvPrinter.printRecords(records);
        csvPrinter.flush();

        assertEquals("Value1,Value2\nValue3,Value4\n", writer.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(metaData.getColumnCount()).thenReturn(2);
        Mockito.when(resultSet.getString(1)).thenReturn("Value1");
        Mockito.when(resultSet.getString(2)).thenReturn("Value2");

        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();

        assertEquals("Value1,Value2\nValue1,Value2\n", writer.toString());
    }

    @Test
    public void testGetOut() {
        assertTrue(csvPrinter.getOut() instanceof StringWriter);
    }
}