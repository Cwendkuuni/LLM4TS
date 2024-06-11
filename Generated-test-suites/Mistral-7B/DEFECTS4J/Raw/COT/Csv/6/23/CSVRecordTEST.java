package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CSVRecord} class.
 */
public class CSVRecordTest {

    private CSVFormat csvFormat;
    private CSVParser csvParser;

    @Before
    public void setup() {
        csvFormat = CSVFormat.DEFAULT.withHeader("name", "age", "comment");
        csvParser = new CSVParserBuilder().withFormat(csvFormat).build();
    }

    @Test
    public void testCSVRecord_Basic() {
        String[] values = new String[] {"Alice", "25", "Student"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("comment", 2);

        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 0);

        assertEquals("Alice", csvRecord.get("name"));
        assertEquals("25", csvRecord.get("age"));
        assertNull(csvRecord.getComment());
        assertEquals(0, csvRecord.getRecordNumber());
        assertEquals(Arrays.toString(values), csvRecord.toString());
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testCSVRecord_GetByEnum() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            assertEquals("Alice", record.get(CSVColumn.NAME.getColumnName()));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_NoHeaderMapping() {
        String[] values = new String[] {"Alice", "25", "Student"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 0);
        csvRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_MissingColumn() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            record.get("non-existent-column");
        }
    }

    @Test
    public void testCSVRecord_GetByIndex() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            assertEquals("Alice", record.get(0));
            assertEquals("25", record.get(1));
            assertNull(record.getComment());
        }
    }

    @Test
    public void testCSVRecord_GetComment() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            assertNull(record.getComment());
            csvParser.getRecordComment(); // consume comment for next record
            record = csvParser.getRecord();
            assertEquals("Student", record.getComment());
        }
    }

    @Test
    public void testCSVRecord_GetRecordNumber() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            assertEquals(0, record.getRecordNumber());
            csvParser.getRecord(); // consume record
            record = csvParser.getRecord();
            assertEquals(1, record.getRecordNumber());
        }
    }

    @Test
    public void testCSVRecord_IsConsistent() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            assertTrue(record.isConsistent());
            // consume the next record
            record = csvParser.getRecord();
            assertFalse(record.isConsistent());
        }
    }

    @Test
    public void testCSVRecord_IsMapped() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            assertTrue(record.isMapped("name"));
            assertTrue(record.isMapped("age"));
            assertFalse(record.isMapped("non-existent-column"));
        }
    }

    @Test
    public void testCSVRecord_IsSet() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            assertTrue(record.isSet("name"));
            assertTrue(record.isSet("age"));
            assertFalse(record.isSet("non-existent-column"));
        }
    }

    @Test
    public void testCSVRecord_Iterator() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            List<String> list = record.toList();
            Iterator<String> iterator = record.iterator();
            int i = 0;
            for (String value : list) {
                assertEquals(value, iterator.next());
                i++;
            }
            assertTrue(iterator.hasNext());
            assertFalse(iterator.hasPrevious());
        }
    }

    @Test
    public void testCSVRecord_PutIn() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            Map<String, String> map = new HashMap<String, String>();
            map.putAll(record.putIn(map));
            assertEquals(record.get(0), map.get("name"));
            assertEquals(record.get(1), map.get("age"));
            assertNull(map.get("comment"));
        }
    }

    @Test
    public void testCSVRecord_Size() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            assertEquals(3, record.size());
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCSVRecord_GetByIndexOutOfBounds() {
        try (CSVParser csvParser = this.csvParser.parse(getClass().getResource("/test.csv").openStream())) {
            CSVRecord record = csvParser.getRecord();
            record.get(3);
        }
    }
}