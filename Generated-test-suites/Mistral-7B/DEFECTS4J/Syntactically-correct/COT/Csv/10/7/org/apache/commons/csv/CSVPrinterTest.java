package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.apache.commons.csv.CSVParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVPrinter
 *
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream byteArrayOutputStream;
    private CSVFormat csvFormat;
    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    /**
     * Set up test environment
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream();
        csvFormat = CSVFormat.DEFAULT;
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, csvFormat);
    }

    /**
     * Tear down test environment
     *
     * @throws IOException
     */
    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
        stringWriter.close();
        byteArrayOutputStream.close();
    }

    /**
     * Test constructor
     */
    @Test
    public void testCSVPrinterConstructor() {
        CSVPrinter printer = new CSVPrinter(byteArrayOutputStream, csvFormat);
        assertNull(printer.getOut());
        assertSame(csvFormat, printer.getFormat());
    }

    /**
     * Test close method
     *
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        csvPrinter.close();
        assertTrue(csvPrinter.isClosed());
    }

    /**
     * Test flush method
     *
     * @throws IOException
     */
    @Test
    public void testFlush() throws IOException {
        String printResult = csvPrinter.print("value1").print("value2").flush();
        assertEquals(printResult, stringWriter.toString());
    }

    /**
     * Test print method with null value
     *
     * @throws IOException
     */
    @Test
    public void testPrintNull() throws IOException {
        csvPrinter.print(null);
        assertEquals(CSVPrinter.EMPTY, csvPrinter.getOut().toString());
    }

    /**
     * Test print method with Object value
     *
     * @throws IOException
     */
    @Test
    public void testPrintObject() throws IOException {
        csvPrinter.print("value1");
        csvPrinter.print(new Integer(1));
        csvPrinter.print(new Double(1.1));
        String printResult = csvPrinter.flush();
        assertEquals("value1,1,1.1", printResult);
    }

    /**
     * Test print method with CharSequence value
     *
     * @throws IOException
     */
    @Test
    public void testPrintCharSequence() throws IOException {
        csvPrinter.print("value1");
        csvPrinter.print("value2");
        String printResult = csvPrinter.flush();
        assertEquals("value1,value2", printResult);
    }

    /**
     * Test print method with different quote characters
     *
     * @throws IOException
     */
    @Test
    public void testPrintQuoteCharacters() throws IOException {
        CSVFormat format = new CSVFormat(',', '"', '"', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(byteArrayOutputStream, format);
        printer.print("value with \"quotes\"");
        printer.close();
        String testString = new String(byteArrayOutputStream.toByteArray()).substring(0, byteArrayOutputStream.size() - 1);
        assertEquals("value with \"quotes\"", testString);
    }

    /**
     * Test print method with escaping enabled
     *
     * @throws IOException
     */
    @Test
    public void testPrintEscaping() throws IOException {
        CSVFormat format = new CSVFormat(',', '\\', '"', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(byteArrayOutputStream, format);
        printer.print("value1,\"value2\",value3");
        printer.close();
        String testString = new String(byteArrayOutputStream.toByteArray()).substring(0, byteArrayOutputStream.size() - 1);
        assertEquals("value1,\"value2\",value3", testString);
    }

    /**
     * Test print method with same quote character as value
     *
     * @throws IOException
     */
    @Test
    public void testPrintSameQuoteChar() throws IOException {
        CSVFormat format = new CSVFormat(',', '"', '"', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(byteArrayOutputStream, format);
        printer.print("value with \"quotes\"");
        printer.close();
        String testString = new String(byteArrayOutputStream.toByteArray()).substring(0, byteArrayOutputStream.size() - 1);
        assertEquals("value with \"quotes\"", testString);
    }

    /**
     * Test print method with comment enabled
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testPrintComment() throws IOException, SQLException {
        CSVFormat format = new CSVFormat(',', '"', '"', Quote.MINIMAL);
        format.setComment("COMMENT");
        CSVPrinter printer = new CSVPrinter(byteArrayOutputStream, format);
        printer.printComment("This is a comment");
        printer.close();
        String testString = new String(byteArrayOutputStream.toByteArray()).substring(0, byteArrayOutputStream.size() - 1);
        assertEquals("COMMENT This is a comment\n", testString);
    }

    /**
     * Test println method
     *
     * @throws IOException
     */
    @Test
    public void testPrintln() throws IOException {
        csvPrinter.print("value1");
        csvPrinter.println();
        csvPrinter.print("value2");
        csvPrinter.println();
        String printResult = csvPrinter.flush();
        assertEquals(LF + "value1" + CR, printResult.substring(0, 5));
        assertEquals(LF + "value2" + CR, printResult.substring(printResult.indexOf(LF) + 1, printResult.length()));
    }

    /**
     * Test printRecord method with Iterable values
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordIterable() throws IOException {
        csvPrinter.printRecord(new String[]{"value1", "value2"});
        String printResult = csvPrinter.flush();
        assertEquals("value1,value2\n", printResult);
    }

    /**
     * Test printRecord method with Object array values
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordObjectArray() throws IOException {
        csvPrinter.printRecord(new String[]{"value1", "value2"});
        String printResult = csvPrinter.flush();
        assertEquals("value1,value2\n", printResult);
    }

    /**
     * Test printRecords method with Iterable values
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordsIterable() throws IOException {
        csvPrinter.printRecords(new String[][]{{"value11", "value12"}, {"value21", "value22"}});
        String printResult = csvPrinter.flush();
        assertEquals("value11,value12\nvalue21,value22\n", printResult);
    }

    /**
     * Test printRecords method with Object array values
     *
     * @throws IOException
     */
    @Test
    public void testPrintRecordsObjectArray() throws IOException {
        csvPrinter.printRecords(new String[][]{{"value11", "value12"}, {"value21", "value22"}});
        String printResult = csvPrinter.flush();
        assertEquals("value11,value12\nvalue21,value22\n", printResult);
    }

    /**
     * Test printRecords method with SQL ResultSet values
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        String query = "SELECT name, age, country FROM users";
        try (Connection connection = DatabaseUtils.createConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            csvPrinter.printRecords(resultSet);
            String printResult = csvPrinter.flush();
            String expectedResult = "name,age,country\n";
            while (resultSet.next()) {
                expectedResult += resultSet.getString(1) + "," + resultSet.getString(2) + ","
                        + resultSet.getString(3) + "\n";
            }
            assertEquals(expectedResult, printResult);
        }
    }
}