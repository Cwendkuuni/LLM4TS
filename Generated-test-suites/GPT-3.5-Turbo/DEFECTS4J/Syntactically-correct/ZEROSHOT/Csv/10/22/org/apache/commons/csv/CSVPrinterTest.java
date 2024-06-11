import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        // Test with null value
        csvPrinter.print(null);
        assertEquals("", out.toString());

        // Test with regular value
        csvPrinter.print("Hello");
        assertEquals("Hello", out.toString());

        // Test with number value
        csvPrinter.print(123);
        assertEquals("Hello123", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        csvPrinter.printComment("This is a comment");
        assertEquals("### This is a comment", out.toString());
    }

    @Test
    public void testPrintRecordIterable() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        List<String> values = Arrays.asList("Hello", "World");
        csvPrinter.printRecord(values);
        assertEquals("Hello,World", out.toString());
    }

    @Test
    public void testPrintRecordArray() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        String[] values = {"Hello", "World"};
        csvPrinter.printRecord(values);
        assertEquals("Hello,World", out.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        List<String[]> records = Arrays.asList(
            new String[]{"Hello", "World"},
            new String[]{"Foo", "Bar"}
        );
        csvPrinter.printRecords(records);
        assertEquals("Hello,World\nFoo,Bar\n", out.toString());
    }

    @Test
    public void testPrintRecordsArray() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        String[][] records = {
            {"Hello", "World"},
            {"Foo", "Bar"}
        };
        csvPrinter.printRecords(records);
        assertEquals("Hello,World\nFoo,Bar\n", out.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        ResultSet resultSet = // create a mock ResultSet
        csvPrinter.printRecords(resultSet);
        // assert the output
    }
}