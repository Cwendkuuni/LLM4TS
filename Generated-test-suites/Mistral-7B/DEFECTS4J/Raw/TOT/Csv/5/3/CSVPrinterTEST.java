package org.apache.commons.csv.test;

import org.apache.commons.csv.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream out;
    private CSVPrinter printer;

    @Before
    public void setUp() throws SQLException {
        out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormatBuilder()
                .setHeader("header1", "header2")
                .build();
        printer = new CSVPrinter(new OutputStreamWriter(out), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    /**
     * Test print method with String value.
     */
    @Test
    public void testPrintString() throws IOException {
        printer.print("value1");
        printer.print(',');
        printer.print("value2");
        printer.println();

        CSVParser parser = CSVParser.parse(new StringReader(out.toString()));
        List<String[]> records = parser.getRecords();
        assertEquals(1, records.size());
        String[] record = records.get(0);
        assertEquals("value1", record[0]);
        assertEquals("value2", record[1]);
    }

    /**
     * Test print method with null value.
     */
    @Test
    public void testPrintNull() throws IOException {
        printer.print("value1");
        printer.print(',');
        printer.print(null);
        printer.println();

        CSVParser parser = CSVParser.parse(new StringReader(out.toString()));
        List<String[]> records = parser.getRecords();
        assertEquals(1, records.size());
        String[] record = records.get(0);
        assertNull(record[1]);
    }

    /**
     * Test print method with Object value.
     */
    @Test
    public void testPrintObject() throws IOException, SQLException {
        Statement stmt = TestUtil.createTestDatabase().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name, age FROM users");

        printer.print("name");
        printer.print(',');
        printer.print("age");
        printer.println();

        while (rs.next()) {
            printer.print(rs.getString("name"));
            printer.print(',');
            printer.print(rs.getInt("age"));
            printer.println();
        }

        rs.close();
        stmt.close();

        CSVParser parser = CSVParser.parse(new StringReader(out.toString()));
        List<String[]> records = parser.getRecords();
        assertEquals(4, records.size());
        String[] record1 = records.get(0);
        assertEquals("Alice", record1[0]);
        assertEquals("25", record1[1]);
        String[] record2 = records.get(1);
        assertEquals("Bob", record2[0]);
        assertEquals("30", record2[1]);
        String[] record3 = records.get(2);
        assertEquals("Charlie", record3[0]);
        assertEquals("35", record3[1]);
        String[] record4 = records.get(3);
        assertNull(record4);
    }

    /**
     * Test printComment method.
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment.");
        printer.println();

        CSVParser parser = CSVParser.parse(new StringReader(out.toString()));
        List<String[]> records = parser.getRecords();
        assertEquals(1, records.size());
        String[] record = records.get(0);
        assertTrue(record[0].startsWith("#"));
        assertEquals("This is a comment.", record[0].substring(1));
    }

    /**
     * Test printRecord method with Iterable value.
     */
    @Test
    public void testPrintRecordIterable() throws IOException {
        List<Object> values = Arrays.asList("value1", "value2", null);
        printer.printRecord(values);

        CSVParser parser = CSVParser.parse(new StringReader(out.toString()));
        List<String[]> records = parser.getRecords();
        assertEquals(1, records.size());
        String[] record = records.get(0);
        assertEquals("value1", record[0]);
        assertNull(record[1]);
        assertEquals("value2", record[2]);
    }

    /**
     * Test printRecord method with Object array value.
     */
    @Test
    public void testPrintRecordObjectArray() throws IOException {
        Object[] values = new Object[]{
                "value1", "value2", null
        };
        printer.printRecord(values);

        CSVParser parser = CSVParser.parse(new StringReader(out.toString()));
        List<String[]> records = parser.getRecords();
        assertEquals(1, records.size());
        String[] record = records.get(0);
        assertEquals("value1", record[0]);
        assertNull(record[1]);
        assertEquals("value2", record[2]);
    }

    /**
     * Test printRecords method with Iterable value.
     */
    @Test
    public void testPrintRecordsIterable() throws IOException, SQLException {
        Statement stmt = TestUtil.createTestDatabase().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name, age FROM users");

        printer.printRecords(rs);

        CSVParser parser = CSVParser.parse(new StringReader(out.toString()));
        List<String[]> records = parser.getRecords();
        assertEquals(4, records.size());
        String[] record1 = records.get(0);
        assertEquals("Alice", record1[0]);
        assertEquals("25", record1[1]);
        String[] record2 = records.get(1);
        assertEquals("Bob", record2[0]);
        assertEquals("30", record2[1]);
        String[] record3 = records.get(2);
        assertEquals("Charlie", record3[0]);
        assertEquals("35", record3[1]);
        String[] record4 = records.get(3);
        assertNull(record4);

        rs.close();
        stmt.close();
    }

    /**
     * Test printRecords method with Object array value.
     */
    @Test
    public void testPrintRecordsObjectArray() throws IOException, SQLException {
        Statement stmt = TestUtil.createTestDatabase().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name, age FROM users");

        Object[][] values = {{"Alice", 25}, {"Bob", 30}, {"Charlie", 35}, {null, null}};
        printer.printRecords(values);

        CSVParser parser = CSVParser.parse(new StringReader(out.toString()));
        List<String[]> records = parser.getRecords();
        assertEquals(4, records.size());
        String[] record1 = records.get(0);
        assertEquals("Alice", record1[0]);
        assertEquals("25", record1[1]);
        String[] record2 = records.get(1);
        assertEquals("Bob", record2[0]);
        assertEquals("30", record2[1]);
        String[] record3 = records.get(2);
        assertEquals("Charlie", record3[0]);
        assertEquals("35", record3[1]);
        String[] record4 = records.get(3);
        assertNull(record4);

        rs.close();
        stmt.close();
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        printer.close();
        assertEquals("", out.toString());
    }
}