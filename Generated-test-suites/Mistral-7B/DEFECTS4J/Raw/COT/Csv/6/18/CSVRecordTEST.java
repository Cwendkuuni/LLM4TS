package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * CSVRecordTest
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecord_NormalCase() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("Male", record.get(3));
        assertEquals("", record.get(4)); // check for out of bounds index
        assertEquals(1L, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_NoHeaderMapping() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get("Name"); // should throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_InvalidMapping() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 4); // value out of bound

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        record.get("Age"); // should throw an exception
    }

    @Test
    public void testCSVRecord_GetWithEnum() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("John", record.get(CSVRecord.Gender.NAME));
        assertEquals("Male", record.get(CSVRecord.Gender.NAME));
    }

    @Test
    public void testCSVRecord_Iterator() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        Iterator<String> iterator = record.iterator();
        List<String> list = Arrays.asList(iterator.next(), iterator.next(), iterator.next(), iterator.next());

        assertEquals(values[0], list.get(0));
        assertEquals(values[1], list.get(1));
        assertEquals(values[2], list.get(2));
        assertEquals(values[3], list.get(3));
    }

    @Test
    public void testCSVRecord_PutIn() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1L);
        map = record.putIn(map);

        assertEquals(values[0], map.get("Name"));
        assertEquals(values[1], map.get("Doe")); // check for case insensitive
        assertEquals(values[2], map.get("Age"));
        assertEquals(values[3], map.get("Gender"));
    }

    @Test
    public void testCSVRecord_Size() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(values.length, record.size());
    }

    @Test
    public void testCSVRecord_ToString() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("[John, Doe, 30, Male]", record.toString());
    }

    @Test
    public void testCSVRecord_Values() {
        String[] values = new String[]{"John", "Doe", "30", "Male"};

        CSVRecord record = new CSVRecord(values, null, null, 1L);

        String[] recordValues = record.values();

        assertEquals(values.length, recordValues.length);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], recordValues[i]);
        }
    }
}