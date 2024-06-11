import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private StringWriter writer;

    @Before
    public void setUp() {
        writer = new StringWriter();
        printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint_NullValue() throws IOException {
        printer.print(null);
        printer.flush();
        assertEquals("", writer.toString());
    }

    @Test
    public void testPrint_StringValue() throws IOException {
        printer.print("Hello");
        printer.flush();
        assertEquals("Hello", writer.toString());
    }

    @Test
    public void testPrint_IntValue() throws IOException {
        printer.print(123);
        printer.flush();
        assertEquals("123", writer.toString());
    }

    @Test
    public void testPrint_FloatValue() throws IOException {
        printer.print(1.23);
        printer.flush();
        assertEquals("1.23", writer.toString());
    }

    @Test
    public void testPrint_ObjectArray() throws IOException {
        Object[] values = {"Hello", 123, 1.23};
        printer.printRecord(values);
        printer.flush();
        assertEquals("Hello,123,1.23", writer.toString());
    }

    @Test
    public void testPrint_IterableObject() throws IOException {
        List<Object> values = new ArrayList<>();
        values.add("Hello");
        values.add(123);
        values.add(1.23);
        printer.printRecord(values);
        printer.flush();
        assertEquals("Hello,123,1.23", writer.toString());
    }

    @Test
    public void testPrint_Comment() throws IOException {
        printer.printComment("This is a comment");
        printer.flush();
        assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrint_RecordSeparator() throws IOException {
        printer.println();
        printer.flush();
        assertEquals("\n", writer.toString());
    }

    @Test
    public void testPrint_RecordsFromIterable() throws IOException {
        List<String> record1 = Arrays.asList("Hello", "World");
        List<String> record2 = Arrays.asList("Foo", "Bar");
        List<List<String>> records = Arrays.asList(record1, record2);
        printer.printRecords(records);
        printer.flush();
        assertEquals("Hello,World\nFoo,Bar\n", writer.toString());
    }

    @Test
    public void testPrint_RecordsFromArray() throws IOException {
        Object[] record1 = {"Hello", "World"};
        Object[] record2 = {"Foo", "Bar"};
        Object[][] records = {record1, record2};
        printer.printRecords(records);
        printer.flush();
        assertEquals("Hello,World\nFoo,Bar\n", writer.toString());
    }

    @Test
    public void testPrint_RecordsFromResultSet() throws IOException, SQLException {

        ResultSet resultSet = new MockResultSet(new String[]{"Name", "Age"}, new String[][]{{"John", "30"}, {"Jane", "25"}});

        printer.printRecords(resultSet);
        printer.flush();

        String expectedOutput = "John,30\nJane,25\n";
        assertEquals(expectedOutput, writer.toString());
    }

    // Additional test cases for edge cases and error scenarios can be added here

}