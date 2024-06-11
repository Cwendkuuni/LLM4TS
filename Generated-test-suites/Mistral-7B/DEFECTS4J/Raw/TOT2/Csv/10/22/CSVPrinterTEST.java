package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

/**
 * Test cases for CSVPrinter class.
 */
public class CSVPrinterTest {

    /**
     * Test CSVPrinter with basic usage.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testBasicUsage() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        csvPrinter.print("Name");
        csvPrinter.print("Age");
        csvPrinter.print("City");
        csvPrinter.println();

        csvPrinter.print("Alice");
        csvPrinter.print(25);
        csvPrinter.print("New York");
        csvPrinter.println();

        csvPrinter.print("Bob");
        csvPrinter.print(30);
        csvPrinter.print("Chicago");
        csvPrinter.println();

        csvPrinter.close();

        List<CSVRecord> records = CSVParser.parse(outStream.toString(), CSVFormat.DEFAULT);
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);

        assertEquals("Name,Age,City", record1.getRecordNumber(), 0);
        assertEquals("Alice", record1.get(0));
        assertEquals(Integer.class, record1.get(1).getClass());
        assertEquals(25, record1.get(1));
        assertEquals("City", record1.get(2));

        assertEquals("Name,Age,City", record2.getRecordNumber(), 1);
        assertEquals("Bob", record2.get(0));
        assertEquals(Integer.class, record2.get(1).getClass());
        assertEquals(30, record2.get(1));
        assertEquals("City", record2.get(2));

        assertEquals("Name,Age,City", record3.getRecordNumber(), 2);
        assertEquals("City", record3.get(0));
        // no value for City in record3

        assertEquals(3, records.size());
    }

    /**
     * Test CSVPrinter with quoting.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testQuoting() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outStream),
                CSVFormat.builder().setQuoteChar('"').build());
        String value = "O'Reilly";

        csvPrinter.print("Name");
        csvPrinter.print(value);
        csvPrinter.println();

        csvPrinter.close();

        List<CSVRecord> records = CSVParser.parse(outStream.toString(), CSVFormat.builder().setQuoteChar('"')
                .build());
        CSVRecord record = records.get(0);

        assertEquals("Name,Name", record.getRecordNumber(), 0);
        assertEquals(value, record.get(0));
    }

    /**
     * Test CSVPrinter with escaping.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testEscaping() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outStream),
                CSVFormat.builder().setEscapeChar('\\').build());
        String value = "ColouR";

        csvPrinter.print("Name");
        csvPrinter.print(value);
        csvPrinter.println();

        csvPrinter.close();

        List<CSVRecord> records = CSVParser.parse(outStream.toString(), CSVFormat.builder().setEscapeChar('\\')
                .build());
        CSVRecord record = records.get(0);

        assertEquals("Name,Name", record.getRecordNumber(), 0);
        assertEquals(value, record.get(0));
    }

    /**
     * Test CSVPrinter with quotes and escapes.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testQuotesAndEscapes() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outStream),
                CSVFormat.builder().setQuoteChar('"').setEscapeChar('\\').build());
        String value = "ColouR\"Quoted\"";

        csvPrinter.print("Name");
        csvPrinter.print(value);
        csvPrinter.println();

        csvPrinter.close();

        List<CSVRecord> records = CSVParser.parse(outStream.toString(), CSVFormat.builder().setQuoteChar('"')
                .setEscapeChar('\\').build());
        CSVRecord record = records.get(0);

        assertEquals("Name,Name", record.getRecordNumber(), 0);
        assertEquals(value, record.get(0));
    }

    /**
     * Test CSVPrinter with comments.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testComments() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT.withCommentor(';'));

        csvPrinter.printRecord(Arrays.asList("Name;", "Age;", "City;"));
        csvPrinter.println();

        csvPrinter.printRecord(Arrays.asList("Alice;;", "25;", "New York;"));
        csvPrinter.println();

        csvPrinter.printComment("This is a comment;");
        csvPrinter.println();

        csvPrinter.printRecord(Arrays.asList("Bob;;", "30;", "Chicago;"));
        csvPrinter.println();

        csvPrinter.close();

        List<CSVRecord> records = CSVParser.parse(outStream.toString(), CSVFormat.DEFAULT.withCommentor(';'));
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);

        assertEquals("Name;,Age;,City;", record1.getRecordNumber(), 0);
        assertEquals("Alice;;", record1.get(0));
        assertEquals(Integer.class, record1.get(1).getClass());
        assertEquals(25, record1.get(1));
        assertEquals("City;", record1.get(2));

        assertEquals("Name;,Age;,City;", record2.getRecordNumber(), 1);
        assertEquals("Bob;;", record2.get(0));
        assertEquals(Integer.class, record2.get(1).getClass());
        assertEquals(30, record2.get(1));
        assertEquals("City;", record2.get(2));

        assertEquals("Name;,Age;,City;", record3.getRecordNumber(), 2);
        assertEquals("This is a comment;", record3.get(0));
        // no values for record3

        assertEquals(3, records.size());
    }

    /**
     * Test CSVPrinter with empty record.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testEmptyRecord() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        csvPrinter.printRecord(Collections.emptyList());
        csvPrinter.println();

        csvPrinter.close();

        List<CSVRecord> records = CSVParser.parse(outStream.toString(), CSVFormat.DEFAULT);

        CSVRecord record = records.get(0);
        assertEquals("", record.getRecordNumber(), 0);
        assertEquals(Collections.emptyList(), record.toArray());

        assertEquals(1, records.size());
    }

    /**
     * Test CSVPrinter with null values.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testNullValues() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
        csvPrinter.printRecord(Arrays.asList(null, null, null));
        csvPrinter.println();

        csvPrinter.close();

        List<CSVRecord> records = CSVParser.parse(outStream.toString(), CSVFormat.DEFAULT);

        CSVRecord record = records.get(0);
        assertEquals("", record.getRecordNumber(), 0);
        assertEquals(Collections.nEmptyList(), record.toArray());

        assertEquals(1, records.size());
    }

    /**
     * Test CSVPrinter with SQL ResultSet.
     *
     * @throws IOException
     *             If an I/O error occurs
     * @throws SQLException
     *             If a database access error occurs
     */
    @org.junit.Test
    public void testCSVPrinterWithSQLResultSet() throws SQLException, IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        String sql = "SELECT id, name, city FROM users";

        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "sa")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outStream), CSVFormat.DEFAULT);
            csvPrinter.printRecords(resultSet);
            csvPrinter.close();

            List<CSVRecord> records = CSVParser.parse(outStream.toString(), CSVFormat.DEFAULT);

            // Assuming that SQL query returns records with "id", "name" and "city" fields
            CSVRecord record1 = records.get(0);
            CSVRecord record2 = records.get(1);

            assertEquals("id,name,city", record1.getRecordNumber(), 0);
            assertEquals(record1.get(0), 1);
            assertEquals(record1.get(1), "Alice");
            assertEquals(record1.get(2), "New York");

            assertEquals("id,name,city", record2.getRecordNumber(), 1);
            assertEquals(record2.get(0), 2);
            assertEquals(record2.get(1), "Bob");
            assertEquals(record2.get(2), "Chicago");

            assertEquals(2, records.size());
        }
    }

    /**
     * Test CSVPrinter with null CSVFormat.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullCSVFormat() {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        new CSVPrinter(new OutputStreamWriter(outStream), null);
    }

    /**
     * Test CSVPrinter with null Appendable.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullAppendable() {
        new CSVPrinter(null, CSVFormat.DEFAULT);
    }
}