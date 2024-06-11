package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Token;

public class CSVParserTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testParseFile() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        File testFile = new File("testFile.csv");
        Files.write(testFile.getAbsoluteFile().toPath(), csvContent.getBytes());

        CSVParser parser = CSVParser.parse(testFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        Files.delete(testFile.toPath());

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertEquals("1", record1.get("id"));
        assertEquals("John Doe", record1.get("name"));
        assertEquals("2", record2.get("id"));
        assertEquals("Jane Doe", record2.get("name"));
    }

    @Test
    public void testParseString() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertEquals("1", record1.get("id"));
        assertEquals("John Doe", record1.get("name"));
        assertEquals("2", record2.get("id"));
        assertEquals("Jane Doe", record2.get("name"));
    }

    @Test
    public void testParseUrl() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        URL url = new URL("http://localhost/test.csv");
        InputStream inputStream = url.openStream();
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertEquals("1", record1.get("id"));
        assertEquals("John Doe", record1.get("name"));
        assertEquals("2", record2.get("id"));
        assertEquals("Jane Doe", record2.get("name"));

        inputStream.close();
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("id", "ID")
                .setHeader("name", "Name")
                .build();
        Reader inputReader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(inputReader, customFormat);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertEquals("1", record1.get("ID"));
        assertEquals("John Doe", record1.get("Name"));
        assertEquals("2", record2.get("ID"));
        assertEquals("Jane Doe", record2.get("Name"));
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        File testFile = new File("testFile.csv");
        CSVParser parser = CSVParser.parse(testFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        Files.delete(testFile.toPath());

        assertTrue(records.isEmpty());
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFormat() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe\n3,aple"; // Invalid format
        Reader inputReader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(inputReader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Expected IOException
    }

    @Test
    public void testGetRecordsIntoCollection() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        List<CSVRecord> result = new ArrayList<>();

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        parser.getRecords(result);
        parser.close();

        assertEquals(2, result.size());
        CSVRecord record1 = result.get(0);
        CSVRecord record2 = result.get(1);

        assertEquals("1", record1.get("id"));
        assertEquals("John Doe", record1.get("name"));
        assertEquals("2", record2.get("id"));
        assertEquals("Jane Doe", record2.get("name"));
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        parser.close();

        assertSame(parser.getHeaderMap(), headerMap);
        assertEquals(2, headerMap.size());
        assertEquals("id", headerMap.get("id").toString());
        assertEquals("name", headerMap.get("name").toString());
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        long recordNumber = parser.getRecordNumber();
        parser.close();

        assertEquals(2, recordNumber);
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        long currentLineNumber = parser.getCurrentLineNumber();
        parser.close();

        assertEquals(1, currentLineNumber);
    }

    @Test
    public void testGetRecordsWithEmptyCollection() throws IOException {
        List<CSVRecord> result = Collections.emptyList();
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);

        parser.getRecords(result);
        parser.close();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testClose() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        parser.close();

        try {
            parser.nextRecord();
        } catch (IOException e) {
            assertEquals("(line 1) closed", e.getMessage());
        }
    }

    @Test
    public void testIterator() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        List<CSVRecord> records = new ArrayList<>();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);

        parser.iterator().forEachRemaining(records::add);
        parser.close();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertEquals("1", record1.get("id"));
        assertEquals("John Doe", record1.get("name"));
        assertEquals("2", record2.get("id"));
        assertEquals("Jane Doe", record2.get("name"));
    }

    @Test
    public void testIteratorHasNext() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        List<CSVRecord> records = new ArrayList<>();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);

        Iterator<CSVRecord> iterator = parser.iterator();

        assertTrue(iterator.hasNext());
        records.add(iterator.next());

        assertTrue(iterator.hasNext());
        records.add(iterator.next());

        assertFalse(iterator.hasNext());

        parser.close();
    }

    @Test
    public void testIteratorNext() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        List<CSVRecord> records = new ArrayList<>();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);

        CSVRecord record1 = parser.iterator().next();
        records.add(record1);

        CSVRecord record2 = parser.iterator().next();
        records.add(record2);

        parser.close();

        assertEquals(2, records.size());

        CSVRecord result1 = records.get(0);
        CSVRecord result2 = records.get(1);

        assertEquals("1", result1.get("id"));
        assertEquals("John Doe", result1.get("name"));
        assertEquals("2", result2.get("id"));
        assertEquals("Jane Doe", result2.get("name"));
    }

    @Test
    public void testIteratorRemove() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        List<CSVRecord> records = new ArrayList<>();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);

        Iterator<CSVRecord> iterator = parser.iterator();

        CSVRecord record1 = iterator.next();
        records.add(record1);

        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());

        parser.close();
    }

    @Test
    public void testNextRecord() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);

        CSVRecord record1 = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();

        parser.close();

        assertEquals(2, parser.getRecordNumber());

        CSVRecord result1 = record1;
        CSVRecord result2 = record2;

        assertEquals("1", result1.get("id"));
        assertEquals("John Doe", result1.get("name"));
        assertEquals("2", result2.get("id"));
        assertEquals("Jane Doe", result2.get("name"));
    }

    @Test
    public void testNextRecordEmpty() throws IOException {
        String csvContent = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);

        CSVRecord record = parser.nextRecord();

        parser.close();

        assertNull(record);
    }

    @Test
    public void testNextRecordEOF() throws IOException {
        String csvContent = "id,name\n1,John Doe\n2,Jane Doe";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);

        parser.nextRecord();
        parser.nextRecord();

        CSVRecord record = parser.nextRecord();

        parser.close();

        assertNull(record);
    }
}