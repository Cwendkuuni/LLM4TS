package org.apache.commons.csv;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        assertNotNull(printer);
        try {
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPrint() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);

        // test null values
        printer.print(null);
        printer.flush();
        assertEquals("", out.toString());

        // test other types
        printer.print(123);
        printer.print("hello");
        printer.print(true);
        printer.print(CSVPrinterTest.class);
        printer.print((char) 0xDEADBEEF);
        printer.flush();

        String expected = "123,\"hello\",true,org.apache.commons.csv.CSVPrinterTest,\u00DEAD\u00BEEF";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintQuoting() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);

        // test null values
        printer.print(null);
        printer.flush();
        assertEquals("\"\",", out.toString());

        // test other types
        printer.print(123);
        printer.print("hello");
        printer.print(true);
        printer.print(CSVPrinterTest.class);
        printer.print((char) 0xDEADBEEF);
        printer.print("hello, world");
        printer.flush();

        String expected = "123,\"hello\",true,org.apache.commons.csv.CSVPrinterTest,\"\\u00DEAD\\u00BEEF\",\"hello, world\"";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printComment("This is a comment");
        printer.flush();
        String expected = "---\n" + "This is a comment\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecords() throws SQLException, IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);

        String[] data = {"Alice", "25", "female"};
        printer.printRecords(data);
        String expected = "Alice,25,female\n";
        assertEquals(expected, out.toString());

        ResultSet resultSet = getSampleResultSet();
        printer.printRecords(resultSet);
        expected = "Alice,25,female\n" + "Bob,21,male\n" + "Charlie,30,male\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testQuotePolicies() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.setQuotePolicy(Quote.ALL);
        printer.print(123);
        printer.flush();
        assertEquals("123", out.toString());

        printer.setQuotePolicy(Quote.NON_NUMERIC);
        printer.print(123);
        printer.flush();
        assertEquals("123", out.toString());

        printer.setQuotePolicy(Quote.MINIMAL);
        printer.print(123);
        printer.flush();
        assertEquals("123", out.toString());

        printer.setQuotePolicy(Quote.NONE);
        printer.print(123);
        printer.flush();
        assertEquals("", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidQuotePolicy() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.setQuotePolicy(new Quote("INVALID"));
    }

    private ResultSet getSampleResultSet() throws SQLException {
        return new MockResultSet(new String[][] {{"Alice", "25", "female"}, {"Bob", "21", "male"}, {"Charlie", "30", "male"}});
    }

    private static class MockResultSet implements ResultSet {
        private final String[][] data;
        private int currentRow = 0;

        public MockResultSet(String[][] data) {
            this.data = data;
        }

        public boolean next() throws SQLException {
            currentRow++;
            return currentRow <= data.length;
        }

        public String getString(int column) throws SQLException {
            return data[currentRow - 1][column];
        }

        public void close() throws SQLException {}
    }
}