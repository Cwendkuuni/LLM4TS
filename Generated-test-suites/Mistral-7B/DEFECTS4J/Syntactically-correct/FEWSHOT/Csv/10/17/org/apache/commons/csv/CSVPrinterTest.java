package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("hello");
        printer.print("world");
        printer.println();

        assertEquals("\"hello\",\"world\"\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintNull() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(null);
        printer.print("world");
        printer.println();

        assertEquals("\"\",\"world\"\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintQuoteEscape() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true).withQuoteEscaping(Quote.BACKSLASH));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello, World!");
        printer.println();

        assertEquals("\"Hello, World!\",\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintQuoteMinimal() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello, World!");
        printer.println();

        assertEquals("Hello, World!\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintDelimiter() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';'));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello");
        printer.print("World");
        printer.println();

        assertEquals("Hello;World\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintComment() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withCommentingEnabled(true));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("This is a comment");
        printer.println();

        assertEquals("#This is a comment\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecord() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(Arrays.asList("Hello", "World"));
        printer.println();

        assertEquals("Hello,World\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecordObject() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        Object[] record = new Object[] {"Hello", "World"};
        printer.printRecord(record);
        printer.println();

        assertEquals("Hello,World\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecordIterable() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        Iterable<Object> record = () -> Arrays.asList("Hello", "World").iterator();
        printer.printRecord(record);
        printer.println();

        assertEquals("Hello,World\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecordsIterable() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        Iterable<Object> records = () -> Arrays.asList(Arrays.asList("Hello1", "World1"), Arrays.asList("Hello2", "World2"))
                .iterator();
        printer.printRecords(records);

        assertEquals("Hello1,World1\nHello2,World2\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecordsArray() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        Object[][] records = new Object[][] {{"Hello1", "World1"}, {"Hello2", "World2"}};
        printer.printRecords(records);

        assertEquals("Hello1,World1\nHello2,World2\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecordsResultSet() throws SQLException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        String query = "SELECT name, age FROM users";
        ResultSet resultSet = TestHelper.getTestResultSet(query);

        printer.printRecords(resultSet);

        assertEquals("name,age\nJohn,25\nMary,30\n", out.toString());

        printer.close();
    }

    @Test
    public void testPrintln() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.println();

        assertEquals("\n", out.toString());

        printer.close();
    }

    @Test
    public void testIsNewRecord() {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), CSVFormat.DEFAULT);
        assertTrue(printer.isNewRecord());
        printer.print("foo");
        assertFalse(printer.isNewRecord());
        printer.println();
        assertTrue(printer.isNewRecord());
        printer.close();
    }
}