import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    @Test
    public void testPrintObject() throws IOException {
        Writer writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        String value = "test";
        csvPrinter.print(value);

        csvPrinter.close();

        assertEquals("\"test\"", writer.toString());
    }

    @Test
    public void testPrintNullObject() throws IOException {
        Writer writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        String value = null;
        csvPrinter.print(value);

        csvPrinter.close();

        assertEquals("", writer.toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        Writer writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        CSVPrinter csvPrinter = new CSVPrinter(writer, format);

        String value = "test,value";
        csvPrinter.print(value);

        csvPrinter.close();

        assertEquals("\"test,value\"", writer.toString());
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        Writer writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        CSVPrinter csvPrinter = new CSVPrinter(writer, format);

        String value = "test\\value";
        csvPrinter.print(value);

        csvPrinter.close();

        assertEquals("test\\\\value", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        Writer writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart('#');
        CSVPrinter csvPrinter = new CSVPrinter(writer, format);

        String comment = "This is a comment";
        csvPrinter.printComment(comment);

        csvPrinter.close();

        assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        Writer writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        String[] values = {"value1", "value2", "value3"};
        csvPrinter.printRecord(values);

        csvPrinter.close();

        assertEquals("value1,value2,value3\n", writer.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        Writer writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        String[] values1 = {"value1", "value2", "value3"};
        String[] values2 = {"value4", "value5", "value6"};
        csvPrinter.printRecords(values1, values2);

        csvPrinter.close();

        assertEquals("value1,value2,value3\nvalue4,value5,value6\n", writer.toString());
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        Writer writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        Iterable<String> values1 = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Arrays.asList("value1", "value2", "value3").iterator();
            }
        };

        Iterable<String> values2 = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Arrays.asList("value4", "value5", "value6").iterator();
            }
        };

        csvPrinter.printRecords(values1, values2);

        csvPrinter.close();

        assertEquals("value1,value2,value3\nvalue4,value5,value6\n", writer.toString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException, SQLException {
        Writer writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");

        csvPrinter.printRecords(resultSet);

        csvPrinter.close();

        assertEquals("value1,value2\nvalue1,value2\n", writer.toString());
    }
}