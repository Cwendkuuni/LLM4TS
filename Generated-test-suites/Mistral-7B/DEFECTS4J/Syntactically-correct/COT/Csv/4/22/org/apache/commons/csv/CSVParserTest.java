import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setup() throws IOException {
        String csvData = "name,age\nAlice,23\nBob,25\n";
        parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
    }

    @Test
    public void testParseBasic() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("Alice", record.get(0));
            Assert.assertEquals("23", record.get(1));
            Assert.assertNull(record.get(2));
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEndOfStream() throws IOException {
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
    }

    @Test
    public void testParseInvalidCsv() throws IOException {
        String invalidCsv = "name,age\nAlice,23\nBob,25\nname,age";
        CSVParser parserInvalidCsv = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(invalidCsv.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsInvalidCsv = parserInvalidCsv.getRecords();
        Assert.assertEquals(2, recordsInvalidCsv.size());
        for (CSVRecord record : recordsInvalidCsv) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("Alice", record.get(0));
            Assert.assertEquals("23", record.get(1));
            Assert.assertEquals("name", record.get(2)); // This should be null
        }
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFormat() throws IOException {
        String invalidFormatCsv = "name,age\nAlice,23\nBob,25\nname:age";
        CSVParser parserInvalidFormatCsv = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(invalidFormatCsv.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        parserInvalidFormatCsv.nextRecord();
        parserInvalidFormatCsv.nextRecord();
    }

    @Test
    public void testParseEmptyCsv() throws IOException {
        String emptyCsv = "";
        CSVParser parserEmptyCsv = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(emptyCsv.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsEmptyCsv = parserEmptyCsv.getRecords();
        Assert.assertEquals(0, recordsEmptyCsv.size());
    }

    @Test
    public void testParseComment() throws IOException {
        String csvWithComment = "name,age\nAlice,23\n# This is a comment\nBob,25\n";
        CSVParser parserWithComment = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvWithComment.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsWithComment = parserWithComment.getRecords();
        Assert.assertEquals(2, recordsWithComment.size());
        for (CSVRecord record : recordsWithComment) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("Alice", record.get(0));
            Assert.assertEquals("23", record.get(1));
            Assert.assertEquals("", record.getComment());
        }
    }

    @Test
    public void testParseMissingHeader() throws IOException {
        String csvWithoutHeader = "Alice,23\nBob,25\n";
        CSVParser parserWithoutHeader = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvWithoutHeader.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsWithoutHeader = parserWithoutHeader.getRecords();
        Assert.assertEquals(2, recordsWithoutHeader.size());
        for (CSVRecord record : recordsWithoutHeader) {
            Assert.assertArrayEquals(Collections.emptyMap().keySet().toArray(new String[0]), record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("Alice", record.get(0));
            Assert.assertEquals("23", record.get(1));
        }
    }

    @Test
    public void testParseHeaderMismatch() throws IOException {
        String csvWithHeaderMismatch = "name,age,id\nAlice,23\nBob,25";
        CSVParser parserWithHeaderMismatch = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvWithHeaderMismatch.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsWithHeaderMismatch = parserWithHeaderMismatch.getRecords();
        Assert.assertEquals(2, recordsWithHeaderMismatch.size());
        for (CSVRecord record : recordsWithHeaderMismatch) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("Alice", record.get(0));
            Assert.assertEquals("23", record.get(1));
            Assert.assertNull(record.get(2));
        }
    }

    @Test
    public void testParseEmptyRecord() throws IOException {
        String csvWithEmptyRecord = "name,age\nAlice,23\n,\nBob,25";
        CSVParser parserWithEmptyRecord = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvWithEmptyRecord.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsWithEmptyRecord = parserWithEmptyRecord.getRecords();
        Assert.assertEquals(3, recordsWithEmptyRecord.size());
        for (int i = 0; i < recordsWithEmptyRecord.size(); i++) {
            CSVRecord record = recordsWithEmptyRecord.get(i);
            if (i == 1) {
                Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
                Assert.assertEquals(2, record.size());
                Assert.assertEquals("", record.get(0));
                Assert.assertEquals("", record.get(1));
            } else {
                Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
                Assert.assertEquals(2, record.size());
                Assert.assertEquals("Alice", record.get(0));
                Assert.assertEquals("23", record.get(1));
            }
        }
    }

    @Test
    public void testParseQuotedValues() throws IOException {
        String csvQuotedValues = "name,age\nAlice,\"Smith, Jnr.\",23\nBob,\"Schmidt, Dr.\",25";
        CSVParser parserQuotedValues = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvQuotedValues.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsQuotedValues = parserQuotedValues.getRecords();
        Assert.assertEquals(2, recordsQuotedValues.size());
        for (CSVRecord record : recordsQuotedValues) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Alice", record.get(0));
            Assert.assertEquals("Smith, Jnr.", record.get(1));
            Assert.assertEquals("23", record.get(2));
            Assert.assertEquals("Bob", record.get(3));
            Assert.assertEquals("Schmidt, Dr.", record.get(4));
            Assert.assertEquals("25", record.get(5));
        }
    }

    @Test
    public void testParseQuotedValuesWithEscapedQuotes() throws IOException {
        String csvQuotedValuesWithEscapes = "name,age\nAlice,\"Smith, Jnr.\",23\nBob,\"Schmidt, Dr. \"O'Reilly\",25";
        CSVParser parserQuotedValuesWithEscapes = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvQuotedValuesWithEscapes.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsQuotedValuesWithEscapes = parserQuotedValuesWithEscapes.getRecords();
        Assert.assertEquals(2, recordsQuotedValuesWithEscapes.size());
        for (CSVRecord record : recordsQuotedValuesWithEscapes) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Alice", record.get(0));
            Assert.assertEquals("Smith, Jnr.", record.get(1));
            Assert.assertEquals("Bob", record.get(2));
            Assert.assertEquals("Schmidt, Dr. O'Reilly", record.get(3));
            Assert.assertEquals("25", record.get(4));
        }
    }

    @Test
    public void testParseQuotedValuesWithEmptyValue() throws IOException {
        String csvQuotedValuesWithEmptyValue = "name,age\nAlice,\"Smith, Jnr.\",23\nBob,\"\",25";
        CSVParser parserQuotedValuesWithEmptyValue = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvQuotedValuesWithEmptyValue.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsQuotedValuesWithEmptyValue = parserQuotedValuesWithEmptyValue.getRecords();
        Assert.assertEquals(2, recordsQuotedValuesWithEmptyValue.size());
        for (CSVRecord record : recordsQuotedValuesWithEmptyValue) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Alice", record.get(0));
            Assert.assertEquals("Smith, Jnr.", record.get(1));
            Assert.assertEquals("Bob", record.get(2));
            Assert.assertEquals("", record.get(3));
            Assert.assertEquals("25", record.get(4));
        }
    }

    @Test
    public void testParseQuotedValuesWithMissingQuotes() throws IOException {
        String csvQuotedValuesWithMissingQuotes = "name,age\nAlice,Smith, Jnr.,23\nBob,Schmidt, Dr. O'Reilly,25";
        CSVParser parserQuotedValuesWithMissingQuotes = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvQuotedValuesWithMissingQuotes.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsQuotedValuesWithMissingQuotes = parserQuotedValuesWithMissingQuotes.getRecords();
        Assert.assertEquals(2, recordsQuotedValuesWithMissingQuotes.size());
        for (CSVRecord record : recordsQuotedValuesWithMissingQuotes) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Alice", record.get(0));
            Assert.assertEquals("Smith, Jnr.", record.get(1));
            Assert.assertEquals("Bob", record.get(2));
            Assert.assertEquals("Schmidt, Dr. O'Reilly", record.get(3));
            Assert.assertEquals("25", record.get(4));
        }
    }

    @Test
    public void testParseQuotedValuesWithMissingQuotesInFirstColumn() throws IOException {
        String csvQuotedValuesWithMissingQuotesInFirstColumn = "name,age\nAlice,Smith, Jnr.,23\nBob,\"Schmidt, Dr. O'Reilly\",25";
        CSVParser parserQuotedValuesWithMissingQuotesInFirstColumn = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvQuotedValuesWithMissingQuotesInFirstColumn.getBytes(StandardCharsets.UTF_8))), CSVFormat.DEFAULT);
        List<CSVRecord> recordsQuotedValuesWithMissingQuotesInFirstColumn = parserQuotedValuesWithMissingQuotesInFirstColumn.getRecords();
        Assert.assertEquals(2, recordsQuotedValuesWithMissingQuotesInFirstColumn.size());
        for (CSVRecord record : recordsQuotedValuesWithMissingQuotesInFirstColumn) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Alice", record.get(0));
            Assert.assertEquals("Smith, Jnr.", record.get(1));
            Assert.assertEquals("Bob", record.get(2));
            Assert.assertEquals("Schmidt, Dr. O'Reilly", record.get(3));
            Assert.assertEquals("25", record.get(4));
        }
    }

}