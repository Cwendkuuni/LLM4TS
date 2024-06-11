import org.junit.Assert;
import org.junit.Test;

public class CSVPrinterTest {

    // Test case for print(Object value)
    @Test
    public void testPrintObject() throws IOException {
        // Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);

        // Act
        csvPrinter.print("Hello");
        csvPrinter.print("World");
        csvPrinter.flush();

        // Assert
        Assert.assertEquals("Hello,World\n", sb.toString());
    }

    // Test case for print(Object... values)
    @Test
    public void testPrintArray() throws IOException {
        // Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);

        // Act
        csvPrinter.print("Hello", "World");
        csvPrinter.flush();

        // Assert
        Assert.assertEquals("Hello,World\n", sb.toString());
    }

    // Test case for println()
    @Test
    public void testPrintln() throws IOException {
        // Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);

        // Act
        csvPrinter.println();
        csvPrinter.flush();

        // Assert
        Assert.assertEquals("\n", sb.toString());
    }

    // Test case for printRecord(Iterable<?> values)
    @Test
    public void testPrintRecordIterable() throws IOException {
        // Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);

        // Act
        csvPrinter.printRecord(Arrays.asList("Hello", "World"));
        csvPrinter.flush();

        // Assert
        Assert.assertEquals("Hello,World\n", sb.toString());
    }

    // Test case for printRecord(Object... values)
    @Test
    public void testPrintRecordArray() throws IOException {
        // Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);

        // Act
        csvPrinter.printRecord("Hello", "World");
        csvPrinter.flush();

        // Assert
        Assert.assertEquals("Hello,World\n", sb.toString());
    }

    // Test case for printRecords(Iterable<?> values)
    @Test
    public void testPrintRecordsIterable() throws IOException {
        // Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);

        // Act
        csvPrinter.printRecords(Arrays.asList("Hello", "World"));
        csvPrinter.flush();

        // Assert
        Assert.assertEquals("Hello\nWorld\n", sb.toString());
    }

    // Test case for printRecords(Object[] values)
    @Test
    public void testPrintRecordsArray() throws IOException {
        // Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);

        // Act
        csvPrinter.printRecords(new Object[] {"Hello", "World"});
        csvPrinter.flush();

        // Assert
        Assert.assertEquals("Hello\nWorld\n", sb.toString());
    }

    // Test case for printRecords(ResultSet resultSet)
    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        // Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        ResultSet resultSet = createMockResultSet();

        // Act
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();

        // Assert
        Assert.assertEquals("1,John\n2,Jane\n3,David\n", sb.toString());
    }

    private ResultSet createMockResultSet() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE persons(id INT PRIMARY KEY, name VARCHAR(255))");
        statement.execute("INSERT INTO persons VALUES(1, 'John')");
        statement.execute("INSERT INTO persons VALUES(2, 'Jane')");
        statement.execute("INSERT INTO persons VALUES(3, 'David')");

        return statement.executeQuery("SELECT * FROM persons");
    }
}