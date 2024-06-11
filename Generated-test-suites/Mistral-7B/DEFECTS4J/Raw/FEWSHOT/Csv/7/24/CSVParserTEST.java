package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws Exception {
        File file = new File("src/test/resources/test-parse-file.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(4, records.size());
        CSVRecord record0 = records.get(0);
        assertArrayEquals(new String[] { "A1", "B1" }, record0.valuesToArray());
        CSVRecord record1 = records.get(1);
        assertArrayEquals(new String[] { "", "B2" }, record1.valuesToArray());
        CSVRecord record2 = records.get(2);
        assertArrayEquals(new String[] { "C1", "D1" }, record2.valuesToArray());
        CSVRecord record3 = records.get(3);
        assertArrayEquals(new String[] { "", "", "D2" }, record3.valuesToArray());
    }

    @Test
    public void testParseString() throws Exception {
        String csvString = "A1,B1\n,B2\nC1,D1\n,,D2";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(4, records.size());
        CSVRecord record0 = records.get(0);
        assertArrayEquals(new String[] { "A1" }, record0.valuesToArray());
        CSVRecord record1 = records.get(1);
        assertArrayEquals(new String[] { "" }, record1.valuesToArray());
        CSVRecord record2 = records.get(2);
        assertArrayEquals(new String[] { "C1" }, record2.valuesToArray());
        CSVRecord record3 = records.get(3);
        assertArrayEquals(new String[] { "", "", "D2" }, record3.valuesToArray());
    }

    @Test
    public void testParseUrl() throws Exception {
        URL url = new URL("http://TestCSVParser.com/test.csv");
        CSVParser parser = CSVParser.parse(url, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(4, records.size());
        CSVRecord record0 = records.get(0);
        assertArrayEquals(new String[] { "A1", "B1" }, record0.valuesToArray());
        CSVRecord record1 = records.get(1);
        assertArrayEquals(new String[] { "", "B2" }, record1.valuesToArray());
        CSVRecord record2 = records.get(2);
        assertArrayEquals(new String[] { "C1", "D1" }, record2.valuesToArray());
        CSVRecord record3 = records.get(3);
        assertArrayEquals(new String[] { "", "", "D2" }, record3.valuesToArray());
    }

    @Test
    public void testIterate() throws Exception {
        File file = new File("src/test/resources/test-parse-file.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        parser.close();
        assertEquals(4, records.size());
        CSVRecord record0 = records.get(0);
        assertArrayEquals(new String[] { "A1", "B1" }, record0.valuesToArray());
        CSVRecord record1 = records.get(1);
        assertArrayEquals(new String[] { "", "B2" }, record1.valuesToArray());
        CSVRecord record2 = records.get(2);
        assertArrayEquals(new String[] { "C1", "D1" }, record2.valuesToArray());
        CSVRecord record3 = records.get(3);
        assertArrayEquals(new String[] { "", "", "D2" }, record3.valuesToArray());
    }

    @Test
    public void testGetHeaderMap() throws Exception {
        File file = new File("src/test/resources/test-parse-file-header.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        parser.close();
        assertSame(headerMap, parser.getHeaderMap());
        List<String> keys = new ArrayList<>(headerMap.keySet());
        Collections.sort(keys);
        assertEquals(2, keys.size());
        assertEquals("COLUMN1", keys.get(0));
        assertEquals("COLUMN2", keys.get(1));
    }

    @Test
    public void testGetRecordsIntoCollection() throws Exception {
        File file = new File("src/test/resources/test-parse-file.csv");
        List<CSVRecord> records = new ArrayList<>();
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        parser.getRecords(records);
        parser.close();
        assertEquals(4, records.size());
        CSVRecord record0 = records.get(0);
        assertArrayEquals(new String[] { "A1" }, record0.valuesToArray());
        CSVRecord record1 = records.get(1);
        assertArrayEquals(new String[] { "" }, record1.valuesToArray());
        CSVRecord record2 = records.get(2);
        assertArrayEquals(new String[] { "C1" }, record2.valuesToArray());
        CSVRecord record3 = records.get(3);
        assertArrayEquals(new String[] { "", "", "D2" }, record3.valuesToArray());
    }

    @Test
    public void testClose() throws Exception {
        File file = new File("src/test/resources/test-parse-file.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        try {
            records.add(new CSVRecord(new String[0], Collections.emptyMap(), null, 0));
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testIsClosed() throws Exception {
        File file = new File("src/test/resources/test-parse-file.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testIteratorHasNext() throws Exception {
        File file = new File("src/test/resources/test-parse-file.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = new ArrayList<>();
        Iterator<CSVRecord> iterator = parser.iterator();
        for (int i = 0; i < 4; i++) {
            assertTrue(iterator.hasNext());
            CSVRecord record = iterator.next();
            records.add(record);
        }
        assertFalse(iterator.hasNext());
        parser.close();
        assertEquals(4, records.size());
    }

    @Test
    public void testIteratorNext() throws Exception {
        File file = new File("src/test/resources/test-parse-file.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = new ArrayList<>();
        Iterator<CSVRecord> iterator = parser.iterator();
        for (int i = 0; i < 4; i++) {
            CSVRecord record = iterator.next();
            records.add(record);
        }
        parser.close();
        assertEquals(4, records.size());
        CSVRecord record0 = records.get(0);
        assertArrayEquals(new String[] { "A1" }, record0.valuesToArray());
        CSVRecord record1 = records.get(1);
        assertArrayEquals(new String[] { "" }, record1.valuesToArray());
        CSVRecord record2 = records.get(2);
        assertArrayEquals(new String[] { "C1" }, record2.valuesToArray());
        CSVRecord record3 = records.get(3);
        assertArrayEquals(new String[] { "", "", "D2" }, record3.valuesToArray());
    }

    @Test
    public void testIteratorRemove() throws Exception {
        File file = new File("src/test/resources/test-parse-file.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = new ArrayList<>();
        Iterator<CSVRecord> iterator = parser.iterator();
        for (int i = 0; i < 4; i++) {
            CSVRecord record = iterator.next();
            records.add(record);
        }
        try {
            iterator.remove();
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
        parser.close();
        assertEquals(4, records.size());
    }

    @Test
    public void testGetRecordsNullInput() throws Exception {
        String input = null;
        CSVParser parser = new CSVParser(new StringReader(input), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertNull(records);
    }
}