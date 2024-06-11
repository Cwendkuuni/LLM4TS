package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("data.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(5, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(Arrays.asList("Name", "Age"), record1.getHeaderMap().keySet());
        assertEquals(2, record1.size());
        assertEquals("Alice", record1.get("Name"));
        assertEquals(30, record1.get("Age").intValue());

        CSVRecord record2 = records.get(1);
        assertEquals(Arrays.asList("Name", "Age"), record2.getHeaderMap().keySet());
        assertEquals(2, record2.size());
        assertEquals("Bob", record2.get("Name"));
        assertEquals(25, record2.get("Age").intValue());

        CSVParser.closeQuietly(parser);
        CSVParser.closeQuietly(file);
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Name,Age\nAlice,30\nBob,25";
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(Arrays.asList("Name", "Age"), record1.getHeaderMap().keySet());
        assertEquals(2, record1.size());
        assertEquals("Alice", record1.get("Name"));
        assertEquals(30, record1.get("Age").intValue());

        CSVParser.closeQuietly(parser);
    }

    @Test
    public void testParseError() throws IOException {
        String csvData = "Name,Age\nAlice,30\nBob,25\nInvalid";
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(new StringReader(csvData), CSVFormat.RFC4180);
            parser.getRecords();
        } catch (IOException e) {
            // expected
        } finally {
            CSVParser.closeQuietly(parser);
        }
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("https://example.com/data.csv");
        CSVParser parser = CSVParser.parse(url, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        // TODO: Test records

        CSVParser.closeQuietly(parser);
    }

    @Test
    public void testParseArray() throws IOException {
        String csvData = "Name,Age\nAlice,30\nBob,25";
        CSVFormat format = CSVFormat.RFC4180;
        Reader reader = new StringReader(csvData);
        CSVParser parser = new CSVParser(reader, format);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord[] expectedRecords = {new CSVRecord(new String[]{"Name", "Age"}, new LinkedHashMap<>(), null, 0),
                new CSVRecord(new String[]{"Alice", "30"}, new LinkedHashMap<>(), null, 1),
                new CSVRecord(new String[]{"Bob", "25"}, new LinkedHashMap<>(), null, 2)};

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertArrayEquals(expectedRecords[i].getValues(), record.getValues());
            assertEquals(expectedRecords[i].getRecordNumber(), record.getRecordNumber());
        }

        CSVParser.closeQuietly(parser);
    }

    @Test
    public void testNoHeader() throws IOException {
        String csvData = "Alice,30\nBob,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.RFC4180.builder().setHeader(null).build());
        List<CSVRecord> records = parser.getRecords();

        CSVRecord[] expectedRecords = {new CSVRecord(new String[]{"Alice", "30"}, new LinkedHashMap<>(), null, 0),
                new CSVRecord(new String[]{"Bob", "25"}, new LinkedHashMap<>(), null, 1)};

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertArrayEquals(expectedRecords[i].getValues(), record.getValues());
            assertEquals(expectedRecords[i].getRecordNumber(), record.getRecordNumber());
        }

        CSVParser.closeQuietly(parser);
    }

    @Test
    public void testExcelFormat() throws IOException {
        String csvData = "Name      Age\nAlice   30\nBob     25";
        CSVFormat format = CSVFormat.EXCEL;
        Reader reader = new StringReader(csvData);
        CSVParser parser = new CSVParser(reader, format);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord[] expectedRecords = {new CSVRecord(new String[]{"Name", "Age"}, new LinkedHashMap<>(), null, 0),
                new CSVRecord(new String[]{"Alice", "30"}, new LinkedHashMap<>(), null, 1),
                new CSVRecord(new String[]{"Bob", "25"}, new LinkedHashMap<>(), null, 2)};

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertArrayEquals(expectedRecords[i].getValues(), record.getValues());
            assertEquals(expectedRecords[i].getRecordNumber(), record.getRecordNumber());
        }

        CSVParser.closeQuietly(parser);
    }

    @Test
    public void testExcelFormatWithHeader() throws IOException {
        String csvData = "Name      Age\nAlice   30\nBob     25";
        CSVFormat format = CSVFormat.EXCEL.withHeader();
        Reader reader = new StringReader(csvData);
        CSVParser parser = new CSVParser(reader, format);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord[] expectedRecords = {new CSVRecord(new String[]{"Name", "Age"}, new LinkedHashMap<>(), null, 0),
                new CSVRecord(new String[]{"Alice", "30"}, new LinkedHashMap<>(), null, 1),
                new CSVRecord(new String[]{"Bob", "25"}, new LinkedHashMap<>(), null, 2)};

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertArrayEquals(expectedRecords[i].getValues(), record.getValues());
            assertEquals(expectedRecords[i].getRecordNumber(), record.getRecordNumber());
            assertEquals(expectedRecords[i].getHeaderMap().keySet(), record.getHeaderMap().keySet());
        }

        CSVParser.closeQuietly(parser);
    }

    @Test
    public void testExcelFormatWithComment() throws IOException {
        String csvData = "Name      Age\nAlice   30\n---\nBob     25";
        CSVFormat format = CSVFormat.EXCEL.withComment('--');
        Reader reader = new StringReader(csvData);
        CSVParser parser = new CSVParser(reader, format);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord[] expectedRecords = {new CSVRecord(new String[]{"Name", "Age"}, new LinkedHashMap<>(), null, 0),
                new CSVRecord(new String[]{"Alice", "30"}, new LinkedHashMap<>(), null, 1),
                new CSVRecord(new String[]{"Bob", "25"}, new LinkedHashMap<>(), "--", 2)};

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertArrayEquals(expectedRecords[i].getValues(), record.getValues());
            assertEquals(expectedRecords[i].getRecordNumber(), record.getRecordNumber());
            assertEquals(expectedRecords[i].getComment(), record.getComment());
        }

        CSVParser.closeQuietly(parser);
    }

    @Test
    public void testParseIntoMemory() throws IOException {
        String csvData = "Name      Age\nAlice   30\nBob     25";
        CSVFormat format = CSVFormat.EXCEL;
        Reader reader = new StringReader(csvData);
        CSVParser parser = new CSVParser(reader, format);
        List<CSVRecord> records = parser.getRecords();
        List<CSVRecord> expectedRecords = Arrays.asList(new CSVRecord(new String[]{"Name", "Age"}, new LinkedHashMap<>(), null, 0),
                new CSVRecord(new String[]{"Alice", "30"}, new LinkedHashMap<>(), null, 1),
                new CSVRecord(new String[]{"Bob", "25"}, new LinkedHashMap<>(), null, 2));
        assertEquals(expectedRecords, parser.getRecords());

        CSVParser.closeQuietly(parser);
    }

    @Test
    public void testInvalidFormat() throws IOException {
        String csvData = "Name,Age\nAlice,30\nBob,25--";
        CSVFormat format = CSVFormat.EXCEL;
        Reader reader = new StringReader(csvData);
        CSVParser parser = new CSVParser(reader, format);

        try {
            parser.getRecords();
            fail();
        } catch (IOException e) {
            // expected
        }

        CSVParser.closeQuietly(parser);
    }

    @Test
    public void testSkipHeaderRecord() throws IOException {
        String csvData = "Name,Age\nName,Age\nAlice,30\nBob,25";
        CSVFormat format = CSVFormat.EXCEL;
        Reader reader = new StringReader(csvData);
        CSVParser parser = new CSVParser(reader, format);
        parser.getFormat().setSkipHeaderRecord(true);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord[] expectedRecords = {new CSVRecord(new String[]{"Alice", "30"}, new LinkedHashMap<>(), null, 1),
                new CSVRecord(new String[]{"Bob", "25"}, new LinkedHashMap<>(), null, 2)};

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertArrayEquals(expectedRecords[i].getValues(), record.getValues());
            assertEquals(expectedRecords[i].getRecordNumber(), record.getRecordNumber());
        }

        CSVParser.closeQuietly(parser);
    }
}