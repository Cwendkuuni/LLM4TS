import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.csv.*;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(file, format);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        String data = "name,age\nJohn,25";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(data, format);
        assertNotNull(parser);
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        String data = "name,age\nJohn,25\nJane,30";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(data, format);
        assertEquals(0, parser.getCurrentLineNumber());
        parser.nextRecord();
        assertEquals(1, parser.getCurrentLineNumber());
    }

    @Test
    public void testGetRecords() throws IOException {
        String data = "name,age\nJohn,25\nJane,30";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(data, format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        String data = "name,age\nJohn,25\nJane,30";
        CSVFormat format = CSVFormat.DEFAULT.withHeader("name", "age");
        CSVParser parser = CSVParser.parse(data, format);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(2, headerMap.size());
    }

    @Test
    public void testNextRecord() throws IOException {
        String data = "name,age\nJohn,25";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = new CSVParser(new StringReader(data), format);
        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
    }

}