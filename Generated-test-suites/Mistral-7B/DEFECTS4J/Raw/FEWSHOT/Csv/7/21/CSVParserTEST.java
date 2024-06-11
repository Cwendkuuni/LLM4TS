package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File csvData = new File("src/test/resources/csv/test.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.RFC4180);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"name1", "value1", "value2"}, record.toArray());
        }
    }

    @Test
    public void testParseFileWithExcelFormat() throws IOException {
        File csvData = new File("src/test/resources/csv/test_excel.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.EXCEL);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"Name", "Value1", "Value2"}, record.getHeaderMap().keySet().toArray());
            assertArrayEquals(new String[]{"name1", "value1", "value2"}, record.toArray());
        }
    }

    @Test
    public void testParseString() throws IOException {
        CSVParser parser = CSVParser.parse(
                "a,b\nc,d\n",
                CSVFormat.builder()
                        .setHeader(new String[]{"Col1", "Col2"})
                        .build());

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Map<String, Integer> headerMap = record.getHeaderMap();
            assertArrayEquals(new String[]{"Col1", "Col2"}, headerMap.keySet().toArray());
            assertArrayEquals(new String[]{"a", "b"}, record.toArray());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() throws IOException {
        File csvData = null;
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.RFC4180);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws IOException {
        File csvData = new File("src/test/resources/csv/test.csv");
        CSVFormat format = null;
        CSVParser parser = CSVParser.parse(csvData, format);
    }

    @Test(expected = IOException.class)
    public void testParseNullReader() throws IOException {
        CSVParser parser = new CSVParser(null, CSVFormat.RFC4180);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidUrl() throws IOException {
        URL url = new URL("file:///src/test/resources/csv/test.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVParser parser = CSVParser.parse(url, charset, CSVFormat.RFC4180);

        try {
            parser.getRecords();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetRecordsAfterClose() throws IOException {
        File csvData = new File("src/test/resources/csv/test.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.RFC4180);
        parser.close();
        parser.getRecords();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorAfterClose() throws IOException {
        File csvData = new File("src/test/resources/csv/test.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.RFC4180);
        Iterator<CSVRecord> iterator = parser.iterator();
        parser.close();
        iterator.next();
    }

    @Test
    public void testGetRecordsIntoList() throws IOException {
        File csvData = new File("src/test/resources/csv/test.csv");
        List<CSVRecord> records = new ArrayList<>();
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.RFC4180);
        parser.getRecords(records);
        assertEquals(3, records.size());
    }

    @Test
    public void testGetRecordsWithCustomFormat() throws IOException {
        File csvData = new File("src/test/resources/csv/test.csv");
        CSVFormat customFormat = CSVFormat.builder()
                .setHeader(new String[]{"Col1", "Col2"})
                .build();
        List<CSVRecord> records = new ArrayList<>();
        CSVParser parser = CSVParser.parse(csvData, customFormat);
        parser.getRecords(records);

        for (CSVRecord record : records) {
            Map<String, Integer> headerMap = record.getHeaderMap();
            assertArrayEquals(new String[]{"Col1", "Col2"}, headerMap.keySet().toArray());
            assertArrayEquals(new String[]{"a", "b"}, record.toArray());
        }
    }
}