import org.apache.commons.csv.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws Exception {
        final String csvData = "a,b\n1,2\n3,4\n";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.RFC4180);
    }

    @After
    public void tearDown() throws Exception {
        if (parser != null) {
            parser.close();
        }
    }

    /**
     * Test parsing a simple CSV file.
     */
    @Test
    public void testParseSimpleCSV() throws Exception {
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record;
        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            assertEquals(Integer.valueOf(i + 1), record.getRecordNumber());
            assertEquals(2, record.size());

            assertEquals("a", record.get(0));
            assertEquals("b", record.get(1));
        }
    }

    /**
     * Test parsing a CSV file with empty values.
     */
    @Test
    public void testParseCSVWithEmptyValues() throws Exception {
        final String csvData = "a,,b\n1,,\n3,,4";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.RFC4180);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());

        CSVRecord record;
        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            assertEquals(Integer.valueOf(i + 1), record.getRecordNumber());
            assertEquals(2, record.size());

            assertEquals("a", record.get(0));
            assertNull(record.get(1));
        }
    }

    /**
     * Test parsing a CSV file with header.
     */
    @Test
    public void testParseCSVWithHeader() throws Exception {
        final String csvData = "Name,Age\nJohn,30\nMary,25";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record;
        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            assertEquals(Integer.valueOf(i + 1), record.getRecordNumber());
            assertEquals(2, record.size());

            assertEquals("Name", record.getHeader().get(0));
            assertEquals("Age", record.getHeader().get(1));

            assertEquals("John", record.get(0));
            assertEquals("30", record.get(1));

            if (i == 0) {
                assertEquals("Name", record.getHeaderName(0));
                assertEquals("Age", record.getHeaderName(1));
            }
        }
    }

    /**
     * Test parsing a CSV file with comment.
     */
    @Test
    public void testParseCSVWithComment() throws Exception {
        final String csvData = "a,b\n1,2# This is a comment\n3,4";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.RFC4180);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());

        CSVRecord record;
        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            assertEquals(Integer.valueOf(i + 1), record.getRecordNumber());
            assertEquals(2, record.size());

            assertEquals("a", record.get(0));
            assertEquals("b", record.get(1));

            String comment = record.getComment();
            if (i == 1) {
                assertEquals("This is a comment", comment);
            } else {
                assertNull(comment);
            }
        }
    }

    /**
     * Test parsing a CSV file with header and comment.
     */
    @Test
    public void testParseCSVWithHeaderAndComment() throws Exception {
        final String csvData = "Name,Age\nJohn,30# This is a comment for John record\nMary,25# This is a comment for Mary record";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record;
        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            assertEquals(Integer.valueOf(i + 1), record.getRecordNumber());
            assertEquals(2, record.size());

            assertEquals("Name", record.getHeader().get(0));
            assertEquals("Age", record.getHeader().get(1));

            assertEquals("John", record.get(0));
            assertEquals("30", record.get(1));

            if (i == 0) {
                assertEquals("Name", record.getHeaderName(0));
                assertEquals("Age", record.getHeaderName(1));
                assertEquals("This is a comment for John record", record.getComment());
            }

            if (i == 1) {
                assertEquals("Mary", record.get(0));
                assertEquals("25", record.get(1));
                assertNull(record.getComment());
            }
        }
    }

    /**
     * Test parsing a CSV file with empty header.
     */
    @Test
    public void testParseCSVWithEmptyHeader() throws Exception {
        final String csvData = "a,b\n1,2";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record;
        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            assertEquals(Integer.valueOf(i + 1), record.getRecordNumber());
            assertEquals(2, record.size());

            assertNull(record.getHeader());
            assertNull(record.getHeaderName(0));
        }
    }

    /**
     * Test parsing a CSV file with missing header.
     */
    @Test(expected = NoSuchElementException.class)
    public void testParseCSVWithMissingHeader() throws Exception {
        final String csvData = "a,b\n1,2";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.nextRecord(); // consume the first record
        parser.nextRecord(); // try to consume the second record without header
    }

    /**
     * Test parsing a CSV file with malformed data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseCSVWithMalformedData() throws Exception {
        final String csvData = "a,b\n1,2\n3,4,5";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.RFC4180);
        parser.nextRecord();
        parser.nextRecord();
    }

    /**
     * Test parsing a CSV file with a leading empty line.
     */
    @Test
    public void testParseCSVWithLeadingEmptyLine() throws Exception {
        final String csvData = "\nName,Age\nJohn,30\nMary,25";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record;
        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            assertEquals(Integer.valueOf(i + 1), record.getRecordNumber());
            assertEquals(2, record.size());

            assertEquals("Name", record.getHeader().get(0));
            assertEquals("Age", record.getHeader().get(1));

            assertEquals("John", record.get(0));
            assertEquals("30", record.get(1));

            if (i == 0) {
                assertEquals("Name", record.getHeaderName(0));
                assertEquals("Age", record.getHeaderName(1));
            }
        }
    }

    /**
     * Test parsing a CSV file with a trailing empty line.
     */
    @Test
    public void testParseCSVWithTrailingEmptyLine() throws Exception {
        final String csvData = "Name,Age\nJohn,30\nMary,25\n";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record;
        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            assertEquals(Integer.valueOf(i + 1), record.getRecordNumber());
            assertEquals(2, record.size());

            assertEquals("Name", record.getHeader().get(0));
            assertEquals("Age", record.getHeader().get(1));

            assertEquals("John", record.get(0));
            assertEquals("30", record.get(1));

            if (i == 0) {
                assertEquals("Name", record.getHeaderName(0));
                assertEquals("Age", record.getHeaderName(1));
            }
        }
    }
}