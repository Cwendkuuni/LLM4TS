package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

/**
 * Test class for CSVPrinter
 */
public class CSVPrinterTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        CSVFormat format = new CSVFormat(' ', COMMA, COMMENT, '"', '\'\'').withQuotePolicy(Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(new StringWriter(), format);
    }

    /**
     * Test print method with null value
     */
    @org.junit.Test
    public void testPrintWithNullValue() throws IOException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withNullString("NULL");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.print(null);
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(out.toString().getBytes());
        CSVRecord record = records.get(0);
        assertEquals("NULL", record.get(0).getRaw());
    }

    /**
     * Test print method with single value
     */
    @org.junit.Test
    public void testPrintWithSingleValue() throws IOException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withNullString("NULL");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.print("Test");
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(out.toString().getBytes());
        CSVRecord record = records.get(0);
        assertEquals("Test", record.get(0).getRaw());
    }

    /**
     * Test print method with multiple values
     */
    @org.junit.Test
    public void testPrintWithMultipleValues() throws IOException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withNullString("NULL");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.print("Test1");
        printer.print("Test2");
        printer.println();
        printer.print("Test3");
        printer.print("Test4");
        printer.println();
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(out.toString().getBytes());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals("Test1", record1.get(0).getRaw());
        assertEquals("Test2", record1.get(1).getRaw());
        assertNull(record1.get(2));
        assertEquals("Test3", record2.get(0).getRaw());
        assertEquals("Test4", record2.get(1).getRaw());
        assertNull(record2.get(2));
    }

    /**
     * Test printAndQuote method with single value
     */
    @org.junit.Test
    public void testPrintAndQuoteWithSingleValue() throws IOException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withQuotePolicy(Quote.ALWAYS);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.print("Test\"");
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(out.toString().getBytes());
        CSVRecord record = records.get(0);
        assertEquals("\"Test\"", record.get(0).getRaw());
    }

    /**
     * Test printAndQuote method with multiple values
     */
    @org.junit.Test
    public void testPrintAndQuoteWithMultipleValues() throws IOException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withQuotePolicy(Quote.ALWAYS);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.print("Test1\"");
        printer.print("Test2\"");
        printer.println();
        printer.print("Test3\"");
        printer.print("Test4\"");
        printer.println();
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(out.toString().getBytes());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals("\"Test1\"", record1.get(0).getRaw());
        assertEquals("\"Test2\"", record1.get(1).getRaw());
        assertNull(record1.get(2));
        assertEquals("\"Test3\"", record2.get(0).getRaw());
        assertEquals("\"Test4\"", record2.get(1).getRaw());
        assertNull(record2.get(2));
    }

    /**
     * Test printAndEscape method with single value
     */
    @org.junit.Test
    public void testPrintAndEscapeWithSingleValue() throws IOException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withEscapingEnabled(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.print("\r\nTest\"");
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(out.toString().getBytes());
        CSVRecord record = records.get(0);
        assertEquals("Test\"", record.get(0).getRaw());
    }

    /**
     * Test printAndEscape method with multiple values
     */
    @org.junit.Test
    public void testPrintAndEscapeWithMultipleValues() throws IOException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withEscapingEnabled(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.print("Test1\r\nTest2\"");
        printer.println();
        printer.print("Test3\r\nTest4\"");
        printer.println();
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(out.toString().getBytes());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals("Test1", record1.get(0).getRaw());
        assertEquals("Test2\"", record1.get(1).getRaw());
        assertNull(record1.get(2));
        assertEquals("Test3", record2.get(0).getRaw());
        assertEquals("Test4\"", record2.get(1).getRaw());
        assertNull(record2.get(2));
    }

    /**
     * Test printComment method with single comment
     */
    @org.junit.Test
    public void testPrintCommentWithSingleComment() throws IOException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withCommentingEnabled(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printComment("Test Comment");
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.parse(out.toString().getBytes());
        CSVRecord record = records.get(0);
        assertEquals("Test Comment", record.get(0).getRaw());
    }

    /**
     * Test printComment method with multiple comments
     */
    @org.junit.Test
    public void testPrintCommentWithMultipleComments() throws IOException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withCommentingEnabled(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printComment("Test Comment 1");
        printer.println();
        printer.printComment("Test Comment 2");
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.parse(out.toString().getBytes());
        List<String> comments = Arrays.asList("Test Comment 1", "Test Comment 2");
        assertEquals(comments.size(), records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(comments.get(i), record.get(0).getRaw());
        }
    }

    /**
     * Test println method
     */
    @org.junit.Test
    public void testPrintln() throws IOException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withCommentingEnabled(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.println();
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.parse(out.toString().getBytes());
        List<String> comments = Collections.singletonList(LF);
        assertEquals(comments.size(), records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(comments.get(i).charAt(0), record.get(0).getRaw().charAt(0));
        }
    }

    /**
     * Test printRecord method with single record
     */
    @org.junit.Test
    public void testPrintRecordWithSingleRecord() throws IOException, SQLException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withCommentingEnabled(true);
        Statement stmt = TestUtils.getConnection("jdbc:derby:memory:test;create=true").createStatement();
        stmt.executeUpdate("CREATE TABLE test (col1 INT, col2 VARCHAR(10))");
        stmt.executeUpdate("INSERT INTO test VALUES (1, 'Test')");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecords(stmt.executeQuery("SELECT * FROM test"));
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.parse(out.toString().getBytes());
        List<String> headers = Arrays.asList("COL1", "COL2");
        List<Object[]> data = Arrays.asList(new Object[]{1, "Test"});
        assertEquals(headers.size(), records.get(0).size());
        for (int i = 0; i < records.get(0).size(); i++) {
            assertEquals(headers.get(i), records.get(0).get(i).getHeaderName());
            assertEquals(records.get(0).get(i).getRaw(), String.valueOf(data.get(0)[i]));
        }
        assertEquals(data.size(), records.size() - 1);
    }

    /**
     * Test printRecord method with multiple records
     */
    @org.junit.Test
    public void testPrintRecordWithMultipleRecords() throws IOException, SQLException {
        CSVFormat format = new CSVFormat(',', COMMA, COMMENT, '"', '\'\'').withCommentingEnabled(true);
        Statement stmt = TestUtils.getConnection("jdbc:derby:memory:test;create=true").createStatement();
        stmt.executeUpdate("CREATE TABLE test (col1 INT, col2 VARCHAR(10))");
        stmt.executeUpdate("INSERT INTO test VALUES (1, 'Test1')");
        stmt.executeUpdate("INSERT INTO test VALUES (2, 'Test2')");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecords(stmt.executeQuery("SELECT * FROM test"));
        printer.close();

        List<CSVRecord> records = CSVFormat.DEFAULT.parse(out.toString().getBytes());
        List<String> headers = Arrays.asList("COL1", "COL2");
        List<Object[]> data = Arrays.asList(new Object[]{1, "Test1"}, new Object[]{2, "Test2"});
        assertEquals(headers.size(), records.get(0).size());
        for (int i = 0; i < records.get(0).size(); i++) {
            assertEquals(headers.get(i), records.get(0).get(i).getHeaderName());
            assertEquals(records.get(0).get(i).getRaw(), String.valueOf(data.get(0)[i]));
        }
        for (int i = 1; i < records.size(); i++) {
            List<String> recordHeaders = Arrays.asList("COL1", "COL2");
            List<Object[]> recordData = Arrays.asList(new Object[]{i + 1, "Test" + (i + 1)});
            for (int j = 0; j < records.get(i).size(); j++) {
                assertEquals(recordHeaders.get(j), records.get(i).get(j).getHeaderName());
                assertEquals(records.get(i).get(j).getRaw(), String.valueOf(recordData.get(0)[j]));
            }
        }
    }
}