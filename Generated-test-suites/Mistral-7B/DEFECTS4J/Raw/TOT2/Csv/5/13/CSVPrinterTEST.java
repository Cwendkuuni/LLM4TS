package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for CSVPrinter class.
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream output;
    private CSVFormat format;
    private CSVPrinter printer;

    /**
     * Sets up test environment.
     *
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        output = new ByteArrayOutputStream();
        format = new CSVFormat(CSVFormat.DEFAULT, false, false);
        printer = new CSVPrinter(output, format);
    }

    /**
     * Tears down test environment.
     *
     * @throws IOException
     */
    @After
    public void tearDown() throws IOException {
        if (printer != null) {
            printer.close();
        }
    }

    /**
     * Test print method with null and empty value.
     *
     * @throws IOException
     */
    @Test
    public void testPrintNullAndEmpty() throws IOException {
        printer.print(null);
        printer.print("");
        assertEquals("", output.toString());
    }

    /**
     * Test print method with simple string.
     *
     * @throws IOException
     */
    @Test
    public void testPrintSimpleString() throws IOException {
        printer.print("test");
        assertEquals("test", output.toString());
    }

    /**
     * Test print method with multiple values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintMultipleValues() throws IOException {
        printer.print(1);
        printer.print("test");
        printer.print(3.14);
        assertEquals("1,test,3.14", output.toString());
    }

    /**
     * Test print method with quoting.
     *
     * @throws IOException
     */
    @Test
    public void testPrintQuoting() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT, true, false);
        printer = new CSVPrinter(output, format);
        printer.print("test,test\"test\"");
        assertEquals("test,\"test\"test\"", output.toString());
    }

    /**
     * Test print method with escaping.
     *
     * @throws IOException
     */
    @Test
    public void testPrintEscaping() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT, false, true);
        printer = new CSVPrinter(output, format);
        printer.print("test,test\\test\\");
        assertEquals("test,test\\test\\", output.toString());
    }

    /**
     * Test print method with comment.
     *
     * @throws IOException
     */
    @Test
    public void testPrintComment() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT, false, false);
        format.setComment("COMMENT");
        printer = new CSVPrinter(output, format);
        printer.printComment("test comment");
        assertEquals("COMMENT test comment" + LF, output.toString());
    }

    /**
     * Test println method with empty line.
     *
     * @throws IOException
     */
    @Test
    public void testPrintlnEmpty() throws IOException {
        printer.println();
        assertEquals("", output.toString());
    }

    /**
     * Test println method with single value.
     *
     * @throws IOException
     */
    @Test
    public void testPrintlnSingleValue() throws IOException {
        printer.print("test");
        printer.println();
        assertEquals("test" + CR, output.toString());
    }

    /**
     * Test println method with multiple values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintlnMultipleValues() throws IOException {
        printer.print(1);
        printer.print("test");
        printer.print(3.14);
        printer.println();
        assertEquals("1,test,3.14" + CR, output.toString());
    }

    /**
     * Test printRecord method with null and empty array.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordNullAndEmpty() throws IOException {
        printer.printRecord(null);
        printer.printRecord();
        assertEquals("", output.toString());
    }

    /**
     * Test printRecord method with simple array.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordSimpleArray() throws IOException {
        Object[] array = {"test1", "test2"};
        printer.printRecord(array);
        assertEquals("test1,test2" + CR, output.toString());
    }

    /**
     * Test printRecord method with quoting array.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordQuotingArray() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT, true, false);
        Object[] array = {"test1", "test2\"test2\""};
        printer = new CSVPrinter(output, format);
        printer.printRecord(array);
        assertEquals("test1,\"test2\"test2\"" + CR, output.toString());
    }

    /**
     * Test printRecord method with escaping array.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordEscapingArray() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT, false, true);
        Object[] array = {"test1,test1\\test1", "test2,test2\\test2\\"};
        printer = new CSVPrinter(output, format);
        printer.printRecord(array);
        assertEquals("test1,test1\\test1,test2,test2\\test2\\" + CR, output.toString());
    }

    /**
     * Test printRecord method with comment array.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordCommentArray() throws IOException, SQLException {
        format = new CSVFormat(CSVFormat.DEFAULT, false, false);
        format.setComment("COMMENT");
        Object[] array = {"test1", "COMMENT test2"};
        printer = new CSVPrinter(output, format);
        printer.printRecord(array);
        assertEquals("test1" + CR + "COMMENT test2" + CR, output.toString());

        // Test printRecords method with SQLResultSet
        ResultSet rs = getTestResultSet();
        printer.printRecords(rs);
        assertEquals(getTestResultSetString(), output.toString());
    }

    /**
     * Test printRecords method with null and empty iterable.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordsNullAndEmpty() throws IOException {
        printer.printRecords(null);
        printer.printRecords();
        assertEquals("", output.toString());
    }

    /**
     * Test printRecords method with simple iterable.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordsSimpleIterable() throws IOException {
        Iterable<String> iterable = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    String nextValue = "test1,test2";
                    @Override
                    public boolean hasNext() {
                        return nextValue != null;
                    }

                    @Override
                    public String next() {
                        String value = nextValue;
                        nextValue = null;
                        return value;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        printer.printRecords(iterable);
        assertEquals("test1,test2" + CR, output.toString());
    }

    /**
     * Test printRecords method with quoting iterable.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordsQuotingIterable() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT, true, false);
        Iterable<String> iterable = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    String nextValue = "test1,\"test2\"test2\"";
                    @Override
                    public boolean hasNext() {
                        return nextValue != null;
                    }

                    @Override
                    public String next() {
                        String value = nextValue;
                        nextValue = null;
                        return value;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        printer = new CSVPrinter(output, format);
        printer.printRecords(iterable);
        assertEquals("test1,\"test2\"test2\"" + CR, output.toString());
    }

    /**
     * Test printRecords method with escaping iterable.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordsEscapingIterable() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT, false, true);
        Iterable<String> iterable = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    String nextValue = "test1,test1\\test1,test2,test2\\test2\\";
                    @Override
                    public boolean hasNext() {
                        return nextValue != null;
                    }

                    @Override
                    public String next() {
                        String value = nextValue;
                        nextValue = null;
                        return value;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        printer = new CSVPrinter(output, format);
        printer.printRecords(iterable);
        assertEquals("test1,test1\\test1,test2,test2\\test2\\" + CR, output.toString());
    }

    /**
     * Test printRecords method with comment iterable.
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordsCommentIterable() throws IOException, SQLException {
        format = new CSVFormat(CSVFormat.DEFAULT, false, false);
        format.setComment("COMMENT");
        Iterable<String> iterable = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    String nextValue = "test1,COMMENT test2";
                    @Override
                    public boolean hasNext() {
                        return nextValue != null;
                    }

                    @Override
                    public String next() {
                        String value = nextValue;
                        nextValue = null;
                        return value;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        printer = new CSVPrinter(output, format);
        printer.printRecords(iterable);
        assertEquals("test1" + CR + "COMMENT test2" + CR, output.toString());
    }

    /**
     * Test flush method.
     *
     * @throws IOException
     */
    @Test
    public void testFlush() throws IOException {
        printer.print("test");
        printer.flush();
        assertEquals("test", output.toString());
    }

    /**
     * Test close method.
     *
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        printer.print("test");
        printer.close();
        assertFalse(printer.isOpen());
        assertEquals("test", output.toString());
    }

    /**
     * Test close method with exception.
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test(expected = SQLException.class)
    public void testCloseWithException() throws IOException, SQLException {
        format = new CSVFormat(CSVFormat.DEFAULT, false, false);
        format.setComment("COMMENT");
        printer = new CSVPrinter(output, format);
        printer.printComment("test comment");
        printer.close();
    }

    /**
     * Helper method to get test ResultSet.
     *
     * @return test ResultSet
     * @throws SQLException
     */
    private ResultSet getTestResultSet() throws SQLException {
        // Create a test ResultSet
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn("test1");
        when(resultSet.getString(2)).thenReturn("test2");
        when(resultSet.getString(3)).thenReturn("test3");
        return resultSet;
    }

    /**
     * Helper method to get test ResultSet string.
     *
     * @return test ResultSet string
     */
    private String getTestResultSetString() {
        return "test1,test2,test3";
    }
}