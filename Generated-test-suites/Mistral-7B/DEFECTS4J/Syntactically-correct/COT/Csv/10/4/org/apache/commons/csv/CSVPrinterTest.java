package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Quote;
import org.junit.Test;

/**
 * Tests for CSVPrinter class.
 */
public class CSVPrinterTest {

    @Test
    public void testCSVPrinterBasicFunctionality() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, false);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("Test");
        printer.printRecord(new String[]{"FirstName", "LastName"});
        printer.printRecord(new String[]{"John", "Doe"});
        printer.printComment("This is a test comment.");
        printer.println();
        printer.printRecord(new String[]{"1", "Two", "Three"});
        printer.printRecord(new String[]{"Four", "Five", "Six"});
        printer.close();

        String expectedCSV = "Test," + CR +
                "FirstName,LastName" + CR +
                "John,Doe" + CR +
                "This is a test comment." + CR +
                "1,Two,Three" + CR +
                "Four,Five,Six" + CR;

        assertEquals(expectedCSV, outStream.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterNullFormat() throws IOException {
        new CSVPrinter(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterNullOut() throws IOException {
        new CSVPrinter(null, new CSVFormat(','));
    }

    @Test
    public void testCSVPrinterPrintNullValue() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, false);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print(null);
        printer.printRecord(new String[]{"", ""});
        printer.printComment("This is a test comment.");
        printer.println();
        printer.printRecord(new String[]{"", "", ""});
        printer.close();

        String expectedCSV = Constants.EMPTY + "," + CR +
                Constants.EMPTY + Constants.EMPTY + CR +
                "This is a test comment." + CR +
                Constants.EMPTY + Constants.EMPTY + Constants.EMPTY + CR;

        assertEquals(expectedCSV, outStream.toString());
    }

    @Test
    public void testCSVPrinterPrintEmptyString() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, false);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecord(new String[]{"", ""});
        printer.println();
        printer.printRecord(new String[]{"", "", ""});
        printer.close();

        String expectedCSV = Constants.EMPTY + "," + CR +
                Constants.EMPTY + Constants.EMPTY + CR +
                Constants.EMPTY + Constants.EMPTY + Constants.EMPTY + CR;

        assertEquals(expectedCSV, outStream.toString());
    }

    @Test
    public void testCSVPrinterPrintQuotedValue() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, true);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecord(new String[]{"Test, \"Quoted Value\""});
        printer.println();
        printer.printRecord(new String[]{"", ""});
        printer.close();

        String expectedCSV = "Test,\"Quoted Value\"\n" +
                Constants.EMPTY + Constants.EMPTY + CR;

        assertEquals(expectedCSV, outStream.toString());
    }

    @Test
    public void testCSVPrinterPrintEscapedValue() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, true);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecord(new String[]{"Test, \"Test, \\\"Test\\\", Test\""});
        printer.println();
        printer.printRecord(new String[]{"", ""});
        printer.close();

        String expectedCSV = "Test,\"Test,\\\"Test\\\", Test\"\n" +
                Constants.EMPTY + Constants.EMPTY + CR;

        assertEquals(expectedCSV, outStream.toString());
    }

    @Test
    public void testCSVPrinterPrintComment() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, false);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printComment("This is a test comment.");
        printer.println();
        printer.printRecord(new String[]{"Test"});
        printer.close();

        String expectedCSV = "This is a test comment.\nTest" + CR;

        assertEquals(expectedCSV, outStream.toString());
    }

    @Test
    public void testCSVPrinterPrintRecords() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, false);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecords(new String[][]{{"Test 1", "Test 2"}, {"Test 3", "Test 4"}});
        printer.println();
        printer.close();

        String expectedCSV = "Test 1,Test 2\nTest 3,Test 4\n";

        assertEquals(expectedCSV, outStream.toString());
    }

    @Test
    public void testCSVPrinterPrintRecordsWithQuotes() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, true);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecords(new String[][]{{"Test 1, \"Test 2\""}, {"Test 3, \"Test 4\""}});
        printer.println();
        printer.close();

        String expectedCSV = "Test 1,\"Test 2\"\nTest 3,\"Test 4\"\n";

        assertEquals(expectedCSV, outStream.toString());
    }

    @Test
    public void testCSVPrinterPrintRecordsWithEscapes() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, true);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecords(new String[][]{{"Test 1, \"Test, \\\"Test\\\", Test\""}, {"Test 3, \"Test, \\\"Test\\\", Test\""}});
        printer.println();
        printer.close();

        String expectedCSV = "Test 1,\"Test,\\\"Test\\\", Test\"\nTest 3,\"Test,\\\"Test\\\", Test\"\n";

        assertEquals(expectedCSV, outStream.toString());
    }

    @Test
    public void testCSVPrinterPrintRecordsFromResultSet() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, false);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        String query = "SELECT * FROM test";
        DatabaseUtils.openConnection("jdbc:hsqldb:mem:testdb;shutdown=true", "sa", "sa");
        try (Connection connection = DriverManager.getConnection(query);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("CREATE TABLE test (col1 varchar(50), col2 varchar(50))")) {

            printer.printRecords(resultSet);
            printer.println();
            printer.close();

        } finally {
            DatabaseUtils.shutdown();
        }

        String expectedCSV = "COL1,COL2\nTest 1,Test 2\nTest 3,Test 4\nTest 5,Test 6\nTest 7,Test 8\n";

        assertEquals(expectedCSV, outStream.toString());
    }
}