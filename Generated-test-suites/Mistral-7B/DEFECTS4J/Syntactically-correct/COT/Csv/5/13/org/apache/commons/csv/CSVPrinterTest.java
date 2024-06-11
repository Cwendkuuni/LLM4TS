import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        String outputDelimiter = ",";
        String outputEscape = "\\";
        CSVFormat format = new CSVFormat(outputDelimiter, Quote.MINIMAL, outputEscape, null);
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Test String");
        Assert.assertEquals("Test String", outputStream.toString().trim());
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        Assert.assertEquals("", outputStream.toString().trim());
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        printer.print("Test1", "Test2");
        Assert.assertEquals("Test1,Test2", outputStream.toString().trim());
    }

    @Test
    public void testPrintMultipleValuesWithNewLine() throws IOException {
        printer.println();
        printer.print("Test1");
        printer.println();
        printer.print("Test2");
        Assert.assertEquals("Test1\nTest2", outputStream.toString().trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, null, null);
        try {
            new CSVPrinter(new OutputStreamWriter(outputStream), format);
        } catch (IllegalArgumentException expected) {
            // Expected exception
        }
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        CSVFormat format = new CSVFormat('"', Quote.MINIMAL, null, null);
        printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);
        printer.print("Test String with comma, in it");
        Assert.assertEquals("Test String with comma,\\ in it", outputStream.toString().trim());
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, "\\", null);
        printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);
        printer.print("Test String with comma, in it");
        Assert.assertEquals("Test String with comma, in it", outputStream.toString().trim());
        printer.print("Test String with comma, in it");
        printer.print("Test String with comma,\"in quote\", in it");
        printer.println();
        printer.print("Test String with CR in it\r");
        printer.println();
        printer.print("Test String with LF in it\n");
        printer.println();
        printer.print("Test String with Delimiter in it,");
        printer.println();
        printer.print("Test String with Escape in it\\");
        printer.println();
        Assert.assertEquals("Test String with comma,\"in quote\", in it\rTest String with CR in it\nTest String with LF in it\nTest String with Delimiter in it,\"\rTest String with Escape in it\\", outputStream.toString().trim());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("# This is a comment");
        Assert.assertEquals("# This is a comment\n", outputStream.toString().trim());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        String[] values = {"Test1", "Test2"};
        printer.printRecords(Arrays.asList(values));
        Assert.assertEquals("Test1,Test2\n", outputStream.toString().trim());

        List<String> valuesList = Collections.singletonList("Test3");
        printer.printRecords(valuesList);
        Assert.assertEquals("Test3\n", outputStream.toString().trim());

        Iterable<String> valuesIterable = Arrays.asList("Test4", "Test5").iterator();
        printer.printRecords(valuesIterable);
        Assert.assertEquals("Test4,Test5\n", outputStream.toString().trim());

        printer.printComment("# This is a comment");
        printer.println();
        printer.printRecord(values);
        Assert.assertEquals("Test1,Test2\n# This is a comment\n", outputStream.toString().trim());

        printer.printComment("# This is a comment");
        printer.println();
        printer.printRecords(valuesList);
        Assert.assertEquals("Test3\n# This is a comment\n", outputStream.toString().trim());

        printer.printComment("# This is a comment");
        printer.println();
        printer.printRecords(valuesIterable);
        Assert.assertEquals("Test4,Test5\n# This is a comment\n", outputStream.toString().trim());

        ResultSet resultSet = MockResultSet.newMockResultSet();
        printer.printRecords(resultSet);
        Assert.assertEquals("Test1,Test2,Test3\nTest4,Test5,Test6\nTest7,Test8,Test9\n", outputStream.toString().trim());
    }

    @Test
    public void testPrintRecordsWithQuotes() throws IOException, SQLException {
        CSVFormat format = new CSVFormat('"', Quote.ALL, null, null);
        printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);
        String[] values = {"Test1", "Test2"};
        printer.printRecords(Arrays.asList(values));
        Assert.assertEquals("\"Test1\",\"Test2\"\n", outputStream.toString().trim());

        List<String> valuesList = Collections.singletonList("Test3");
        printer.printRecords(valuesList);
        Assert.assertEquals("\"Test3\"\n", outputStream.toString().trim());

        Iterable<String> valuesIterable = Arrays.asList("Test4", "Test5").iterator();
        printer.printRecords(valuesIterable);
        Assert.assertEquals("\"Test4\",\"Test5\"\n", outputStream.toString().trim());

        ResultSet resultSet = MockResultSet.newMockResultSet();
        printer.printRecords(resultSet);
        Assert.assertEquals("\"Test1\",\"Test2\",\"Test3\"\n\"Test4\",\"Test5\",\"Test6\"\n\"Test7\",\"Test8\",\"Test9\"\n", outputStream.toString().trim());
    }

    @Test
    public void testClose() throws IOException {
        printer.print("Test1");
        printer.print("Test2");
        printer.println();
        printer.close();
        Assert.assertEquals("Test1,Test2\n", outputStream.toString().trim());
    }

    @Test
    public void testFlush() throws IOException {
        printer.print("Test1");
        printer.print("Test2");
        printer.println();
        printer.flush();
        Assert.assertEquals("Test1,Test2\n", outputStream.toString().trim());
    }
}

class MockResultSet implements Iterator<Object>, Iterable<Object> {

    private boolean nextCalled = false;
    private int index = 0;
    private List<Object> values;

    static MockResultSet newMockResultSet() {
        return new MockResultSet();
    }

    private MockResultSet() {
        values = Arrays.asList("Test1", "Test2", "Test3");
    }

    @Override
    public boolean hasNext() {
        return index < values.size();
    }

    @Override
    public Object next() {
        if (!nextCalled) {
            nextCalled = true;
            return values.get(index++);
        }
        return null;
    }

    @Override
    public Iterator<Object> iterator() {
        return this;
    }

    @Override
    public int size() {
        return values.size();
    }
}