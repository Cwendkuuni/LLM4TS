package org.apache.commons.csv.test;

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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the CSVPrinter class.
 */
public class CSVPrinterTest {

    private CSVFormat format;
    private CSVPrinter printer;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true)
                .withQuoteChar(Quote.DOUBLE_QUOTE));
        out = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
    }

    @After
    public void tearDown() throws IOException {
        if (printer != null) {
            printer.close();
        }
    }

    @Test
    public void testConstructor() {
        CSVPrinter printer = new CSVPrinter(null, null);
        assertNull(printer);

        CSVPrinter printer2 = new CSVPrinter(new ByteArrayOutputStream(), null);
        assertNull(printer2);

        CSVPrinter printer3 = new CSVPrinter(null, new CSVFormat());
        assertNull(printer3);

        CSVPrinter printer4 = new CSVPrinter(new StringWriter(new StringWriter(new ByteArrayOutputStream())), null);
        assertNull(printer4);

        CSVPrinter printer5 = new CSVPrinter(new StringWriter(new StringWriter(out)), null);
        assertSame(out, printer5.getOut());

        CSVPrinter printer6 = new CSVPrinter(out, format);
        assertSame(format, printer6.getFormat());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.println();
        assertEquals("", out.toString());
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        printer.print("");
        printer.println();
        assertEquals("", out.toString());
    }

    @Test
    public void testPrintValue() throws IOException {
        printer.print("test");
        printer.println();
        assertEquals("test", out.toString());
    }

    @Test
    public void testPrintValueQuoted() throws IOException {
        format.setQuoteAll(true);
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
        printer.print("test");
        printer.println();
        assertEquals("\"test\"", out.toString());
    }

    @Test
    public void testPrintValues() throws IOException {
        printer.print("test1");
        printer.print("test2");
        printer.print("test3");
        printer.println();
        assertEquals("test1,test2,test3", out.toString());
    }

    @Test
    public void testPrintValuesQuoted() throws IOException {
        format.setQuoteAll(true);
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
        printer.print("test1");
        printer.print("test2");
        printer.print("test3");
        printer.println();
        assertEquals("\"test1\",\"test2\",\"test3\"", out.toString());
    }

    @Test
    public void testPrintValuesQuoteChar() throws IOException {
        format.setQuoteChar(Quote.SINGLE_QUOTE);
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
        printer.print("test1");
        printer.print("test2");
        printer.print("test3");
        printer.println();
        assertEquals("\"test1\",\"test2\",\"test3\"", out.toString());
    }

    @Test
    public void testPrintValuesQuoteCharQuoted() throws IOException {
        format.setQuoteAll(true);
        format.setQuoteChar(Quote.SINGLE_QUOTE);
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
        printer.print("test1");
        printer.print("test2");
        printer.print("test3");
        printer.println();
        assertEquals("\"\"test1\"\"" + SP + "\"\"test2\"\"" + SP + "\"\"test3\"\"", out.toString());
    }

    @Test
    public void testPrintValuesEscaped() throws IOException {
        format.setEscape('\u001a');
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
        printer.print("test1");
        printer.print("test2");
        printer.print("test3");
        printer.println();
        assertEquals("test1,test2,test3", out.toString());

        printer.print("test1\u001a");
        printer.print("test2\u001aCR");
        printer.print("test3\u001aLF");
        printer.println();
        assertEquals("test1,\"\\u001a\",test2,\"\\u001aCR\",test3,\"\\u001aLF\"", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("test comment");
        printer.println();
        assertEquals(COMMENT + LF + " test comment" + LF, out.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(Arrays.asList("test1", "test2", "test3"));
        assertEquals("test1,test2,test3", out.toString());
    }

    @Test
    public void testPrintRecordQuoted() throws IOException {
        format.setQuoteAll(true);
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
        printer.printRecord(Arrays.asList("test1", "test2", "test3"));
        assertEquals("\"test1\",\"test2\",\"test3\"", out.toString());
    }

    @Test
    public void testPrintRecordQuoteChar() throws IOException {
        format.setQuoteChar(Quote.SINGLE_QUOTE);
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
        printer.printRecord(Arrays.asList("test1", "test2", "test3"));
        assertEquals("\"test1\",\"test2\",\"test3\"", out.toString());
    }

    @Test
    public void testPrintRecordQuoteCharQuoted() throws IOException {
        format.setQuoteAll(true);
        format.setQuoteChar(Quote.SINGLE_QUOTE);
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
        printer.printRecord(Arrays.asList("test1", "test2", "test3"));
        assertEquals("\"\"test1\"\"\" + SP + \"\"\"test2\"\"\" + SP + \"\"\"test3\"\"\"", out.toString());
    }

    @Test
    public void testPrintRecordEscaped() throws IOException {
        format.setEscape('\u001a');
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
        printer.printRecord(Arrays.asList("test1", "test2", "test3"));
        assertEquals("test1,test2,test3", out.toString());

        format.setEscape('\u001b');
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), format);
        printer.printRecord(Arrays.asList("test1", "test2", "test3"));
        assertEquals("test1,test2,test3", out.toString());

        printer.printRecord(Arrays.asList("test1\u001a", "test2\u001aCR", "test3\u001aLF"));
        assertEquals("test1,\"\\u001a\",test2,\"\\u001aCR\",test3,\"\\u001aLF\"", out.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        printer.printRecords(Arrays.asList(Arrays.asList("test1", "test2"), Arrays.asList("test3", "test4")));
        assertEquals("test1,test2" + LF + "test3,test4", out.toString());

        printer.printRecords(Arrays.asList(Arrays.asList("test1", "test2"), Arrays.asList("test3", "test4")), format
                .getRecordSeparator());
        assertEquals("test1,test2" + format.getRecordSeparator() + "test3,test4", out.toString());

        printer.printRecords(CSVFormat.DEFAULT.withQuoteAll(true).withQuoteChar(Quote.DOUBLE_QUOTE)
                .withRecordSeparator("\u001a"), Arrays.asList(Arrays.asList("test1", "test2"),
                        Arrays.asList("test3", "test4")));
        assertEquals("\"test1\",\"test2\"\u001a\"test3\",\"test4\"", out.toString());

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getString(Mockito.anyInt())).thenReturn("testValue");
        printer.printRecords(resultSet);
        assertEquals("testValue" + LF + "testValue" + LF, out.toString());
    }
}