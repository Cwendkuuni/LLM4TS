import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QUOTE_MINIMAL;
import org.apache.commons.csv.QUOTE_NONE;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(QUOTE_MINIMAL));
        printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);
    }

    @After
    public void tearDown() throws Exception {
        printer.close();
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Hello World");
        assertEquals("Hello World\n", outputStream.toString());
    }

    @Test
    public void testPrint_withQuotes() throws IOException {
        printer.print("\"Hello World\"");
        assertEquals("\"Hello World\"\n", outputStream.toString());
    }

    @Test
    public void testPrint_withQuotesAndEscapes() throws IOException {
        printer.print("\"Hello World\\\"\"");
        assertEquals("\"Hello World\\\"\"\n", outputStream.toString());
    }

    @Test
    public void testPrint_withEmptyString() throws IOException {
        printer.print("");
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrint_withNull() throws IOException {
        printer.print(null);
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintln() throws IOException {
        printer.println("Hello World");
        assertEquals("Hello World\n", outputStream.toString());
    }

    @Test
    public void testPrintln_withQuotes() throws IOException {
        printer.println("\"Hello World\"");
        assertEquals("\"Hello World\"\n", outputStream.toString());
    }

    @Test
    public void testPrintln_withQuotesAndEscapes() throws IOException {
        printer.println("\"Hello World\\\"\"");
        assertEquals("\"Hello World\\\"\"\n", outputStream.toString());
    }

    @Test
    public void testPrintln_withEmptyLine() throws IOException {
        printer.println();
        assertEquals("\n", outputStream.toString());
    }

    @Test
    public void testPrintln_emptyLineAtStart() throws IOException {
        printer.println();
        printer.println("Hello World");
        assertEquals("\nHello World\n", outputStream.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        List<Object> record = Arrays.asList("Hello World 1", "Hello World 2", "Hello World 3");
        printer.printRecord(record);
        assertEquals("Hello World 1,Hello World 2,Hello World 3\n", outputStream.toString());
    }

    @Test
    public void testPrintRecord_emptyRecord() throws IOException {
        printer.printRecord(Collections.emptyList());
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        List<List<Object>> records = Arrays.asList(
                Arrays.asList("Hello World 1", "Hello World 2", "Hello World 3"),
                Arrays.asList("Hello World 4", "Hello World 5", "Hello World 6")
        );
        printer.printRecords(records);
        assertEquals("Hello World 1,Hello World 2,Hello World 3\nHello World 4,Hello World 5,Hello World 6\n", outputStream.toString());
    }

    @Test
    public void testPrintRecords_emptyRecords() throws IOException {
        List<List<Object>> records = Collections.emptyList();
        printer.printRecords(records);
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintRecords_singleRecord() throws IOException {
        List<Object> record = Arrays.asList("Hello World");
        printer.printRecords(record);
        assertEquals("Hello World\n", outputStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        String expectedComment = "This is a comment\n" + CR + LF;
        assertEquals(expectedComment, outputStream.toString());
    }

    @Test
    public void testPrintComment_emptyComment() throws IOException {
        printer.printComment("");
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintComment_commentAtStart() throws IOException {
        printer.printComment("");
        printer.println("Hello World");
        assertEquals("\nHello World\n", outputStream.toString());
    }
}