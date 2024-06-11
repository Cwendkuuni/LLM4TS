import org.apache.commons.csv.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CSVPrinterTest {

    private CSVParser parser;
    private ByteArrayOutputStream outputStream;
    private CSVPrinter printer;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(outputStream), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws Exception {
        printer.close();
        parser = null;
    }

    @Test
    public void testNewRecordIsTrueInitially() throws IOException {
        assertEquals(true, printer.newRecord);
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.printNull();
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record = parser.getRecord();
        assertNull(record.get(0));
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        printer.print("");
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record = parser.getRecord();
        assertEquals("", record.get(0));
    }

    @Test
    public void testPrintSingleQuotedString() throws IOException {
        printer.print("'Hello'");
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record = parser.getRecord();
        assertEquals("Hello", record.get(0));
    }

    @Test
    public void testPrintDoubleQuotedString() throws IOException {
        printer.print("\"Hello\"");
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record = parser.getRecord();
        assertEquals("Hello", record.get(0));
    }

    @Test
    public void testPrintEscapedString() throws IOException {
        printer.print("\"Hello\\, World\\nThis is a comment.\"");
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record = parser.getRecord();
        assertEquals("Hello, World\nThis is a comment.", record.get(0));
    }

    @Test
    public void testPrintMultipleValuesWithNewRecord() throws IOException {
        printer.print("First");
        printer.println();
        printer.print("Second");
        printer.println();
        printer.print("Third");
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record1 = parser.getRecord();
        assertEquals("First", record1.get(0));
        CSVRecord record2 = parser.getRecord();
        assertEquals("Second", record2.get(0));
        CSVRecord record3 = parser.getRecord();
        assertEquals("Third", record3.get(0));
    }

    @Test
    public void testPrintMultipleValuesWithoutNewRecord() throws IOException {
        printer.print("First");
        printer.print(CSVFormat.DEFAULT.getDelimiter());
        printer.print("Second");
        printer.println();
        printer.print("Third");
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record1 = parser.getRecord();
        assertEquals("First", record1.get(0));
        CSVRecord record2 = parser.getRecord();
        assertEquals("Second", record2.get(0));
        CSVRecord record3 = parser.getRecord();
        assertEquals("Third", record3.get(0));
    }

    @Test
    public void testPrintComment() throws IOException, SQLException {
        printer.printComment("This is a comment.");
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record = parser.getRecord();
        assertNull(record);
        parser.nextRecord();
        record = parser.getRecord();
        assertEquals("", record.get(0));
        assertEquals("This is a comment.", parser.getComment());
    }

    @Test
    public void testPrintRecord() throws IOException {
        Object[] values = {"First", "Second", "Third"};
        printer.printRecord(values);
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record = parser.getRecord();
        assertEquals("First", record.get(0));
        assertEquals("Second", record.get(1));
        assertEquals("Third", record.get(2));
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        Object[][] values = {{"First1", "Second1", "Third1"}, {"First2", "Second2", "Third2"}};
        printer.printRecords(Arrays.asList(values));
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record1 = parser.getRecord();
        assertEquals("First1", record1.get(0));
        assertEquals("Second1", record1.get(1));
        assertEquals("Third1", record1.get(2));

        CSVRecord record2 = parser.getRecord();
        assertEquals("First2", record2.get(0));
        assertEquals("Second2", record2.get(1));
        assertEquals("Third2", record2.get(2));
    }

    @Test
    public void testPrintRecordsIterable() throws IOException, SQLException {
        Object[][] values = {{"First1", "Second1", "Third1"}, {"First2", "Second2", "Third2"}};
        printer.printRecords(Collections.singleton(values));
        parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
        CSVRecord record1 = parser.getRecord();
        assertEquals("First1", record1.get(0));
        assertEquals("Second1", record1.get(1));
        assertEquals("Third1", record1.get(2));

        CSVRecord record2 = parser.getRecord();
        assertEquals("First2", record2.get(0));
        assertEquals("Second2", record2.get(1));
        assertEquals("Third2", record2.get(2));
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
        String query = "SELECT id, name, age FROM users WHERE id = 1";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            printer.printRecords(resultSet);
            parser = new CSVParser(new StringReader(outputStream.toString()), CSVFormat.DEFAULT);
            CSVRecord record = parser.getRecord();
            assertEquals("1", record.get(0));
            assertEquals("Alice", record.get(1));
            assertEquals("30", record.get(2));

            record = parser.getRecord();
            assertEquals("2", record.get(0));
            assertEquals("Bob", record.get(1));
            assertEquals("25", record.get(2));
        }
    }
}