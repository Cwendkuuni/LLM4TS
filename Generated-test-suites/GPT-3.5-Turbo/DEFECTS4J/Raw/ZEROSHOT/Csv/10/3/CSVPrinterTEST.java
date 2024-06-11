import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVFormat format = CSVFormat.newFormat(',')
                .withQuoteChar('\'')
                .withNullString("NULL")
                .withCommentStart('#')
                .build();
        CSVPrinter printer = new CSVPrinter(sb, format);

        printer.print("John");
        assertEquals("John", sb.toString());
        printer.print("Doe");
        assertEquals("John,Doe", sb.toString());
        printer.print(25);
        assertEquals("John,Doe,25", sb.toString());
        printer.print(null);
        assertEquals("John,Doe,25,NULL", sb.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVFormat format = CSVFormat.newFormat(',')
                .withCommentMarker('#')
                .build();
        CSVPrinter printer = new CSVPrinter(sb, format);

        printer.printComment("This is a comment");
        assertEquals("# This is a comment\n", sb.toString());
    }

    @Test
    public void testPrintRecordIterable() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVFormat format = CSVFormat.newFormat(',')
                .withQuoteChar('\'')
                .build();
        CSVPrinter printer = new CSVPrinter(sb, format);

        printer.printRecord("John", "Doe");
        assertEquals("'John','Doe'\n", sb.toString());

        printer.printRecord("Jane", "Smith");
        assertEquals("'John','Doe'\n'Jane','Smith'\n", sb.toString());
    }

    @Test
    public void testPrintRecordArray() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVFormat format = CSVFormat.newFormat(',')
                .withQuoteChar('\'')
                .build();
        CSVPrinter printer = new CSVPrinter(sb, format);

        printer.printRecord(new String[]{"John", "Doe"});
        assertEquals("'John','Doe'\n", sb.toString());

        printer.printRecord(new String[]{"Jane", "Smith"});
        assertEquals("'John','Doe'\n'Jane','Smith'\n", sb.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVFormat format = CSVFormat.newFormat(',')
                .withQuoteChar('\'')
                .build();
        CSVPrinter printer = new CSVPrinter(sb, format);

        printer.printRecords(new String[]{"John", "Doe"});
        assertEquals("'John','Doe'\n", sb.toString());

        printer.printRecords(new String[]{"Jane", "Smith"});
        assertEquals("'John','Doe'\n'Jane','Smith'\n", sb.toString());
    }

    @Test
    public void testPrintRecordsArray() throws IOException {
        StringBuilder sb = new StringBuilder();
        CSVFormat format = CSVFormat.newFormat(',')
                .withQuoteChar('\'')
                .build();
        CSVPrinter printer = new CSVPrinter(sb, format);

        printer.printRecords(new String[]{"John", "Doe"});
        assertEquals("'John','Doe'\n", sb.toString());

        printer.printRecords(new String[]{"Jane", "Smith"});
        assertEquals("'John','Doe'\n'Jane','Smith'\n", sb.toString());
    }

    // More test cases for other methods in the CSVPrinter class

}