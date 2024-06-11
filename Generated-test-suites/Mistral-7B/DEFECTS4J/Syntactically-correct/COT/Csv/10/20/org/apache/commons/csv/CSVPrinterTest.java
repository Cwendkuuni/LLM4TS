package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Test;

/**
 * Test class for CSVPrinter.
 */
public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws IOException, SQLException {
        // Test constructor
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(new PrintWriter(out), format);

        // Test print methods
        printer.print(null);
        printer.print("Hello");
        printer.print("World");
        printer.println();

        printer.printComment("# This is a comment");
        printer.println();

        printer.printRecord("First", "Second", "Third");
        printer.println();

        printer.printRecords(new Object[]{"First", "Second", "Third"});
        printer.println();

        // Test print methods with quoting and escaping
        printer.format = new CSVFormat(CSVFormat.DEFAULT.withQuote('"', Quote.MINIMAL));
        printer.print("Hello \"World\"");
        printer.println();

        printer.format = new CSVFormat(CSVFormat.DEFAULT.withQuote('"', Quote.MINIMAL).withEscape('\\'));
        printer.print("Hello \\nWorld");
        printer.println();

        printer.format = new CSVFormat(CSVFormat.DEFAULT.withQuote('"', Quote.MINIMAL).withEscape('\\'));
        printer.print("Hello \"World\" with \\n escape");
        printer.println();

        printer.format = new CSVFormat(CSVFormat.DEFAULT.withQuote('"', Quote.MINIMAL).withEscape('\\'));
        printer.print("Hello \"World\" with \r escape");
        printer.println();

        // Test print methods with commenting
        printer.format = new CSVFormat(CSVFormat.DEFAULT.withComment('#'));
        printer.printComment("This is a comment");
        printer.println();

        printer.printRecord("First", "Second", "Third");
        printer.printComment("This is a comment on a record");
        printer.println();

        // Test printRecord method with Iterable and Object[]
        printer.printRecords(java.util.Arrays.asList(new Object[]{"First", "Second", "Third"}));
        printer.println();

        printer.printRecords(java.util.Arrays.asList("First", "Second", "Third"));
        printer.println();

        // Test printRecords method with ResultSet
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        ResultSet rs = java.sql.Statement.executeQuery("SELECT 'Hello', 'World' from test");
        CSVPrinter printer2 = new CSVPrinter(new PrintWriter(out2), format);
        CSVPrinter printer3 = new CSVPrinter(new PrintWriter(out3), format);
        printer2.printRecords(rs);
        printer3.printRecords(rs);
        assertEquals(out2.toString(), out3.toString());

        // Test getOut() method
        assertEquals(printer.getOut(), out);

        // Test flush() method
        printer.flush();
        assertEquals(out.toString(), printer.getOut().toString());

        // Test close() method
        printer.close();
        assertEquals("", printer.getOut().toString());

        // Test exceptions
        try {
            new CSVPrinter(null, format);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new CSVPrinter(out, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test print methods with empty string
        printer = new CSVPrinter(new PrintWriter(out), format);
        printer.print("");
        printer.println();
        assertEquals("", out.toString());

        printer.printRecord(java.util.Collections.emptyList());
        printer.println();
        assertEquals("", out.toString());

        printer.printRecords(java.util.Collections.emptyList());
        printer.println();
        assertEquals("", out.toString());
    }
}