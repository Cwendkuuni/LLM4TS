package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVPrinter.Quote;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.CSVRecordWriter;
import org.apache.commons.csv.CSVWriter;
import org.apache.commons.csv.DefaultCSVFormat;
import org.apache.commons.csv.QuoteMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private static final String TEST_RECORD_1 = "Record 1";
    private static final String TEST_RECORD_2 = "Record 2";
    private static final String TEST_RECORD_3 = "Record 3";
    private static final String TEST_COMMENT = "This is a test comment.";

    private ByteArrayOutputStream out;
    private CSVPrinter printer;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        final CSVFormat format = new DefaultCSVFormat(',', QuoteMode.MINIMAL);
        printer = new CSVPrinter(new StringWriter(new StringWriter(out).getBufferString()), format);
    }

    @After
    public void tearDown() throws Exception {
        if (printer != null) {
            printer.close();
        }
    }

    @Test
    public void testPrintBasic() throws IOException {
        printer.print(TEST_RECORD_1);
        printer.println();
        printer.print(TEST_RECORD_2);
        printer.println();
        printer.print(TEST_RECORD_3);
        printer.println();

        final String result = out.toString();
        final String expected = TEST_RECORD_1 + "\n" + TEST_RECORD_2 + "\n" + TEST_RECORD_3 + "\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.println();
        final String result = out.toString();
        final String expected = format.getNullString();
        assertEquals(expected, result);
    }

    @Test
    public void testPrintEmptyArray() throws IOException {
        Object[] emptyArray = new Object[0];
        printer.printRecord(emptyArray);
        printer.println();

        final String result = out.toString();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPrintIterable() throws IOException, SQLException {
        Statement stmt = null;
        try {
            stmt = TestHelper.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM test_table");

            printer.printRecords(rs);
            printer.flush();
            printer.close();

            final String result = out.toString();
            final CSVRecordWriter writer = new CSVWriter(new StringWriter());
            CSVRecordIterator iterator = CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipLinesNumber(1).iterator(rs);
            writer.writeAll(iterator);
            final String expected = writer.getWriter().toString();

            assertEquals(expected, result);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Test
    public void testPrintObjectArray() throws IOException {
        final Object[] objectArray = {TEST_RECORD_1, TEST_RECORD_2, TEST_RECORD_3};
        printer.printRecords(objectArray);
        printer.println();

        final String result = out.toString();
        final String expected = TEST_RECORD_1 + "\n" + TEST_RECORD_2 + "\n" + TEST_RECORD_3 + "\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintObjectIterable() throws IOException {
        final Iterable<Object> objectIterable = () -> new Object[] {TEST_RECORD_1, TEST_RECORD_2, TEST_RECORD_3};
        printer.printRecords(objectIterable);
        printer.println();

        final String result = out.toString();
        final String expected = TEST_RECORD_1 + "\n" + TEST_RECORD_2 + "\n" + TEST_RECORD_3 + "\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment(TEST_COMMENT);
        printer.println();

        final String result = out.toString();
        final String expected = COMMENT + " " + TEST_COMMENT + "\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        final String quotedValue = "\"Value with quotes\"";
        printer.print(quotedValue);
        printer.println();

        final String result = out.toString();
        final String expected = "\"" + quotedValue + "\"\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        final String escapedValue = "Value with \\n and \\r";
        printer.print(escapedValue);
        printer.println();

        final String result = out.toString();
        final String expected = "Value with \\n and \\r\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintQuotedEscapedValue() throws IOException {
        final String quotedEscapedValue = "\"Value with \\\\n and \\\\r\"";
        printer.print(quotedEscapedValue);
        printer.println();

        final String result = out.toString();
        final String expected = "\"Value with \\n and \\r\"\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new CSVPrinter(null, null));
    }

    @Test
    public void testPrintIOException() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Output stream error");
            }
        };
        final CSVPrinter printer = new CSVPrinter(out, new DefaultCSVFormat(','));
        assertFalse(printer.isClosed());

        printer.print(TEST_RECORD_1);
        printer.println();

        printer.close();

        assertThrows(IOException.class, printer::flush);
    }
}