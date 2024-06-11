import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringBuilder stringBuilder;

    @Before
    public void setup() {
        stringBuilder = new StringBuilder();
        csvPrinter = new CSVPrinter(stringBuilder, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint_NullValue() throws Exception {
        csvPrinter.print(null);
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testPrint_EmptyString() throws Exception {
        csvPrinter.print("");
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testPrint_QuotesAndEscapesNeeded() throws Exception {
        csvPrinter.print("value,");
        assertEquals("\"value,\"", stringBuilder.toString());
    }

    @Test
    public void testPrint_NoQuotesOrEscapesNeeded() throws Exception {
        csvPrinter.print("value");
        assertEquals("value", stringBuilder.toString());
    }

    @Test
    public void testPrintComment_CommentingEnabled() throws Exception {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", stringBuilder.toString());
    }

    @Test
    public void testPrintComment_CommentingDisabled() throws Exception {
        csvPrinter.format.setCommentingEnabled(false);
        csvPrinter.printComment("This is a comment");
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testPrintln() throws Exception {
        csvPrinter.println();
        assertEquals(System.getProperty("line.separator"), stringBuilder.toString());
    }

    @Test
    public void testPrintRecord_Iterable() throws Exception {
        csvPrinter.printRecord(Arrays.asList("value1", "value2"));
        assertEquals("value1,value2" + System.getProperty("line.separator"), stringBuilder.toString());
    }

    @Test
    public void testPrintRecord_Array() throws Exception {
        csvPrinter.printRecord("value1", "value2");
        assertEquals("value1,value2" + System.getProperty("line.separator"), stringBuilder.toString());
    }

    @Test
    public void testPrintRecords_Iterable() throws Exception {
        csvPrinter.printRecords(Arrays.asList(Arrays.asList("value1", "value2"), Arrays.asList("value3", "value4")));
        assertEquals("value1,value2" + System.getProperty("line.separator") + "value3,value4" + System.getProperty("line.separator"), stringBuilder.toString());
    }

    @Test
    public void testPrintRecords_Array() throws Exception {
        csvPrinter.printRecords(new Object[]{"value1", "value2"});
        assertEquals("value1,value2" + System.getProperty("line.separator"), stringBuilder.toString());
    }
}