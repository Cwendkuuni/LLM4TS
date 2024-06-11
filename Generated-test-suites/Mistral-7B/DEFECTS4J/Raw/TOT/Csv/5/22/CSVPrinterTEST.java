package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.*;
import static org.apache.commons.csv.CSVPrinter.*;
import static org.apache.commons.csv.Constants.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * This class contains test cases for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    /**
     * Test method for {@link CSVPrinter#CSVPrinter(Appendable, CSVFormat)} with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterWithNullOut() {
        new CSVPrinter(null, new CSVFormat(EXCEL, COMMA, '\n'));
    }

    /**
     * Test method for {@link CSVPrinter#CSVPrinter(Appendable, CSVFormat)} with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterWithNullFormat() {
        new CSVPrinter(new ByteArrayOutputStream(), null);
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)} with null input.
     */
    @Test
    public void testPrintWithNullInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.print(null);
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final CSVRecord record = parser.nextRecord();
        assertEquals(Collections.emptyList(), record.toList());
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)} with empty String input.
     */
    @Test
    public void testPrintWithEmptyStringInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.print("");
        printer.print(null);
        printer.print("");
        printer.println();
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<String> expectedList = Arrays.asList(new String[0], "", "");
        final List<String> actualList = parser.getRecords().get(0).toList();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)} with boolean input.
     */
    @Test
    public void testPrintWithBooleanInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.print(true);
        printer.print(false);
        printer.println();
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<String> expectedList = Arrays.asList("true", "false");
        final List<String> actualList = parser.getRecords().get(0).toList();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)} with int input.
     */
    @Test
    public void testPrintWithIntInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.print(123);
        printer.println();
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<String> expectedList = Arrays.asList("123");
        final List<String> actualList = parser.getRecords().get(0).toList();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)} with long input.
     */
    @Test
    public void testPrintWithLongInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.print(1234567890L);
        printer.println();
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<String> expectedList = Arrays.asList("1234567890");
        final List<String> actualList = parser.getRecords().get(0).toList();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)} with double input.
     */
    @Test
    public void testPrintWithDoubleInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.print(123.45);
        printer.println();
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<String> expectedList = Arrays.asList("123.45");
        final List<String> actualList = parser.getRecords().get(0).toList();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)} with String input.
     */
    @Test
    public void testPrintWithStringInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.print("test string");
        printer.println();
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<String> expectedList = Arrays.asList("test string");
        final List<String> actualList = parser.getRecords().get(0).toList();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)} with char input.
     */
    @Test
    public void testPrintWithCharInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.print('A');
        printer.println();
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<String> expectedList = Arrays.asList("A");
        final List<String> actualList = parser.getRecords().get(0).toList();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Iterable)} with Iterable input.
     */
    @Test
    public void testPrintRecordsWithIterableInput() throws IOException, SQLException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final String url = "jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1";
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));

        try (final Connection connection = DriverManager.getConnection(url)) {
            final Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS test (id INT PRIMARY KEY, name VARCHAR(255))");
            statement.execute("INSERT INTO test VALUES (1, 'John')");
            statement.execute("INSERT INTO test VALUES (2, 'Jane')");

            printer.printRecords(statement.getResultSet());
            printer.close();
        }

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<CSVRecord> expectedList = Arrays.asList(
                CSVRecord.parse("id,name\n" +
                                "1,John\n" +
                                "2,Jane\n"),
                CSVRecord.parse("id,name\n" +
                                "1,John\n" +
                                "2,Jane\n")
        );
        final List<CSVRecord> actualList = parser.getRecords();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Object[])} with Object array input.
     */
    @Test
    public void testPrintRecordsWithObjectArrayInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final String[] data = {"John", "25", "Male"};
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.printRecords(data);
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<CSVRecord> expectedList = Arrays.asList(CSVRecord.parse("Name,Age,Gender\n" +
                                                                          "John,25,Male"));
        final List<CSVRecord> actualList = parser.getRecords();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Iterable)} with Iterable of Object arrays input.
     */
    @Test
    public void testPrintRecordsWithIterableOfObjectArraysInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final String[][] data = {{"John", "25", "Male"}, {"Jane", "30", "Female"}};
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.printRecords(Arrays.asList(data));
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<CSVRecord> expectedList = Arrays.asList(CSVRecord.parse("Name,Age,Gender\n" +
                                                                          "John,25,Male\n" +
                                                                          "Jane,30,Female"));
        final List<CSVRecord> actualList = parser.getRecords();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link CSVPrinter#printComment(String)} with comment string input.
     */
    @Test
    public void testPrintCommentWithStringInput() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.printComment("This is a comment");
        printer.println();
        printer.close();

        final CSVParser parser = new CSVParser(new StringReader(output.toString()), new CSVFormat(EXCEL, COMMA, '\n'));
        final List<CSVRecord> records = parser.getRecords();
        final String comment = records.isEmpty() ? "" : records.get(0).get(0);
        assertEquals("This is a comment", comment);
    }

    /**
     * Test method for {@link CSVPrinter#println()} without previous print statements.
     */
    @Test
    public void testPrintlnWithoutPreviousPrintStatements() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.println();
        printer.close();

        final String expectedOutput = "";
        final String actualOutput = output.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link CSVPrinter#println()} with previous print statements.
     */
    @Test
    public void testPrintlnWithPreviousPrintStatements() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), new CSVFormat(EXCEL, COMMA, '\n'));
        printer.print("John");
        printer.print("Doe");
        printer.println();
        printer.close();

        final String expectedOutput = "John,Doe\n";
        final String actualOutput = output.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}