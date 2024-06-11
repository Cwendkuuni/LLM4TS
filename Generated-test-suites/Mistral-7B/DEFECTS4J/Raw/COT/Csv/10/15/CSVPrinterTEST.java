package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVFormat format;
    private CSVPrinter printer;
    private ByteArrayOutputStream outStream;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        format = new CSVFormat(CSVFormat.DEFAULT.withHeader());
        outStream = new ByteArrayOutputStream();
        stringWriter = new StringWriter(new PrintWriter(outStream));
        printer = new CSVPrinter(stringWriter, format);
    }

    @Test
    public void testCSVPrinterBasicFunctionality() throws IOException {
        // Test printing individual values
        printer.print("Hello");
        printer.print(123);
        printer.print(true);
        assertEquals("Hello,123,true\n", stringWriter.toString());

        // Test printing a record with multiple values
        printer.printRecord(Arrays.asList("Apple", "1", "false"));
        assertEquals("Apple,1,false\nHello,123,true\n", stringWriter.toString());

        // Test printing comments
        printer.printComment("This is a comment");
        assertEquals("Apple,1,false\nThis is a comment\nHello,123,true\n", stringWriter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterNullFormat() {
        new CSVPrinter(stringWriter, null);
    }

    @Test
    public void testCSVPrinterQuoting() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        printer = new CSVPrinter(outStream, format);

        // Test printing a simple value with no quoting required
        printer.print("Hello");
        assertEquals("\"Hello\"\n", stringWriter.toString());

        // Test printing a simple value that requires quoting
        printer.print("O'Reilly");
        assertEquals("\"O'Reilly\"\n", stringWriter.toString());

        // Test printing a value that contains the delimiter
        printer.print("Field,Value");
        assertEquals("\"Field,Value\"\n", stringWriter.toString());

        // Test printing a value that contains the quote character
        printer.print("Field,\"Value\"");
        assertEquals("\"Field,\"Value\"\"\n", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterEscaping() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withEscape('\u001E'));
        printer = new CSVPrinter(outStream, format);

        // Test printing a simple value with no escaping required
        printer.print("Hello");
        assertEquals("Hello\n", stringWriter.toString());

        // Test printing a simple value that requires escaping
        printer.print("Field\u001EValue");
        assertEquals("Field\\u001EValue\n", stringWriter.toString());

        // Test printing a value that contains the escape character
        printer.print("Field\\u001EValue");
        assertEquals("Field\\\\u001EValue\n", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterPrintln() throws IOException {
        // Test printing a single line with println
        printer.println();
        assertEquals("\n", stringWriter.toString());

        // Test printing multiple lines with multiple println calls
        printer.println();
        printer.println();
        assertEquals("\n\n", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterPrintRecord() throws IOException {
        // Test printing a single record
        printer.printRecord(Arrays.asList("Apple", "1", "false"));
        assertEquals("Apple,1,false\n", stringWriter.toString());

        // Test printing multiple records
        printer.printRecords(Arrays.asList(Arrays.asList("Apple", "1", "false"), Arrays.asList("Orange", "2", "true")));
        assertEquals("Apple,1,false\nApple,1,false\nOrange,2,true\n", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterPrintRecordsWithNullValue() throws IOException {
        printer.printRecords(Arrays.asList(Collections.singletonList(null)));
        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterPrintRecordsWithEmptyRecord() throws IOException {
        printer.printRecords(Collections.emptyList());
        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterPrintRecordsWithEmptyArray() throws IOException {
        printer.printRecords((Object[]) new Object[0]);
        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterPrintRecordsWithObjectArray() throws IOException {
        printer.printRecords(new Object[]{"Apple", "1", "false"});
        assertEquals("Apple,1,false\n", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterPrintRecordsWithIterable() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(anyInt())).thenReturn("Apple", "1", "false");

        printer.printRecords(resultSet);

        verify(resultSet).next();
        verify(resultSet).next();
        verify(resultSet).next();
        verifyNoMoreInteractions(resultSet);

        assertEquals("Apple,1,false\nApple,1,false\nApple,1,false\n", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterPrintComment() throws IOException {
        printer.printComment("This is a comment");
        assertEquals("Apple,1,false\nThis is a comment\nApple,1,false\n", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterQuotePolicy() throws IOException {
        // Test minimal quote policy
        format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL));
        printer = new CSVPrinter(outStream, format);

        printer.print("Field1 field2");
        assertEquals("Field1 field2\n", stringWriter.toString());

        printer.print("Field1 'field2' field3");
        assertEquals("Field1 \"field2\" field3\n", stringWriter.toString());

        String value = "Field1,\"field2\" field3";
        printer.print(value);
        assertEquals("Field1,\"field2\" field3\n", stringWriter.toString());

        // Test non-numeric quote policy
        format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.NON_NUMERIC));
        printer = new CSVPrinter(outStream, format);

        printer.print("123");
        assertEquals("123\n", stringWriter.toString());

        value = "123,Field2";
        printer.print(value);
        assertEquals("123,Field2\n", stringWriter.toString());

        printer.print("123,\"Field2\"");
        assertEquals("123,\"Field2\"\n", stringWriter.toString());

        // Test all quote policy
        format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.ALL));
        printer = new CSVPrinter(outStream, format);

        printer.print("Field1 field2");
        assertEquals("\"Field1\" \"field2\"\n", stringWriter.toString());

        printer.print("Field1 'field2' field3");
        assertEquals("\"Field1\" \"field2\" \"field3\"\n", stringWriter.toString());

        printer.print("Field1 \"field2\" field3");
        assertEquals("\"Field1\" \"field2\" \"field3\"\n", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterEscapePolicy() throws IOException {
        // Test default escape policy
        format = new CSVFormat(CSVFormat.DEFAULT.withEscape('\u001E'));
        printer = new CSVPrinter(outStream, format);

        printer.print("Field1\u001EValue");
        assertEquals("Field1\\u001EValue\n", stringWriter.toString());

        printer.print("Field1\\u001EValue");
        assertEquals("Field1\\\\u001EValue\n", stringWriter.toString());

        // Test no escape policy
        format = new CSVFormat(CSVFormat.DEFAULT.withoutEscape());
        printer = new CSVPrinter(outStream, format);

        printer.print("Field1\u001EValue");
        assertEquals("Field1\u001EValue\n", stringWriter.toString());

        printer.print("Field1\\u001EValue");
        assertEquals("Field1\\u001EValue\n", stringWriter.toString());

        // Test custom escape character
        format = new CSVFormat(CSVFormat.DEFAULT.withEscape('\u0001'));
        printer = new CSVPrinter(outStream, format);

        printer.print("Field1\u001EValue");
        assertEquals("Field1\\001Value\n", stringWriter.toString());

        printer.print("Field1\\u001EValue");
        assertEquals("Field1\\\\001Value\n", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterFlush() throws IOException {
        // Test default flush policy
        printer.flush();
        assertEquals(stringWriter.toString(), outStream.toString());

        // Test custom flush policy
        printer.close();
        assertEquals(stringWriter.toString(), outStream.toString());
    }

    @Test
    public void testCSVPrinterClose() throws IOException {
        // Test default close policy
        printer.close();
        assertEquals(stringWriter.toString(), outStream.toString());

        // Test custom close policy
        printer = new CSVPrinter(outStream, format);
        printer.close();
        assertEquals(stringWriter.toString(), outStream.toString());
    }

    @Test
    public void testCSVPrinterWithQuoteAndEscape() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true).withEscape('\u001E'));
        printer = new CSVPrinter(outStream, format);

        printer.print("Field1\u001EValue");
        assertEquals("Field1\"Value\"\n", stringWriter.toString());

        printer.print("Field1\\u001EValue");
        assertEquals("Field1\"Value\"\n", stringWriter.toString());

        printer.print("Field1\\\"Value");
        assertEquals("Field1\"Value\"\n", stringWriter.toString());

        printer.print("Field1\"Value");
        assertEquals("Field1\"Value\"\n", stringWriter.toString());

        printer.print("Field1,\"Value");
        assertEquals("Field1,\"Value\"\n", stringWriter.toString());

        printer.print("Field1,\"Value\"");
        assertEquals("Field1,\"Value\"\n", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterWithQuoteAndDelimiter() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true).withDelimiter(CR));
        printer = new CSVPrinter(outStream, format);

        printer.print("Field1" + CR + "Value");
        assertEquals("Field1\rValue\r", stringWriter.toString());

        printer.print("Field1" + CR + "Value");
        assertEquals("Field1\"Value\"\r", stringWriter.toString());

        printer.print("Field1\"Value");
        assertEquals("Field1\"Value\"\r", stringWriter.toString());

        printer.print("Field1\"Value");
        assertEquals("Field1\"Value\"\r", stringWriter.toString());

        printer.print("Field1,\"Value");
        assertEquals("Field1,\"Value\"\r", stringWriter.toString());

        printer.print("Field1,\"Value");
        assertEquals("Field1,\"Value\"\r", stringWriter.toString());
    }

    @Test
    public void testCSVPrinterWithQuoteAndEscapeAndDelimiter() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true).withEscape('\u001E').withDelimiter(CR));
        printer = new CSVPrinter(outStream, format);

        printer.print("Field1" + CR + "Value");
        assertEquals("Field1\"Value\"\r", stringWriter.toString());

        printer.print("Field1\\" + CR + "Value");
        assertEquals("Field1\"Value\"\r", stringWriter.toString());

        printer.print("Field1\\" + CR + "Value");
        assertEquals("Field1\"Value\"\r", stringWriter.toString());

        printer.print("Field1\u001EValue");
        assertEquals("Field1\"Value\"\r", stringWriter.toString());

        printer.print("Field1\"Value");
        assertEquals("Field1\"Value\"\r", stringWriter.toString());

        printer.print("Field1\"Value");
        assertEquals("Field1\"Value\"\r", stringWriter.toString());

        printer.print("Field1,\"Value");
        assertEquals("Field1,\"Value\"\r", stringWriter.toString());

        printer.print("Field1,\"Value");
        assertEquals("Field1,\"Value\"\r", stringWriter.toString());
    }
}