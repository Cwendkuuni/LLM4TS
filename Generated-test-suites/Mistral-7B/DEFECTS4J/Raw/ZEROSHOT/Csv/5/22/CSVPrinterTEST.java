import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        StringWriter writer = new StringWriter(outputStream);
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';'));
        printer = new CSVPrinter(writer, format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Hello");
        printer.flush();
        assertEquals("Hello", outputStream.toString().trim());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.flush();
        assertEquals("", outputStream.toString().trim());
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        printer.print("Hello");
        printer.print("World");
        printer.println();
        printer.printRecord(Arrays.asList("Testing", "CSVPrinter"));
        printer.flush();

        String expected = "Hello;World\nTesting,CSVPrinter\n";
        assertEquals(expected, outputStream.toString().trim());
    }

    @Test
    public void testPrintMultipleValuesNull() throws IOException {
        printer.print("Hello");
        printer.print("World");
        printer.println();
        printer.printRecord(Arrays.asList("Testing", null, "CSVPrinter"));
        printer.flush();

        String expected = "Hello;World\nTesting,;CSVPrinter\n";
        assertEquals(expected, outputStream.toString().trim());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.println();
        printer.printComment("This is a comment");
        printer.flush();

        String expected = "\n# This is a comment\n";
        assertEquals(expected, outputStream.toString().trim());
    }

    @Test
    public void testPrintRecordIterable() throws IOException, SQLException {
        printer.println();
        List<String> values = new ArrayList<>();
        values.add("John");
        values.add("Doe");
        values.add("age: 35");
        printer.printRecords(values);
        printer.flush();

        String expected = "\nJohn,Doe,age: 35\n";
        assertEquals(expected, outputStream.toString().trim());
    }

    @Test
    public void testPrintRecordObjectArray() throws IOException, SQLException {
        printer.println();
        Object[] values = {"John", "Doe", "age: 35"};
        printer.printRecords(values);
        printer.flush();

        String expected = "\nJohn,Doe,age: 35\n";
        assertEquals(expected, outputStream.toString().trim());
    }

    @Test
    public void testPrintRecordResultSet() throws IOException, SQLException {
        printer.println();
        String query = "SELECT name, age FROM users WHERE id = 1";
        ResultSet resultSet = getResultSet(query);
        printer.printRecords(resultSet);
        printer.flush();

        String expected = "\nname,age\nJohn Doe,35\n";
        assertEquals(expected, outputStream.toString().trim());
    }

    @Test
    public void testQuoting() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';')
                .withQuoteMode(CSVFormat.QUOTE_MINIMAL));
        printer = new CSVPrinter(outputStream, format);
        printer.println();
        printer.print("Hello, World!");
        printer.println();
        printer.print("Hello, World! with quotes \"Hello, World!\"");
        printer.println();
        printer.print("Hello, World! with quotes \"Hello,\" \"World!\"");
        printer.flush();

        String expected = "\nHello, World!\nHello, World! \"Hello, World!\"\nHello, World! \"Hello,\" \"World!\"\n";
        assertEquals(expected, outputStream.toString().trim());
    }

    @Test
    public void testEscaping() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';')
                .withQuoteMode(CSVFormat.QUOTE_ALWAYS)
                .withEscape('\\')
                .withEscapeMode(CSVFormat.ESCAPE_ALL));
        printer = new CSVPrinter(outputStream, format);
        printer.println();
        printer.print("Hello,\"World\"");
        printer.println();
        printer.print("Hello,\"Hello, World!\"");
        printer.println();
        printer.print("Hello,\"Hello,\"World!\"");
        printer.println();
        printer.print("Hello,\"Hello,\"World! with backslashes \\Hello,\\ \"World!\"\\");
        printer.println();
        printer.flush();

        String expected = "\nHello,\"Hello,\"World!\nHello,\"Hello,\"World! with backslashes \\\"Hello,\\\" \"World!\"\\\";\nHello,\"Hello,\"World!\nHello,\"Hello, World!\"\n";
        assertEquals(expected, outputStream.toString().trim());
    }

    @Test
    public void testQuotePolicy() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';')
                .withQuoteMode(CSVFormat.QUOTE_ALL)
                .withNullString("NULL"));
        printer = new CSVPrinter(outputStream, format);
        printer.println();
        printer.print(null);
        printer.println();
        printer.printRecord(Arrays.asList("Hello", "World"));
        printer.println();
        printer.printRecord(Arrays.asList("Hello", "World", null));
        printer.println();
        printer.printRecord(Arrays.asList("Hello", null, "World"));
        printer.println();
        printer.printRecord(Arrays.asList(null, "Hello", "World"));
        printer.println();
        printer.printRecord(Arrays.asList(null, null, "World"));
        printer.println();
        printer.printRecord(Arrays.asList(null, null, null));
        printer.flush();

        String expected = "\nNULL\nHello,World\nHello,NULL,World\nNULL,Hello,World\nNULL,NULL,World\nNULL,NULL,NULL\n";
        assertEquals(expected, outputStream.toString().trim());
    }

    @Test
    public void testQuotePolicyMinimal() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';')
                .withQuoteMode(CSVFormat.QUOTE_MINIMAL)
                .withNullString("NULL"));
        printer = new CSVPrinter(outputStream, format);
        printer.println();
        printer.print(null);
        printer.println();
        printer.printRecord(Arrays.asList("Hello", "World"));
        printer.println();
        printer.printRecord(Arrays.asList("Hello", "World", null));
        printer.println();
        printer.printRecord(Arrays.asList("Hello", null, "World"));
        printer.println();
        printer.printRecord(Arrays.asList(null, "Hello", "World"));
        printer.println();
        printer.printRecord(Arrays.asList(null, null, "World"));
        printer.println();
        printer.printRecord(Arrays.asList(null, null, null));
        printer.flush();

        String expected = "\nNULL\nHello,World\nNULL,Hello,World\nNULL,NULL,World\nNULL,NULL,NULL\n";
        assertEquals(expected, outputStream.toString().trim());
    }

    private ResultSet getResultSet(String query) throws SQLException {
        // Placeholder for database access code
        return new Object[] {};
    }
}