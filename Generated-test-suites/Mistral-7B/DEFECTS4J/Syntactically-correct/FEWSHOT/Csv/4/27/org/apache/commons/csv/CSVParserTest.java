package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        String filePath = "src/test/resources/csv/test.csv";
        CSVParser parser = CSVParser.parse(new File(filePath), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(18, records.size());

        Iterator<CSVRecord> it = parser.iterator();
        for (int i = 0; i < 18; i++) {
            CSVRecord record = it.next();
            if (i == 17) {
                assertNull(record);
            } else {
                assertArrayEquals(new String[]{"Id", "Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
                assertEquals(1, record.size());
                assertEquals("1", record.get(0).get(0));
                assertEquals("Alice", record.get(0).get(1));
                assertEquals(25, Integer.parseInt(record.get(0).get(2)));
            }
        }

        it = parser.iterator();
        assertFalse(it.hasNext());
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Id,Name,Age\n1,\"Alice\",25\n2,\"Bob\",30\n3,\"Eve\",35";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        Iterator<CSVRecord> it = parser.iterator();
        for (int i = 0; i < 3; i++) {
            CSVRecord record = it.next();
            if (i == 2) {
                assertNull(record);
            } else {
                assertArrayEquals(new String[]{"Id", "Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
                assertEquals(3, record.size());
                assertEquals("1", record.get(0).get(0));
                assertEquals("Alice", record.get(0).get(1));
                assertEquals(25, Integer.parseInt(record.get(0).get(2)));
            }
        }

        it = parser.iterator();
        assertFalse(it.hasNext());
        parser.close();
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://example.com/data.csv";
        CSVParser parser = CSVParser.parse(new URL(urlString), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(10, records.size());

        Iterator<CSVRecord> it = parser.iterator();
        for (int i = 0; i < 10; i++) {
            CSVRecord record = it.next();
            assertArrayEquals(new String[]{"Column1", "Column2"}, record.getHeaderMap().keySet().toArray(new String[0]));
            assertEquals(2, record.size());
            assertEquals("Value1_" + i, record.get(0).get(0));
            assertEquals("Value2_" + i, record.get(0).get(1));
        }

        it = parser.iterator();
        assertFalse(it.hasNext());
        parser.close();
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        String csvData = "Id,Name,Age\n1,\"Alice\",25\n2,\"Bob\",30\n3,\"Eve\",35";
        CSVFormat customFormat = CSVFormat.custom().setHeader("Id", "Number")
                .setHeader("Name", "Name").setMissingValue("NA").build();

        CSVParser parser = CSVParser.parse(new StringReader(csvData), customFormat);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        Iterator<CSVRecord> it = parser.iterator();
        for (int i = 0; i < 3; i++) {
            CSVRecord record = it.next();
            if (i == 2) {
                assertNull(record);
            } else {
                assertArrayEquals(new String[]{"Number", "Name"}, record.getHeaderMap().keySet().toArray(new String[0]));
                assertEquals(2, record.size());
                assertEquals("1", record.get(0).get(0));
                assertEquals("Alice", record.get(0).get(1));
                assertEquals(25, Integer.parseInt(record.get(0).get(0)));
                assertEquals("NA", record.getMap().get("Age"));
            }
        }

        it = parser.iterator();
        assertFalse(it.hasNext());
        parser.close();
    }

    @Test
    public void testGetRecordsIteratively() throws IOException {
        String csvData = "Id,Name,Age\n1,\"Alice\",25\n2,\"Bob\",30\n3,\"Eve\",35";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> allRecords = new ArrayList<>();
        allRecords.add(parser.nextRecord());
        while (parser.hasNext()) {
            allRecords.add(parser.nextRecord());
        }

        List<CSVRecord> records = parser.getRecords();
        assertEquals(allRecords, records);
        parser.close();
    }

    @Test
    public void testGetRecordsDirectly() throws IOException {
        String csvData = "Id,Name,Age\n1,\"Alice\",25\n2,\"Bob\",30\n3,\"Eve\",35";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        Iterator<CSVRecord> it = parser.iterator();
        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"Id", "Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            assertEquals(3, record.size());
            assertEquals("1", record.get(0).get(0));
            assertEquals("Alice", record.get(0).get(1));
            assertEquals(25, Integer.parseInt(record.get(0).get(2)));
            it.next();
        }

        it = parser.iterator();
        assertFalse(it.hasNext());
        parser.close();
    }

    @Test
    public void testGetRecordValues() throws IOException {
        String csvData = "Id,Name,Age\n1,\"Alice\",25\n2,\"Bob\",30\n3,\"Eve\",35";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        CSVRecord record = parser.nextRecord();
        assertEquals(3, record.size());
        assertEquals("1", record.get(0));
        assertEquals("Alice", record.get(1));
        assertEquals(25, Integer.parseInt(record.get(2)));

        record = parser.nextRecord();
        assertEquals(3, record.size());
        assertEquals("2", record.get(0));
        assertEquals("Bob", record.get(1));
        assertEquals(30, Integer.parseInt(record.get(2)));

        record = parser.nextRecord();
        assertEquals(3, record.size());
        assertEquals("3", record.get(0));
        assertEquals("Eve", record.get(1));
        assertEquals(35, Integer.parseInt(record.get(2)));

        record = parser.nextRecord();
        assertNull(record);

        parser.close();
    }

    @Test
    public void testGetRecordValuesWithCustomFormat() throws IOException {
        String csvData = "Id,Name,Age\n1,\"Alice\",25\n2,\"Bob\",30\n3,\"Eve\",35";
        CSVFormat customFormat = CSVFormat.custom()
                .setHeader("Id", "number").setHeader("Name", "name")
                .setMissingValue(Collections.emptyMap()).build();

        CSVParser parser = CSVParser.parse(new StringReader(csvData), customFormat);

        CSVRecord record = parser.nextRecord();
        assertEquals(3, record.size());
        assertEquals("1", record.get(0));
        assertEquals("Alice", record.get(1));
        assertEquals(25, Integer.parseInt(record.get(2)));

        record = parser.nextRecord();
        assertEquals(3, record.size());
        assertEquals("2", record.get(0));
        assertEquals("Bob", record.get(1));
        assertEquals(30, Integer.parseInt(record.get(2)));

        record = parser.nextRecord();
        assertEquals(3, record.size());
        assertEquals("3", record.get(0));
        assertEquals("Eve", record.get(1));
        assertEquals(35, Integer.parseInt(record.get(2)));

        record = parser.nextRecord();
        assertNull(record);

        parser.close();
    }
}