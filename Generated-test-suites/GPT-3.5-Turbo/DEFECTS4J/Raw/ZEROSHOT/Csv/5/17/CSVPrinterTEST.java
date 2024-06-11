import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class CSVPrinterTest {

    @Test
    public void testPrintNullValue() throws IOException {
        //Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(sb, CSVFormat.DEFAULT);

        //Act
        printer.print(null);
        printer.flush();

        //Assert
        assertEquals("", sb.toString());
    }

    @Test
    public void testPrintStringValue() throws IOException {
        //Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(sb, CSVFormat.DEFAULT);

        //Act
        printer.print("Hello");
        printer.flush();

        //Assert
        assertEquals("Hello", sb.toString());
    }

    @Test
    public void testPrintNumericValue() throws IOException {
        //Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(sb, CSVFormat.DEFAULT);

        //Act
        printer.print(123);
        printer.flush();

        //Assert
        assertEquals("123", sb.toString());
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        //Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(sb, CSVFormat.DEFAULT.withQuoteChar('"'));

        //Act
        printer.print("Hello, World");
        printer.flush();

        //Assert
        assertEquals("\"Hello, World\"", sb.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        //Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(sb, CSVFormat.DEFAULT);

        //Act
        printer.printRecord("John", "Doe", 30);
        printer.flush();

        //Assert
        assertEquals("John,Doe,30" + System.lineSeparator(), sb.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        //Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(sb, CSVFormat.DEFAULT);

        //Act
        Object[] record1 = {"John", "Doe", 30};
        Object[] record2 = {"Jane", "Smith", 25};
        printer.printRecords(record1, record2);
        printer.flush();

        //Assert
        assertEquals("John,Doe,30" + System.lineSeparator() + "Jane,Smith,25" + System.lineSeparator(), sb.toString());
    }
  
    @Test
    public void testPrintComment() throws IOException {
        //Arrange
        StringBuilder sb = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(sb, CSVFormat.DEFAULT);

        //Act
        printer.printComment("This is a comment");
        printer.flush();

        //Assert
        assertEquals("# This is a comment" + System.lineSeparator(), sb.toString());
    }

}