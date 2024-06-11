import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    @Test
    public void testPrintRecord_singleValue() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecord("value1");
        csvPrinter.close();
        assertEquals("value1\n", writer.toString());
    }

    @Test
    public void testPrintRecord_multipleValues() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecord("value1", "value2", "value3");
        csvPrinter.close();
        assertEquals("value1,value2,value3\n", writer.toString());
    }

    @Test
    public void testPrintRecord_nullValue() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecord("value1", null, "value3");
        csvPrinter.close();
        assertEquals("value1,,value3\n", writer.toString());
    }

    @Test
    public void testPrintRecords_singleIterable() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        List<String> values = Arrays.asList("value1", "value2", "value3");
        csvPrinter.printRecords(values);
        csvPrinter.close();
        assertEquals("value1,value2,value3\n", writer.toString());
    }

    @Test
    public void testPrintRecords_multipleIterable() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        List<String> values1 = Arrays.asList("value1", "value2", "value3");
        List<String> values2 = Arrays.asList("value4", "value5", "value6");
        csvPrinter.printRecords(values1);
        csvPrinter.printRecords(values2);
        csvPrinter.close();
        assertEquals("value1,value2,value3\nvalue4,value5,value6\n", writer.toString());
    }

    @Test
    public void testPrintRecords_arrayValues() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        Object[] values1 = {"value1", "value2", "value3"};
        Object[] values2 = {"value4", "value5", "value6"};
        csvPrinter.printRecords(values1);
        csvPrinter.printRecords(values2);
        csvPrinter.close();
        assertEquals("value1,value2,value3\nvalue4,value5,value6\n", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printComment("This is a comment");
        csvPrinter.close();
        assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecord_withQuotes() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withQuoteChar('\''));
        csvPrinter.printRecord("value1", "value2", "value3");
        csvPrinter.close();
        assertEquals("'value1','value2','value3'\n", writer.toString());
    }

    @Test
    public void testPrintRecords_emptyList() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        List<String> values = new ArrayList<>();
        csvPrinter.printRecords(values);
        csvPrinter.close();
        assertEquals("\n", writer.toString());
    }

    @Test
    public void testPrintRecords_emptyArray() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        Object[] values = new Object[]{};
        csvPrinter.printRecords(values);
        csvPrinter.close();
        assertEquals("\n", writer.toString());
    }

    @Test
    public void testPrintRecords_resultSet() throws IOException, SQLException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        csvPrinter.printRecords(resultSet);
        csvPrinter.close();
        assertEquals("", writer.toString());
    }
}