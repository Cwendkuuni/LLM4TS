package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVPrinter.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream output;
    private static final String TEST_VALUE = "test value";
    private static final String TEST_COMMENT = "# this is a comment";

    @Before
    public void setup() {
        output = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.newBuilder().build();
        try {
            printer = new CSVPrinter(output, format);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize CSVPrinter.", e);
        }
    }

    @Test
    public void testPrint() throws IOException {
        printer.print(TEST_VALUE);
        assertEquals(TEST_VALUE, parseValueFromOutput(0));
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        printer.print("test \"value\"");
        assertEquals("test \"value\"", parseValueFromOutput(0));
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        assertEquals("", parseValueFromOutput(0));
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(TEST_VALUE, TEST_VALUE, TEST_VALUE);
        String[] expectedValues = { TEST_VALUE, TEST_VALUE, TEST_VALUE };
        verifyRecordValues(expectedValues);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment(TEST_COMMENT);
        String expectedComment = TEST_COMMENT + "\n" + COMMENT + SP;
        assertEquals(expectedComment, parseValueFromOutput(0));
    }

    @Test
    public void testPrintQuotedRecord() throws IOException {
        CSVFormat format = CSVFormat.newBuilder().setQuoteAll(true).build();
        try {
            printer = new CSVPrinter(output, format);
            printer.printRecord("test \"value\"", "test \"value\"", "test \"value\"");
            String[] expectedValues = { "test \"value\"", "test \"value\"", "test \"value\"" };
            verifyRecordValues(expectedValues);
        } finally {
            printer.close();
        }
    }

    @Test
    public void testPrintQuotedRecordWithEscapedValues() throws IOException {
        CSVFormat format = CSVFormat.newBuilder().setQuoteAll(true)
                .setEscapeChar(CSVFormat.DEFAULT_ESCAPE_CHAR).build();
        try {
            printer = new CSVPrinter(output, format);
            printer.printRecord("test \"value\\nwith\\nlinebreaks \\r\\rand \\t\\ttabs\"",
                    "test \"value2\\nwith\\nlinebreaks \\r\\rand \\t\\ttabs2\"",
                    "test \"value3\\nwith\\nlinebreaks \\r\\rand \\t\\ttabs3\"");
            String[] expectedValues = { "test \"value\\nwith\\nlinebreaks \\r\\rand \\t\\ttabs\"",
                    "test \"value2\\nwith\\nlinebreaks \\r\\rand \\t\\ttabs2\"",
                    "test \"value3\\nwith\\nlinebreaks \\r\\rand \\t\\ttabs3\"" };
            verifyRecordValues(expectedValues);
        } finally {
            printer.close();
        }
    }

    @Test
    public void testPrintRecordWithQuotedValues() throws IOException {
        CSVFormat format = CSVFormat.newBuilder().setQuoteAll(false)
                .setQuoteChar(CSVFormat.DEFAULT_QUOTE_CHAR).build();
        try {
            printer = new CSVPrinter(output, format);
            printer.printRecord("test \"value\"", "test \"value\"" , "test \"value\"");
            String[] expectedValues = { "test \"value\"", "test \"value\"", "test value" };
            verifyRecordValues(expectedValues);
        } finally {
            printer.close();
        }
    }

    @Test
    public void testPrintRecordsFromIterable() throws IOException {
        Iterable<String> iterable = () -> new String[] { TEST_VALUE, TEST_VALUE };
        printer.printRecords(iterable);
        String[] expectedValues = { TEST_VALUE, TEST_VALUE };
        verifyRecordValues(expectedValues);
    }

    @Test
    public void testPrintRecordsFromArray() throws IOException {
        String[] array = { TEST_VALUE, TEST_VALUE };
        printer.printRecords(array);
        String[] expectedValues = { TEST_VALUE, TEST_VALUE };
        verifyRecordValues(expectedValues);
    }

    @Test
    public void testPrintRecordsFromResultSet() throws SQLException, IOException {
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        CSVPrinter printer2 = new CSVPrinter(output2, CSVFormat.DEFAULT);
        String[][] data = { { "test value 1", "test value 2" }, { "test value 3", "test value 4" } };
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenAnswer(i -> data[i][0]);
        when(resultSet.getString(2)).thenAnswer(i -> data[i][1]);
        try {
            printer2.printRecords(resultSet);
            printer.printRecords(output2.toString());
            String[] expectedValues1 = { "test value 1", "test value 2" };
            String[] expectedValues2 = { "test value 3", "test value 4" };
            verifyRecordValues(expectedValues1);
            verifyRecordValues(expectedValues2);
        } finally {
            printer.close();
            printer2.close();
        }
    }

    @Test
    public void testFlush() throws IOException {
        printer.flush();
        byte[] outputBytes = output.toByteArray();
        assertFalse("Output stream should not be empty.", outputBytes.length == 0);
    }

    @Test
    public void testClose() throws IOException {
        printer.close();
        byte[] outputBytes = output.toByteArray();
        assertTrue("Output stream should not be empty.", outputBytes.length > 0);
        assertFalse("InputStream should be closed.", output.isOpen());
    }

    private void verifyRecordValues(String[] expectedValues) throws IOException, SQLException {
        CSVParser parser = new CSVParser(new StringReader(output.toString()));
        CSVRecord record = parser.getHeaderMap().isEmpty() ? parser.getNextRecord() : parser.getRecordMap().get(0);
        for (int i = 0; i < expectedValues.length; i++) {
            assertEquals(expectedValues[i], record.get(i).getRawText());
        }
        verify(printer, times(expectedValues.length + 1)).println();
    }

    private String parseValueFromOutput(int index) throws IOException {
        CSVParser parser = new CSVParser(new StringReader(output.toString()));
        CSVRecord record = parser.getRecordMap().get(index);
        return record.get(0).getRawText();
    }

}