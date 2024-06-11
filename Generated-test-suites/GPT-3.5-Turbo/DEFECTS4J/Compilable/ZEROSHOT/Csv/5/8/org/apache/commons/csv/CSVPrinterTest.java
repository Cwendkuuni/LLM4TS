package org.apache.commons.csv;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    @Before
    public void setup() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint_NullValue_PrintsEmpty() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testPrint_ObjectValue_PrintsToString() throws IOException {
        csvPrinter.print(42);
        csvPrinter.flush();
        assertEquals("42", stringWriter.toString());
    }

    @Test
    public void testPrint_StringValue_PrintsValue() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.flush();
        assertEquals("Hello", stringWriter.toString());
    }

    @Test
    public void testPrint_DoubleQuoteValue_EscapesQuotes() throws IOException {
        csvPrinter.print("\"Hello\"");
        csvPrinter.flush();
        assertEquals("\"\"\"Hello\"\"\"", stringWriter.toString());
    }

    @Test
    public void testPrint_CommaValue_PrintsWithDelimiter() throws IOException {
        csvPrinter.print("Hello, World");
        csvPrinter.flush();
        assertEquals("Hello, World", stringWriter.toString());
    }

    @Test
    public void testPrint_RecordSeparator_PrintsNewLine() throws IOException {
        csvPrinter.println();
        csvPrinter.flush();
        assertEquals(System.getProperty("line.separator"), stringWriter.toString());
    }

    @Test
    public void testPrint_Comment_PrintsWithCommentStart() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("# This is a comment" + System.getProperty("line.separator"), stringWriter.toString());
    }

    @Test
    public void testPrint_Record_PrintsValuesWithDelimiter() throws IOException {
        csvPrinter.printRecord(Arrays.asList("Value1", "Value2", "Value3"));
        csvPrinter.flush();
        assertEquals("Value1,Value2,Value3" + System.getProperty("line.separator"), stringWriter.toString());
    }

    @Test
    public void testPrint_Records_PrintsWithSeparateLines() throws IOException {
        List<List<String>> records = Arrays.asList(
                Arrays.asList("Value1", "Value2", "Value3"),
                Arrays.asList("Value4", "Value5", "Value6")
        );
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        assertEquals("Value1,Value2,Value3" + System.getProperty("line.separator") +
                "Value4,Value5,Value6" + System.getProperty("line.separator"), stringWriter.toString());
    }

}