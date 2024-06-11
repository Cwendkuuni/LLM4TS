package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(out, format);
        assertSame(out, printer.getOut());
    }

    @Test
    public void testClose() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.close();
    }

    @Test
    public void testFlush() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.flush();
    }

    @Test
    public void testPrint() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(null);
        printer.print(" ");
        printer.print("foo");
        printer.print(123);
        printer.print(true);
        printer.print(1.23);
        printer.print('a');
        printer.print("bar");

        String expectedResult = ",,foo,123,true,1.2,a,bar\n";
        assertEquals(expectedResult, out.toString());

        printer.close();
    }

    @Test
    public void testPrintQuoting() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(';', Quote.ALL);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(null);
        printer.print(" ");
        printer.print("foo");
        printer.print(123);
        printer.print(true);
        printer.print(1.23);
        printer.print('a');
        printer.print("bar");
        printer.print("\"foo bar\"");
        printer.print("\"123\"");
        printer.print("\"true\"");
        printer.print("\"1.2\"");
        printer.print("\"a\"");
        printer.print("\"bar\"");

        String expectedResult = ";,;foo," +
                "\"foo\";," +
                "\"123\";," +
                "\"true\";," +
                "\"1.2\";," +
                "\"a\";," +
                "\"bar\";," +
                "\"foo bar\";," +
                "\"123\";," +
                "\"true\";," +
                "\"1.2\";," +
                "\"a\";," +
                "\"bar\"\n";
        assertEquals(expectedResult, out.toString());

        printer.close();
    }

    @Test
    public void testPrintEscaping() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(';', Quote.NONE);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(null);
        printer.print(" ");
        printer.print("foo");
        printer.print(123);
        printer.print(true);
        printer.print(1.23);
        printer.print('a');
        printer.print("bar");
        printer.print("foo,bar");
        printer.print("123,true");
        printer.print("1.2,a");
        printer.print("bar");

        String expectedResult = ";,;foo,123,true,1.2,a,bar\n";
        assertEquals(expectedResult, out.toString());

        printer.close();
    }

    @Test
    public void testPrintQuotingAndEscaping() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(';', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(null);
        printer.print(" ");
        printer.print("foo");
        printer.print(123);
        printer.print(true);
        printer.print(1.23);
        printer.print('a');
        printer.print("bar");
        printer.print("\"foo bar\"");
        printer.print("\"123\"");
        printer.print("\"true\"");
        printer.print("\"1.2\"");
        printer.print("\"a\"");
        printer.print("\"bar\"");
        printer.print("foo,bar");
        printer.print("123,true");
        printer.print("1.2,a");
        printer.print("bar");
        printer.print("\\,");
        printer.print("\\n");
        printer.print("\\r");
        printer.print("\\f");
        printer.print("\\b");
        printer.print("\\t");

        String expectedResult = ";,;foo,123,true,1.2,a,bar," +
                "\"foo bar\";," +
                "\"123\";," +
                "\"true\";," +
                "\"1.2\";," +
                "\"a\";," +
                "\"bar\"\n";
        assertEquals(expectedResult, out.toString());

        printer.close();
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        format.setComment("--");
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("This is a comment");
        String expectedResult = "--This is a comment\n";
        assertEquals(expectedResult, out.toString());

        printer.close();
    }

    @Test
    public void testPrintln() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.println();
        String expectedResult = "\n";
        assertEquals(expectedResult, out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(out, format);

        Iterable<Object> values1 = Arrays.asList("foo", "bar");
        printer.printRecord(values1);

        Iterable<Object> values2 = Arrays.asList(1, 2, 3);
        printer.printRecord(values2);

        Iterable<Object> values3 = Arrays.asList(new Object[] {"foo", "bar"});
        printer.printRecord(values3);

        Iterable<Object> values4 = Arrays.asList(new Iterable<?>[] {
                Arrays.asList("foo1", "bar1"),
                Arrays.asList("foo2", "bar2")
        });
        printer.printRecords(values4);

        String expectedResult = ",foo,bar\n" +
                ",1,2,3\n" +
                "\"foo\",\"bar\"\n" +
                "\"foo1\",\"bar1\"\n" +
                "\"foo2\",\"bar2\"\n";
        assertEquals(expectedResult, out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecordsWithQuoting() throws IOException, SQLException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.ALL);
        CSVPrinter printer = new CSVPrinter(out, format);

        Iterable<Object> values1 = Arrays.asList("foo", "bar");
        printer.printRecord(values1);

        Iterable<Object> values2 = Arrays.asList(1, 2, 3);
        printer.printRecord(values2);

        Iterable<Object> values3 = Arrays.asList(new Object[] {"foo", "bar"});
        printer.printRecord(values3);

        Iterable<Object> values4 = Arrays.asList(new Iterable<?>[] {
                Arrays.asList("foo1", "bar1"),
                Arrays.asList("foo2", "bar2")
        });
        printer.printRecords(values4);

        String expectedResult = ",\"foo\",\"bar\"\n" +
                ",1,2,3\n" +
                "\"foo\",\"bar\"\n" +
                "\"foo1\",\"bar1\"\n" +
                "\"foo2\",\"bar2\"\n";
        assertEquals(expectedResult, out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecordsWithQuotingAndEscaping() throws IOException, SQLException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.ALL);
        format.setEscapeChar('\\');
        CSVPrinter printer = new CSVPrinter(out, format);

        Iterable<Object> values1 = Arrays.asList("foo", "bar");
        printer.printRecord(values1);

        Iterable<Object> values2 = Arrays.asList(1, 2, 3);
        printer.printRecord(values2);

        Iterable<Object> values3 = Arrays.asList(new Object[] {"foo", "bar"});
        printer.printRecord(values3);

        Iterable<Object> values4 = Arrays.asList(new Iterable<?>[] {
                Arrays.asList("foo1", "bar1"),
                Arrays.asList("foo2", "bar2")
        });
        printer.printRecords(values4);

        String expectedResult = ",\"foo\",\"bar\"\n" +
                ",1,2,3\n" +
                "\"foo\",\"bar\"\n" +
                "\"foo1\",\"bar1\"\n" +
                "\"foo2\",\"bar2\"\n";
        assertEquals(expectedResult, out.toString());

        printer.close();
    }

    @Test
    public void testPrintRecordsWithResultSet() throws SQLException, IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(out, format);

        String sql = "SELECT * FROM test_table";
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/test_db", "sa", "");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        printer.printRecords(resultSet);
        resultSet.close();
        statement.close();
        connection.close();

        String expectedResult = ",foo,bar,baz\n" +
                ",foo1,bar1,baz1\n" +
                ",foo2,bar2,baz2\n";
        assertEquals(expectedResult, out.toString());

        printer.close();
    }
}