package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File csvData = new File("src/test/resources/csv/simple.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        assertEquals(3, parser.getHeaderMap().size());

        for (CSVRecord record : records) {
            assertNotNull(record);
            assertArrayEquals(new String[]{"Header1", "Header2", "Header3"}, record.getHeaderNames());
            assertArrayEquals(new String[]{"Value1", "Value2", "Value3"}, record.getValues());
        }
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Header1,Header2,Header3\nValue1,Value2,Value3";
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());
        assertEquals(3, parser.getHeaderMap().size());

        CSVRecord record = records.get(0);
        assertNotNull(record);
        assertArrayEquals(new String[]{"Header1", "Header2", "Header3"}, record.getHeaderNames());
        assertArrayEquals(new String[]{"Value1", "Value2", "Value3"}, record.getValues());
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("http://example.com/csv/simple.csv");
        CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        assertEquals(3, parser.getHeaderMap().size());

        for (CSVRecord record : records) {
            assertNotNull(record);
            assertArrayEquals(new String[]{"Header1", "Header2", "Header3"}, record.getHeaderNames());
            assertArrayEquals(new String[]{"Value1", "Value2", "Value3"}, record.getValues());
        }
    }

    @Test
    public void testParseWithCustomFormat() throws IOException {
        String csvData = "Value1,Value2\nValue3,Value4";
        CSVFormat customFormat = new CSVFormatBuilder().setHeaderNames(new String[]{"CustomHeader1", "CustomHeader2"})
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(csvData), customFormat);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertEquals("CustomHeader1", headerMap.get("0"));
        assertEquals("CustomHeader2", headerMap.get("1"));

        for (CSVRecord record : records) {
            assertNotNull(record);
            assertArrayEquals(new String[]{"Value1", "Value2"}, record.getValues());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() throws IOException {
        CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws IOException {
        File csvData = new File("src/test/resources/csv/simple.csv");
        CSVParser.parse(csvData, null);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFormat() throws IOException {
        File csvData = new File("src/test/resources/csv/invalid.csv");
        CSVParser.parse(csvData, new CSVFormatBuilder().build());
    }
}