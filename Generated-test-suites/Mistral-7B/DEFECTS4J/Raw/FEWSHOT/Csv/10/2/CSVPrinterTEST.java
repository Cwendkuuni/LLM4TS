package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', QuoteMode.MINIMAL, LineEnding.LF, '\'\'');
        CSVPrinter printer = new CSVPrinter(outStream, format);

        // Test simple printing
        printer.print("foo");
        printer.print("bar");
        printer.println();

        String expected = "foo,bar\n";
        assertEquals(expected, outStream.toString());

        // Test quoting
        printer.printQuoteMode(QuoteMode.ALL);
        printer.print("foo,bar");
        printer.println();

        expected = "\"foo\",\"bar\"\n";
        assertEquals(expected, outStream.toString());

        // Test escaping
        printer.printEscaping(true);
        printer.print("foo,bar");
        printer.println();

        expected = "foo,bar\n"; // No change since escaping is not enabled by default

        // Test record separator
        printer.setRecordSeparator("\r\n");
        printer.printRecord(Arrays.asList("foo", "bar"));
        printer.println();

        expected = "foo\r\nbar\r\n";
        assertEquals(expected, outStream.toString());

        // Test commenting
        printer.printComment("This is a comment");
        printer.println();

        expected += "This is a comment\r\n";
        assertEquals(expected, outStream.toString());

        // Test printing iterable
        printer.printRecords(Arrays.asList(Arrays.asList("foo1", "bar1"), Arrays.asList("foo2", "bar2")));

        expected += "foo1,bar1\nfoo2,bar2\n";
        assertEquals(expected, outStream.toString());

        // Test printing array
        printer.printRecords(new String[]{"foo1", "bar1", "baz1"});

        expected += "foo1,bar1\nbaz1\n";
        assertEquals(expected, outStream.toString());

        // Test printing result set
        ByteArrayOutputStream rsOutputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(rsOutputStream, format);
        ResultSet rs = null; // assume a mock result set
        printer.printRecords(rs);

        assertEquals("", rsOutputStream.toString()); // Empty result set

        rs = new MockResultSet(); // Assume a mock result set
        printer.printRecords(rs);

        expected = "\"\",,\"\",,\"\",,\"\"\n"; // Empty record
        assertEquals(expected, rsOutputStream.toString());

        rs.next();
        rs.setString(1, "foo");
        rs.setString(2, "bar");

        printer.printRecords(rs);

        expected += "foo,bar\n";
        assertEquals(expected, rsOutputStream.toString());
    }

    @Test
    public void testCSVPrinterCloseAndFlush() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', QuoteMode.MINIMAL, LineEnding.LF, '\'\'');
        CSVPrinter printer = new CSVPrinter(outStream, format);
        printer.print("foo");
        printer.print("bar");
        printer.println();

        printer.close();
        printer.flush();

        String expected = "foo,bar\n";
        assertEquals(expected, outStream.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterNullFormat() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(outStream, null);
    }

    @Test(expected = IOException.class)
    public void testCSVPrinterNullOut() throws IOException {
        CSVFormat format = new CSVFormat(',', QuoteMode.MINIMAL, LineEnding.LF, '\'\'');
        CSVPrinter printer = new CSVPrinter(null, format);
    }

    // Assumes a mock ResultSet implementation
    private static class MockResultSet implements ResultSet {
        @Override
        public boolean next() throws SQLException {
            return true;
        }

        @Override
        public String getString(int columnIndex) throws SQLException {
            switch (columnIndex) {
            case 1:
                return "foo";
            case 2:
                return "bar";
            default:
                throw new SQLException("Column index out of range: " + columnIndex);
            }
        }

        @Override
        public int getMetaData() throws SQLException {
            throw new SQLException("Method not supported.");
        }
    }
}