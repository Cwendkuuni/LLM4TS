package org.apache.commons.csv; // make sure the import statement matches your package structure

import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.COMMENT_START;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.QUOTE_CHAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private static final String DELIMITER = ",";
    private static final String QUOTE = "\"";
    private static final String RECORD_SEPARATOR = "\n";

    private ByteArrayOutputStream outputStream;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream), createCSVFormat());
    }

    @After
    public void tearDown() throws IOException {
        if (csvPrinter != null) {
            csvPrinter.close();
        }
    }

    @Test
    public void testCSVPrinterConstructor() {
        assertNotNull(csvPrinter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterConstructorWithNullOut() {
        new CSVPrinter(null, createCSVFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterConstructorWithNullFormat() {
        new CSVPrinter(new OutputStreamWriter(outputStream), null);
    }

    @Test
    public void testClose() throws IOException {
        csvPrinter.close();
        assertFalse(csvPrinter.isOpen());
    }

    @Test
    public void testFlush() throws IOException {
        csvPrinter.print("test");
        csvPrinter.flush();
        assertFalse(csvPrinter.isOpen());
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("test");
        String result = outputStream.toString();
        assertEquals("test", result);
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        csvPrinter.print("test\"" + QUOTE + "test\"");
        String result = outputStream.toString();
        assertEquals("test\"" + QUOTE + "test\"", result);
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        csvPrinter.print(QUOTE + "test\\" + QUOTE + "test" + QUOTE);
        String result = outputStream.toString();
        assertEquals("\"test\\\\test\"", result);
    }

    @Test
    public void testPrintDelimiter() throws IOException {
        csvPrinter.print("test");
        csvPrinter.print("next");
        String result = outputStream.toString();
        assertEquals("test" + DELIMITER + "next", result);
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord(Arrays.asList("test1", "test2"));
        String result = outputStream.toString();
        assertEquals("test1," + DELIMITER + "test2\n", result);
    }

    @Test
    public void testPrintRecordWithQuotedValues() throws IOException {
        csvPrinter.printRecord(Arrays.asList("test1\"" + QUOTE + "test2\"" + QUOTE, "test3"));
        String result = outputStream.toString();
        assertEquals("test1\"" + QUOTE + "test2\"" + QUOTE + DELIMITER + "test3\n", result);
    }

    @Test
    public void testPrintRecordWithEscapedValues() throws IOException {
        csvPrinter.printRecord(Arrays.asList("test1\"test2\\\"test3\"", "test4"));
        String result = outputStream.toString();
        assertEquals("test1\"test2\\\"test3\"" + DELIMITER + "test4\n", result);
    }

    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(Arrays.asList(Arrays.asList("test1", "test2"), Arrays.asList("test3", "test4")));
        String result = outputStream.toString();
        assertEquals("test1," + DELIMITER + "test2\n" + "test3," + DELIMITER + "test4\n", result);
    }

    @Test
    public void testPrintRecordsWithQuotedValues() throws IOException {
        List<List<String>> records = Arrays.asList(Arrays.asList("test1\"" + QUOTE + "test2\"" + QUOTE, "test3"),
                Arrays.asList("test11\"" + QUOTE + "test22\"" + QUOTE, "test4"));
        csvPrinter.printRecords(records);
        String result = outputStream.toString();
        assertEquals("test1\"" + QUOTE + "test2\"" + QUOTE + DELIMITER + "test3\n" + "test11\"" + QUOTE + "test22\""
                + QUOTE + DELIMITER + "test4\n", result);
    }

    @Test
    public void testPrintRecordsWithEscapedValues() throws IOException {
        List<List<String>> records = Arrays.asList(
                Arrays.asList("test1\\\"test2\\\\test3\\\"", "test4"),
                Arrays.asList("test11\\\"test22\\\\test33\\\"", "test5"));
        csvPrinter.printRecords(records);
        String result = outputStream.toString();
        assertEquals("test1\\\"test2\\\\test3\\\"" + DELIMITER + "test4\n" + "test11\\\"test22\\\\test33\\\""
                + DELIMITER + "test5\n", result);
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        String result = outputStream.toString();
        assertEquals(RECORD_SEPARATOR + COMMENT_START + SP + "This is a comment" + CR, result.substring(0, 45) + "...");
    }

    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        String result = outputStream.toString();
        assertEquals(RECORD_SEPARATOR, result);
    }

    @Test
    public void testPrintRecordWithNullValue() throws IOException {
        csvPrinter.printRecord(Collections.singletonList(null));
        String result = outputStream.toString();
        assertEquals(EMPTY, result);
    }

    @Test
    public void testPrintRecordsWithNullValue() throws IOException {
        csvPrinter.printRecords(Collections.singletonList(Collections.singletonList(null)));
        String result = outputStream.toString();
        assertEquals(EMPTY, result);
    }

    @Test
    public void testPrintRecordsWithNullValuesInMiddle() throws IOException {
        csvPrinter.printRecords(Arrays.asList(Arrays.asList("test1", null, "test3"), Arrays.asList("test4", "test5")));
        String result = outputStream.toString();
        assertEquals("test1," + DELIMITER + EMPTY + DELIMITER + "test3\n" + "test4," + DELIMITER + "test5\n", result);
    }

    @Test
    public void testPrintRecordsWithEmptyLine() throws IOException {
        csvPrinter.printRecord(Collections.singletonList(EMPTY));
        String result = outputStream.toString();
        assertEquals(RECORD_SEPARATOR, result);
    }

    @Test
    public void testPrintRecordsWithEmptyLinesInMiddle() throws IOException {
        csvPrinter.printRecords(Collections.singletonList(Collections.singletonList(EMPTY)));
        String result = outputStream.toString();
        assertEquals(RECORD_SEPARATOR, result);

        csvPrinter.printRecords(Arrays.asList(Arrays.asList("test1"), Arrays.asList(EMPTY), Arrays.asList("test3")));
        result = outputStream.toString();
        assertEquals("test1," + DELIMITER + EMPTY + "\n" + RECORD_SEPARATOR + "test3\n", result);
    }

    @Test
    public void testPrintRecordsWithComment() throws IOException, SQLException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.printRecord(Arrays.asList("test1", "test2"));
        String result = outputStream.toString();
        assertEquals(RECORD_SEPARATOR + COMMENT_START + SP + "This is a comment" + CR + "test1," + DELIMITER
                + "test2\n", result.substring(0, 45) + "...");
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException, SQLException {
        ResultSet resultSet = createMockResultSet();
        csvPrinter.printRecords(resultSet);
        String result = outputStream.toString();
        assertEquals("test1,test2\n" + "test3,test4\n", result);
    }

    private CSVFormat createCSVFormat() {
        return new CSVFormat(',', Quote.MINIMAL);
    }

    private ResultSet createMockResultSet() throws SQLException {
        return new MockResultSet(Arrays.asList(new Object[]{"test1", "test2"}, new Object[]{"test3", "test4"}));
    }
}