package org.apache.commons.csv;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringBuilder stringBuilder;

    @Before
    public void setUp() throws IOException {
        stringBuilder = new StringBuilder();
        csvPrinter = new CSVPrinter(stringBuilder, CSVFormat.DEFAULT);
    }

    @Test
    public void testConstructor() throws IOException {
        assertNotNull(csvPrinter.getOut());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOut() throws IOException {
        new CSVPrinter(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFormat() throws IOException {
        new CSVPrinter(stringBuilder, null);
    }

    @Test(expected = IOException.class)
    public void testPrintIOException() throws IOException {
        stringBuilder = new StringBuilder() {
            @Override
            public StringBuilder append(CharSequence csq) throws IOException {
                throw new IOException();
            }
        };
        csvPrinter = new CSVPrinter(stringBuilder, CSVFormat.DEFAULT);
        csvPrinter.print("test");
    }

    @Test
    public void testClose() throws IOException {
        csvPrinter.close();
    }

    @Test
    public void testFlush() throws IOException {
        csvPrinter.flush();
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("test");
        assertEquals("test", stringBuilder.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        csvPrinter.print(null);
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        String comment = "This is a comment";
        csvPrinter.printComment(comment);
        assertEquals("# " + comment + System.lineSeparator(), stringBuilder.toString());
    }

    @Test
    public void testPrintCommentDisable() throws IOException {
        csvPrinter = new CSVPrinter(stringBuilder, CSVFormat.DEFAULT.withCommentingDisabled());
        csvPrinter.printComment("Test comment");
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        assertEquals(System.lineSeparator(), stringBuilder.toString());
    }

    @Test
    public void testPrintRecordIterable() throws IOException {
        Iterable<String> values = Arrays.asList("test1", "test2", "test3");
        csvPrinter.printRecord(values);
        assertEquals("test1,test2,test3" + System.lineSeparator(), stringBuilder.toString());
    }

    @Test
    public void testPrintRecordArray() throws IOException {
        Object[] values = {"test1", "test2", "test3"};
        csvPrinter.printRecord(values);
        assertEquals("test1,test2,test3" + System.lineSeparator(), stringBuilder.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        Iterable<Object[]> values = Arrays.asList(
                new Object[]{"test1", "test2", "test3"},
                new Object[]{"test4", "test5", "test6"}
        );
        csvPrinter.printRecords(values);
        assertEquals("test1,test2,test3" + System.lineSeparator() +
                "test4,test5,test6" + System.lineSeparator(), stringBuilder.toString());
    }

    @Test
    public void testPrintRecordsArray() throws IOException {
        Object[] values = {
                new Object[]{"test1", "test2", "test3"},
                new Object[]{"test4", "test5", "test6"}
        };
        csvPrinter.printRecords(values);
        assertEquals("test1,test2,test3" + System.lineSeparator() +
                "test4,test5,test6" + System.lineSeparator(), stringBuilder.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws SQLException, IOException {
        ResultSet resultSet = new TestResultSet();
        csvPrinter.printRecords(resultSet);
        assertEquals("result1,result2,result3" + System.lineSeparator() +
                "result4,result5,result6" + System.lineSeparator(), stringBuilder.toString());
    }

    private static class TestResultSet implements ResultSet {

        private int rowCount = 2;

        @Override
        public boolean next() throws SQLException {
            return rowCount-- > 0;
        }

        @Override
        public String getString(int columnIndex) throws SQLException {
            switch (columnIndex) {
                case 1:
                    return "result1";
                case 2:
                    return "result2";
                case 3:
                    return "result3";
                default:
                    return "";
            }
        }

        // Other ResultSet methods implemented as required

    }

}