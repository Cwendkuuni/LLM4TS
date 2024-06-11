package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

/**
 * Test class for CSVPrinter
 */
public class CSVPrinterTest {

    @org.junit.Test
    public void testCSVPrinter() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL)
                .withEscapingMode(EscapingMode.AUTO)
                .withHeader("Header1", "Header2", "Header3")
                .withCommentMarker("#")
                .withRecordSeparator("\n")
                .withDelimiter("|"));

        try (CSVPrinter printer = new CSVPrinter(outStream, format)) {
            printer.printRecord(1, "Test1", "Test2|Test3", "Test4# This is a comment");
            printer.printComment("This is a comment on a new line");
            printer.println();
            printer.printRecord(2, "Test5", "Test6|Test7", "Test8# This is another comment");
            printer.println();
            printer.close();

            String csvString = outStream.toString();
            assertFalse(csvString.isEmpty());

            CSVParser parser = CSVParser.parse(new StringReader(csvString));
            CSVRecord record = parser.getHeaderMap().get(0);
            assertEquals("Header1", record.get("Header1"));
            assertEquals("Header2", record.get("Header2"));
            assertEquals("Header3", record.get("Header3"));

            record = parser.getNextRecord();
            assertEquals(Integer.valueOf(1), record.get(0));
            assertEquals("Test1", record.get(1));
            assertEquals("Test2|Test3", record.get(2));
            assertEquals("Test4# This is a comment", record.get(3));

            record = parser.getNextRecord();
            assertEquals(Integer.valueOf(2), record.get(0));
            assertEquals("Test5", record.get(1));
            assertEquals("Test6|Test7", record.get(2));
            assertEquals("Test8# This is another comment", record.get(3));

            assertFalse(parser.hasNextRecord());
        }

        try (CSVPrinter printer = new CSVPrinter(outStream, format.withQuoteMode(QuoteMode.ALL))) {
            printer.printRecord(1, null, null, null);
            printer.println();
            printer.printRecord(2, new Object[] { null, null, null });
            printer.println();
            printer.close();

            String csvString = outStream.toString();
            assertEquals(2 * Constants.RECORD_SEPARATOR.length() + 3 * Constants.DELIMITER.length() * 12 + 2 * Constants.COMMENT_START.length() + 6, csvString.length());
            String csv = csvString;
            assertTrue(csv.contains("null"));
            assertTrue(csv.contains(Constants.COMMENT_START + Constants.COMMENT_START));
            assertFalse(csv.contains(Constants.COMMENT_START + "Test"));
        }

        try (CSVPrinter printer = new CSVPrinter(outStream, format.withQuoteMode(QuoteMode.ALL).withEscapingMode(EscapingMode.ALWAYS))) {
            printer.printRecord(1, "Test\"1\"", "Test\"2|3\"", "Test\"4\"\"");
            printer.println();
            printer.close();

            String csvString = outStream.toString();
            assertEquals(3 * Constants.DELIMITER.length() * 10 + 2 * Constants.RECORD_SEPARATOR.length() + 2 * Constants.COMMENT_START.length() + 4, csvString.length());
            String csv = csvString;
            assertTrue(csv.contains("\"Test\"1\""));
            assertTrue(csv.contains("\"Test\"2|3\""));
            assertTrue(csv.contains("\"Test\"4\"\""));
            assertFalse(csv.contains("\"Test1\""));
            assertFalse(csv.contains("\"Test2|3\""));
            assertFalse(csv.contains("\"Test4\""));
        }

        try (CSVPrinter printer = new CSVPrinter(outStream, format.withQuoteMode(QuoteMode.MINIMAL).withEscapingMode(EscapingMode.ALWAYS))) {
            printer.printRecord(1, "Test\"1\"", "Test\"2|3\"", "Test\"4\"\"");
            printer.println();
            printer.close();

            String csvString = outStream.toString();
            assertEquals(3 * Constants.DELIMITER.length() * 10 + 2 * Constants.RECORD_SEPARATOR.length() + 2 * Constants.COMMENT_START.length() + 4, csvString.length());
            String csv = csvString;
            assertTrue(csv.contains("Test\"1\""));
            assertTrue(csv.contains("Test\"2|3\""));
            assertTrue(csv.contains("Test\"4\"\""));
            assertFalse(csv.contains("Test\"1"));
            assertFalse(csv.contains("Test\"2|3"));
            assertFalse(csv.contains("Test\"4"));
        }
    }
}