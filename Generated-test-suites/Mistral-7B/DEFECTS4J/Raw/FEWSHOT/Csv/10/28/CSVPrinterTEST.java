package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        // Print strings
        printer.print("Hello");
        printer.print("World");
        printer.println();

        assertEquals("Hello,World\n", out.toString());

        // Print null
        printer.print(null);
        printer.println();
        assertEquals("null\n", out.toString());

        // Print numbers
        printer.print(123);
        printer.println();
        assertEquals("123\n", out.toString());

        // Print objects
        StringWriter sw = new StringWriter();
        printer.print(sw);
        printer.println();
        assertEquals("null\n", out.toString());

        // Print arrays
        String[] values = {"foo", "bar"};
        printer.printRecord(values);
        printer.println();
        assertEquals("foo,bar\n", out.toString());

        // Print iterables
        String input = "1,2,3,4";
        Iterable<String> iterable = () -> input.split(",");
        printer.printRecords(iterable);
        printer.println();
        assertEquals("1,2,3,4\n", out.toString());

        // Print JDBC result set
        ResultSet resultSet = mockResultSet();
        printer.printRecords(resultSet);
        printer.close();
        assertEquals("1,foo,bar,4\n2,one,two,5\n", out.toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("foo,bar");
        printer.println();
        assertEquals("\"foo,\"\"bar\"\n", out.toString());

        printer.print("\"foo,bar\"");
        printer.println();
        assertEquals("\"\"foo,bar\"\"\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withEscape('\\'));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("foo,bar");
        printer.println();
        assertEquals("foo,bar\n", out.toString());

        printer.print("foo,bar\n");
        printer.println();
        assertEquals("foo,bar\\n\n", out.toString());

        printer.print("foo,\"bar\"");
        printer.println();
        assertEquals("foo,bar\"\n", out.toString());

        printer.print("foo,\"bar\\\"");
        printer.println();
        assertEquals("foo,bar\\\"\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintComment() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("This is a comment");
        printer.println();
        assertEquals("# This is a comment\n", out.toString());

        printer.println();
        printer.printComment("Another comment");
        printer.println();
        assertEquals("# Another comment\n", out.toString());

        printer.println();
        printer.printComment("");
        printer.println();
        assertEquals("\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecord() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(Arrays.asList("foo", "bar"));
        printer.println();
        assertEquals("foo,bar\n", out.toString());

        printer.printRecord(Arrays.asList(1, 2, 3));
        printer.println();
        assertEquals("1,2,3\n", out.toString());

        printer.println();
        printer.printRecord(Arrays.asList(null, ""));
        printer.println();
        assertEquals("null,\"\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecords(Arrays.asList(Arrays.asList("foo", "bar"), Arrays.asList(1, 2, 3)));
        printer.println();
        assertEquals("foo,bar\n1,2,3\n", out.toString());

        printer.printRecords(Arrays.asList(Arrays.asList(null), Arrays.asList("")));
        printer.println();
        assertEquals("null,\"\n", out.toString());

        printer.printRecords(Arrays.asList(Arrays.asList(null), Arrays.asList(""), Arrays.asList("foo", "bar")));
        printer.println();
        assertEquals("null,\"\nnull,\"\nfoo,bar\n", out.toString());

        printer.close();
    }

    @Test
    public void testFlushAndClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("foo,bar");
        printer.println();
        printer.flush();
        assertEquals("foo,bar\n", out.toString());

        printer.close();

        // Should not be able to print after close
        try {
            printer.print("baz");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("stream has been closed"));
        }
    }

    @Test
    public void testQuotePolicies() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        Quote quotePolicy = Quote.MINIMAL;
        format.setQuotePolicy(quotePolicy);
        printer = new CSVPrinter(out, format);

        printer.print("foo,bar");
        printer.println();
        assertEquals("foo,bar\n", out.toString());

        printer.print("foo,\"bar\"");
        printer.println();
        assertEquals("foo,\"bar\"\n", out.toString());

        printer.close();

        // Test with ALL quote policy
        format.setQuotePolicy(Quote.ALL);
        printer = new CSVPrinter(out, format);

        printer.print("foo,bar");
        printer.println();
        assertEquals("\"foo,\"\"bar\"\"\n", out.toString());

        printer.close();
    }

    @Test
    public void testCopyConstructor() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(Arrays.asList("foo", "bar"));
        printer.println();

        CSVPrinter printer2 = new CSVPrinter(printer);
        printer2.printRecord(Arrays.asList("baz", "qux"));
        printer2.println();

        assertEquals("foo,bar\nbaz,qux\n", out.toString());

        printer.close();
        printer2.close();
    }

    private ResultSet mockResultSet() throws SQLException {
        return new MockResultSet();
    }

    private static class MockResultSet implements ResultSet {
        private String data = "1,foo,bar,4\n2,one,two,5\n";
        private int index = 0;

        @Override
        public boolean next() throws SQLException {
            index++;
            return index <= data.split("\n").length;
        }

        @Override
        public String getString(int column) throws SQLException {
            String[] values = data.split("\n")[index - 1].split(",");
            return values[column];
        }

        @Override
        public boolean wasNull() throws SQLException {
            return false;
        }
    }
}