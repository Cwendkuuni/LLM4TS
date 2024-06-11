package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVPrinter.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private OutputStream out;
    private CSVPrinter printer;

    @Before
    public void setup() throws IOException {
        out = new ByteArrayOutputStream();
        printer = new CSVPrinter(out, DEFAULT);
    }

    @Test
    public void testDefault() throws IOException {
        printer.print("test");
        printer.println();
        printer.close();

        final String expected = "test\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testQuoting() throws IOException {
        printer = new CSVPrinter(out, DEFAULT.withQuote('"'));
        printer.print("test, \"test, test\"");
        printer.println();
        printer.close();

        final String expected = "test,\"test, test\"\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testEscaping() throws IOException {
        printer = new CSVPrinter(out, DEFAULT.withEscape('\\'));
        printer.print("test, test,\"test, test\", test, test, \"test, test\"");
        printer.println();
        printer.close();

        final String expected = "test, test,\"test, test\", test, test, \"test, test\"\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testCommenting() throws IOException {
        printer = new CSVPrinter(out, DEFAULT.withComment('%'));
        printer.printComment("This is a comment.");
        printer.println();
        printer.close();

        final String expected = "%This is a comment.\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testRecordSeparator() throws IOException {
        printer = new CSVPrinter(out, DEFAULT.withRecordSeparator("\n---\n"));
        printer.print("test");
        printer.println();
        printer.print("record1");
        printer.println();
        printer.print("record2");
        printer.println();
        printer.close();

        final String expected = "test\n---\nrecord1\n---\nrecord2\n---\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testCustomFormat() throws IOException {
        printer = new CSVPrinter(out, new CSVFormat('|', '\t', new Quote('"')));
        printer.print("test|\"test, test\"");
        printer.println();
        printer.close();

        final String expected = "test|\"test, test\"\n";
        assertEquals(expected, out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOut() {
        new CSVPrinter(null, DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() {
        new CSVPrinter(out, null);
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.println();
        printer.close();

        final String expected = DEFAULT.getNullString() + "\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintEmptyArray() throws IOException {
        printer.printRecords(Collections.emptyList().toArray());
        printer.println();
        printer.close();

        final String expected = "";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintArray() throws IOException {
        final Object[] testArray = new Object[] {"test1", "test2"};
        printer.printRecords(testArray);
        printer.println();
        printer.close();

        final String expected = "test1,test2\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintIterable() throws IOException, SQLException {
        List<String> values = Arrays.asList("test1", "test2");
        printer.printRecords(values);
        printer.println();
        printer.close();

        final String expected = "test1,test2\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintResultSet() throws IOException, SQLException {
        // Assuming a mock ResultSet containing values "test1", "test2", "test3"
        printer.printRecords(resultSet);
        printer.println();
        printer.close();

        final String expected = "test1,test2,test3\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecordWithNull() throws IOException {
        Object[] testArray = new Object[] {"test1", null, "test3"};
        printer.printRecords(testArray);
        printer.println();
        printer.close();

        final String expected = "test1,null,test3\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecordWithObject() throws IOException {
        Object[] testArray = new Object[] {"test1", new Object()};
        printer.printRecords(testArray);
        printer.println();
        printer.close();

        final String expected = "test1,<object>\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecordWithArray() throws IOException {
        Object[] testArray = new Object[] {"test1", new Object[] {"test2", "test3"}};
        printer.printRecords(testArray);
        printer.println();
        printer.close();

        final String expected = "test1,\"test2, test3\"\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecordWithIterable() throws IOException {
        Object[] testArray = new Object[] {"test1", Arrays.asList("test2", "test3")};
        printer.printRecords(testArray);
        printer.println();
        printer.close();

        final String expected = "test1,\"test2, test3\"\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecordWithNullIterable() throws IOException {
        Object[] testArray = new Object[] {"test1", null};
        printer.printRecords(testArray);
        printer.println();
        printer.close();

        final String expected = "test1,null\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecordWithNullArray() throws IOException {
        Object[] testArray = new Object[] {"test1", null};
        printer.printRecords(testArray);
        printer.println();
        printer.close();

        final String expected = "test1,null\n";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecordWithEmptyIterable() throws IOException {
        Object[] testArray = new Object[] {"test1", Collections.emptyList().toArray()};
        printer.printRecords(testArray);
        printer.println();
        printer.close();

        final String expected = "test1,\"\n";
        assertEquals(expected, out.toString());
    }
}