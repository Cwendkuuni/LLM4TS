package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() throws Exception {
        outContent = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(outContent), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws Exception {
        printer.close();
    }

    @Test
    public void testPrintSingleValue() throws IOException {
        printer.print("Test Value");
        printer.println();
        String expected = "Test Value\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        printer.println();
        String expected = CSVFormat.DEFAULT.getNullString();
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        printer.print("Test1");
        printer.print("Test2");
        printer.println();
        String expected = "Test1,Test2\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintMultipleNullValues() throws IOException {
        printer.print(null);
        printer.print(null);
        printer.println();
        String expected = CSVFormat.DEFAULT.getNullString() + CSVFormat.DEFAULT.getNullString();
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintValueWithEscape() throws IOException {
        printer.print("Test Value with \"quotes\"");
        printer.println();
        String expected = "Test Value with \"quotes\"\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintValueWithEscapeAndQuote() throws IOException {
        printer.print("Test Value with \"quotes\" in it");
        printer.println();
        String expected = "Test Value with \"" + CSVFormat.DEFAULT.getQuoteChar() + "quotes\"" + CSVFormat.DEFAULT.getQuoteChar() + "\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintRecordWithIterable() throws IOException {
        printer.printRecord(new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new Iterator<Object>() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return "Value " + (++count);
                    }

                    private int count = 0;
                };
            }
        });
        String expected = "Value 1,Value 2,Value 3\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintRecordWithObjectArray() throws IOException {
        printer.printRecord(new String[]{"Value1", "Value2", "Value3"});
        String expected = "Value1,Value2,Value3\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintRecordWithResultSet() throws SQLException, IOException {
        String sql = "SELECT COL1, COL2, COL3 FROM TEST_TABLE";
        ResultSet resultSet = // initialize the result set here
        printer.printRecords(resultSet);
        String expected = // expected output based on the result set
        assertEquals(expected, outContent.toString());
    }
}