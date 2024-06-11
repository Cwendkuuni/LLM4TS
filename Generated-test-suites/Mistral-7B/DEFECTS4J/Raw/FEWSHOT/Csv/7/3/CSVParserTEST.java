package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("testData.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);
        assertArrayEquals( new String[]{"Name", "Age", "City"}, record1.getHeaderMap().keySet().toArray(new String[0]));
        assertEquals("Alice", record1.get(0));
        assertEquals(25, record1.get(1));
        assertEquals("New York", record1.get(2));
        assertEquals("Bob", record2.get(0));
        assertEquals(30, record2.get(1));
        assertEquals("Los Angeles", record2.get(2));
        assertNull(record3.get(0));
        assertNull(record3.get(1));
        assertNull(record3.get(2));
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Name,Age,City\nAlice,25,New York\nBob,30,Los Angeles\n";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(3, records.size());
        // Test record1 as previus test
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);
        assertArrayEquals( new String[]{"Name", "Age", "City"}, record1.getHeaderMap().keySet().toArray(new String[0]));
        assertEquals("Alice", record1.get(0));
        assertEquals(25, record1.get(1));
        assertEquals("New York", record1.get(2));
        assertEquals("Bob", record2.get(0));
        assertEquals(30, record2.get(1));
        assertEquals("Los Angeles", record2.get(2));
        assertNull(record3.get(0));
        assertNull(record3.get(1));
        assertNull(record3.get(2));
    }

    @Test
    public void testParseUrl() throws IOException {
        String csvData = "Name,Age,City\nAlice,25,New York\nBob,30,Los Angeles\n";
        URL url = new URL("http://test.com/testData.csv");
        CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(3, records.size());
        // Test record1 as previus test
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);
        assertArrayEquals( new String[]{"Name", "Age", "City"}, record1.getHeaderMap().keySet().toArray(new String[0]));
        assertEquals("Alice", record1.get(0));
        assertEquals(25, record1.get(1));
        assertEquals("New York", record1.get(2));
        assertEquals("Bob", record2.get(0));
        assertEquals(30, record2.get(1));
        assertEquals("Los Angeles", record2.get(2));
        assertNull(record3.get(0));
        assertNull(record3.get(1));
        assertNull(record3.get(2));
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        String csvData = "Name,Age,City\nAlice,25,New York\nBob,30,Los Angeles\n";
        CSVFormat customFormat = CSVFormat.custom().setHeader("Nome", "Età", "Città");
        CSVParser parser = new CSVParser(new StringReader(csvData), customFormat);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);
        Map<String, Integer> headerMap = record1.getHeaderMap();
        List<String> headerNames = new ArrayList<>(headerMap.keySet());
        Collections.sort(headerNames);
        List<String> expectedHeaderNames = Arrays.asList("Età", "Città", "Nome");
        assertEquals(expectedHeaderNames, headerNames);
        assertEquals("Alice", record1.get(headerMap.get("Nome")));
        assertEquals(25, record1.get(headerMap.get("Età")));
        assertEquals("New York", record1.get(headerMap.get("Città")));
        assertEquals("Bob", record2.get(headerMap.get("Nome")));
        assertEquals(30, record2.get(headerMap.get("Età")));
        assertEquals("Los Angeles", record2.get(headerMap.get("Città")));
        assertNull(record3.get(headerMap.get("Nome")));
        assertNull(record3.get(headerMap.get("Età")));
        assertNull(record3.get(headerMap.get("Città")));
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        File file = new File("testDataEmpty.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(0, records.size());
    }

    @Test
    public void testParseEmptyString() throws IOException {
        String csvData = "";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(0, records.size());
    }

    @Test
    public void testParseEmptyUrl() throws IOException {
        URL url = new URL("http://test.com/testDataEmpty.csv");
        CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(0, records.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws IOException {
        File file = new File("testData.csv");
        CSVParser parser = CSVParser.parse(file, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() throws IOException {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
    }
}