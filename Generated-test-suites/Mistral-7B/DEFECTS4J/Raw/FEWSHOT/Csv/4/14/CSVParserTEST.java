package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("src/test/resources/csv/test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(5, records.size());
        CSVRecord record1 = records.get(0);
        assertNull(record1.getRecordNumber());
        assertEquals(3, record1.size());
        assertEquals("Name", record1.getHeaderMap().get("Name"));
        assertEquals("Name1", record1.get("Name1"));
        assertEquals("Name2", record1.get("Name2"));
        assertEquals(1.0, record1.getDouble("Age"), 0.001);
        assertEquals("Male", record1.get("Sex"));
        assertEquals("1985-01-01", record1.get("Birthday"));
        CSVRecord record2 = records.get(1);
        assertEquals(2, record2.size());
        assertNull(record2.getHeaderMap());
        assertEquals("Name3", record2.get("Name3"));
        assertEquals("Value3", record2.get("Value3"));
    }

    @Test
    public void testParseString() throws IOException {
        String csv = "Name1,Name2,Age,Sex,Birthday\n" +
                "Name11,Name12,\"19.5\",Male,\"1984-12-31\"\n" +
                "Name21,Name22,20.0,Female,\"1983-01-01\"\n" +
                "Name31,Name32,21.5,Male,\"1982-12-31\"\n";
        CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        assertNull(record1.getRecordNumber());
        assertEquals(5, record1.size());
        assertEquals("Name1", record1.getHeaderMap().get("Name1"));
        assertEquals("Name11", record1.get("Name1"));
        assertEquals("Name2", record1.getHeaderMap().get("Name2"));
        assertEquals("Name12", record1.get("Name2"));
        assertEquals(19.5, record1.getDouble("Age"), 0.001);
        assertEquals("Male", record1.get("Sex"));
        assertEquals("1984-12-31", record1.get("Birthday"));
        CSVRecord record2 = records.get(1);
        assertEquals(5, record2.size());
        assertNull(record2.getHeaderMap());
        assertEquals("Name21", record2.get("Name1"));
        assertEquals("Name22", record2.get("Name2"));
        assertEquals(20.0, record2.getDouble("Age"), 0.001);
        assertEquals("Female", record2.get("Sex"));
        assertEquals("1983-01-01", record2.get("Birthday"));
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(5, records.size());
        CSVRecord record1 = records.get(0);
        assertNull(record1.getRecordNumber());
        assertEquals(3, record1.size());
        // ... same checks as in testParseFile()

        // Test with custom format
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("Name", "Name1", "Name2")
                .setRecordSeparator("\r\n")
                .build();
        parser = CSVParser.parse(url, Charset.forName("UTF-8"), customFormat);
        records = parser.getRecords();
        parser.close();
        assertEquals(5, records.size());
        CSVRecord record1Custom = records.get(0);
        assertNull(record1Custom.getRecordNumber());
        assertEquals(3, record1Custom.size());
        assertEquals("Name", record1Custom.getHeaderMap().get("Name1"));
        assertEquals("Name1", record1Custom.get("Name1"));
        assertEquals("Name2", record1Custom.get("Name2"));
        // ... same checks as in testParseFile()
    }
}