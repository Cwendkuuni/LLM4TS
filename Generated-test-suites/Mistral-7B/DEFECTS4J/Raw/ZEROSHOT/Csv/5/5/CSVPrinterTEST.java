import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Quote;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVRecord.builder;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws IOException, SQLException {
        // Test printing to a StringWriter
        CSVPrinter printer = createCSVPrinter(new StringWriter(), DEFAULT);
        printer.print("Hello");
        printer.print(123);
        printer.printRecord(builder().add("World").add(456).build());
        printer.println();
        printer.printComment("This is a comment");
        printer.close();

        String expected = "\"Hello\"123,\"World\",456\n# This is a comment";
        Assert.assertEquals(expected, printer.getOut().toString());

        // Test printing to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer = createCSVPrinter(outputStream, DEFAULT);
        printer.print("Hello");
        printer.print(123);
        printer.printRecord(builder().add("World").add(456).build());
        printer.println();
        printer.printComment("This is a comment");
        printer.close();

        String actual = new String(outputStream.toByteArray());
        Assert.assertEquals(expected, actual);

        // Test printing to a SQL ResultSet
        String url = "jdbc:h2:mem:testdb";
        String query = "SELECT id, name, age FROM test_table";

        try (Connection connection = DriverManager.getConnection(url)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            CSVPrinter printerToResultSet = new CSVPrinter(resultSet, DEFAULT);
            printerToResultSet.printRecords(resultSet);
            printerToResultSet.close();
        }
    }

    private CSVPrinter createCSVPrinter(Appendable out, CSVFormat format) throws IOException {
        if (out instanceof StringWriter) {
            ((StringWriter) out).write("Test output\n");
        }

        return new CSVPrinter(out, format);
    }
}