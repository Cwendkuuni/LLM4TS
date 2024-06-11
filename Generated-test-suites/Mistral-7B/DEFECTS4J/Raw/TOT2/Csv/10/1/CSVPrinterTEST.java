import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws IOException {
        // Test constructor
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        // Test print method with and without quoting and escaping
        printer.print("Hello, World!");
        printer.print(123);
        printer.print("\"Hello, World!\"");
        printer.print('"');
        printer.print("\"Hello, World!\",123");
        printer.print("\"Hello, World!\",\"\"");
        printer.print("\"Hello, World!\",\"Hello, World!\"");
        printer.print("\"Hello, World!\",\"Hello, World!\",123");
        printer.print("\"Hello, World!\",\"Hello, World!\",\"\"");
        printer.print("\"Hello, World!\",\"Hello, World!\",\"Hello, World!\"");
        printer.print("\"Hello, World!\",\"Hello, World!\",\"Hello, World!\"");
        printer.print("\"Hello, World!\",\"Hello, World!\",\"Hello, World!\"");

        // Test println method
        printer.println("Hello, World!");
        printer.println(123);
        printer.println("\"Hello, World!\"");
        printer.println('"');
        printer.println("\"Hello, World!\",123");
        printer.println("\"Hello, World!\",\"\"");
        printer.println("\"Hello, World!\",\"Hello, World!\"");
        printer.println("\"Hello, World!\",\"Hello, World!\",123");
        printer.println("\"Hello, World!\",\"Hello, World!\",\"\"");
        printer.println("\"Hello, World!\",\"Hello, World!\",\"Hello, World!\"");
        printer.println("\"Hello, World!\",\"Hello, World!\",\"Hello, World!\"");
        printer.println("\"Hello, World!\",\"Hello, World!\",\"Hello, World!\"");

        // Test printComment method
        printer.printRecord(Collections.singletonList("# Comment"));
        printer.println();

        // Test println method after printComment method
        printer.printComment("Comment");
        printer.println();

        // Test printRecords method with Iterable
        final List<String> list = Arrays.asList("1", "2", "3");
        printer.printRecords(list);

        // Test printRecords method with Object[]
        final String[] array = new String[]{"1", "2", "3"};
        printer.printRecords(array);

        // Test printRecords method with ResultSet
        final ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        final OutputStreamWriter writer2 = new OutputStreamWriter(out2);
        final CSVPrinter printer2 = new CSVPrinter(writer2, CSVFormat.DEFAULT);
        final ResultSet resultSet = TestHelper.getResultSet();
        printer2.printRecords(resultSet);

        // Test flush method
        printer.flush();

        // Test close method
        printer.close();

        // Verify output
        final String expected = "Hello, World!\r\n123\r\n\"Hello, World!\"\r\n\"\"\r\n\"Hello, World!\",123\r\n\"Hello, World!\",\"\"\r\n\"Hello, World!\",\"Hello, World!\"\r\n\"Hello, World!\",\"Hello, World!\",123\r\n\"Hello, World!\",\"Hello, World!\",\"\"\r\n\"Hello, World!\",\"Hello, World!\",\"Hello, World!\"\r\n\"Hello, World!\",\"Hello, World!\",\"Hello, World!\"\r\n# Comment\r\n";
        Assert.assertEquals(expected, out.toString());
        Assert.assertEquals(expected, out2.toString());
    }
}