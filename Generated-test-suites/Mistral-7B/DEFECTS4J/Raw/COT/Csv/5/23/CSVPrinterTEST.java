package org.apache.commons.csv; // update the package name if needed

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVFormat format;
    private CSVPrinter printer;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() throws IOException {
        format = CSVFormat.DEFAULT.withQuoteAll(true).withCommentMarker("#");
        output = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(output), format);
    }

    @Test
    public void testEmptyString() throws IOException {
        printer.print(" ");
        printer.println();
        String result = output.toString();
        assertEquals("", result);
    }

    @Test
    public void testSingleCharacterString() throws IOException {
        printer.print('a');
        printer.println();
        String result = output.toString();
        assertEquals("\"a\"", result);
    }

    @Test
    public void testSingleQuotedString() throws IOException {
        printer.print("'abc'");
        printer.println();
        String result = output.toString();
        assertEquals("\"'abc'\"" , result);
    }

    @Test
    public void testDoubleQuotedString() throws IOException {
        printer.print("\"abc\"");
        printer.println();
        String result = output.toString();
        assertEquals("\"\"abc\"\"", result);
    }

    @Test
    public void testEscapedString() throws IOException {
        printer.print("\"ab\\cde\"");
        printer.println();
        String result = output.toString();
        assertEquals("\"\"ab\\cde\\\"\"", result);
    }

    @Test
    public void testEscapedQuote() throws IOException {
        printer.print("\"ab\"\"cd\"");
        printer.println();
        String result = output.toString();
        assertEquals("\"\"ab\"\\\"\"cd\"\"", result);
    }

    @Test
    public void testEmptyRecord() throws IOException {
        printer.println();
        printer.close();
        String result = output.toString();
        assertEquals("", result);
    }

    @Test
    public void testRecordWithMultipleStrings() throws IOException {
        printer.print("a");
        printer.print(SP);
        printer.print("b");
        printer.print(SP);
        printer.print("c");
        printer.println();
        String result = output.toString();
        assertEquals("\"a\"\"," + SP + "\"b\"\"," + SP + "\"c\"", result);
    }

    @Test
    public void testRecordWithNullStrings() throws IOException {
        printer.print("a");
        printer.print(SP);
        printer.print(null);
        printer.print(SP);
        printer.print("c");
        printer.println();
        String result = output.toString();
        assertEquals("\"a\"\"," + SP + Constants.COMMENT + "," + SP + "\"c\"", result);
    }

    @Test
    public void testRecordWithEmptyStringAtBeginning() throws IOException {
        printer.print("");
        printer.print(SP);
        printer.print("b");
        printer.print(SP);
        printer.print("c");
        printer.println();
        String result = output.toString();
        assertEquals("\"\"\"," + SP + "\"b\"\"," + SP + "\"c\"", result);
    }

    @Test
    public void testRecordWithEmptyStringAtEnd() throws IOException {
        printer.print("a");
        printer.print(SP);
        printer.print("b");
        printer.print(SP);
        printer.print("");
        printer.println();
        String result = output.toString();
        assertEquals("\"a\"\"," + SP + "\"b\"", result);
    }

    @Test
    public void testRecordWithEmptyStringInMiddle() throws IOException {
        printer.print("a");
        printer.print(SP);
        printer.print("");
        printer.print(SP);
        printer.print("c");
        printer.println();
        String result = output.toString();
        assertEquals("\"a\"\"," + SP + "," + SP + "\"\"", result);
    }

    @Test
    public void testRecordWithEscapedStringInMiddle() throws IOException {
        printer.print("a");
        printer.print(SP);
        printer.print("b\\\"c");
        printer.print(SP);
        printer.print("d");
        printer.println();
        String result = output.toString();
        assertEquals("\"a\"\"," + SP + "\"b\\\\\"c\"", result);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("# This is a comment");
        printer.println();
        printer.close();
        String result = output.toString();
        assertEquals("# This is a comment" + LF, result);
    }

    @Test
    public void testPrintRecordWithComment() throws IOException, SQLException {
        String[] values = {"Alice", "# Born in Paris", "25"};
        printer.printRecord(values);
        printer.println();
        printer.close();
        String result = output.toString();
        assertEquals("# Born in Paris" + LF + "\"Alice\"\"," + SP + "\"25\"", result);
    }

    @Test
    public void testPrintRecordsWithEmptyList() throws IOException {
        List<String> values = Collections.emptyList();
        printer.printRecords(values);
        printer.close();
        String result = output.toString();
        assertEquals("", result);
    }

    @Test
    public void testPrintRecordsWithSingleElementList() throws IOException {
        List<String> values = Arrays.asList("Natural");
        printer.printRecords(values);
        printer.close();
        String result = output.toString();
        assertEquals("\"" + values.get(0) + "\"", result);
    }

    @Test
    public void testPrintRecordsWithMultipleElementsList() throws IOException {
        List<String> values = Arrays.asList("Alice", "Bob", "Charlie");
        printer.printRecords(values);
        printer.close();
        String result = output.toString();
        CSVRecord record = CSVRecord.parse(result);
        assertEquals(values, record.toList());
    }

    @Test
    public void testPrintRecordsWithObjectArray() throws IOException {
        String[] values = {"Alice", null, "Charlie"};
        printer.printRecords(values);
        printer.close();
        String result = output.toString();
        CSVRecord record = CSVRecord.parse(result);
        List<String> expected = Arrays.asList("Alice", null, "Charlie");
        assertEquals(expected, record.toList());
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException, SQLException {
        String[][] values = {{"Alice", "# Born in Paris", "25"}, {"Bob", "30"}};
        printer.printRecords(Arrays.asList(values));
        printer.close();
        String result = output.toString();
        for (String[] record : values) {
            CSVRecord expectedRecord = new CSVRecord(Arrays.asList(record));
            CSVRecord recordObj = CSVRecord.parse(result);
            assertEquals(expectedRecord.toList(), recordObj.toList());
        }
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException, SQLException {
        ResultSet resultSet = TestUtils.createResultSetWithTwoRecords("name INT, age INT");
        printer.printRecords(resultSet);
        printer.close();
        String result = output.toString();
        CSVRecord expectedRecord1 = new CSVRecord(Arrays.asList("1", "Alice"));
        CSVRecord recordObj1 = CSVRecord.parse(result);
        assertEquals(expectedRecord1.toList(), recordObj1.toList());
        result = output.toString();
        CSVRecord expectedRecord2 = new CSVRecord(Arrays.asList("2", "Bob"));
        CSVRecord recordObj2 = CSVRecord.parse(result);
        assertEquals(expectedRecord2.toList(), recordObj2.toList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOut() throws IOException {
        new CSVPrinter(null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFormat() throws IOException {
        new CSVPrinter(new ByteArrayOutputStream(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintWithNullObject() throws IOException {
        printer.print(null);
    }

    @Test
    public void testPrintWithNumber() throws IOException {
        printer.print(123);
        printer.println();
        String result = output.toString();
        assertEquals(Integer.toString(123), result);
    }

    @Test(expected = IOException.class)
    public void testPrintWithNullOut() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, format);
        printer.print("a");
    }

    @Test(expected = IOException.class)
    public void testPrintWithNullFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), null);
        printer.print("a");
    }

    @Test(expected = IOException.class)
    public void testPrintWithNullOutAndFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, null);
        printer.print("a");
    }

    @Test(expected = IOException.class)
    public void testPrintWithFormatAndNullOut() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), null);
        printer.setFormat(format);
        printer.print("a");
    }

    @Test(expected = IOException.class)
    public void testPrintWithFormatAndNullValue() throws IOException {
        printer.setFormat(format);
        printer.print(null);
    }

    @Test(expected = IOException.class)
    public void testPrintlnWithNullOut() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, format);
        printer.println();
    }

    @Test(expected = IOException.class)
    public void testPrintlnWithNullFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), null);
        printer.println();
    }

    @Test(expected = IOException.class)
    public void testPrintlnWithNullOutAndFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, null);
        printer.println();
    }

    @Test(expected = IOException.class)
    public void testPrintlnWithFormatAndNullOut() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), format);
        printer.println();
    }

    @Test(expected = IOException.class)
    public void testFlushWithNullOut() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, format);
        printer.flush();
    }

    @Test(expected = IOException.class)
    public void testFlushWithNullFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), null);
        printer.flush();
    }

    @Test(expected = IOException.class)
    public void testFlushWithNullOutAndFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, null);
        printer.flush();
    }

    @Test(expected = IOException.class)
    public void testFlushWithFormatAndNullOut() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), format);
        printer.flush();
    }

    @Test(expected = IOException.class)
    public void testCloseWithNullOut() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, format);
        printer.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithNullFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), null);
        printer.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithNullOutAndFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, null);
        printer.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithFormatAndNullOut() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), format);
        printer.close();
    }
}

class TestUtils {

    public static ResultSet createResultSetWithTwoRecords(String s) {
        return new TestResultSet(Arrays.asList(new String[]{"1", "Alice"}, new String[]{"2", "Bob"}));
    }

    private static class TestResultSet implements ResultSet {

        private final List<String[]> data;

        public TestResultSet(List<String[]> data) {
            this.data = data;
        }

        @Override
        public boolean next() throws SQLException {
            return data.size() > 0;
        }

        @Override
        public String getString(int column) throws SQLException {
            String[] row = data.get(0);
            return row[column];
        }

        @Override
        public int getInt(int column) throws SQLException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() throws SQLException {
        }
    }
}