import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test class for CSVParser.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file with no header.
     */
    @Test
    public void testSimpleCSV() throws Exception {
        String csvContent = "1,2\n3,4";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("1", record.get(0));
        assertEquals("2", record.get(1));
        assertNull(record.get(2));
    }

    /**
     * Test parsing a CSV file with a header.
     */
    @Test
    public void testCSVWithHeader() throws Exception {
        String csvContent = "Name,Age\nJohn,25\nMary,30";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        assertEquals(2, firstRecord.size());
        assertEquals("Name", firstRecord.getHeaderIndex("Name"));
        assertEquals("Age", firstRecord.getHeaderIndex("Age"));
        assertEquals("John", firstRecord.get(0));
        assertEquals("25", firstRecord.get(1));
        assertNull(firstRecord.get(2));

        CSVRecord secondRecord = records.get(1);
        assertEquals("Mary", secondRecord.get(0));
        assertEquals("30", secondRecord.get(1));
        assertNull(secondRecord.get(2));
    }

    /**
     * Test parsing an empty CSV file.
     */
    @Test
    public void testEmptyCSV() throws Exception {
        String csvContent = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertSame(0, records.size());
    }

    /**
     * Test parsing a CSV file with invalid characters.
     */
    @Test(expected = IOException.class)
    public void testInvalidCSV() throws Exception {
        String csvContent = "Name,Age\nJohn,25\nMary,30\nName,30";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        parser.getRecords(); // Read all records to cause parsing
    }
}