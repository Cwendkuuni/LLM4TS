import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.commons.csv.CSVFormat.RFC4180;
import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.builder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        String csvContent = "name,age,city\nJohn,35,New York\nMary,28,Los Angeles\n";
        this.parser = parse(new StringReader(csvContent), RFC4180);
    }

    @Test
    public void testCreateParserFromFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = parse(file, RFC4180);
        assertTrue(parser != null);
        parser.close();
        file.delete();
    }

    @Test
    public void testCreateParserFromString() throws IOException {
        String csvContent = "name,age,city\nJohn,35,New York\nMary,28,Los Angeles\n";
        CSVParser parser = parse(new StringReader(csvContent), RFC4180);
        assertTrue(parser != null);
        parser.close();
    }

    @Test
    public void testCreateParserFromURL() throws IOException {
        String csvContent = "name,age,city\nJohn,35,New York\nMary,28,Los Angeles\n";
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = parse(url, Charset.forName("UTF-8"), RFC4180);
        assertTrue(parser != null);
        parser.close();
    }

    @Test
    public void testParseCSVRecord() throws IOException {
        CSVRecord record = this.parser.nextRecord();
        assertArrayEquals(new String[]{"John", "35", "New York"}, record.toArray());
    }

    @Test
    public void testParseEmptyCSVRecord() throws IOException {
        CSVRecord record = this.parser.nextRecord();
        assertNull(record);
    }

    @Test
    public void testParseCSVRecords() throws IOException {
        List<CSVRecord> records = this.parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"John", "35", "New York"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Mary", "28", "Los Angeles"}, record2.toArray());
    }

    @Test
    public void testParseCSVRecordsWithNullValues() throws IOException {
        String csvContent = "name,age,city\nJohn,35,New York\nMary,null,Los Angeles";
        CSVParser parser = parse(new StringReader(csvContent), RFC4180);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"John", "35", "New York"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Mary", null, "Los Angeles"}, record2.toArray());

        parser.close();
    }

    @Test
    public void testParseCSVRecordsWithEmptyLine() throws IOException {
        String csvContent = "name,age,city\nJohn,35,New York\n\nMary,28,Los Angeles";
        CSVParser parser = parse(new StringReader(csvContent), RFC4180);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"John", "35", "New York"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Mary", "28", "Los Angeles"}, record2.toArray());

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSVFile() throws IOException {
        String csvContent = "name,age,city\nJohn,35,New York\nMary,28,Los Angeles\nname,age,city";
        CSVParser parser = parse(new StringReader(csvContent), RFC4180);
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord(); // This should fail
        parser.close();
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        long currentLineNumber = this.parser.getCurrentLineNumber();
        assertEquals(1, currentLineNumber);
        this.parser.nextRecord();
        currentLineNumber = this.parser.getCurrentLineNumber();
        assertEquals(2, currentLineNumber);
        this.parser.close();
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        long recordNumber = this.parser.getRecordNumber();
        assertEquals(0, recordNumber);
        this.parser.nextRecord();
        recordNumber = this.parser.getRecordNumber();
        assertEquals(1, recordNumber);
        this.parser.nextRecord();
        recordNumber = this.parser.getRecordNumber();
        assertEquals(2, recordNumber);
        this.parser.close();
    }

    @Test
    public void testIterator() throws IOException {
        List<CSVRecord> records = new ArrayList<>();
        records.add(builder().add("John").add(35).add("New York").build());
        records.add(builder().add("Mary").add(28).add("Los Angeles").build());
        Iterator<CSVRecord> iterator = records.iterator();

        CSVRecord record = iterator.next();
        assertArrayEquals(new String[]{"John", "35", "New York"}, record.toArray());

        record = iterator.next();
        assertArrayEquals(new String[]{"Mary", "28", "Los Angeles"}, record.toArray());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCloseParser() throws IOException {
        this.parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseClosedParser() throws IOException {
        this.parser.close();
        this.parser.nextRecord();
    }

    @Test
    public void testParseExcelFormat() throws IOException {
        String csvContent = "Name,Age,City\nJohn,35,New York\nMary,28,Los Angeles\n";
        CSVParser parser = parse(new StringReader(csvContent), EXCEL);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"John", "35", "New York"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Mary", "28", "Los Angeles"}, record2.toArray());

        parser.close();
    }

    @Test
    public void testParseCSVWithDelimiterOtherThanComma() throws IOException {
        String csvContent = "name|age|city\nJohn|35|New York\nMary|28|Los Angeles\n";
        CSVFormat format = new CSVFormatBuilder().setDelimiter('|').build();
        CSVParser parser = parse(new StringReader(csvContent), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"John", "35", "New York"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Mary", "28", "Los Angeles"}, record2.toArray());

        parser.close();
    }
}