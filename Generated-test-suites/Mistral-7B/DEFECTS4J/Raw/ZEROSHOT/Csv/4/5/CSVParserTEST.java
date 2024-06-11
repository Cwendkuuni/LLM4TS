import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CSVParser Test")
public class CSVParserTest {

    @Test
    public void testParseRfc4180() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Los Angeles\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            assertEquals(3, record.size());
            assertEquals("Name", record.getHeaderMap().get("Name").toString());
            assertEquals("Age", record.getHeaderMap().get("Age").toString());
            assertEquals("City", record.getHeaderMap().get("City").toString());
            assertEquals("John", record.get("Name"));
            assertEquals(30, record.get("Age"));
            assertEquals("New York", record.get("City"));
        }
        parser.close();
    }

    @Test
    public void testParseExcel() throws Exception {
        String csvContent = "Name, Age, City\nJohn, 30, New York\nMike, 25, Los Angeles\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            assertEquals(3, record.size());
            assertEquals("Name", record.getHeaderMap().get("Name").toString());
            assertEquals("Age", record.getHeaderMap().get("Age").toString());
            assertEquals("City", record.getHeaderMap().get("City").toString());
            assertEquals("John", record.get("Name"));
            assertEquals(30, record.get("Age"));
            assertEquals("New York", record.get("City"));
        }
        parser.close();
    }

    @Test
    public void testParseCustomFormat() throws Exception {
        String csvContent = "Name, Age, City\nJohn, 30, New York\nMike, 25, Los Angeles\n";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeaderNames("Name", "Age", "City")
                .setDelimiter(';')
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            assertEquals(3, record.size());
            assertEquals("Name", record.getHeaderMap().get("Name").toString());
            assertEquals("Age", record.getHeaderMap().get("Age").toString());
            assertEquals("City", record.getHeaderMap().get("City").toString());
            assertEquals("John", record.get("Name"));
            assertEquals(30, record.get("Age"));
            assertEquals("New York", record.get("City"));
        }
        parser.close();
    }

    @Test
    public void testGetHeaderMap() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Los Angeles\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.RFC4180);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(3, headerMap.size());
        assertEquals("Name", headerMap.get("Name").toString());
        assertEquals("Age", headerMap.get("Age").toString());
        assertEquals("City", headerMap.get("City").toString());
        parser.close();
    }

    @Test
    public void testGetCurrentLineNumber() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Los Angeles\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.RFC4180);
        long currentLineNumber = parser.getCurrentLineNumber();
        assertEquals(1, currentLineNumber);
        parser.nextRecord();
        currentLineNumber = parser.getCurrentLineNumber();
        assertEquals(2, currentLineNumber);
        parser.nextRecord();
        currentLineNumber = parser.getCurrentLineNumber();
        assertEquals(3, currentLineNumber);
        parser.close();
    }

    @Test
    public void testGetRecordNumber() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Los Angeles";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        long recordNumber = parser.getRecordNumber();
        assertEquals(0, recordNumber);
        parser.nextRecord();
        recordNumber = parser.getRecordNumber();
        assertEquals(1, recordNumber);
        parser.nextRecord();
        recordNumber = parser.getRecordNumber();
        assertEquals(2, recordNumber);
        parser.close();
    }

    @Test
    public void testGetRecords() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Los Angeles";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        parser.close();
    }
}