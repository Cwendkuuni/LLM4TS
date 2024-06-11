package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVPrinter.quote;
import static org.apache.commons.csv.CSVPrinter.quotePolicy;
import static org.apache.commons.csv.CSVPrinter.quoting;
import static org.apache.commons.csv.CSVPrinter.escaping;
import static org.apache.commons.csv.CSVPrinter.commenting;
import static org.apache.commons.csv.CSVPrinter.recordSeparator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private Writer writer;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() throws IOException {
        output = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(output);
        printer = new CSVPrinter(writer, DEFAULT);
    }

    // Test printing a single value

    @Test
    public void testPrintSingleStringValue() throws IOException {
        printer.print("hello");
        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("hello");
    }

    @Test
    public void testPrintSingleNullValue() throws IOException {
        printer.print(null);
        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).isEmpty();
    }

    @Test
    public void testPrintSingleNumberValue() throws IOException {
        printer.print(123);
        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("123");
    }

    // Test printing multiple values

    @Test
    public void testPrintMultipleStringValues() throws IOException {
        printer.print("hello");
        printer.print("world");
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("hello");
        assert record.get(1).equals("world");
    }

    @Test
    public void testPrintMultipleNullValues() throws IOException {
        printer.print(null);
        printer.print(null);
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).isEmpty();
        assert record.get(1).isEmpty();
    }

    @Test
    public void testPrintMultipleNumberValues() throws IOException {
        printer.print(123);
        printer.print(456);
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("123");
        assert record.get(1).equals("456");
    }

    @Test
    public void testPrintMultipleMixedValues() throws IOException {
        printer.print("hello");
        printer.print(456);
        printer.print(null);
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("hello");
        assert record.get(1).equals("456");
        assert record.get(2).isEmpty();
    }

    // Test quoting

    @Test
    public void testPrintQuotedSingleStringValue() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withQuote('"'));
        printer.print("hello");
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("\"hello\"");
    }

    @Test
    public void testPrintQuotedMultipleStringValues() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withQuote('"'));
        printer.print("hello");
        printer.print("world");
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("\"hello\"");
        assert record.get(1).equals("\"world\"");
    }

    @Test
    public void testPrintQuotedAndEscapedStringValue() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withQuote('"').withEscape('\u001a'));
        printer.print("hello\u001abello");
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("\"hello\\u001abello\"");
    }

    // Test escaping

    @Test
    public void testPrintEscapedSingleStringValue() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withEscape('\u001a'));
        printer.print("hello");
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("hello");
    }

    @Test
    public void testPrintEscapedMultipleStringValues() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withEscape('\u001a'));
        printer.print("hello");
        printer.print("world");
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("hello");
        assert record.get(1).equals("world");
    }

    @Test
    public void testPrintEscapedQuotedStringValue() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withEscape('\u001a').withQuote('"'));
        printer.print("hello\"");
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("hello\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintEscapedQuotedStringValueWithInvalidEscapeChar() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withQuote('"').withEscape('.
'));
        printer.print("hello\"");
        printer.println();

        printer.readRecord();
    }

    // Test commenting

    @Test
    public void testPrintComment() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withComment('#'));
        printer.printComment("# This is a comment");
        printer.println();
        printer.print("hello");
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.isEmpty();

        record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("hello");
        assert record.get(1).isEmpty();
        assert record.get(1).startsWith("# This is a comment");
    }

    // Test record separator

    @Test
    public void testPrintRecordSeparator() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withRecordSeparator("\u001e"));
        printer.print("hello");
        printer.print("world");
        printer.println();
        printer.print("foo");
        printer.println();

        CSVRecord record1 = printer.readRecord();
        assert record1 != null;
        assert record1.get(0).equals("hello");
        assert record1.get(1).equals("world");

        CSVRecord record2 = printer.readRecord();
        assert record2 != null;
        assert record2.get(0).equals("foo");
        assert record2.isEmpty();

        assert printer.readRecord() == null;
    }

    // Test quoting policy

    @Test
    public void testPrintQuotingMinimalPolicy() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withQuotePolicy(CSVPrinter.Quote.MINIMAL));
        printer.print(null);
        printer.print("hello");
        printer.print(456);
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).isEmpty();
        assert record.get(1).equals("hello");
        assert record.get(2).equals("456");
    }

    @Test
    public void testPrintQuotingAllPolicy() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withQuotePolicy(CSVPrinter.Quote.ALL));
        printer.print(null);
        printer.print("hello");
        printer.print(456);
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).equals("\"\"");
        assert record.get(1).equals("\"hello\"");
        assert record.get(2).equals("456");
    }

    @Test
    public void testPrintQuotingNonePolicy() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withQuotePolicy(CSVPrinter.Quote.NONE));
        printer.print(null);
        printer.print("hello");
        printer.print(456);
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).isEmpty();
        assert record.get(1).equals("hello");
        assert record.get(2).equals("456");
    }

    @Test
    public void testPrintQuotingNonNumericPolicy() throws IOException {
        printer = new CSVPrinter(writer, DEFAULT.withQuotePolicy(CSVPrinter.Quote.NON_NUMERIC));
        printer.print(null);
        printer.print(456);
        printer.print("hello");
        printer.println();

        CSVRecord record = printer.readRecord();
        assert record != null;
        assert record.get(0).isEmpty();
        assert record.get(1).equals("456");
        assert record.get(2).equals("\"hello\"");
    }

    // Test printing custom format

    @Test
    public void testPrintCustomFormat() throws IOException, ParseException {
        printer = new CSVPrinter(writer, DEFAULT.withHeader("Name", "Age", "Gender")
                .withFormat("%-10s, %-3d, %-5s"));
        printer.printRecord(Arrays.asList("Alice", 25, "Female"));
        printer.println();
        printer.printRecord(Arrays.asList("Bob", 30, "Male"));
        printer.flush();

        List<CSVRecord> records = printer.readAllRecords();
        assert records.size() == 2;

        CSVRecord record1 = records.get(0);
        assert record1.get(0).equals("Name");
        assert record1.get(1).equals("Name");
        assert record1.get(2).equals("Age");
        assert record1.get(3).equals("Age");
        assert record1.get(4).equals("Gender");
        assert record1.get(5).equals("Gender");
        assert record1.size() == 6;
        assert record1.get(0).length() == 10;
        assert record1.get(1).length() == 3;
        assert record1.get(2).length() == 5;

        CSVRecord record2 = records.get(1);
        assert record2.get(0).equals("Alice");
        assert record2.get(1).equals("Alice");
        assert record2.get(2).equals("25");
        assert record2.get(3).equals("Name");
        assert record2.get(4).equals("Age");
        assert record2.get(5).equals("25");
        assert record2.size() == 6;
        assert record2.get(0).length() == 10;
        assert record2.get(1).length() == 3;
        assert record2.get(2).length() == 1;
        assert record2.get(3).length() == 3;
        assert record2.get(4).length() == 3;
        assert record2.get(5).length() == 3;

        CSVRecord record3 = printer.readRecord();
        assert record3 == null;
    }
}