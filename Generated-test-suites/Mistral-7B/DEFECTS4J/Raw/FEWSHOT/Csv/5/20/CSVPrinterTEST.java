package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.junit.Assert.assertArrayEquals;
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
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private ByteArrayOutputStream out;
    private CSVFormat format;
    private CSVPrinter printer;

    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        format = new CSVFormat(',', Quote.ALL, '\u001E');
        printer = new CSVPrinter(new OutputStreamWriter(out), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
        out.close();
    }

    @Test
    public void testClose() throws IOException {
        printer.close();
        assertNull(printer.getOut());
    }

    @Test
    public void testFlush() throws IOException {
        printer.flush();
        assertFalse(out.toString().isEmpty());
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Hello, World!");
        assertEquals("Hello, World!\n", out.toString());

        printer.print(null);
        assertEquals("null\n", out.toString());

        printer.print(new Integer(42));
        assertEquals("42\n", out.toString());

        printer.print(new List<String>() {{
            add("One");
            add("Two");
            add("Three");
        }});
        assertEquals("\"One\",\"Two\",\"Three\"\n", out.toString());
    }

    @Test
    public void testPrintQuoted() throws IOException {
        printer.printQuoted("Hello, World!");
        assertEquals("\"Hello, World!\"\n", out.toString());

        printer.printQuoted(null);
        assertEquals("\"null\"\n", out.toString());

        printer.printQuoted(new Integer(42));
        assertEquals("\"42\"\n", out.toString());

        printer.printQuoted(new List<String>() {{
            add("One");
            add("Two");
            add("Three");
        }});
        assertEquals("\"One\",\"Two\",\"Three\"\n", out.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(Arrays.asList("One", "Two", "Three"));
        assertEquals("One,Two,Three\n", out.toString());
    }

    @Test
    public void testPrintRecordQuoted() throws IOException {
        printer.printRecordQuoted(Arrays.asList("One", "Two", "Three"));
        assertEquals("\"One\",\"Two\",\"Three\"\n", out.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        String[] data = {"One", "Two", "Three"};
        printer.printRecords(Arrays.asList(data));
        assertEquals("One,Two,Three\n", out.toString());

        ResultSet resultSet = createMockResultSet(data);
        printer.printRecords(resultSet);
        assertEquals("One,Two,Three\n", out.toString());
    }

    private static ResultSet createMockResultSet(String[] data) throws SQLException {
        return new MockResultSet(Arrays.asList(data));
    }
}

class MockResultSet implements ResultSet {

    private final Iterable<Object> data;

    public MockResultSet(Iterable<Object> data) {
        this.data = data;
    }

    @Override
    public boolean next() throws SQLException {
        return !this.data.isEmpty();
    }

    @Override
    public String getString(int column) throws SQLException {
        List<Object> row = (List<Object>) this.data.iterator().next();
        return (String) row.get(column);
    }

    @Override
    public int getMetaData() throws SQLException {
        throw new UnsupportedOperationException();
    }
}