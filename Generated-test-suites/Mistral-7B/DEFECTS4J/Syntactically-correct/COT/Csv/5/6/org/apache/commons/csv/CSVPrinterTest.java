package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

import org.junit.After;
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
    public void setUp() throws IOException {
        format = CSVFormat.DEFAULT;
        out = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(out), format);
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        assertEquals("\"\"", out.toString().trim());
    }

    @Test
    public void testPrintString() throws IOException {
        printer.print("test");
        assertEquals("test", out.toString().trim());
    }

    @Test
    public void testPrintObject() throws IOException {
        Object obj = new Object();
        printer.print(obj);
        assertEquals("", out.toString().trim());
    }

    @Test
    public void testPrintArray() throws IOException {
        Object[] arr = new Object[] {"test1", "test2"};
        printer.printRecords(arr);
        CSVParser parser = new CSVParser(new StringReader(out.toString()), format);
        CSVRecord record = parser.nextRecord();
        assertEquals(2, record.size());
        assertEquals("test1", record.get(0));
        assertEquals("test2", record.get(1));
    }

    @Test
    public void testPrintIterable() throws IOException, SQLException {
        String[] arr = {"test1", "test2"};
        printer.printRecords(arr);
        assertEquals("test1", out.toString().split(System.lineSeparator())[0].split(format.getDelimiter())[0]);
        assertEquals("test2", out.toString().split(System.lineSeparator())[1].split(format.getDelimiter())[0]);

        ResultSet rs = createMockResultSet();
        printer.printRecords(rs);
        assertEquals("test1", out.toString().split(System.lineSeparator())[0].split(format.getDelimiter())[0]);
        assertEquals("test2", out.toString().split(System.lineSeparator())[1].split(format.getDelimiter())[0]);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("# This is a comment");
        assertEquals("# This is a comment", out.toString().trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        CSVFormat invalidFormat = new CSVFormatBuilder().setDelimiter('a').build();
        CSVPrinter printer = new CSVPrinter(out, invalidFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOut() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, null);
    }

    @Test
    public void testQuote() throws IOException {
        format.setQuotePolicy(Quote.ALL);
        printer = new CSVPrinter(out, format);
        printer.print("test");
        assertEquals("\"test\"", out.toString().trim());

        printer.print("test,\"test\"");
        assertEquals("\"test\",\"test\"" + CR, out.toString().trim());

        printer.print("test,\"test,\"test\"");
        assertEquals("\"test\",\"test,\"test\"" + CR, out.toString().trim());
    }

    @Test
    public void testEscape() throws IOException {
        format.setEscape('\u0001');
        printer = new CSVPrinter(out, format);
        printer.print("test,test");
        assertEquals("test,test" + CR, out.toString().trim());

        printer.print("test,\"test\"");
        assertEquals("test,\"test\"" + CR, out.toString().trim());

        printer.print("test,\"test,\"test\"");
        assertEquals("test,\"test,\"test\"" + CR, out.toString().trim());

        printer.print("test,\"test,\"test,\"test\"");
        assertEquals("test,\"test,\"test,\"test\"" + CR, out.toString().trim());

        printer.print("test,\"test,\"test,\"test\u0001\"");
        assertEquals("test,\"test,\"test,\"test\u0001\"" + CR, out.toString().trim());

        printer.print("test,\"test,\"test,\"test\u0001test\"");
        assertEquals("test,\"test,\"test,\"test\u0001test\"" + CR, out.toString().trim());
    }

    @Test
    public void testRecordSeparator() throws IOException {
        format.setRecordSeparator(LF);
        printer = new CSVPrinter(out, format);
        printer.println();
        printer.print("test");
        printer.println();
        printer.print("test");
        assertEquals("test\n\ntest" + LF, out.toString().trim());

        format.setRecordSeparator(CR);
        printer = new CSVPrinter(out, format);
        printer.println();
        printer.print("test");
        printer.println();
        printer.print("test");
        assertEquals("test" + CR + "\n" + "test" + CR, out.toString().trim());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(Arrays.asList("test1", "test2"));
        assertEquals("test1,test2" + CR, out.toString().trim());

        printer.printRecord(Arrays.asList("test1", "test2", null));
        assertEquals("test1,test2,\"\"\n" + "test1,test2,null" + CR, out.toString().trim());

        printer.printRecord(Arrays.asList(null, "test2", null));
        assertEquals("\"\",test2,\"\"\n" + "\"\",test2,null" + CR, out.toString().trim());
    }

    @Test
    public void testPrintRecords() throws IOException {
        printer.printRecords(Arrays.asList(Arrays.asList("test1", "test2"), Arrays.asList("test3", "test4")));
        CSVParser parser = new CSVParser(new StringReader(out.toString()), format);
        CSVRecord record = parser.nextRecord();
        assertEquals(2, record.size());
        assertEquals("test1", record.get(0));
        assertEquals("test2", record.get(1));

        record = parser.nextRecord();
        assertEquals(2, record.size());
        assertEquals("test3", record.get(0));
        assertEquals("test4", record.get(1));
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    private static ResultSet createMockResultSet() throws SQLException {
        return new MockResultSet(new Object[] {"test1", "test2"}, new Object[] {"test3", "test4"});
    }

}