package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Tests for {@link CSVPrinter} class.
 *
 * @author <a href="mailto:johan.boodhof@gmail.com">Johan Boodhof</a>
 */
public class CSVPrinterTest {

    private CSVFormat format;
    private Appendable appendable;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        format = new CSVFormat(CSVFormat.DEFAULT, new String[]{"name", "age"});
        appendable = new ByteArrayOutputStream();
        printer = new CSVPrinter(new PrintWriter(new OutputStreamWriter(appendable)), format);
    }

    @Test
    public void shouldCreatePrinterInstance() {
        assertNotNull(printer);
    }

    @Test
    public void shouldInitializePrinterWithFormat() {
        assertNotNull(printer.getFormat());
        assertEquals(format, printer.getFormat());
    }

    @Test
    public void shouldInitializePrinterWithOut() {
        assertNotNull(printer.getOut());
        assertEquals(appendable, printer.getOut());
    }

    @Test
    public void shouldPrintSingleStringValue() throws IOException {
        printer.print("Test");
        final String output = appendable.toString();
        final String expected = "Test\n";
        assertEquals(expected, output);
    }

    @Test
    public void shouldPrintSingleNullValue() throws IOException {
        printer.print(null);
        final String output = appendable.toString();
        final String expected = format.getNullString();
        assertEquals(expected, output);
    }

    @Test
    public void shouldPrintSingleNumberValue() throws IOException {
        printer.print(123);
        final String output = appendable.toString();
        final String expected = "123\n";
        assertEquals(expected, output);
    }

    @Test
    public void shouldQuoteStringValueWhenQuotingEnabled() throws IOException {
        format.setQuoteAll(true);
        printer = new CSVPrinter(new PrintWriter(new OutputStreamWriter(appendable)), format);
        printer.print("Test");
        final String output = appendable.toString();
        final String expected = "\"Test\"\n";
        assertEquals(expected, output);
    }

    @Test
    public void shouldEscapeValuesWhenEscapingEnabled() throws IOException {
        format.setEscape('\\');
        printer = new CSVPrinter(new PrintWriter(new OutputStreamWriter(appendable)), format);
        printer.print("Test\"LF");
        final String output = appendable.toString();
        final String expected = "Test\\\"LF\n";
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNullFormatOrOut() {
        new CSVPrinter(null, format);
    }

    @Test
    public void shouldFlushOutput() throws IOException {
        printer.flush();
        final String output = appendable.toString();
        assertTrue(output.isEmpty());
    }

    @Test
    public void shouldCloseOutput() throws IOException {
        printer.close();
        final String output = appendable.toString();
        assertTrue(output.isEmpty());
        assertFalse(printer.isOpen());
    }

    @Test
    public void shouldHandleNewRecord() throws IOException {
        printer.print("Test");
        printer.println();
        printer.print("Another test");
        printer.println();
        final String output = appendable.toString();
        final String expected = "Test\nAnother test\n";
        assertEquals(expected, output);
    }

    @Test
    public void shouldHandleQuotedValues() throws IOException {
        printer.print("Test\"Quoted");
        printer.println();
        final String output = appendable.toString();
        final String expected = "\"Test\"\"Quoted\"\n";
        assertEquals(expected, output);
    }

    @Test
    public void shouldHandleEscapedValues() throws IOException {
        format.setEscape('\\');
        printer = new CSVPrinter(new PrintWriter(new OutputStreamWriter(appendable)), format);
        printer.print("Test\\\"Quoted");
        printer.println();
        final String output = appendable.toString();
        final String expected = "Test\\\\\"Quoted\"\n";
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfInvalidFormat() {
        new CSVPrinter(new PrintWriter(new OutputStreamWriter(appendable)), new CSVFormat());
    }

    @Test
    public void shouldPrintNullValueAsEmptyStringWhenQuoteAllIsFalse() throws IOException {
        printer = new CSVPrinter(new PrintWriter(new OutputStreamWriter(appendable)), format);
        printer.print(null);
        printer.println();
        final String output = appendable.toString();
        assertEquals(format.getNullString(), output);
    }

    @Test
    public void shouldPrintNullValueAsQuotedEmptyStringWhenQuoteAllIsTrue() throws IOException {
        format.setQuoteAll(true);
        printer = new CSVPrinter(new PrintWriter(new OutputStreamWriter(appendable)), format);
        printer.print(null);
        printer.println();
        final String output = appendable.toString();
        assertEquals("\"\"\n", output);
    }

    @Test
    public void shouldPrintRecordWithMultipleValues() throws IOException {
        final List<Object> values = Arrays.asList("Test1", "Test2", "Test3");
        printer.printRecord(values);
        final String output = appendable.toString();
        final String expected = "Test1,Test2,Test3\n";
        assertEquals(expected, output);
    }

    @Test
    public void shouldPrintEmptyRecord() throws IOException {
        printer.printRecord(Collections.emptyList());
        final String output = appendable.toString();
        assertTrue(output.isEmpty());
    }

    @Test
    public void shouldPrintRecordWithMultipleNullValues() throws IOException {
        final List<Object> values = Arrays.asList(null, null, null);
        printer.printRecord(values);
        final String output = appendable.toString();
        assertEquals(format.getNullString(), output);
    }

    @Test
    public void shouldPrintRecordsFromIterable() throws IOException {
        final List<Object> values = Arrays.asList("Test1", "Test2", "Test3");
        printer.printRecords(values);
        final String output = appendable.toString();
        final String expected = "Test1,Test2,Test3\nTest1,Test2,Test3\n";
        assertEquals(expected, output);
    }

    @Test
    public void shouldPrintRecordsFromArray() throws IOException {
        final Object[] values = new Object[]{"Test1", "Test2", "Test3"};
        printer.printRecords(values);
        final String output = appendable.toString();
        final String expected = "Test1,Test2,Test3\nTest1,Test2,Test3\n";
        assertEquals(expected, output);
    }

    @Test
    public void shouldPrintRecordsFromResultSet() throws SQLException, IOException {
        final ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });
        when(resultSet.getString(ArgumentMatchers.anyInt())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                final int index = (int) invocation.getArguments()[0];
                if (index == 1) {
                    return "Test1";
                } else if (index == 2) {
                    return "Test2";
                } else {
                    return "Test3";
                }
            }
        });
        printer.printRecords(resultSet);
        final String output = appendable.toString();
        final String expected = "Test1,Test2,Test3\nTest1,Test2,Test3\n";
        assertEquals(expected, output);
    }

    @Test
    public void shouldPrintComment() throws IOException {
        printer.printComment("Test comment");
        final String output = appendable.toString();
        final String expected = "# Test comment\n";
        assertEquals(expected, output);
    }
}