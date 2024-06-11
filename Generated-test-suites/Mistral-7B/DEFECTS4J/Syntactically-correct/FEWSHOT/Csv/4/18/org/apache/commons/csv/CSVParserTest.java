package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        String csvFilePath = "testData/simple.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col1", "Col2"}, record1.getValues());
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"", "Col2", "Col3"}, record2.getValues());
        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Col1", "", "Col3"}, record3.getValues());
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "Col1,Col2,Col3\n" +
                " ,Col2,Col3\n" +
                "Col1,      ,Col3\n";
        InputStream stream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.UTF_8));
        CSVParser parser = CSVParser.parse(new InputStreamReader(stream), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col1", "Col2"}, record1.getValues());
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"", "Col2", "Col3"}, record2.getValues());
        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Col1", "", "Col3"}, record3.getValues());
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "https://example.com/data.csv";
        InputStream stream = CSVParserTest.class.getResourceAsStream(urlString);
        CSVParser parser = CSVParser.parse(new InputStreamReader(stream), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        // Replace this call with actual test data
        assertEquals(3, records.size());
    }

    @Test
    public void testEmptyCsvFile() throws IOException {
        String csvFilePath = "testData/empty.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(0, records.size());
    }

    @Test
    public void testInvalidCsvFile() throws IOException {
        String csvFilePath = "testData/invalid.csv";
        assertThrows(IOException.class, () -> {
            CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
            parser.getRecords();
        });
    }

    @Test
    public void testParseRecordWise() throws IOException {
        String csvFilePath = "testData/simple.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
        for (CSVRecord record : parser) {
            // Process the record here
            assertArrayEquals(record.getValues(), new String[]{"Col1", "Col2"});
        }
        parser.close();
    }

    @Test
    public void testParseIntoMemory() throws IOException {
        String csvFilePath = "testData/simple.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        List<CSVRecord> inMemoryRecords = new ArrayList<>(records);
        // Now process the inMemoryRecords instead of the parser
    }

    @Test
    public void testParseInvalidFormat() throws IOException {
        String csvString = "Col1,Col2\n" +
                "1,2\n" +
                "Col1,Col2\n";
        InputStream stream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> {
            CSVParser parser = new CSVParser(new InputStreamReader(stream));
            parser.getRecords();
        });
    }

    @Test
    public void testParseNullFile() throws IOException {
        CSVParser parser = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(null, CSVFormat.DEFAULT);
        });
    }

    @Test
    public void testParseNullFormat() throws IOException {
        File file = new File("testData/simple.csv");
        CSVParser parser = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(file, null);
        });
    }

    @Test
    public void testParseNullReader() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(null, format);
        });
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        String csvFilePath = "testData/simple.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(3, records.get(0).getRecordNumber());
        assertEquals(3, records.get(1).getRecordNumber());
        assertEquals(3, records.get(2).getRecordNumber());
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        String csvFilePath = "testData/simple.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
        assertEquals(1, parser.getCurrentLineNumber());
        parser.nextRecord();
        assertEquals(2, parser.getCurrentLineNumber());
        parser.nextRecord();
        assertEquals(3, parser.getCurrentLineNumber());
        parser.close();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        String csvFilePath = "testData/simple.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        parser.close();
        assertEquals(2, headerMap.size());
        assertTrue(headerMap.containsKey("Col1"));
        assertTrue(headerMap.containsKey("Col2"));
        assertEquals(0, headerMap.get("Col1"));
        assertEquals(1, headerMap.get("Col2"));
    }

    @Test
    public void testIterator() throws IOException {
        String csvFilePath = "testData/simple.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        int index = 0;
        for (CSVRecord record : parser) {
            assertEquals(records.get(index++), record);
        }
        assertFalse(parser.hasNext());
    }
}