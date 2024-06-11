package org.apache.commons.csv.test;

import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private StringWriter out;

    @Before
    public void setUp() {
        out = new StringWriter();
        try {
            printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPrintString() throws IOException {
        printer.print("Hello, World!");
        printer.println();
        Assert.assertEquals("Hello, World!\n", out.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.println();
        Assert.assertEquals("\"\"\n", out.toString());
    }

    @Test
    public void testPrintNumber() throws IOException {
        printer.print(123);
        printer.println();
        Assert.assertEquals("123\n", out.toString());

        printer.print(1.23);
        printer.println();
        Assert.assertEquals("1.23\n", out.toString());
    }

    @Test
    public void testPrintChar() throws IOException {
        printer.print('a');
        printer.println();
        Assert.assertEquals("\"a\"\n", out.toString());
    }

    @Test
    public void testPrintBoolean() throws IOException {
        printer.print(true);
        printer.println();
        Assert.assertEquals("true\n", out.toString());

        printer.print(false);
        printer.println();
        Assert.assertEquals("false\n", out.toString());
    }

    @Test
    public void testQuoting() throws IOException {
        printer.print("\"Test\"");
        printer.println();
        Assert.assertEquals("\"\"Test\"\"\n", out.toString());
    }

    @Test
    public void testEscaping() throws IOException {
        printer.print("\"Test, \"Quoted\", \"\",,,, \"Field\"");
        printer.println();
        Assert.assertEquals("\"Test, \"Quoted\", \"\",,,, \"Field\"\n", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("# This is a comment.");
        printer.println();
        Assert.assertEquals("# This is a comment.\n", out.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(new String[]{"1", "Two", "Three"});
        Assert.assertEquals("1,Two,Three\n", out.toString());
    }

    @Test
    public void testPrintRecordWithQuotes() throws IOException {
        printer.printRecord(new String[]{"\"One\"", "\"Two,\"", "\"Three\""});
        Assert.assertEquals("\"One\",\"Two,\",\"Three\"\n", out.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        ResultSet rs = TestData.createResultSet();
        printer.printRecords(rs);
        Assert.assertEquals(
                "\"Name\",\"Age\",\"City\"\n" +
                        "\"Alice\",30,\"New York\"\n" +
                        "\"Bob\",25,\"Boston\"\n" +
                        "\"Charlie\",35,\"Chicago\"\n", out.toString());
    }

    @Test
    public void testPrintRecordsWithNull() throws IOException, SQLException {
        ResultSet rs = TestData.createResultSetWithNull();
        printer.printRecords(rs);
        Assert.assertEquals(
                "\"Name\",\"Age\",\"City\"\n" +
                        "\"Alice\",null,\"New York\"\n" +
                        "\"Bob\",25,\"Boston\"\n" +
                        "\"Charlie\",35,null\n", out.toString());
    }
}