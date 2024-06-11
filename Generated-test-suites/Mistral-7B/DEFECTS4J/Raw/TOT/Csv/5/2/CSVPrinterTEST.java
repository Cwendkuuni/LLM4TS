package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.Quote.MINIMAL;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private CSVFormat format;
    private ByteArrayOutputStream out;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        format = CSVFormat.DEFAULT.withQuotePolicy(MINIMAL);
        out = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(out), format);
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        printer.print("");
        String csv = out.toString();
        assertEquals("", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintSingleValue() throws IOException {
        printer.print("value");
        String csv = out.toString();
        assertEquals("value", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintMultipleValuesOnSameLine() throws IOException {
        printer.print("value1");
        printer.print("value2");
        printer.print("value3");
        printer.println();
        String csv = out.toString();
        assertEquals("value1,value2,value3", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintMultipleValuesOnNewLines() throws IOException {
        printer.print("value1");
        printer.println();
        printer.print("value2");
        printer.println();
        printer.print("value3");
        String csv = out.toString();
        String[] lines = csv.split(System.lineSeparator());
        assertEquals(3, lines.length);
        assertEquals("value1", lines[0]);
        assertEquals("value2", lines[1]);
        assertEquals("value3", lines[2]);
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        String csv = out.toString();
        assertEquals("", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        printer.print("value with quotes");
        printer.println();
        String csv = out.toString();
        assertEquals("\"value with quotes\"", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        printer.print("value with CR, LF and SP");
        printer.println();
        String csv = out.toString();
        assertEquals("value with CR, LF and SP", csv.split(System.lineSeparator())[0]);
        assertTrue(csv.contains(CR.toString()));
        assertTrue(csv.contains(LF.toString()));
        assertTrue(csv.contains(SP.toString()));
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        String csv = out.toString();
        assertEquals("#This is a comment", csv.split(System.lineSeparator())[csv.split(System.lineSeparator())
                .length - 1]);
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(new String[]{"value1", "value2", "value3"});
        String csv = out.toString();
        assertEquals("value1,value2,value3", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        CSVPrinter printer2 = new CSVPrinter(new OutputStreamWriter(out2), format);

        ResultSet rs = TestUtil.getSampleData();
        printer2.printRecords(rs);
        String csv = out2.toString();

        printer.printRecords(rs);
        String csvExpected = out.toString();

        assertEquals(csvExpected, csv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStream() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, format);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, null);
    }

    @Test
    public void testPrintRecordWithNullValues() throws IOException {
        printer.printRecord(new String[]{"value1", null, "value3"});
        String csv = out.toString();
        assertEquals("value1,null,value3", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintRecordWithEmptyArray() throws IOException {
        printer.printRecord(new String[0]);
        assertEquals("", out.toString());
    }

    @Test
    public void testPrintRecordWithObjectArray() throws IOException {
        printer.printRecord(new Object[]{"value1", "value2", 123});
        String csv = out.toString();
        assertEquals("value1,value2,123", csv.split(System.lineSeparator())[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testPrintRecordWithNullIterator() throws IOException {
        printer.printRecord(null);
    }

    @Test(expected = NullPointerException.class)
    public void testPrintRecordWithNullObject() throws IOException {
        printer.printRecord(null);
    }

    @Test
    public void testPrintRecordWithIterable() throws IOException {
        Iterable<String> iterable = Arrays.asList("value1", "value2");
        printer.printRecord(iterable);
        String csv = out.toString();
        assertEquals("value1,value2", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintRecordWithArrayList() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        printer.printRecord(list);
        String csv = out.toString();
        assertEquals("value1,value2", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintRecordsWithEmptyIterable() throws IOException {
        Iterable<String> iterable = Collections.emptyList();
        printer.printRecords(iterable);
        assertEquals("", out.toString());
    }

    @Test
    public void testPrintRecordsWithArrayListAndNullValue() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        list.add("value1");
        list.add(null);
        list.add("value3");
        printer.printRecords(list);
        String csv = out.toString();
        assertEquals("value1,null,value3", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintRecordsWithArrayListAndEmptyArray() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        list.add(new String[0]);
        printer.printRecords(list);
        String csv = out.toString();
        assertEquals("value1,value2,", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintRecordsWithArrayListAndObjectArray() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        Object[] array = new Object[]{"value3", new Integer(123)};
        printer.printRecords(list, array);
        String csv = out.toString();
        assertEquals("value1,value2,value3,123", csv.split(System.lineSeparator())[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordsWithNullIterable() throws IOException {
        printer.printRecords(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordsWithNullObjectArray() throws IOException {
        printer.printRecords((Object[]) null);
    }

    @Test
    public void testPrintRecordsWithVarargs() throws IOException {
        printer.printRecords("value1", "value2", "value3");
        String csv = out.toString();
        assertEquals("value1,value2,value3", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintRecordsWithVarargsAndNullValue() throws IOException {
        printer.printRecords("value1", null, "value3");
        String csv = out.toString();
        assertEquals("value1,null,value3", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintRecordsWithVarargsAndEmptyArray() throws IOException {
        printer.printRecords("value1", "value2", new String[0]);
        String csv = out.toString();
        assertEquals("value1,value2,", csv.split(System.lineSeparator())[0]);
    }

    @Test
    public void testPrintRecordsWithVarargsAndObjectArray() throws IOException {
        printer.printRecords("value1", "value2", new Object[]{"value3", new Integer(123)});
        String csv = out.toString();
        assertEquals("value1,value2,value3,123", csv.split(System.lineSeparator())[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordsWithNullVarargs() throws IOException {
        printer.printRecords((String[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordsWithNullObjectArrayVarargs() throws IOException {
        printer.printRecords("value1", (Object[][]) null);
    }
}