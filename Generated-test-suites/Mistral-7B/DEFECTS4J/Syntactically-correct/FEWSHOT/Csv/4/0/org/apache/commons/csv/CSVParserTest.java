package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFileWithHeader() throws IOException {
        final File csvData = new File("src/test/resources/csvParserTestData.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals(4, records.size());
        assertEquals("Name", headerMap.get("Name"));
        assertEquals("Age", headerMap.get("Age"));
        assertEquals("City", headerMap.get("City"));
        assertEquals("Country", headerMap.get("Country"));

        assertEquals("John Doe", firstRecord.get("Name"));
        assertEquals(25, firstRecord.get("Age"));
        assertEquals("London", firstRecord.get("City"));
        assertEquals("England", firstRecord.get("Country"));
    }

    @Test
    public void testParseFileWithoutHeader() throws IOException {
        final File csvData = new File("src/test/resources/csvParserTestDataWithoutHeader.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        assertEquals(2, records.size());
        assertEquals("1,John Doe,25,London,England", firstRecord.toString());
        assertEquals("2,Jane Doe,30,Paris,France", records.get(1).toString());
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Name,Age,City,Country\n" +
                "John Doe,25,London,England\n" +
                "Jane Doe,30,Paris,France";
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        assertEquals(2, records.size());
        assertEquals("Name", firstRecord.getColumnName(0));
        assertEquals("Age", firstRecord.getColumnName(1));
        assertEquals("City", firstRecord.getColumnName(2));
        assertEquals("Country", firstRecord.getColumnName(3));

        assertEquals("John Doe", firstRecord.get("Name"));
        assertEquals(25, firstRecord.get("Age"));
        assertEquals("London", firstRecord.get("City"));
        assertEquals("England", firstRecord.get("Country"));
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("http://example.com/csvData.csv");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        assertEquals(4, records.size());
        assertEquals("Name", firstRecord.getColumnName(0));
        assertEquals("Age", firstRecord.getColumnName(1));
        assertEquals("City", firstRecord.getColumnName(2));
        assertEquals("Country", firstRecord.getColumnName(3));

        assertEquals("John Doe", firstRecord.get("Name"));
        assertEquals(25, firstRecord.get("Age"));
        assertEquals("London", firstRecord.get("City"));
        assertEquals("England", firstRecord.get("Country"));

        parser.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String csvData = "Name,Age,City,Country\n" +
                "John Doe,25,London,England\n" +
                "Jane Doe,30,Paris,France";

        byte[] csvDataBytes = csvData.getBytes("UTF-8");

        InputStream inputStream = new ByteArrayInputStream(csvDataBytes);
        CSVParser parser = new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        assertEquals(2, records.size());
        assertEquals("Name", firstRecord.getColumnName(0));
        assertEquals("Age", firstRecord.getColumnName(1));
        assertEquals("City", firstRecord.getColumnName(2));
        assertEquals("Country", firstRecord.getColumnName(3));

        assertEquals("John Doe", firstRecord.get("Name"));
        assertEquals(25, firstRecord.get("Age"));
        assertEquals("London", firstRecord.get("City"));
        assertEquals("England", firstRecord.get("Country"));

        parser.close();
    }

    @Test
    public void testParseMemory() throws IOException {
        String csvData = "Name,Age,City,Country\n" +
                "John Doe,25,London,England\n" +
                "Jane Doe,30,Paris,France";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals("Name", records.get(0).getColumnName(0));
        assertEquals("Age", records.get(0).getColumnName(1));
        assertEquals("City", records.get(0).getColumnName(2));
        assertEquals("Country", records.get(0).getColumnName(3));

        assertEquals("John Doe", records.get(0).get("Name"));
        assertEquals(25, records.get(0).get("Age"));
        assertEquals("London", records.get(0).get("City"));
        assertEquals("England", records.get(0).get("Country"));

        assertEquals("Jane Doe", records.get(1).get("Name"));
        assertEquals(30, records.get(1).get("Age"));
        assertEquals("Paris", records.get(1).get("City"));
        assertEquals("France", records.get(1).get("Country"));

        parser.close();
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        File emptyCsvData = new File("src/test/resources/emptyCsvParserTestData.csv");
        CSVParser parser = CSVParser.parse(emptyCsvData, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
        parser.close();
    }
}