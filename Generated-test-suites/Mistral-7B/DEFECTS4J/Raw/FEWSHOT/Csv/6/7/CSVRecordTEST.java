package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertEquals(3, record.size());
        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(0L, record.getRecordNumber());
        assertTrue(record.isConsistent());

        String name = "Name";
        String age = "30";
        String city = "New York";

        String[] newValues = new String[]{name, age, city};
        Map<String, Integer> newMapping = new HashMap<>();
        newMapping.put("Name", 0);
        newMapping.put("Age", 1);
        newMapping.put("City", 2);

        CSVRecord newRecord = new CSVRecord(newValues, newMapping, null, 1L);

        assertEquals(name, newRecord.get(name));
        assertEquals(age, newRecord.get(age));
        assertEquals(city, newRecord.get(city));
        assertEquals(1L, newRecord.getRecordNumber());
        assertTrue(newRecord.isConsistent());
    }

    @Test
    public void testGet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        String name = "Name";
        String age = "30";
        String city = "New York";

        record.values[1] = age;

        assertEquals(age, record.get(age));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnmappedColumn() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        String unMappedColumn = "UnmappedColumn";

        record.get(unMappedColumn);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        String[] values = new String[]{"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 0L);

        String name = "Name";

        record.get(name);
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        String comment = "This is a comment";

        CSVRecord record = new CSVRecord(values, mapping, comment, 0L);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isConsistent());

        record.values = new String[2];

        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("UnmappedColumn"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("UnmappedColumn"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        List<String> list = record.toList();

        Iterator<String> iterator = record.iterator();

        assertEquals(list.get(0), iterator.next());
        assertEquals(list.get(1), iterator.next());
        assertEquals(list.get(2), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Map<String, String> map = new HashMap<>();

        record.putIn(map);

        assertEquals(map.get("Name"), values[0]);
        assertEquals(map.get("Age"), values[1]);
        assertEquals(map.get("City"), values[2]);
    }
}