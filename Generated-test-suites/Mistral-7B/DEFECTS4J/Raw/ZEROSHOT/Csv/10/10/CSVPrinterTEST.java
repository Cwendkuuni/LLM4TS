import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws IOException, SQLException {
        // Test constructor
        StringWriter writer = new StringWriter();
        CSVFormat format = new CSVFormat(';', '\u0001');
        CSVPrinter printer = new CSVPrinter(writer, format);

        // Test print methods
        printer.print("Hello");
        printer.print(42);
        printer.print(3.14);
        printer.print(true);
        printer.print('ä');
        printer.print(new String("Test"));
        printer.print(null);

        printer.println();

        printer.printRecord("Record1", 1, 1.2, '€');
        printer.printRecord(new String[]{"Record2", "Test", "3", null});
        printer.printComment("This is a comment");

        printer.flush();

        String expectedOutput = "\"Hello\",42,3.14,true,\"ä\",Test,null;\n" +
                "\"Record1\",1,1.2,\"€\";\n" +
                "# This is a comment\n";

        Assert.assertEquals(expectedOutput, writer.toString());

        // Test flushing and closing
        printer.close();

        // Test printRecord method with Iterable
        printer = new CSVPrinter(writer, format);
        Iterable<String> iterable = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    int index = 0;
                    String[] values = {"Value1", "Value2", "Value3"};

                    @Override
                    public boolean hasNext() {
                        return index < values.length;
                    }

                    @Override
                    public String next() {
                        return values[index++];
                    }
                };
            }
        };
        printer.printRecords(iterable);
        printer.close();

        expectedOutput = "\"Value1\",\"Value2\",\"Value3\";\n";

        Assert.assertEquals(expectedOutput, writer.toString());

        // Test printRecord method with Object[]
        printer = new CSVPrinter(writer, format);
        String[] record1 = {"Value1", "Value2", "Value3"};
        printer.printRecord(record1);
        printer.close();

        expectedOutput = "\"Value1\",\"Value2\",\"Value3\";\n";

        Assert.assertEquals(expectedOutput, writer.toString());

        // Test printRecords method with ResultSet
        String url = "jdbc:hsqldb:mem:testdb";
        String query = "SELECT id, name, age FROM USERS";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            printer = new CSVPrinter(writer, format);
            printer.printRecords(resultSet);
            printer.close();

            Assert.assertEquals(expectedOutput, writer.toString());

            expectedOutput = "id,name,age;\n" +
                    "1,\"John\",31;\n" +
                    "2,\"Anna\",29;\n" +
                    "3,\"Peter\",42;\n";

        }
    }
}