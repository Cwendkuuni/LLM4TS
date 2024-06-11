package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        String fileContent = "a,b\nc,d";
        File tempFile = File.createTempFile("CSVParserTest", null);
        try (Writer writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(fileContent, 0, fileContent.length());
        }

        CSVParser parser = CSVParser.parse(tempFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertSame(CSVFormat.DEFAULT, parser.getFormat());
        assertSame(tempFile, parser.getSource());
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));

        parser.close();
        tempFile.delete();
    }

    @Test
    public void testParseString() throws IOException {
        String csvContent = "a,b\nc,d";
        CSVParser parser = CSVParser.parse(csvContent, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));

        parser.close();
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlContent = "a,b\nc,d";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = urlContent.getBytes(StandardCharsets.UTF_8);
        outputStream.write(data, 0, data.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        URL url = new URL("http://example.com/csv.csv");
        CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));

        parser.close();
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        String csvContent = "a,b\nc,d";
        CSVFormat customFormat = new CSVFormat(',', '\n', null, null, null, null, null, true, true, null, null, true,
                null, null, null, null);
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));

        parser.close();
    }

    @Test
    public void testParseWithSkipHeader() throws IOException {
        String csvContent = "name,age\nAlice,30\nBob,25";
        CSVFormat customFormat = new CSVFormat(',', '\n', null, null, null, null, null, true, false, null, null, true,
                null, null, null, null);
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("Alice", record.get(0));
        assertEquals("30", record.get(1));

        parser.close();
    }

    @Test
    public void testParseWithEmptyRecord() throws IOException {
        String csvContent = "\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertTrue(record.isEmpty());

        parser.close();
    }

    @Test
    public void testParseWithHeader() throws IOException {
        String csvContent = "name,age\nAlice,30\nBob,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals(2, firstRecord.size());
        assertEquals("name", firstRecord.getHeader().get(0));
        assertEquals("age", firstRecord.getHeader().get(1));
        assertEquals("Alice", firstRecord.get(0));
        assertEquals("30", firstRecord.get(1));

        CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("name", secondRecord.getHeader().get(0));
        assertEquals("age", secondRecord.getHeader().get(1));
        assertEquals("Bob", secondRecord.get(0));
        assertEquals("25", secondRecord.get(1));

        parser.close();
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        CSVParser parser = CSVParser.parse(new File(""), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());

        parser.close();
    }

    @Test
    public void testParseInvalidFile() throws IOException {
        File tempFile = File.createTempFile("CSVParserTest", null);
        try (Writer writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("Invalid content", 0, "Invalid content".length());
        }

        CSVParser parser = CSVParser.parse(tempFile, CSVFormat.DEFAULT);
        try {
            parser.getRecords();
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // expected
        }

        parser.close();
        tempFile.delete();
    }

    @Test
    public void testParseWithCustomHeader() throws IOException {
        String csvContent = "name,age\nAlice,30\nBob,25";
        Map<String, Integer> customHeader = Collections.singletonMap("alias", 0);
        CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
        parser.setHeaderMap(customHeader);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals("alias", firstRecord.getHeader().get(0));
        assertEquals("age", firstRecord.get(0));
        assertEquals("Alice", firstRecord.get(1));
        assertEquals("30", firstRecord.get(1));

        CSVRecord secondRecord = records.get(1);
        assertEquals("alias", secondRecord.getHeader().get(0));
        assertEquals("age", secondRecord.get(0));
        assertEquals("Bob", secondRecord.get(1));
        assertEquals("25", secondRecord.get(1));

        parser.close();
    }

    @Test
    public void testParseWithComments() throws IOException {
        String csvContent = "name,age#This is a comment\nAlice,30\nBob,25#Another comment";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals(2, firstRecord.size());
        assertEquals("name", firstRecord.getHeader().get(0));
        assertEquals("age", firstRecord.getHeader().get(1));
        assertEquals("Alice", firstRecord.get(0));
        assertEquals("30", firstRecord.get(1));
        assertEquals(null, firstRecord.getComment());

        CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("name", secondRecord.getHeader().get(0));
        assertEquals("age", secondRecord.getHeader().get(1));
        assertEquals("Bob", secondRecord.get(0));
        assertEquals("25", secondRecord.get(1));
        assertEquals("#Another comment", secondRecord.getComment());

        parser.close();
    }

    @Test
    public void testParseWithTrailingEmptyLines() throws IOException {
        String csvContent = "name,age\nAlice,30\nBob,25\n\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals(2, firstRecord.size());
        assertEquals("name", firstRecord.getHeader().get(0));
        assertEquals("age", firstRecord.getHeader().get(1));
        assertEquals("Alice", firstRecord.get(0));
        assertEquals("30", firstRecord.get(1));

        CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("name", secondRecord.getHeader().get(0));
        assertEquals("age", secondRecord.getHeader().get(1));
        assertEquals("Bob", secondRecord.get(0));
        assertEquals("25", secondRecord.get(1));

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyIterator() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader(""), CSVFormat.DEFAULT);
        parser.iterator().next();
    }

    @Test
    public void testParseWithCustomDelimiter() throws IOException {
        String csvContent = "name|age\nAlice|30\nBob|25";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), new CSVFormatBuilder().setDelimiter('|')
                .toBuilder());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals(2, firstRecord.size());
        assertEquals("name", firstRecord.getHeader().get(0));
        assertEquals("age", firstRecord.getHeader().get(1));
        assertEquals("Alice", firstRecord.get(0));
        assertEquals("30", firstRecord.get(1));

        CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("name", secondRecord.getHeader().get(0));
        assertEquals("age", secondRecord.getHeader().get(1));
        assertEquals("Bob", secondRecord.get(0));
        assertEquals("25", secondRecord.get(1));

        parser.close();
    }

}