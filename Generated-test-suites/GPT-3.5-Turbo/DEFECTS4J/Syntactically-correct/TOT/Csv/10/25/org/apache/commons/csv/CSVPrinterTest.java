import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    // Test case for the constructor
    @Test
    public void testCSVPrinterConstructor() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat);

        assertEquals(stringWriter, csvPrinter.getOut());
    }

    // Test case for printing a single value
    @Test
    public void testCSVPrinterSingleValue() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat);

        String value = "Hello World";
        csvPrinter.print(value);

        String expected = "Hello World";
        assertEquals(expected, stringWriter.toString());
    }

    // Test case for printing multiple values in a record
    @Test
    public void testCSVPrinterPrintRecord() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat);

        csvPrinter.printRecord("Alice", 25, "female");
        csvPrinter.printRecord("Bob", 30, "male");

        String expected = "Alice,25, female\nBob, 30, male\n";
        assertEquals(expected, stringWriter.toString());
    }

    // Test case for printing a comment
    @Test
    public void testCSVPrinterPrintComment() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.withCommentMarker('#');

        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat);

        csvPrinter.printComment("This is a comment");

        String expected = "# This is a comment\n";
        assertEquals(expected, stringWriter.toString());
    }

    // Test case for printing multiple values with custom CSV format
    @Test
    public void testCSVPrinterWithCustomFormat() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(';').withQuoteChar('\'').withNullString("NULL");

        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat);

        csvPrinter.printRecord("John", 20, null);

        String expected = "'John';20;NULL\n";
        assertEquals(expected, stringWriter.toString());
    }

    // Test case for printing records from JDBC result set
    @Test
    public void testCSVPrinterPrintRecordsFromResultSet() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(';');

        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat);

        ResultSet resultSet = createMockResultSet(); // helper method to create a mock result set
        csvPrinter.printRecords(resultSet);

        String expected = "1;John;20\n2;Alice;25\n";
        assertEquals(expected, stringWriter.toString());
    }

    // Helper method to create a mock result set
    private ResultSet createMockResultSet() {
        ResultSet resultSet = new MockResultSet();

        resultSet.addColumn("id", Arrays.asList(1, 2));
        resultSet.addColumn("name", Arrays.asList("John", "Alice"));
        resultSet.addColumn("age", Arrays.asList(20, 25));

        return resultSet;
    }

    // ### Add more test cases as needed ###

}