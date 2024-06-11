import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private StringWriter stringWriter;
    private CSVFormat csvFormat;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        csvFormat = CSVFormat.DEFAULT;
        csvPrinter = new CSVPrinter(stringWriter, csvFormat);
    }

    @Test
    public void testPrintObject() throws IOException {
        csvPrinter.print("value1");
        csvPrinter.print("value2");
        csvPrinter.print("value3");
        csvPrinter.flush();
        assertEquals("value1,value2,value3\n", stringWriter.toString());
    }

    @Test
    public void testPrintNullObject() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals("\n", stringWriter.toString());
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        csvPrinter.print("value,with,comma");
        csvPrinter.flush();
        assertEquals("\"value,with,comma\"\n", stringWriter.toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        csvPrinter.print("value \"with\" quotes");
        csvPrinter.flush();
        assertEquals("\"value \"\"with\"\" quotes\"\n", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("# This is a comment\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("value1", "value2", "value3");
        csvPrinter.flush();
        assertEquals("value1,value2,value3\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        Object[] record1 = {"value1", "value2", "value3"};
        Object[] record2 = {"value4", "value5", "value6"};
        Object[] records = {record1, record2};
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        assertEquals("value1,value2,value3\nvalue4,value5,value6\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        Iterable<Object> record1 = new Record("value1", "value2", "value3");
        Iterable<Object> record2 = new Record("value4", "value5", "value6");
        csvPrinter.printRecords(record1);
        csvPrinter.printRecords(record2);
        csvPrinter.flush();
        assertEquals("value1,value2,value3\nvalue4,value5,value6\n", stringWriter.toString());
    }

    private static class Record implements Iterable<Object> {
        private Object[] values;

        public Record(Object... values) {
            this.values = values;
        }

        @Override
        public Iterator<Object> iterator() {
            return Arrays.asList(values).iterator();
        }
    }
}