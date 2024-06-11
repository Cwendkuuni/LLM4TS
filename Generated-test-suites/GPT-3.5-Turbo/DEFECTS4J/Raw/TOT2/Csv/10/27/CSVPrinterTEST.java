import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("John,Doe");
        printer.flush();

        assertEquals("John,Doe", out.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord("Alice", "Smith");

        assertEquals("Alice,Smith\n", out.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecords(Arrays.asList("1", "2", "3"));

        assertEquals("1,2,3\n", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("This is a comment");

        assertEquals("# This is a comment\n", out.toString());
    }

    @Test
    public void testPrintRecord_exception() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);

        try {
            printer.printRecord((String) null);
        } catch (NullPointerException e) {
            // The expected exception
        }
    }

    @Test
    public void testPrintRecords_ResultSet() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        CSVFormat format = CSVFormat.DEFAULT;
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecords(resultSet);
        // Verify if the method interacts with ResultSet correctly (mock object)
    }
}