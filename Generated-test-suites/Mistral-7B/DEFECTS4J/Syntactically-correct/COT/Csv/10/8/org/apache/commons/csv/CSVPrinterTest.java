package org.apache.commons.csv; // make sure the test class is in the same package as the tested class

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    // ====================================================
    // test fixtures
    // ====================================================

    private CSVPrinter printer;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() throws IOException {
        output = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(output), CSVFormat.DEFAULT.withQuoteAll(true));
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
        output.close();
    }

    // ====================================================
    // test methods
    // ====================================================

    /**
     * Test case for {@link CSVPrinter#CSVPrinter(Appendable, CSVFormat)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     * @throws IllegalArgumentException
     *             If the parameters of the format are inconsistent or if either out or format are null.
     */
    @Test
    public void testCSVPrinter() throws IOException, IllegalArgumentException {
        // test valid constructor
        printer = new CSVPrinter(output, CSVFormat.DEFAULT);
    }

    /**
     * Test case for {@link CSVPrinter#close()}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        printer.close();
        printer = null; // make sure the next test starts with a fresh instance

        // test that closing an already closed instance does nothing
        printer = new CSVPrinter(output, CSVFormat.DEFAULT);
        printer.close();
    }

    /**
     * Test case for {@link CSVPrinter#flush()}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testFlush() throws IOException {
        printer.flush();
        printer.print("test");
        printer.flush();

        final String expected = "test" + LF;
        assertEquals(expected, output.toString());
    }

    /**
     * Test case for {@link CSVPrinter#getOut()}.
     */
    @Test
    public void testGetOut() {
        assertFalse(printer.getOut() instanceof CSVPrinter);
    }

    /**
     * Test case for {@link CSVPrinter#print(Object)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrint() throws IOException {
        printer.print("test");

        final String expected = "test" + LF;
        assertEquals(expected, output.toString());

        printer.print(null);
        printer.print("test");
        printer.print(new Object());

        final String actual = output.toString();
        assertEquals(expected + "test" + LF + "test" + LF + "test" + LF, actual);
    }

    /**
     * Test case for {@link CSVPrinter#print(CharSequence, int, int)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintQuoted() throws IOException {
        printer.print("\"test\"");

        final String expected = "\"test\"" + LF;
        assertEquals(expected, output.toString());

        printer.print("\"test\"", 0, 4);

        final String actual = output.toString();
        assertEquals(expected, actual.substring(0, expected.length() - 1) + "\"" + LF, actual);
    }

    /**
     * Test case for {@link CSVPrinter#print(CharSequence, int, int)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintEscaped() throws IOException {
        printer.print("test\\n\\r\\\"");

        final String expected = "test\\n\\r\\\"";
        assertEquals(expected, output.toString());
    }

    /**
     * Test case for {@link CSVPrinter#printComment(String)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.println();
        printer.printComment("test comment");

        final String expected = LF + COMMENT + SP + "test comment" + LF;
        assertEquals(expected, output.toString());
    }

    /**
     * Test case for {@link CSVPrinter#println()}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintln() throws IOException {
        printer.println();

        final String expected = LF;
        assertEquals(expected, output.toString());

        printer.println();
        printer.println();

        final String actual = output.toString();
        assertEquals(expected + expected, actual);
    }

    /**
     * Test case for {@link CSVPrinter#printRecord(Iterable)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintRecordIterable() throws IOException {
        final List<String> values = new LinkedList<>(Arrays.asList("test1", "test2", "test3"));
        printer.printRecord(values);

        final String expected = "test1,test2,test3" + LF;
        assertEquals(expected, output.toString());
    }

    /**
     * Test case for {@link CSVPrinter#printRecord(Object...)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintRecordObject() throws IOException {
        final List<Object> values = new LinkedList<>(Collections.singletonList(new Object[]{"test1", "test2", "test3"}));
        printer.printRecord(values.toArray());

        final String expected = "test1,test2,test3" + LF;
        assertEquals(expected, output.toString());
    }

    /**
     * Test case for {@link CSVPrinter#printRecords(Iterable)}.
     *
     * @throws IOException
     *             If an I/O error occurs
     * @throws SQLException
     *             If a database access error occurs
     */
    @Test
    public void testPrintRecordsIterable() throws IOException, SQLException {
        final ResultSet resultSet = createTestResultSet();
        printer.printRecords(resultSet);

        final String expected = "test1,test2,test3\ntest4,test5,test6" + LF;
        assertEquals(expected, output.toString());
    }

    // ====================================================
    // helper methods
    // ====================================================

    /**
     * Creates a test result set with some sample data.
     *
     * @return a test result set
     * @throws SQLException
     *             If a database access error occurs
     */
    private ResultSet createTestResultSet() throws SQLException {
        final String[] values1 = {"test1", "test2", "test3"};
        final String[] values2 = {"test4", "test5", "test6"};

        final Object[][] testData = new Object[][]{values1, values2};

        final List<Object[]> rows = new LinkedList<>(Arrays.asList(testData));

        // create a mock result set
        final ResultSet resultSet = mock(ResultSet.class);

        // setup the test data in the mock result set
        when(resultSet.next()).thenAnswer(invocation -> {
            boolean hasNext = rows.size() > 0;
            if (hasNext) {
                resultSet.setArray(1, DataSupport.createObjectArray(rows.remove(0)));
            }
            return hasNext;
        });

        return resultSet;
    }
}