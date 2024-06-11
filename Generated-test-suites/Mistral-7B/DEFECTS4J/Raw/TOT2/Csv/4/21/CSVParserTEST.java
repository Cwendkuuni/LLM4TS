import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                "header1,header2,header3\n" +
                        "1,1,1\n" +
                        "2,2,2\n" +
                        "3,3,3\n" +
                        "null,null,null".getBytes());
        parser = new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT);
    }

    @Test
    public void testParseBasic() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (CSVRecord record : records) {
            assertNotNull(record);
            assertArrayEquals(new String[]{"1", "1", "1"}, record.toArray());
            assertEquals(1, record.getRecordNumber());
        }
    }

    @Test
    public void testParseWithHeader() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals("header1", firstRecord.getHeaderMap().get("header1"));
        assertEquals("header2", firstRecord.getHeaderMap().get("header2"));
        assertEquals("header3", firstRecord.getHeaderMap().get("header3"));
        assertArrayEquals(new String[]{"1", "1", "1"}, record.toArray());
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmpty() throws IOException {
        parser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() throws IOException {
        StringReader reader = new StringReader("invalid format");
        parser = new CSVParser(reader, null);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testParseIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                "header1,header2,header3\n" +
                        "1,1,1\n" +
                        "2,2,2\n" +
                        "3,3,3\n" +
                        "error".getBytes());
        parser = new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT);
        parser.getRecords();
    }
}